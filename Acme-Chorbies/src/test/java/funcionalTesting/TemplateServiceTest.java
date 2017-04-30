
package funcionalTesting;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.ChorbiService;
import services.ConfigurationService;
import services.CreditCardService;
import services.TemplateService;
import utilities.AbstractTest;
import domain.Chorbi;
import domain.Configuration;
import domain.Coordinate;
import domain.CreditCard;
import domain.Genre;
import domain.Relationship;
import domain.Template;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class TemplateServiceTest extends AbstractTest {

	// Service to test --------------------------------------------------------

	@Autowired
	private TemplateService			templateService;

	@Autowired
	private ChorbiService			chorbiService;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private CreditCardService		creditCardService;


	//Destinado a probar el caso de uso de editar tu propio Template para realizar distintas busquedas
	protected void templateEdit(final String username, final Genre genre, final Relationship relationShip, final Integer approximatedAge, final String keyword, final Integer templateId, final Class<?> expected) {

		Class<?> caught;

		caught = null;

		try {

			this.authenticate(username);

			final Template template = this.templateService.findOneToEdit(templateId);

			template.setApproximatedAge(approximatedAge);
			template.setGenre(genre);
			template.setRelationShip(relationShip);
			template.setKeyword(keyword);

			this.templateService.save(template);

		} catch (final Throwable oops) {

			caught = oops.getClass();

		}
		this.templateService.flush();

		this.checkExceptions(expected, caught);

	}

	// templateChorbi1=57, templateChorbi2=58,...,etc.
	@Test
	public void driverEditTemplate() {

		final Object testingData[][] = {

			//Editar una template correctamente

			{
				"chorbi1", Genre.MALE, Relationship.LOVE, 25, "hola", 57, null
			},

			//Intentar editar un template con genre a null

			{
				"chorbi1", null, Relationship.ACTIVITIES, 30, "only girls", 57, null
			},

			//Intentar editar un template con relationship a null

			{
				"chorbi1", Genre.FEMALE, null, 23, "only a coffe", 57, null
			},

			//Intentar editar un template con approximated age a null

			{
				"chorbi1", Genre.FEMALE, Relationship.ACTIVITIES, null, "only sport", 57, null
			},

			//Intentar editar un template con keyword vacia

			{
				"chorbi1", Genre.FEMALE, Relationship.FRIENDSHIP, 21, "", 57, null
			},

			//Intentar editar un template con todos los parametros vacios 

			{
				"chorbi1", null, null, null, "", 57, null
			},

			//Intentar editar un template que no es del usuario principal 

			{
				"chorbi1", Genre.MALE, Relationship.LOVE, 25, "hola", 58, IllegalArgumentException.class
			},

			//Intentar editar un template que no existe

			{
				"chorbi1", Genre.MALE, Relationship.LOVE, 25, "hola", 5000, NullPointerException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)

			this.templateEdit((String) testingData[i][0], (Genre) testingData[i][1], (Relationship) testingData[i][2],

			(Integer) testingData[i][3], (String) testingData[i][4], (Integer) testingData[i][5],

			(Class<?>) testingData[i][6]);

	}

	protected void templateEditCoordinate(final String username, final String country,

	final String state, final String province, final String city, final Template template, final Class<?> expected) {

		Class<?> caught;

		caught = null;

		try {

			this.authenticate(username);

			Coordinate coordinate;
			coordinate = template.getCoordinate();

			coordinate.setCity(city);
			coordinate.setCountry(country);
			coordinate.setProvince(province);
			coordinate.setState(state);

			template.setCoordinate(coordinate);
			this.templateService.save(template);

		} catch (final Throwable oops) {

			caught = oops.getClass();

		}
		this.templateService.flush();

		this.checkExceptions(expected, caught);

	}

	@Test
	public void driverEditCoordinate() {

		final Template template1 = this.chorbiService.findOneToSent(63).getTemplate(); // Template del chorbi con id = 63

		final Object testingData[][] = {

			//Editar una Coordinate correctamente

			{
				"chorbi1", "Francia", "Francia", "Paris", "Paris", template1, null
			},

			//Intentar editar una coordinate con el pais en blanco

			{
				"chorbi1", "", "Francia", "Paris", "Paris", template1, null
			},

			//Intentar editar una coordinate con la ciudad en blanco

			{
				"chorbi1", "Francia", "Francia", "Paris", "", template1, null
			}

		};

		for (int i = 0; i < testingData.length; i++)

			this.templateEditCoordinate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2],

			(String) testingData[i][3], (String) testingData[i][4], (Template) testingData[i][5],

			(Class<?>) testingData[i][6]);

	}

	//Destinado a probar el caso de uso donde se muestran los resultados de un template
	protected void templateSearch(final String username, final Integer chorbiId, final Integer opcionId, final Class<?> expected) {

		Class<?> caught;

		caught = null;

		try {

			this.authenticate(username);

			if (opcionId == 0) {

				final Chorbi chorbi = this.chorbiService.findOneToSent(chorbiId);
				final Template template = chorbi.getTemplate();
				final CreditCard c = chorbi.getCreditCard();

				Assert.isTrue(this.creditCardService.validateDate(c.getExpirationMonth(), c.getExpirationYear()));

				final Collection<Chorbi> chorbiesIniciales = template.getChorbies();

				final Configuration confi = this.configurationService.findConfiguration();
				final Integer horaTotal = this.configurationService.getHoraConfiguration(confi);

				final Date momentTemplate = new Date();
				final Date currentDate = new Date();

				final long diff = (currentDate.getTime() - 10000) - momentTemplate.getTime();
				final long minutes = (diff / 1000) / 60;

				Collection<Chorbi> chorbiesBuscados = new ArrayList<Chorbi>();

				if (minutes > horaTotal || template.getChorbies().isEmpty())
					chorbiesBuscados = this.templateService.findChorbiesByMyTemplate(template);
				else
					chorbiesBuscados = template.getChorbies();

				Assert.isTrue(chorbiesBuscados.size() == chorbiesIniciales.size());

			}

			if (opcionId == 1) {
				final Chorbi chorbi = this.chorbiService.findOneToSent(chorbiId);
				final Template template = chorbi.getTemplate();
				final CreditCard c = chorbi.getCreditCard();

				Assert.isTrue(this.creditCardService.validateDate(c.getExpirationMonth(), c.getExpirationYear()));

				final Collection<Chorbi> chorbiesIniciales = template.getChorbies();

				final Configuration confi = this.configurationService.findConfiguration();
				final Integer horaTotal = this.configurationService.getHoraConfiguration(confi);

				final Date momentTemplate = template.getMoment();
				final Date currentDate = new Date();

				final long diff = (currentDate.getTime() - 10000) - momentTemplate.getTime();
				final long minutes = (diff / 1000) / 60;

				Collection<Chorbi> chorbiesBuscados = new ArrayList<Chorbi>();

				if (minutes > horaTotal || template.getChorbies().isEmpty())
					chorbiesBuscados = this.templateService.findChorbiesByMyTemplate(template);
				else
					chorbiesBuscados = template.getChorbies();

				Assert.isTrue(chorbiesBuscados.size() != chorbiesIniciales.size());

			}

		} catch (final Throwable oops) {

			caught = oops.getClass();

		}
		this.templateService.flush();

		this.checkExceptions(expected, caught);

	}
	// templateChorbi1=57, templateChorbi2=58,...,etc.
	//Chorbi = 63,64,65,66,67,68
	@Test
	public void driverTemplateSearch() {

		final Object testingData[][] = {

			{
				//destinado a mostrar los mismo resultados ya almacenados, debido a que la hora de cacheo no ha sido superada
				"chorbi1", 63, 0, null
			}, {
				//destinado a mostrar que los resultados de busqueda son distintos, debido a que la hora de cacheo ha sido superada
				"chorbi1", 63, 1, null
			}, {
				//destinado a probar una busqueda cuando el usuario tiene una tarjeta de credito invalida, no debe mostrar nada.
				"chorbi5", 67, 1, IllegalArgumentException.class
			}, {
				//Realiza una busqueda un chorbi que no existe en el sistema
				"chorbi1", 5000, 1, IllegalArgumentException.class
			}, {
				//Un usuario no utenticando intenta realizar una busqueda
				null, 5000, 1, IllegalArgumentException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)

			this.templateSearch((String) testingData[i][0], (Integer) testingData[i][1], (Integer) testingData[i][2], (Class<?>) testingData[i][3]);

	}

}
