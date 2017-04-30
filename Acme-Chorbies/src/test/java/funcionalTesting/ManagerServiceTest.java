
package funcionalTesting;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import services.ManagerService;
import utilities.AbstractTest;
import domain.Manager;
import form.RegistrationFormManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ManagerServiceTest extends AbstractTest {

	// The SUT
	// ---------------------------------------------------------------------------------

	@Autowired
	private ManagerService	managerService;


	// Creacion del template para el registro de manager
	protected void template(final String user, final String username, final String password, final String passwordCheck, final String phone, final boolean termsOfUse, final String name, final String email, final String surName, final String vatNumber,
		final String company, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			this.authenticate(user);

			final RegistrationFormManager reg = new RegistrationFormManager();

			reg.setUsername(username);
			reg.setPassword(password);
			reg.setPasswordCheck(passwordCheck);

			reg.setName(name);
			reg.setSurName(surName);
			reg.setPhone(phone);
			reg.setEmail(email);

			reg.setVatNumber(vatNumber);
			reg.setCompany(company);

			reg.setTermsOfUse(termsOfUse);

			final Manager manager = this.managerService.reconstruct(reg);
			this.managerService.save(manager);
			this.managerService.flush();
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);

	}

	@Test
	public void RegisterAManager() {

		final Object testingData[][] = {
			// Crear un usuario valido logeandose en el sistema como
			// administrador
			{
				"admin", "manager1000", "manager1000", "manager1000", "+954670441", true, "manager1000", "manager1000@gmail.com", "manager1000", "ESA12345678", "company", null
			},
			// Intentar crear un manager logueandose en el sistema como chorbi
			{
				"chorbi1", "manager1000", "manager1000", "manager1000", "+954670441", true, "manager1000", "manager1000@gmail.com", "manager1000", "ESA12345678", "company", DataIntegrityViolationException.class
			},
			// Intentar crear un manager con campos vacios
			{
				"admin", " ", " ", " ", "+954670441", true, "manager1000", "manager1000@gmail.com", "manager1000", "ESA12345678", "company", DataIntegrityViolationException.class
			},
			// Intentar crear un manager que existe en el sistema 
			{
				"admin", "manager1000", "manager1000", "manager1000", "+954670441", true, "manager1000", "manager1000@gmail.com", "manager1000", "ESA12345678", "company", DataIntegrityViolationException.class
			},

			// Intentar crear un manager sin aceptar los terminos 
			{
				"admin", "manager1000", "manager1000", "manager1000", "+954670441", false, "manager1000", "manager1000@gmail.com", "manager1000", "ESA12345678", "company", DataIntegrityViolationException.class
			},

			// Intentar crear un manager que no cumple el pattern del VatNumber
			{
				"admin", "manager1000", "manager1000", "manager1000", "+954670441", true, "manager1000", "manager1000@gmail.com", "manager1000", "123456789", "company", DataIntegrityViolationException.class
			},

			// Intentar crear un manager que no pone un email valido
			{
				"admin", "manager1000", "manager1000", "manager1000", "+954670441", false, "manager1000", "manager1000", "manager1000", "ESA12345678", "company", DataIntegrityViolationException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (boolean) testingData[i][5], (String) testingData[i][6], (String) testingData[i][7],
				(String) testingData[i][8], (String) testingData[i][9], (String) testingData[i][10], (Class<?>) testingData[i][11]);

	}
}
