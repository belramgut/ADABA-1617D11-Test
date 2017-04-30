
package funcionalTesting;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.BannerService;
import utilities.AbstractTest;
import domain.Banner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class BannerServiceTest extends AbstractTest {

	@Autowired
	private BannerService	bannerService;


	//CASO DE USO DESTINADO A PROBAR LA EDICIÓN DE UN BANNER
	protected void templateEditBanner(final String username, final String url, final int idBanner, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {

			this.authenticate(username);

			final Banner b = this.bannerService.findOne(idBanner);

			final String url_inicial = b.getUrl();

			b.setUrl(url);

			this.bannerService.save(b);
			this.bannerService.flush();

			Assert.isTrue(url_inicial != b.getUrl());

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	@Test
	public void driverEditBanner() {

		final Object testingData[][] = {
			{	//Edición correcta de un banner
				"admin", "https://c1.staticflickr.com/3/2216/2202761744_5964045554_m.jpg", 78, null
			}, {//Sólo el administrador puede editar un banner
				"chorbi1", "https://c1.staticflickr.com/3/2216/2202761744_5964045554_m.jpg", 78, IllegalArgumentException.class
			}, {//La url del banner no puede ser vacía
				"admin", "", 78, ConstraintViolationException.class
			}, {//La url del banner no puede ser nula
				"admin", null, 78, ConstraintViolationException.class
			}, {//La cadena del banner debe ser una url
				"admin", "holaestoesunacadenasinurl", 78, ConstraintViolationException.class
			},

		};

		for (int i = 0; i < testingData.length; i++)
			this.templateEditBanner((String) testingData[i][0], (String) testingData[i][1], (int) testingData[i][2], (Class<?>) testingData[i][3]);

	}
}
