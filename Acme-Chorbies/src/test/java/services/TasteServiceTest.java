
package services;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Chorbi;
import domain.Taste;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class TasteServiceTest extends AbstractTest {

	@Autowired
	private TasteService	tasteService;

	@Autowired
	private ChorbiService	chorbiService;


	//Test de creación positivo
	@Test
	public void testCreate() {

		this.authenticate("chorbi4");

		final Chorbi chorbiToLike = this.chorbiService.findOne(67);

		final Taste new_like = this.tasteService.create(chorbiToLike);
		new_like.setComment("comentario de test de creación");

		System.out.println("-----------------------------------------------------------------------------------");
		System.out.println("--------------------------------CREATE LIKE----------------------------------------");
		System.out.println("-----------------------------------------------------------------------------------");

		System.out.println("Comment: " + new_like.getComment());
		System.out.println("Chorbi to like (se espera el chorbi con id 67): " + new_like.getChorbi().getName() + " y tiene id: " + new_like.getChorbi().getId());

		this.unauthenticate();

	}

	//Test de creación negativo: no hay usuario autenticado

	@Test(expected = IllegalArgumentException.class)
	public void testCreateNegative() {

		this.unauthenticate();

		final Chorbi chorbiToLike = this.chorbiService.findOne(67);

		final Taste new_like = this.tasteService.create(chorbiToLike);
		new_like.setComment("comentario de test de creación");

		System.out.println("-----------------------------------------------------------------------------------");
		System.out.println("--------------------------------CREATE LIKE----------------------------------------");
		System.out.println("-----------------------------------------------------------------------------------");

		System.out.println("Comment: " + new_like.getComment());
		System.out.println("Chorbi to like (se espera el chorbi con id 67): " + new_like.getChorbi().getName() + " y tiene id: " + new_like.getChorbi().getId());

	}

	//Test de creación negativo: no hay chorbi al que dar like
	@Test(expected = IllegalArgumentException.class)
	public void testCreateNegative2() {

		this.authenticate("chorbi1");

		final Taste new_like = this.tasteService.create(null);
		new_like.setComment("comentario de test de creación");

		this.unauthenticate();

	}

	//Test de guardado positivo
	@Test
	public void testSave() {

		this.authenticate("chorbi4");

		final Chorbi chorbiToLike = this.chorbiService.findOne(67);

		final Chorbi principal = this.chorbiService.findByPrincipal();
		final int beforeSave = principal.getGivenTastes().size();

		final Taste new_like = this.tasteService.create(chorbiToLike);
		new_like.setComment("comentario de test de creación");
		final Taste reconstruct_like = this.tasteService.reconstruct(new_like, null, chorbiToLike);
		final Taste like = this.tasteService.save(reconstruct_like);

		this.tasteService.flush();

		final int afterSave = principal.getGivenTastes().size();
		Assert.isTrue(beforeSave < afterSave);
		Assert.isTrue(afterSave == beforeSave + 1);
		Assert.isTrue(principal.getGivenTastes().contains(like));

		System.out.println("-----------------------------------------------------------------------------------");
		System.out.println("--------------------------------SAVE LIKE------------------------------------------");
		System.out.println("-----------------------------------------------------------------------------------");

		System.out.println("El chorbi que da el like es (se espera chorbi 4): " + principal.getName());
		System.out.println("El momento del like es (se espera el momento actual): " + like.getMoment());
		System.out.println("El comentario del like es (se espera comentario de test de creación): " + like.getComment());
		System.out.println("El chorbi al que se da like es (se espera el chorbi 5 con id 67): " + like.getChorbi().getName() + " y tiene id: " + like.getChorbi().getId());

		this.unauthenticate();

	}

	//Test de guardado negativo: no hay like que guardar
	@Test(expected = IllegalArgumentException.class)
	public void testSaveNegative1() {

		this.authenticate("chorbi4");

		this.tasteService.save(null);

		this.unauthenticate();

	}

	//Test de guardado negativo : no hay usuario autenticado
	@Test(expected = IllegalArgumentException.class)
	public void testSaveNegative2() {

		this.unauthenticate();

		final Chorbi chorbiToLike = this.chorbiService.findOne(67);

		final Taste new_like = this.tasteService.create(chorbiToLike);
		new_like.setComment("comentario de test de creación");
		final Taste reconstruct_like = this.tasteService.reconstruct(new_like, null, chorbiToLike);
		this.tasteService.save(reconstruct_like);

		this.tasteService.flush();

		this.unauthenticate();

	}

	//Test de guardado negativo : el usuario logueado intenta darse like a sí mismo
	@Test(expected = IllegalArgumentException.class)
	public void testSaveNegative3() {

		this.authenticate("chorbi5");

		final Chorbi chorbiToLike = this.chorbiService.findOne(67);

		final Taste new_like = this.tasteService.create(chorbiToLike);
		new_like.setComment("comentario de test de creación");
		final Taste reconstruct_like = this.tasteService.reconstruct(new_like, null, chorbiToLike);
		this.tasteService.save(reconstruct_like);

		this.tasteService.flush();

		this.unauthenticate();

	}

	//Test de guardado negativo : el usuario intenta dar like dos veces al mismo usuario
	@Test(expected = IllegalArgumentException.class)
	public void testSaveNegative4() {

		this.authenticate("chorbi1");

		final Chorbi chorbiToLike = this.chorbiService.findOne(64);

		final Taste new_like = this.tasteService.create(chorbiToLike);
		new_like.setComment("comentario de test de creación");
		final Taste reconstruct_like = this.tasteService.reconstruct(new_like, null, chorbiToLike);
		this.tasteService.save(reconstruct_like);

		this.tasteService.flush();

		this.unauthenticate();

	}

	//Test de guardado negativo : el usuario intenta dar like a un usuario baneado
	@Test(expected = IllegalArgumentException.class)
	public void testSaveNegative5() {

		this.authenticate("chorbi1");

		final Chorbi chorbiToLike = this.chorbiService.findOne(65);

		final Taste new_like = this.tasteService.create(chorbiToLike);
		new_like.setComment("comentario de test de creación");
		final Taste reconstruct_like = this.tasteService.reconstruct(new_like, null, chorbiToLike);
		this.tasteService.save(reconstruct_like);

		this.tasteService.flush();

		this.unauthenticate();

	}

	//Test borrado positivo
	@Test
	public void testDelete() {

		this.authenticate("chorbi1");
		final Chorbi principal = this.chorbiService.findByPrincipal();
		final int numlikesdados_before = principal.getGivenTastes().size();
		final Chorbi chorbiWithLikeToCancel = this.chorbiService.findOne(64);
		this.tasteService.delete(chorbiWithLikeToCancel);
		this.tasteService.flush();
		final int numlikesdados_after = principal.getGivenTastes().size();

		this.unauthenticate();

		System.out.println("-----------------------------------------------------------------------------------");
		System.out.println("--------------------------------DELETE LIKE----------------------------------------");
		System.out.println("-----------------------------------------------------------------------------------");

		System.out.println("El número de likes antes de hacer el borrado para el usuario autenticado era (se espera 2): " + numlikesdados_before);
		System.out.println("El número de likes después de hacer el borrado para el usuario autenticado era (se espera 1): " + numlikesdados_after);

	}
	//Test borrado negativo : no hay usuario autenticado
	@Test(expected = IllegalArgumentException.class)
	public void testDeleteNegative() {

		this.unauthenticate();

		final Chorbi chorbiWithLikeToCancel = this.chorbiService.findOne(64);
		this.tasteService.delete(chorbiWithLikeToCancel);
		this.tasteService.flush();

	}

	//Test borrado negativo : el usuario logueado pretende cancelar un like a sí mismo
	@Test(expected = IllegalArgumentException.class)
	public void testDeleteNegative2() {

		this.authenticate("chorbi1");

		final Chorbi chorbiWithLikeToCancel = this.chorbiService.findOne(63);
		this.tasteService.delete(chorbiWithLikeToCancel);
		this.tasteService.flush();

		this.unauthenticate();

	}

	//Test borrado negativo : el usuario logueado pretende cancelar un like que no existe
	@Test(expected = IllegalArgumentException.class)
	public void testDeleteNegative3() {

		this.authenticate("chorbi1");

		final Chorbi chorbiWithLikeToCancel = this.chorbiService.findOne(68);
		this.tasteService.delete(chorbiWithLikeToCancel);
		this.tasteService.flush();

		this.unauthenticate();

	}

	//Test borrado negativo : el usuario logueado pretende cancelar un like a un chorbi baneado
	@Test(expected = IllegalArgumentException.class)
	public void testDeleteNegative4() {

		this.authenticate("chorbi1");

		final Chorbi chorbiWithLikeToCancel = this.chorbiService.findOne(65);
		this.tasteService.delete(chorbiWithLikeToCancel);
		this.tasteService.flush();

		this.unauthenticate();

	}

}
