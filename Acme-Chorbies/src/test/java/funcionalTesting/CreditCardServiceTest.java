
package funcionalTesting;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import services.ChorbiService;
import services.CreditCardService;
import utilities.AbstractTest;
import domain.BrandName;
import domain.Chorbi;
import domain.CreditCard;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class CreditCardServiceTest extends AbstractTest {

	// The SUT
	// ---------------------------------------------------------------------------------

	@Autowired
	private ChorbiService		chorbiService;

	@Autowired
	private CreditCardService	creditCardService;


	//Edit the creditCard from a chorbie
	protected void template(final String username, final int chorbiId, final BrandName brandName, final int cvvCode, final int expirationMonth, final int expirationYear, final String holderName, final String number, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {

			this.authenticate(username);
			final Chorbi chorbi = this.chorbiService.findOne(chorbiId);
			final CreditCard c = chorbi.getCreditCard();
			c.setBrandName(brandName);
			c.setCvvCode(cvvCode);
			c.setExpirationMonth(expirationMonth);
			c.setExpirationYear(expirationYear);
			c.setHolderName(holderName);
			c.setNumber(number);
			this.chorbiService.saveAndFlush2(chorbi, c);
			this.unauthenticate();
		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);

	}

	@Test
	public void ChangeCreditCardInformation() {

		final Object testingData[][] = {
			// Cambiar la creditCard valida
			{
				"chorbi1", 63, BrandName.VISA, 151, 12, 2019, "Alvaro Gonzalez Jimenez", "4024007166826882", null
			},
			// Cambiar la credit card con un cvv no valido
			{
				"chorbi1", 63, BrandName.VISA, 01, 12, 2019, "Alvaro Gonzalez Jimenez", "4024007166826882", ConstraintViolationException.class
			},
			// Cambiar la credit card con un numero no valido
			{
				"chorbi1", 63, BrandName.VISA, 151, 12, 2015, "Alvaro Gonzalez Jimenez", "1233342314321444", ConstraintViolationException.class
			},
			// Cambiar la credit card con un campo en blanco
			{
				"chorbi1", 63, BrandName.VISA, 151, 12, 2015, " ", "4024007166826882", ConstraintViolationException.class
			},

		};

		for (int i = 0; i < testingData.length; i++)
			this.template((String) testingData[i][0], (int) testingData[i][1], (BrandName) testingData[i][2], (int) testingData[i][3], (int) testingData[i][4], (int) testingData[i][5], (String) testingData[i][6], (String) testingData[i][7],
				(Class<?>) testingData[i][8]);

	}
}
