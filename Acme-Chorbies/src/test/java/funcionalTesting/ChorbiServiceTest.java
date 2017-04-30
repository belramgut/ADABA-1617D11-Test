
package funcionalTesting;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.ChorbiService;
import services.TemplateService;
import utilities.AbstractTest;
import domain.Chorbi;
import domain.Template;
import form.RegistrationForm;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ChorbiServiceTest extends AbstractTest {

	// The SUT
	// ---------------------------------------------------------------------------------

	@Autowired
	private ChorbiService	chorbiService;

	@Autowired
	private TemplateService	templateService;


	// CASO DE USO 1: BROWSE THE LIST OF CHORBIES WHO HAVE REGISTERED TO THE
	// SYSTEM
	// ---------------------------------------------------------------------------------
	// Este caso de uso debe generar, para todos los autenticados, una vista de
	// un listado en el que aparezcan todos los chorbies del sistema, excepto
	// los baneados, para los chorbies, y los banneados inclusive para el admin.
	// Según nuestro populate.xml debería mostrar 5 chorbies sin bannear, 6
	// incluyendo los banneados y el chorbi3 es el banneado.
	protected void templateListUseCase1(final String username, final int option, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {

			this.authenticate(username);

			Collection<Chorbi> res = null;

			if (option == 1)
				res = this.chorbiService.findAllNotBannedChorbies();
			if (option == 2)
				res = this.chorbiService.findAll();

			if (username.equals("admin")) {

				Assert.isTrue(res.size() == 6);
				for (final Chorbi chorbi : res) {
					Assert.isTrue((chorbi.getUserAccount().getUsername().equals("chorbi1")) || (chorbi.getUserAccount().getUsername().equals("chorbi2")) || (chorbi.getUserAccount().getUsername().equals("chorbi3"))
						|| (chorbi.getUserAccount().getUsername().equals("chorbi4")) || (chorbi.getUserAccount().getUsername().equals("chorbi5")) || (chorbi.getUserAccount().getUsername().equals("chorbi6")));
					if (chorbi.getUserAccount().getUsername().equals("chorbi3"))
						Assert.isTrue(chorbi.isBan() == true);
					else
						Assert.isTrue(chorbi.isBan() == false);
				}

			} else {

				for (final Chorbi chorbi : res) {
					Assert.isTrue(chorbi.isBan() == false);
					Assert.isTrue((chorbi.getUserAccount().getUsername().equals("chorbi1")) || (chorbi.getUserAccount().getUsername().equals("chorbi2")) || (chorbi.getUserAccount().getUsername().equals("chorbi4"))
						|| (chorbi.getUserAccount().getUsername().equals("chorbi5")) || (chorbi.getUserAccount().getUsername().equals("chorbi6")));
				}

				Assert.isTrue(res.size() == 5);
			}

			this.unauthenticate();
			this.chorbiService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	@Test
	public void driverListUseCase1() {

		final Object testingData[][] = {

			// Para el caso de uso probaremos que:
			// (Para chorbies)
			// 1. Se recojan de la base de datos el número correcto total de
			// chorbies que debería (5)
			// 2. El usuario que accede esté logueado en el sistema
			// 3. No se muestre en el listado ningun chorbi banneado ( sin
			// chorbi3)
			// 4. Que los chorbies que se deben mostrar sean los correctos
			// (mediante el username)

			// Para el caso de uso probaremos que:
			// (Para admin)
			// 1. Se recojan de la base de datos el número correcto total de
			// chorbies que debería (6)
			// 2. El usuario que accede esté logueado en el sistema
			// 3. Se muestren en el listado los chorbies banneados (incluido
			// chorbi3)
			// 4. Que los chorbies que se deben mostrar sean los correctos
			// (mediante el username)
			// 5. Que el chorbi baneado sea el correcto (chorbi3)

			// DETALLES DE CADA PRUEBA:
			// 1. Se deben recoger datos correctos, sin ningun chorbi
			// banneado y con un size de 5
			// 2. Se deben recoger datos incorrectos, aparecen chorbies
			// banneados y el size no es 5
			// 3. No se recogen los datos y se produce una excepción porque
			// no hay ningún usuario logueado en el sistema
			// 4. Se deben recoger datos correctos para el listado de admin
			// (incluyendo los baneados), ya que el admin está autenticado
			// 5. No se recogen los datos correctos, pues no aparecen los
			// chorbies banneados
			// 6. No se deben recoger datos, ya que el chorbi 3 está
			// banneado y no se puede loguear.

			{
				"chorbi1", 1, null
			}, {
				"chorbi2", 2, IllegalArgumentException.class
			}, {
				null, 1, IllegalArgumentException.class
			}, {
				"admin", 2, null
			}, {
				"admin", 1, IllegalArgumentException.class
			}, {
				"chorbi3", 1, IllegalArgumentException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateListUseCase1((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	// CASO DE USO 2,3 y 4:
	// ---------------------------------------------------------------------------------
	// 2 : BROWSE THE LIST OF CHORBIES WHO HAVE REGISTERED TO THE SYSTEM AND
	// NAVIGATE TO THE CHORBIES WHO LIKE THEM
	// 3 : MY LIKES, donde cada usuario autenticado como principal ve un listado
	// de chorbies a los que ha dado su like
	// 4 : PEOPLE WHO I LIKE, donde cada usuario autenticado como principal ve
	// un listado de chorbies que le han dado like
	// Se ha decidido probar los tres casos en conjunto, ya que la funcionalidad
	// es practicamente la misma, pues hacen uso del mismo método de servicio,
	// unicamente difieren en el controlador.

	// Estos casos de uso añaden funcionalidad al anterior, hace que en el
	// listado de chorbies se añadan dos enlaces:
	// El primero, donde podemos ver las personas que han dado like al chorbi de
	// esa fila
	// El segundo, donde podemos ver las personas a las que el chorbi de esa
	// fila ha dado like
	// Igual que en el caso anterior, no deben mostrarse los chorbies banneados

	// Option controlará si el listado es del primer tipo o del segundo
	protected void templateListUseCase2(final String username, final int resultsSize, final int option, final Class<?> expected) {

		Class<?> caught;
		caught = null;

		try {

			this.authenticate(username);

			Collection<Chorbi> res = null;
			final Chorbi principal = this.chorbiService.findByPrincipal();

			if (option == 1)
				res = this.chorbiService.findAllChorbiesWhoLikeThem(principal);
			else
				res = this.chorbiService.findAllChorbiesWhoLikedByThisUser(principal);

			Assert.isTrue(res.size() == resultsSize);

			for (final Chorbi c : res) {
				Assert.isTrue(c.isBan() == false);
				Assert.isTrue(c.getId() != principal.getId());

			}

			this.unauthenticate();
			this.chorbiService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);

	}

	@Test
	public void driverListUseCase2() {

		final Object testingData[][] = {

			// Para el caso de uso probaremos que:
			// 1. Se recojan de la base de datos el número correcto total de
			// chorbies que debería para cada uno y en cada opción
			// 2. El usuario que accede esté logueado en el sistema
			// 3. No se muestre en el listado ningun chorbi banneado (sin
			// chorbi3)
			// 4. Que los chorbies que se deben mostrar sean los correctos
			// (mediante el username)
			// 5. Que no se puede acceder a los datos del chorbi3, por estar
			// banneado

			// DETALLES DE CADA PRUEBA:
			// 1 a 10 --> resultados positivos: comprobando que se recoja la
			// cantidad de chorbies correcta para cada caso y que no estén
			// banneados.
			// 11 y 12 --> resultados negativos : no se debe poder acceder a
			// esta información, ya que el chorbi3 no se puede autenticar al
			// estar banneado
			{
				"chorbi1", 1, 1, null
			}, {
				"chorbi1", 1, 2, null
			}, {
				"chorbi2", 1, 1, null
			}, {
				"chorbi2", 1, 2, null
			}, {
				"chorbi4", 0, 1, null
			}, {
				"chorbi4", 0, 2, null
			}, {
				"chorbi5", 0, 1, null
			}, {
				"chorbi5", 0, 2, null
			}, {
				"chorbi6", 0, 1, null
			}, {
				"chorbi6", 0, 2, null
			}, {
				"chorbi3", 1, 1, IllegalArgumentException.class
			}, {
				"chorbi3", 1, 2, IllegalArgumentException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateListUseCase2((String) testingData[i][0], (int) testingData[i][1], (int) testingData[i][2], (Class<?>) testingData[i][3]);

	}

	// CASO DE USO 5: El administrador puede bannear o volver a permitir a un
	// chorbi -----------------------------------------------------------------

	// Baneamos a un chorbi, y posteriomente comprobamos que el chorbi ha sido
	// banneado correctamente, es decir cuando
	// su atributo booleano ban es true
	protected void templateBanChorbiUseCase5(final String username, final Chorbi chorbi, final boolean result, final Class<?> expected) {

		Class<?> caught;
		caught = null;

		try {

			this.authenticate(username);

			final Chorbi res = chorbi;
			this.chorbiService.banChorbi(res);

			Assert.isTrue(res.isBan() == result);

			this.unauthenticate();
			this.chorbiService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);

	}

	// Chorbi = 63,64,65,66,67,68
	@Test
	public void driverBanChorbiUseCase5() {

		final Chorbi chorbi1 = this.chorbiService.findOneToSent(259); // Obtenemos de la base de datos el chorbi con id = 259 inicialmente no banneado
		final Chorbi chorbi2 = this.chorbiService.findOneToSent(261); //Obtenesmo de la base de datos el chorbi con id = 261 inicialmente banneado

		final Object testingData[][] = {
			// TEST POSITIVO: Bannear un chorbi que aun no esta baneado, y
			// comprobar que el resultado es correcto.
			{
				"admin", chorbi1, true, null
			},
			// TEST NEGATIVO: Bannear un un chorbi que ya ha sido banneado y
			// comprobar que salta correctamente la excepción.
			{
				"admin", chorbi2, true, IllegalArgumentException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateBanChorbiUseCase5((String) testingData[i][0], (Chorbi) testingData[i][1], (boolean) testingData[i][2], (Class<?>) testingData[i][3]);

	}

	// Permitimos a un chorbi, y posteriomente comprobamos que el chorbi ha sido
	// permitido correctamente, es decir cuando
	// su atributo booleano ban es false
	protected void templateUnbanChorbiUseCase5(final String username, final Chorbi chorbi, final boolean result, final Class<?> expected) {

		Class<?> caught;
		caught = null;

		try {

			this.authenticate(username);

			final Chorbi res = chorbi;
			this.chorbiService.unBanChorbi(res);

			Assert.isTrue(res.isBan() == result);

			this.unauthenticate();
			this.chorbiService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);

	}

	@Test
	public void driverUnbanChorbiUseCase5() {

		final Chorbi chorbi1 = this.chorbiService.findOneToSent(261); // Obtenemos de la base de datos el chorbi con id = 261 inicialmente baneado
		final Chorbi chorbi2 = this.chorbiService.findOneToSent(259); // Obtenemos de la base de datos el chorbi con id = 259 inicialmente sin banear

		final Object testingData[][] = {
			// TEST POSITIVO: Permitir un chorbi que esta baneado, y
			// comprobar que el resultado es correcto.
			{
				"admin", chorbi1, false, null
			},
			// TEST NEGATIVO: Permitir un chorbi que ya se le permite entrar
			// en el sistema y comprobar que salta correctamente la
			// excepción.
			{
				"admin", chorbi2, false, IllegalArgumentException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateUnbanChorbiUseCase5((String) testingData[i][0], (Chorbi) testingData[i][1], (boolean) testingData[i][2], (Class<?>) testingData[i][3]);

	}

	// Con este test comprobamos que al intentar logearnos con un chorbi que se
	// encuentra banneado no es posible realizar dicha operación y el test
	// devuelve IllegalArgumentException
	@Test(expected = IllegalArgumentException.class)
	public void testLoginBannedChorbi() {

		this.authenticate("chorbi3");

	}

	protected void template(final String username, final String password, final String passwordCheck, final String phone, final boolean termsOfUse, final String name, final String email, final Date birthDate, final String city, final String country,
		final String description, final String genre, final String state, final String province, final String surName, final String picture, final String relation, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			this.chorbiService.create();
			final RegistrationForm reg = new RegistrationForm();

			reg.setUsername(username);
			reg.setPassword(password);
			reg.setPasswordCheck(passwordCheck);

			reg.setName(name);
			reg.setSurName(surName);
			reg.setPhone(phone);
			reg.setEmail(email);

			reg.setBirthDate(birthDate);
			reg.setDescription(description);
			reg.setPicture(picture);
			reg.setGenre(genre);
			reg.setRelation(relation);

			reg.setCity(city);
			reg.setCountry(country);
			reg.setProvince(province);
			reg.setState(state);

			reg.setTermsOfUse(termsOfUse);
			final Chorbi chorbi = this.chorbiService.reconstruct(reg);
			final Template template = this.templateService.create();
			this.chorbiService.saveAndFlush(chorbi, template);
			this.chorbiService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);

	}

	@Test
	public void RegisterAChorbie() {

		final Object testingData[][] = {
			// Crear un usuario valido
			{
				"customerPrueba", "customerPrueba1", "customerPrueba1", "+954670441", true, "customerPrueba", "customerPrueba@gmail.com", new Date(1220227200), "Seville", "Spain", "Decription", "MALE", "Spain", "Seville", "customerPrueba1",
				"http://www.google.com", "LOVE", null
			},
			// Crear un usuario que tiene campos en blanco
			{
				" ", " ", "customerPrueba1", "+954670441", true, "customerPrueba", "customerPrueba@gmail.com", new Date(1220227200), "Seville", "Spain", "Decription", "MALE", "Spain", "Seville", "customerPrueba1", "http://www.google.com", "LOVE",
				ConstraintViolationException.class
			},
			// Crear un usuario que tiene un telefono no valido
			{
				"customerPrueba2", "customerPrueba1", "customerPrueba1", "+95402", true, "customerPrueba", "customerPrueba@gmail.com", new Date(1220227200), "Seville", "Spain", "Decription", "MALE", "Spain", "Seville", "customerPrueba1",
				"http://www.google.com", "LOVE", ConstraintViolationException.class
			},
			// Crear un usuario que tiene un mail no valido
			{
				"customerPrueb3", "customerPrueba1", "customerPrueba1", "+954670441", true, "customerPrueba", "customerPrueba", new Date(1220227200), "Seville", "Spain", "Decription", "MALE", "Spain", "Seville", "customerPrueba1", "http://www.google.com",
				"LOVE", ConstraintViolationException.class
			},
			// Crear un usuario que no ha aceptado las condiciones y
			// terminos de uso
			{
				"customerPrueb4", "customerPrueba1", "customerPrueba1", "+954670441", false, "customerPrueba", "customerPrueba@gmail.com", new Date(1220227200), "Seville", "Spain", "Decription", "MALE", "Spain", "Seville", "customerPrueba1",
				"http://www.google.com", "LOVE", ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.template((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (boolean) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6], (Date) testingData[i][7],
				(String) testingData[i][8], (String) testingData[i][9], (String) testingData[i][10], (String) testingData[i][11], (String) testingData[i][12], (String) testingData[i][13], (String) testingData[i][14], (String) testingData[i][15],
				(String) testingData[i][16], (Class<?>) testingData[i][17]);

	}

	protected void templateCalculateFeeChorbiUseCase(final String username, final Class<?> expected) {

		Class<?> caught;
		caught = null;

		try {

			this.authenticate(username);

			this.chorbiService.calculateFee();

			this.unauthenticate();
			this.chorbiService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);

	}

	@Test
	public void driverCalculateFeeChorbiUseCase() {

		final Chorbi chorbi1 = this.chorbiService.findOneToSent(259); // Obtenemos un chorbi con una fecha de actualización correcta chorbiId = 259

		final Chorbi chorbi2 = this.chorbiService.findOneToSent(264); // Obtenemos un chorbi con una fecha de actualización igual a la fecha actual chorbiId = 264

		final Object testingData[][] = {
			// TEST POSITIVO: calulamos la fee del chorbi que se ecuentra en un rango de fechas correcto.
			{
				"admin", chorbi1, null
			},
			// TEST NEGATIVO: Intentamos calcular la fee del chorbi sin ser admin
			{
				"chorbi1", chorbi2, IllegalArgumentException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateCalculateFeeChorbiUseCase((String) testingData[i][0], ((Class<?>) testingData[i][1]));
	}
}
