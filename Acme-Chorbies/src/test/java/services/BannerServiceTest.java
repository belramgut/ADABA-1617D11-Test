
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Banner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class BannerServiceTest extends AbstractTest {

	// Service to test --------------------------------------------------------

	@Autowired
	private BannerService	bannerService;


	//Banner = 78,...,86
	//Admin = 51

	// FindAllBanners positivo
	@Test
	public void findAllBanners() {

		this.authenticate("admin");

		final Collection<Banner> b = this.bannerService.findAll();

		Assert.isTrue(this.bannerService.checkAdminPrincipal());
		System.out.println("-----------------------------------------------------------------------------------");
		System.out.println("--------------------------------FIND ALL BANNERS------------------------");
		System.out.println("-----------------------------------------------------------------------------------");

		System.out.println("Se espera obtener un size de 9: ");
		System.out.println("El tamaño de la lista es: " + b.size());

		this.unauthenticate();
	}

	// FindAllBanners negativo
	@Test(expected = IllegalArgumentException.class)
	public void findAllBannersNegative() {

		this.unauthenticate();

		final Collection<Banner> b = this.bannerService.findAll();

		Assert.isTrue(this.bannerService.checkAdminPrincipal());
		System.out.println("-----------------------------------------------------------------------------------");
		System.out.println("--------------------------------FIND ALL BANNERS NEGATIVE------------------------");
		System.out.println("-----------------------------------------------------------------------------------");

		System.out.println("No se espera obtener un size ");
		System.out.println("El tamaño de la lista es: " + b.size());

	}

	// FindOneBanner positivo
	@Test
	public void findOneBanner() {

		this.authenticate("admin");

		final Banner b = this.bannerService.findOne(79);
		Assert.isTrue(this.bannerService.checkAdminPrincipal());

		System.out.println("-----------------------------------------------------------------------------------");
		System.out.println("--------------------------------FIND ONE BANNER------------------------------------");
		System.out.println("-----------------------------------------------------------------------------------");

		System.out.println("Se espera obtener el Banner con id 79: ");
		System.out.println("Id: " + b.getId());

		this.unauthenticate();
	}

	// FinOneBanner negativo
	@Test(expected = IllegalArgumentException.class)
	public void findOneBannerNegative() {

		this.unauthenticate();

		final Banner b = this.bannerService.findOne(79);
		Assert.isTrue(this.bannerService.checkAdminPrincipal());

		System.out.println("-----------------------------------------------------------------------------------");
		System.out.println("--------------------------------FIND ONE BANNER------------------------------------");
		System.out.println("-----------------------------------------------------------------------------------");

		System.out.println("Se espera obtener el Banner con id 79: ");
		System.out.println("Id: " + b.getId());

	}
}
