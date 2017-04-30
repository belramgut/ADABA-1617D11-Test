
package funcionalTesting;

import java.util.Collection;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import services.ChirpService;
import services.ChorbiService;
import utilities.AbstractTest;
import domain.Chirp;
import domain.Chorbi;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ChirpServiceTest extends AbstractTest {

	// Service to test --------------------------------------------------------

	@Autowired
	private ChirpService	chirpService;

	@Autowired
	private ChorbiService	chorbiService;


	//Chorbi = 63,64,65,66,67,68

	//Crear chirp sin errores de validacion y otros casos comunes
	protected void template(final String username, final int enviarId, final int recibirId, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			this.authenticate(username);

			final Chorbi envia = this.chorbiService.findOneToSent(enviarId);
			final Chorbi recibe = this.chorbiService.findOneToSent(recibirId);

			final Collection<Chirp> enviadosInicial = envia.getChirpWrites();
			final int numeroDeMensajesEnviados = enviadosInicial.size();

			final Collection<Chirp> recibidosInicial = recibe.getChirpReceives();
			final int numeroDeMensajesRecibidos = recibidosInicial.size();

			final Chirp pm = this.chirpService.create();

			pm.setAttachments("");
			pm.setCopy(true);
			pm.setRecipient(recibe);
			pm.setSender(envia);
			pm.setText("Me gustaria asistir mañana dia 15 a las 9:00");
			pm.setSubject("Tutoria");

			this.chirpService.save(pm);

			Assert.isTrue(envia.getChirpWrites().size() == numeroDeMensajesEnviados + 1);
			Assert.isTrue(recibe.getChirpReceives().size() == numeroDeMensajesRecibidos + 1);

			this.unauthenticate();
			this.chirpService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);

	}

	//Chorbi = 63,64,65,66,67,68
	@Test
	public void driver() {

		final Object testingData[][] = {
			{   //chorbi 1 envia chirp correcto a chorbi 2
				"chorbi1", 63, 64, null
			}, {
				//chorbi 2 enviar chirp correcto a chorbi 1
				"chorbi2", 64, 63, null
			}, {
				//chorbi 1 envia chirp correcto a chorbi 3
				"chorbi1", 63, 65, null
			}, {
				//Simular post hacking. Logeado como chorbi 3, pero en realidad soy chorbi 2 intentando enviar chirp a chorbi 1
				"chorbi3", 64, 63, IllegalArgumentException.class
			}, {
				//Usuario no autenticado intenta enviar chirp a otro actor.
				null, 63, 64, IllegalArgumentException.class
			}, {
				//Chorbi 1 intenta enviar a un chorbi que no existe un chirp
				"chorbi1", 63, 5000, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.template((String) testingData[i][0], (int) testingData[i][1], (int) testingData[i][2], (Class<?>) testingData[i][3]);

	}

	//Crear mensajes con errores de validacion
	protected void template2(final String username, final int enviarId, final int recibirId, final int opcionId, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			this.authenticate(username);

			final Chorbi envia = this.chorbiService.findOneToSent(enviarId);
			final Chorbi recibe = this.chorbiService.findOneToSent(recibirId);

			final Collection<Chirp> enviadosInicial = envia.getChirpWrites();
			final int numeroDeMensajesEnviados = enviadosInicial.size();

			final Collection<Chirp> recibidosInicial = recibe.getChirpReceives();
			final int numeroDeMensajesRecibidos = recibidosInicial.size();

			final Chirp pm = this.chirpService.create();

			if (opcionId == 1) {
				pm.setAttachments("");
				pm.setCopy(true);
				pm.setRecipient(recibe);
				pm.setSender(envia);
				pm.setSubject("Reunion de mañana");

			}
			if (opcionId == 2) {
				pm.setAttachments("");
				pm.setCopy(true);
				pm.setRecipient(recibe);
				pm.setSender(envia);
				pm.setText("Me gustaria asistir mañana dia 15 a las 9:00");

			}
			if (opcionId == 3) {
				pm.setAttachments("");
				pm.setCopy(true);
				pm.setSender(envia);
				pm.setSubject("Reunion de mañana");
				pm.setText("Me gustaria asistir mañana dia 15 a las 9:00");

			}
			if (opcionId == 4) {
				pm.setAttachments("");
				pm.setCopy(true);
				pm.setRecipient(recibe);
				pm.setSubject("Reunion de mañana");
				pm.setText("Me gustaria asistir mañana dia 15 a las 9:00");

			}
			if (opcionId == 5) {

			}

			this.chirpService.save(pm);

			this.unauthenticate();
			this.chirpService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);

	}
	//Chorbi = 63,64,65,66,67,68
	@Test
	public void driver2() {

		final Object testingData[][] = {
			{
				//chorbi 2 intenta enviar chirp sin texto a chirp 1
				"chorbi2", 64, 63, 1, ConstraintViolationException.class
			}, {
				//chorbi 2 intenta enviar chirp sin titulo a chorbi 3
				"chorbi2", 64, 65, 2, ConstraintViolationException.class
			}, {
				//chorbi 1 intenta enviar chirp sin recipient
				"chorbi1", 63, 64, 3, NullPointerException.class
			}, {
				//No existe sender
				"chorbi1", 63, 64, 4, NullPointerException.class
			}, {
				//Mensaje vacio
				"chorbi1", 63, 63, 5, NullPointerException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.template2((String) testingData[i][0], (int) testingData[i][1], (int) testingData[i][2], (int) testingData[i][3], (Class<?>) testingData[i][4]);

	}

	//Delete a received chirp
	protected void template3(final String username, final int chirpId, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			this.authenticate(username);

			final Chirp c = this.chirpService.findOne(chirpId);

			this.chirpService.deleteReceived(c);

			final Collection<Chirp> cc = this.chirpService.findAll();

			Assert.isTrue(!cc.contains(c));

			this.unauthenticate();
			this.chirpService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);

	}
	//Chorbi = 63,64,65,66,67,68
	@Test
	public void driver3() {

		final Object testingData[][] = {
			{
				//chorbi 2 elimina un chirp suyo
				"chorbi2", 70, null
			}, {
				//chorbi 1 intenta eliminar un chirp que no es suyo
				"chorbi1", 70, IllegalArgumentException.class
			}

			, {
				//chorbi 2 intenta eliminar un chirp que no existe
				"chorbi2", 1000, IllegalArgumentException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.template3((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);

	}

	//Delete a received chirp
	protected void template4(final String username, final int chirpId, final Class<?> expected) {

		Class<?> caught;

		caught = null;
		try {

			this.authenticate(username);

			final Chirp c = this.chirpService.findOne(chirpId);

			this.chirpService.deleteSent(c);

			final Collection<Chirp> cc = this.chirpService.findAll();

			Assert.isTrue(!cc.contains(c));

			this.unauthenticate();
			this.chirpService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);

	}
	//Chorbi = 63,64,65,66,67,68
	@Test
	public void driver4() {

		final Object testingData[][] = {
			{
				//chorbi 1 elimina un chirp suyo
				"chorbi1", 69, null
			}, {
				//chorbi 2 intenta eliminar un chirp que no es suyo
				"chorbi2", 69, IllegalArgumentException.class
			}

			, {
				//chorbi 1 intenta eliminar un chirp que no existe
				"chorbi1", 1000, IllegalArgumentException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.template4((String) testingData[i][0], (int) testingData[i][1], (Class<?>) testingData[i][2]);

	}

}
