
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Chorbi;
import domain.Taste;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ChorbiServiceTest extends AbstractTest {

	@Autowired
	private ChorbiService	chorbiService;


	//Find one positivo: con usuario logueado
	@Test
	public void findOneChorbi() {

		this.authenticate("chorbi1");

		final Chorbi c = this.chorbiService.findOne(64);

		System.out.println("-----------------------------------------------------------------------------------");
		System.out.println("--------------------------------FIND ONE CHORBI------------------------------------");
		System.out.println("-----------------------------------------------------------------------------------");

		System.out.println("Se espera obtener el Chorbi 2 con id 64: ");
		System.out.println("Nombre: " + c.getName());
		System.out.println("Id: " + c.getId());

		this.unauthenticate();

	}

	//Find one negativo: sin usuario logueado
	@Test(expected = IllegalArgumentException.class)
	public void findOneChorbiNegative() {

		this.unauthenticate();

		final Chorbi c = this.chorbiService.findOne(64);

		System.out.println("-----------------------------------------------------------------------------------");
		System.out.println("--------------------------------FIND ONE CHORBI------------------------------------");
		System.out.println("-----------------------------------------------------------------------------------");

		System.out.println("Se espera obtener el Chorbi 2 con id 55: ");
		System.out.println("Nombre: " + c.getName());
		System.out.println("Id: " + c.getId());

	}

	//FindAllNotBannedChorbies positivo
	@Test
	public void findAllNotBannedChorbies() {

		this.authenticate("chorbi1");

		final Collection<Chorbi> c = this.chorbiService.findAllNotBannedChorbies();

		boolean hayAlgunBaneado = false;

		for (final Chorbi chorbi : c)
			if (chorbi.isBan() == true)
				hayAlgunBaneado = true;

		System.out.println("-----------------------------------------------------------------------------------");
		System.out.println("--------------------------------FIND ALL NOT BANNED CHORBIES------------------------");
		System.out.println("-----------------------------------------------------------------------------------");

		System.out.println("Se espera obtener un false y un size de 5: ");
		System.out.println("¿Hay algún usuario baneado en esta lista? : " + hayAlgunBaneado);
		System.out.println("El tamaño de la lista es: " + c.size());

		this.unauthenticate();

	}

	//FindAllNotBannedChorbies negativo: no hay usuario autenticado
	@Test(expected = IllegalArgumentException.class)
	public void findAllNotBannedChorbiesNegative() {

		this.unauthenticate();

		final Collection<Chorbi> c = this.chorbiService.findAllNotBannedChorbies();

		boolean hayAlgunBaneado = false;

		for (final Chorbi chorbi : c)
			if (chorbi.isBan() == true)
				hayAlgunBaneado = true;

		System.out.println("-----------------------------------------------------------------------------------");
		System.out.println("--------------------------------FIND ALL NOT BANNED CHORBIES------------------------");
		System.out.println("-----------------------------------------------------------------------------------");

		System.out.println("Se espera obtener un false y un size de 5: ");
		System.out.println("¿Hay algún usuario baneado en esta lista? : " + hayAlgunBaneado);
		System.out.println("El tamaño de la lista es: " + c.size());

	}

	//FindAllChorbiesWhoLikeThem positivo

	@Test
	public void findAllChorbiesWhoLikeThem() {

		this.authenticate("chorbi2");
		final Chorbi principal = this.chorbiService.findByPrincipal();

		final Collection<Chorbi> c = this.chorbiService.findAllChorbiesWhoLikeThem(principal);

		boolean hayAlgunBaneado = false;

		for (final Chorbi chorbi : c)
			if (chorbi.isBan() == true)
				hayAlgunBaneado = true;

		System.out.println("-----------------------------------------------------------------------------------");
		System.out.println("----------------------FIND ALL CHORBIES WHO LIKE THEM CHORBIES---------------------");
		System.out.println("-----------------------------------------------------------------------------------");

		System.out.println("Se espera obtener un false y un size de 1: ");
		System.out.println("¿Hay algún usuario baneado en esta lista? : " + hayAlgunBaneado);
		System.out.println("El tamaño de la lista es: " + c.size());

		this.unauthenticate();

	}

	//FindAllChorbiesWhoLikeThem negativo: no hay usuario autenticado

	@Test(expected = IllegalArgumentException.class)
	public void findAllChorbiesWhoLikeThemNegative() {

		this.unauthenticate();
		final Chorbi principal = this.chorbiService.findByPrincipal();

		final Collection<Chorbi> c = this.chorbiService.findAllChorbiesWhoLikeThem(principal);

		boolean hayAlgunBaneado = false;

		for (final Chorbi chorbi : c)
			if (chorbi.isBan() == true)
				hayAlgunBaneado = true;

		System.out.println("-----------------------------------------------------------------------------------");
		System.out.println("----------------------FIND ALL CHORBIES WHO LIKE THEM CHORBIES---------------------");
		System.out.println("-----------------------------------------------------------------------------------");

		System.out.println("Se espera obtener un false y un size de 1: ");
		System.out.println("¿Hay algún usuario baneado en esta lista? : " + hayAlgunBaneado);
		System.out.println("El tamaño de la lista es: " + c.size());

	}

	//FindAllChorbiesWhoLikeThis positivo

	@Test
	public void findAllChorbiesWhoLikeByThis() {

		this.authenticate("chorbi1");
		final Chorbi principal = this.chorbiService.findByPrincipal();

		final Collection<Chorbi> c = this.chorbiService.findAllChorbiesWhoLikedByThisUser(principal);

		boolean hayAlgunBaneado = false;

		for (final Chorbi chorbi : c)
			if (chorbi.isBan() == true)
				hayAlgunBaneado = true;

		System.out.println("-----------------------------------------------------------------------------------");
		System.out.println("----------------------FIND ALL CHORBIES WHO LIKE BY THIS CHORBI---------------------");
		System.out.println("-----------------------------------------------------------------------------------");

		System.out.println("Se espera obtener un false y un size de 1: ");
		System.out.println("¿Hay algún usuario baneado en esta lista? : " + hayAlgunBaneado);
		System.out.println("El tamaño de la lista es: " + c.size());

		this.unauthenticate();

	}

	//FindAllChorbiesWhoLikeThis negativo

	@Test(expected = IllegalArgumentException.class)
	public void findAllChorbiesWhoLikeByThisNegativa() {

		this.unauthenticate();
		final Chorbi principal = this.chorbiService.findByPrincipal();

		final Collection<Chorbi> c = this.chorbiService.findAllChorbiesWhoLikedByThisUser(principal);

		boolean hayAlgunBaneado = false;

		for (final Chorbi chorbi : c)
			if (chorbi.isBan() == true)
				hayAlgunBaneado = true;

		System.out.println("-----------------------------------------------------------------------------------");
		System.out.println("----------------------FIND ALL CHORBIES WHO LIKE BY THIS CHORBI---------------------");
		System.out.println("-----------------------------------------------------------------------------------");

		System.out.println("Se espera obtener un false y un size de 1: ");
		System.out.println("¿Hay algún usuario baneado en esta lista? : " + hayAlgunBaneado);
		System.out.println("El tamaño de la lista es: " + c.size());

	}

	//findAllChorbiesWhoLikedByThisUserForNotDoubleLike positivo

	@Test
	public void findAllChorbiesWhoLikedByThisUserForNotDoubleLike() {

		this.authenticate("chorbi1");

		final Chorbi principal = this.chorbiService.findByPrincipal();

		final Collection<Chorbi> c = this.chorbiService.findAllChorbiesWhoLikedByThisUserForNotDoubleLike(principal);

		boolean hayAlgunBaneado = false;

		for (final Chorbi chorbi : c)
			if (chorbi.isBan() == true)
				hayAlgunBaneado = true;

		this.unauthenticate();

		System.out.println("-----------------------------------------------------------------------------------");
		System.out.println("----------------------findAllChorbiesWhoLikedByThisUserForNotDoubleLike-------------");
		System.out.println("-----------------------------------------------------------------------------------");

		System.out.println("Se espera obtener un true y un size de 2: ");
		System.out.println("¿Hay algún usuario baneado en esta lista? : " + hayAlgunBaneado);
		System.out.println("El tamaño de la lista es: " + c.size());

	}

	//findAllChorbiesWhoLikedByThisUserForNotDoubleLike negativo: no hay usuario logueado

	@Test(expected = IllegalArgumentException.class)
	public void findAllChorbiesWhoLikedByThisUserForNotDoubleLikeNegative() {

		this.unauthenticate();

		final Chorbi principal = this.chorbiService.findByPrincipal();

		final Collection<Chorbi> c = this.chorbiService.findAllChorbiesWhoLikedByThisUserForNotDoubleLike(principal);

		boolean hayAlgunBaneado = false;

		for (final Chorbi chorbi : c)
			if (chorbi.isBan() == true)
				hayAlgunBaneado = true;

		System.out.println("-----------------------------------------------------------------------------------");
		System.out.println("----------------------findAllChorbiesWhoLikedByThisUserForNotDoubleLike-------------");
		System.out.println("-----------------------------------------------------------------------------------");

		System.out.println("Se espera obtener un true y un size de 2: ");
		System.out.println("¿Hay algún usuario baneado en esta lista? : " + hayAlgunBaneado);
		System.out.println("El tamaño de la lista es: " + c.size());

	}

	//findAllMyTastesWithoutBannedChorbies positivo

	@Test
	public void findAllMyTastesWithoutBannedChorbies() {

		this.authenticate("chorbi1");

		final Chorbi principal = this.chorbiService.findByPrincipal();

		final Collection<Taste> c = this.chorbiService.findAllMyTastesWithoutBannedChorbies(principal);

		boolean hayAlgunBaneado = false;

		for (final Taste t : c)
			if (t.getChorbi().isBan() == true)
				hayAlgunBaneado = true;

		this.unauthenticate();

		System.out.println("-----------------------------------------------------------------------------------");
		System.out.println("----------------------findAllMyTastesWithoutBannedChorbies-------------------------");
		System.out.println("-----------------------------------------------------------------------------------");

		System.out.println("Se espera obtener un false y un size de 1: ");
		System.out.println("¿Hay algún usuario baneado en esta lista? : " + hayAlgunBaneado);
		System.out.println("El tamaño de la lista es: " + c.size());

	}

	//findAllTastesToMeWithoutBannedChorbies positivo

	@Test
	public void findAllTastesToMeWithoutBannedChorbies() {

		this.authenticate("chorbi1");

		final Chorbi principal = this.chorbiService.findByPrincipal();

		final Collection<Chorbi> c = this.chorbiService.findAllTastesToMeWithoutBannedChorbies(principal);

		boolean hayAlgunBaneado = false;

		for (final Chorbi chorbi : c)
			if (chorbi.isBan() == true)
				hayAlgunBaneado = true;

		this.unauthenticate();

		System.out.println("-----------------------------------------------------------------------------------");
		System.out.println("----------------------findAllTastesToMeWithoutBannedChorbies-------------------------");
		System.out.println("-----------------------------------------------------------------------------------");

		System.out.println("Se espera obtener un false y un size de 1: ");
		System.out.println("¿Hay algún usuario baneado en esta lista? : " + hayAlgunBaneado);
		System.out.println("El tamaño de la lista es: " + c.size());

	}

	//banChorbi: test positivo 

	@Test
	public void banChorbi() {
		this.authenticate("admin");

		final Chorbi chorbi = this.chorbiService.findOneToSent(63);

		boolean haSidoBanneado = false;

		this.chorbiService.banChorbi(chorbi);

		this.unauthenticate();

		final Chorbi bannedChorbi = this.chorbiService.findOneToSent(63);

		if (bannedChorbi.isBan() == true)
			haSidoBanneado = true;

		System.out.println("-----------------------------------------------------------------------------------");
		System.out.println("----------------------------------banChorbi----------------------------------------");
		System.out.println("-----------------------------------------------------------------------------------");

		System.out.println("Se espera obtener un true ");
		System.out.println("¿El chorbi ha sido banneado? : " + haSidoBanneado);

	}

	//banChorbi: test negativo

	@Test(expected = IllegalArgumentException.class)
	public void banChorbiNegative() {
		this.authenticate("admin");

		final Chorbi chorbi = this.chorbiService.findOneToSent(65);

		boolean haSidoBanneado = false;

		this.chorbiService.banChorbi(chorbi);

		this.unauthenticate();

		final Chorbi bannedChorbi = this.chorbiService.findOneToSent(65);

		if (bannedChorbi.isBan() == true)
			haSidoBanneado = true;

		System.out.println("-----------------------------------------------------------------------------------");
		System.out.println("----------------------------------banChorbiNevative----------------------------------------");
		System.out.println("-----------------------------------------------------------------------------------");

		System.out.println("Se espera obtener un true ");
		System.out.println("¿El chorbi ha sido banneado? : " + haSidoBanneado);

	}

	@Test
	public void unBanChorbi() {
		this.authenticate("admin");

		final Chorbi chorbi = this.chorbiService.findOneToSent(65);

		boolean haSidoPermitido = false;

		this.chorbiService.unBanChorbi(chorbi);

		this.unauthenticate();

		final Chorbi bannedChorbi = this.chorbiService.findOneToSent(65);

		if (bannedChorbi.isBan() == false)
			haSidoPermitido = true;

		System.out.println("-----------------------------------------------------------------------------------");
		System.out.println("----------------------------------UnbanChorbi----------------------------------------");
		System.out.println("-----------------------------------------------------------------------------------");

		System.out.println("Se espera obtener un true ");
		System.out.println("¿El chorbi ha sido banneado? : " + haSidoPermitido);

	}

	//banChorbi: test negativo

	@Test(expected = IllegalArgumentException.class)
	public void unBanChorbiNegative() {
		this.authenticate("admin");

		final Chorbi chorbi = this.chorbiService.findOneToSent(63);

		boolean haSidoPermitido = false;

		this.chorbiService.unBanChorbi(chorbi);

		this.unauthenticate();

		final Chorbi bannedChorbi = this.chorbiService.findOneToSent(63);

		if (bannedChorbi.isBan() == true)
			haSidoPermitido = true;

		System.out.println("-----------------------------------------------------------------------------------");
		System.out.println("----------------------------------banChorbiNevative----------------------------------------");
		System.out.println("-----------------------------------------------------------------------------------");

		System.out.println("Se espera obtener un true ");
		System.out.println("¿El chorbi ha sido banneado? : " + haSidoPermitido);

	}

}
