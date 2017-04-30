
package services;

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Chirp;
import domain.Chorbi;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@Transactional
public class ChirpTest extends AbstractTest {

	@Autowired
	private ChirpService	chirpService;

	@Autowired
	private ChorbiService	chorbiService;


	//Find one positivo: con usuario logueado
	@Test
	public void findOneChirp() {

		this.authenticate("chorbi1");

		final Chirp c = this.chirpService.findOne(69);

		System.out.println("-----------------------------------------------------------------------------------");
		System.out.println("--------------------------------FIND ONE CHIRP------------------------------------");
		System.out.println("-----------------------------------------------------------------------------------");

		System.out.println("Se espera obtener el Chirp 1 con id 69: ");
		System.out.println("Subject: " + c.getSubject());
		System.out.println("Id: " + c.getId());

		this.unauthenticate();

	}

	//Find one negativo: no existe dicho chirp
	@Test(expected = IllegalArgumentException.class)
	public void findOneChirpNegative() {

		final Chirp c = this.chirpService.findOne(1000);

		System.out.println("-----------------------------------------------------------------------------------");
		System.out.println("--------------------------------FIND ONE CHIRP------------------------------------");
		System.out.println("-----------------------------------------------------------------------------------");

	}

	//FindALL
	@Test
	public void findAll() {

		System.out.println("-------------------------------------------------------------");
		System.out.println("               TEST CHIRP .FINDALL()");
		System.out.println("-------------------------------------------------------------");

		final Collection<Chirp> result = this.chirpService.findAll();

		for (final Chirp a : result) {
			System.out.println("Id: " + a.getId());
			System.out.println("Subject: " + a.getSubject());
		}
	}

	//create
	@Test
	public void testCreate() {
		System.out.println("-------------------------------------------------------------");
		System.out.println("               TEST CHIRP.CREATE()");
		System.out.println("-------------------------------------------------------------");
		System.out.println("Se esperan los atributos ID = 0 y subject a null");

		final Chirp result = this.chirpService.create();
		System.out.println("ID: " + result.getId());
		System.out.println("Subject: " + result.getSubject());
	}

	//create 2
	@Test
	public void testCreate2() {
		System.out.println("-------------------------------------------------------------");
		System.out.println("               TEST CHIRP.CREATE2()");
		System.out.println("-------------------------------------------------------------");
		System.out.println("Se esperan los atributos ID = 0 y recipient con id=63");

		this.authenticate("chorbi1");
		final Chorbi c = this.chorbiService.findOne(63);

		final Chirp result = this.chirpService.create(c);
		System.out.println("ID: " + result.getId());
		System.out.println("Recipient id: " + result.getRecipient().getId());
	}

	//SendedChirp
	@Test
	public void testSendedChirp() {
		System.out.println("-------------------------------------------------------------");
		System.out.println("               TEST CHIRP.SENDEDCHIRP()");
		System.out.println("-------------------------------------------------------------");

		this.authenticate("chorbi1");
		final Collection<Chirp> cs = this.chirpService.mySendedMessages(63);

		for (final Chirp c : cs) {
			System.out.println("ID: " + c.getId());
			System.out.println("Recipient id: " + c.getRecipient().getId());
			System.out.println("Sender id: " + c.getSender().getId());
			System.out.println("Subject: " + c.getSubject());
		}

	}

	//ReceivedChirp
	@Test
	public void testReceivedChirp() {
		System.out.println("-------------------------------------------------------------");
		System.out.println("               TEST CHIRP.SENDEDCHIRP()");
		System.out.println("-------------------------------------------------------------");

		this.authenticate("chorbi1");
		final Collection<Chirp> cs = this.chirpService.myRecivedMessages(63);

		for (final Chirp c : cs) {
			System.out.println("ID: " + c.getId());
			System.out.println("Recipient id: " + c.getRecipient().getId());
			System.out.println("Sender id: " + c.getSender().getId());
			System.out.println("Subject: " + c.getSubject());
		}

	}

}
