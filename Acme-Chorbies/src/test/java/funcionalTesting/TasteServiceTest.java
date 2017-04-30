
package funcionalTesting;

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.ChorbiService;
import services.TasteService;
import utilities.AbstractTest;
import domain.Chorbi;
import domain.Taste;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class TasteServiceTest extends AbstractTest {

	//The SUT ---------------------------------------------------------------------------------

	@Autowired
	private TasteService	tasteService;

	//Auxiliar Services  ---------------------------------------------------------------------------------

	@Autowired
	private ChorbiService	chorbiService;


	//CASO DE USO : LIKE ANOTHER CHORBI ---------------------------------------------------------------------------------

	//Prueba 1: comprobando que chorbi4 puede dar like a chorbi5 añadiendo un comentario (POSITIVO)
	//Se harán las siguientes comprobaciones:
	//1.Hay un taste más en la base de datos
	//2.Hay un taste más en la collection de likes dados para el principal
	//3.Que ese like de más es el mismo que acabamos de crear y guardar
	//4. Que la collection que tiene los chorbies que le gustan al principal tenga ahora al chorbi5
	//5. Que la collection que tiene los chorbies que le han dado like al chorbi 5 esté el principal

	//Chorbi = 63,64,65,66,67,68
	@Test
	public void testCreateLikeWithComment() {

		this.authenticate("chorbi4");

		final Chorbi principal = this.chorbiService.findByPrincipal();

		final int beforeSave = principal.getGivenTastes().size();

		final Chorbi chorbiToLike = this.chorbiService.findOne(67);

		final Taste new_like = this.tasteService.create(chorbiToLike);
		new_like.setComment("comentario de prueba");
		final Taste reconstruct_like = this.tasteService.reconstruct(new_like, null, chorbiToLike);
		final Taste like = this.tasteService.save(reconstruct_like);

		this.tasteService.flush();

		final int afterSave = principal.getGivenTastes().size();

		Assert.isTrue(this.tasteService.findAll().size() == 5);
		Assert.isTrue(beforeSave < afterSave);
		Assert.isTrue(afterSave == beforeSave + 1);
		Assert.isTrue(principal.getGivenTastes().contains(like));

		final Collection<Chorbi> chorbisWhoLikeToPrincipal = this.chorbiService.findAllChorbiesWhoLikedByThisUser(principal);
		final Collection<Chorbi> chorbisWhoGivesThemLikesToChorbiToLike = this.chorbiService.findAllChorbiesWhoLikeThem(chorbiToLike);

		Assert.isTrue(chorbisWhoLikeToPrincipal.contains(chorbiToLike));
		Assert.isTrue(chorbisWhoGivesThemLikesToChorbiToLike.contains(principal));

	}

	//Prueba 2: comprobando que chorbi4 puede dar like a chorbi5 sin añadir un comentario (POSITIVO)
	//Se harán las siguientes comprobaciones:
	//1.Hay un taste más en la base de datos
	//2.Hay un taste más en la collection de likes dados para el principal
	//3.Que ese like de más es el mismo que acabamos de crear y guardar
	//4. Que la collection que tiene los chorbies que le gustan al principal tenga ahora al chorbi5
	//5. Que la collection que tiene los chorbies que le han dado like al chorbi 5 esté el principal

	@Test
	public void testCreateLikeWithOutComment() {

		this.authenticate("chorbi4");

		final Chorbi principal = this.chorbiService.findByPrincipal();

		final int beforeSave = principal.getGivenTastes().size();

		final Chorbi chorbiToLike = this.chorbiService.findOne(67);

		final Taste new_like = this.tasteService.create(chorbiToLike);
		final Taste reconstruct_like = this.tasteService.reconstruct(new_like, null, chorbiToLike);
		final Taste like = this.tasteService.save(reconstruct_like);

		this.tasteService.flush();

		final int afterSave = principal.getGivenTastes().size();

		Assert.isTrue(this.tasteService.findAll().size() == 5);
		Assert.isTrue(beforeSave < afterSave);
		Assert.isTrue(afterSave == beforeSave + 1);
		Assert.isTrue(principal.getGivenTastes().contains(like));

		final Collection<Chorbi> chorbisWhoLikeToPrincipal = this.chorbiService.findAllChorbiesWhoLikedByThisUser(principal);
		final Collection<Chorbi> chorbisWhoGivesThemLikesToChorbiToLike = this.chorbiService.findAllChorbiesWhoLikeThem(chorbiToLike);

		Assert.isTrue(chorbisWhoLikeToPrincipal.contains(chorbiToLike));
		Assert.isTrue(chorbisWhoGivesThemLikesToChorbiToLike.contains(principal));

	}

	//Prueba 3: comprobando que chorbi1 no puede dar like  dos veces a chorbi2 (NEGATIVO)

	@Test(expected = IllegalArgumentException.class)
	public void testCreateLikeTwice() {

		this.authenticate("chorbi1");
		final Chorbi chorbiToLike = this.chorbiService.findOne(64);

		final Taste new_like = this.tasteService.create(chorbiToLike);
		new_like.setComment("comentario de prueba");
		final Taste reconstruct_like = this.tasteService.reconstruct(new_like, null, chorbiToLike);
		this.tasteService.save(reconstruct_like);

		this.tasteService.flush();

	}

	//Prueba 4: comprobando que chorbi1 no puede darse like a sí mismo (NEGATIVO)

	@Test(expected = IllegalArgumentException.class)
	public void testCreateLikeToYourself() {

		this.authenticate("chorbi1");
		final Chorbi chorbiToLike = this.chorbiService.findOne(63);

		final Taste new_like = this.tasteService.create(chorbiToLike);
		new_like.setComment("comentario de prueba");
		final Taste reconstruct_like = this.tasteService.reconstruct(new_like, null, chorbiToLike);
		this.tasteService.save(reconstruct_like);

		this.tasteService.flush();

	}

	//Prueba 5: comprobando que chorbi1 no puede dar like a chorbi3 que está baneado (NEGATIVO)

	@Test(expected = IllegalArgumentException.class)
	public void testCreateLikeToBannedChorbi() {

		this.authenticate("chorbi1");
		final Chorbi chorbiToLike = this.chorbiService.findOne(65);

		final Taste new_like = this.tasteService.create(chorbiToLike);
		new_like.setComment("comentario de prueba");
		final Taste reconstruct_like = this.tasteService.reconstruct(new_like, null, chorbiToLike);
		this.tasteService.save(reconstruct_like);

		this.tasteService.flush();

	}

	//CASO DE USO : CANCEL LIKE ---------------------------------------------------------------------------------

	//Prueba 1: comprobando que el chorbi1 puede cancelar su like al chorbi2
	//Para este test comprobaremos:
	//1. Que el like traido de la base de datos pertenezca a chorbi1
	//2. Que el like traido de la base de datos tenga como destino el chorbi2
	//3. Que el like cancelado se elimine del sistema
	//4. Que el like cancelado ya no aparezca para ninguno de los dos implicados

	//Chorbi = 63,64,65,66,67,68
	//taste = 73,74,75,76 
	@Test
	public void testCancelLike() {

		this.authenticate("chorbi1");
		final Chorbi principal = this.chorbiService.findByPrincipal();
		final Chorbi chorbiToCancelLike = this.chorbiService.findOne(64);

		final int beforeCancel = this.tasteService.findAll().size();
		//64
		final Taste likeToCancel = this.tasteService.findOne(73);
		Assert.isTrue(likeToCancel.getChorbi().getId() == chorbiToCancelLike.getId());
		Assert.isTrue(principal.getGivenTastes().contains(likeToCancel));

		this.tasteService.delete(chorbiToCancelLike);

		this.tasteService.flush();

		Assert.isTrue(this.tasteService.findAll().size() == beforeCancel - 1);
		Assert.isTrue(!this.chorbiService.findAllChorbiesWhoLikedByThisUserForNotDoubleLike(principal).contains(chorbiToCancelLike));
		Assert.isTrue(!this.chorbiService.findAllChorbiesWhoLikeThem(chorbiToCancelLike).contains(principal));

	}

	//Prueba 2: comprobando que un chorbi no puede cancelar un like que no es suyo (pertenece a chorbi2)

	//taste = 73,74,75,76 
	@Test(expected = IllegalArgumentException.class)
	public void testCancelLikeNotMine() {

		this.authenticate("chorbi1");
		final Chorbi principal = this.chorbiService.findByPrincipal();

		final Taste likeToCancel = this.tasteService.findOne(75);
		Assert.isTrue(!principal.getGivenTastes().contains(likeToCancel));

		this.tasteService.delete(likeToCancel.getChorbi());

		this.tasteService.flush();

	}

	//Prueba 3: comprobando que no se puede cancelar un like que ya se había cancelado y que por tanto, no existe

	//taste = 73,74,75,76 
	@Test(expected = IllegalArgumentException.class)
	public void testCancelLikeNotExists() {

		this.authenticate("chorbi1");

		//Cancela la primera vez
		final Taste likeToCancel = this.tasteService.findOne(73);
		this.tasteService.delete(likeToCancel.getChorbi());
		this.tasteService.flush();

		//Cancela la segunda vez
		final Taste likeToCancel2 = this.tasteService.findOne(73);
		this.tasteService.delete(likeToCancel2.getChorbi());
		this.tasteService.flush();

	}

	//Prueba 4: comprobando que no se puede cancelar un like que  no existe

	@Test(expected = IllegalArgumentException.class)
	public void testCancelLikeNotExists2() {

		this.authenticate("chorbi1");

		//4585965 será el id utilizado para simular un like que no existe
		final Taste likeToCancel = this.tasteService.findOne(4585965);
		this.tasteService.delete(likeToCancel.getChorbi());
		this.tasteService.flush();

	}

	//Prueba 5 : comprobando que no se puede cancelar un like a un chorbi baneado

	//taste = 73,74,75,76 
	@Test(expected = IllegalArgumentException.class)
	public void testCancelLikeToBannedChorbi() {

		this.authenticate("chorbi1");
		final Chorbi principal = this.chorbiService.findByPrincipal();
		final Chorbi chorbiToCancelLike = this.chorbiService.findOne(65);

		final Taste likeToCancel = this.tasteService.findOne(74);
		Assert.isTrue(likeToCancel.getChorbi().getId() == chorbiToCancelLike.getId());
		Assert.isTrue(principal.getGivenTastes().contains(likeToCancel));

		this.tasteService.delete(chorbiToCancelLike);

		this.tasteService.flush();

	}

}
