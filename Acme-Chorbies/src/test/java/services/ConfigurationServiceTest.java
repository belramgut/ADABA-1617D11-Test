
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
import domain.Configuration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ConfigurationServiceTest extends AbstractTest {

	@Autowired
	private ConfigurationService	configService;


	// Configuration = 77
	// Admin = 51

	// FindAllConfigs positivo
	@Test
	public void findAllConfigs() {

		this.authenticate("admin");

		final Collection<Configuration> c = this.configService.findAll();

		Assert.isTrue(this.configService.checkAdminPrincipal());
		System.out.println("-----------------------------------------------------------------------------------");
		System.out.println("--------------------------------FIND ALL CONFIG------------------------");
		System.out.println("-----------------------------------------------------------------------------------");

		System.out.println("Se espera obtener un size de 1: ");
		System.out.println("El tamaño de la lista es: " + c.size());

		this.unauthenticate();
	}

	// FindAllConfigs negativo
	@Test(expected = IllegalArgumentException.class)
	public void findAllConfigsNegative() {

		this.unauthenticate();

		final Collection<Configuration> c = this.configService.findAll();

		Assert.isTrue(this.configService.checkAdminPrincipal());
		System.out.println("-----------------------------------------------------------------------------------");
		System.out.println("--------------------------------FIND ALL CONFIG------------------------");
		System.out.println("-----------------------------------------------------------------------------------");

		System.out.println("Se espera obtener un size de 1: ");
		System.out.println("El tamaño de la lista es: " + c.size());

	}

	// FindOneConfig positivo
	@Test
	public void findOneConfig() {

		this.authenticate("admin");

		final Configuration c = this.configService.findOne(77);

		Assert.isTrue(this.configService.checkAdminPrincipal());
		System.out.println("-----------------------------------------------------------------------------------");
		System.out.println("--------------------------------FIND ONE CONFIG------------------------------------");
		System.out.println("-----------------------------------------------------------------------------------");

		System.out.println("Se espera obtener la configuracion con id 77: ");
		System.out.println("Id: " + c.getId());

		this.unauthenticate();
	}

	// FindOneConfig negativo
	@Test(expected = IllegalArgumentException.class)
	public void findOneConfigNegative() {

		this.unauthenticate();

		final Configuration c = this.configService.findOne(77);

		Assert.isTrue(this.configService.checkAdminPrincipal());
		System.out.println("-----------------------------------------------------------------------------------");
		System.out.println("--------------------------------FIND ONE CONFIG------------------------------------");
		System.out.println("-----------------------------------------------------------------------------------");

		System.out.println("Se espera obtener la configuracion con id 77: ");
		System.out.println("Id: " + c.getId());

	}
}
