
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ChirpRepository;
import domain.Actor;
import domain.Chirp;
import domain.Chorbi;
import domain.Event;
import domain.Manager;

@Service
@Transactional
public class ChirpService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private ChirpRepository	chirpRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private ChorbiService	chorbiService;

	@Autowired
	private TasteService	tasteService;

	@Autowired
	private ActorService	actorService;

	@Autowired
	private ManagerService	managerService;

	@Autowired
	private Validator		validator;


	// Constructors -----------------------------------------------------------

	public ChirpService() {
		super();
	}

	//Simple CRUD methods ---------------------------------

	public Chirp create() {
		Chirp res;

		res = new Chirp();
		res.setCopy(true);

		return res;
	}

	public Chirp create(final Chorbi c) {
		Chirp res;

		res = new Chirp();

		res.setRecipient(c);
		res.setCopy(true);

		return res;
	}

	public Chirp createBulk(final Event c) {
		Chirp res;

		final Manager principal = this.managerService.findByPrincipal();
		Assert.isTrue(principal.getId() == c.getManager().getId());

		res = new Chirp();

		res.setSender(c.getManager());
		res.setCopy(true);

		return res;
	}

	public Chirp reconstruct(final Chirp chirp, final BindingResult binding) {

		Chirp result;
		final Chorbi t = this.chorbiService.findByPrincipal();

		result = chirp;
		final String text = this.tasteService.checkContactInfo(chirp.getText());
		final String subject = this.tasteService.checkContactInfo(chirp.getSubject());

		result.setRecipient(chirp.getRecipient());
		result.setSender(t);
		result.setText(text);
		result.setSubject(subject);
		result.setAttachments(chirp.getAttachments());
		result.setMoment(new Date(System.currentTimeMillis() - 1000));
		result.setCopy(chirp.isCopy());
		this.validator.validate(result, binding);

		return result;

	}

	public Chirp reconstruct2(final Chirp chirp, final BindingResult binding) {

		Chirp result;
		final Actor t = this.actorService.findByPrincipal();

		result = chirp;
		final String text = this.tasteService.checkContactInfo(chirp.getText());
		final String subject = this.tasteService.checkContactInfo(chirp.getSubject());

		result.setRecipient(t);
		result.setSender(t);
		result.setText(text);
		result.setSubject(subject);
		result.setAttachments(chirp.getAttachments());
		result.setMoment(new Date(System.currentTimeMillis() - 1000));
		result.setCopy(chirp.isCopy());
		this.validator.validate(result, binding);

		return result;

	}
	public Chirp reply(final Chirp chirp, final BindingResult binding) {

		Chirp result;
		final Chorbi t = this.chorbiService.findByPrincipal();

		result = chirp;
		final String text = this.tasteService.checkContactInfo(chirp.getText());
		final String subject = this.tasteService.checkContactInfo(chirp.getSubject());

		result.setRecipient(chirp.getRecipient());
		result.setSender(t);
		result.setText(text);
		result.setSubject(subject);
		result.setAttachments(chirp.getAttachments());
		result.setMoment(new Date(System.currentTimeMillis() - 1000));
		result.setCopy(chirp.isCopy());
		this.validator.validate(result, binding);

		return result;

	}

	public Collection<Chirp> findAll() {
		Collection<Chirp> res;
		res = this.chirpRepository.findAll();
		Assert.notNull(res);
		return res;
	}

	public Chirp findOne(final int chirpId) {
		Chirp res;
		res = this.chirpRepository.findOne(chirpId);
		Assert.notNull(res);
		return res;
	}

	public void save(final Chirp m) {
		Assert.notNull(m);

		final Chorbi c = this.chorbiService.findByPrincipal();

		final Chirp chirp1 = new Chirp();
		chirp1.setSubject(m.getSubject());
		chirp1.setText(m.getText());
		chirp1.setMoment(m.getMoment());
		chirp1.setAttachments(m.getAttachments());
		chirp1.setRecipient(m.getRecipient());
		chirp1.setSender(m.getSender());
		chirp1.setCopy(false);

		final Chirp chirp2 = new Chirp();
		chirp2.setSubject(m.getSubject());
		chirp2.setText(m.getText());
		chirp2.setMoment(m.getMoment());
		chirp2.setAttachments(m.getAttachments());
		chirp2.setRecipient(m.getRecipient());
		chirp2.setSender(m.getSender());
		chirp2.setCopy(true);

		final Collection<Chirp> cr = m.getRecipient().getChirpReceives();
		cr.add(chirp1);

		final Collection<Chirp> cs = m.getSender().getChirpWrites();
		cs.add(chirp2);

		Assert.isTrue(c.getId() == m.getSender().getId());

		this.chirpRepository.save(chirp2);
		this.chirpRepository.save(chirp1);

	}

	public void saveBulk(final Chirp m, final Event v) {
		Assert.notNull(m);

		final Manager principal = this.managerService.findByPrincipal();
		Assert.isTrue(principal.getId() == v.getManager().getId());

		final Collection<Chorbi> cc = v.getRegistered();
		for (final Chorbi c : cc) {

			final Chirp chirp2 = new Chirp();
			chirp2.setSubject(m.getSubject());
			chirp2.setText(m.getText());
			chirp2.setMoment(m.getMoment());
			chirp2.setAttachments(m.getAttachments());
			chirp2.setRecipient(c);
			chirp2.setSender(principal);
			chirp2.setCopy(false);

			final Collection<Chirp> cr = c.getChirpReceives();
			cr.add(chirp2);
			this.chirpRepository.save(chirp2);

		}

	}

	public void editEventChirp(final Event event, final Manager principal) {
		final Collection<Chorbi> chorbiesToChirp = event.getRegistered();
		for (final Chorbi chorbi : chorbiesToChirp) {
			final Chirp mensaje = this.create();
			mensaje.setSubject("Something has change in the event:  " + event.getTitle() + " // Algo ha cambiado en el evento: " + event.getTitle());
			mensaje.setText("We have modified something in this event, you may want to know! // ¡Hemos modificado algo en este evento, quizás quieras saberlo!");
			final Date current = new Date();
			mensaje.setMoment(current);
			mensaje.setAttachments("");
			mensaje.setRecipient(chorbi);
			mensaje.setSender(principal);
			mensaje.setCopy(false);

			final Collection<Chirp> cr = chorbi.getChirpReceives();
			cr.add(mensaje);

			this.chirpRepository.save(mensaje);

		}
	}

	public void deleteEventChirp(final Event event, final Manager principal) {
		final Collection<Chorbi> chorbiesToChirp = event.getRegistered();
		for (final Chorbi chorbi : chorbiesToChirp) {
			final Chirp mensaje = this.create();
			mensaje.setSubject("We canceled this event:  " + event.getTitle() + " // Hemos cancelado este evento: " + event.getTitle());
			mensaje.setText("We canceled this event, you may want to know! // ¡Hemos cancelado este evento, quizás quieras saberlo!");
			final Date current = new Date();
			mensaje.setMoment(current);
			mensaje.setAttachments("");
			mensaje.setRecipient(chorbi);
			mensaje.setSender(principal);
			mensaje.setCopy(false);

			final Collection<Chirp> cr = chorbi.getChirpReceives();
			cr.add(mensaje);

			this.chirpRepository.save(mensaje);

		}
	}

	public void deleteReceived(final Chirp m) {
		final Chorbi principal = this.chorbiService.findByPrincipal();
		Assert.notNull(m);

		Assert.isTrue(principal.getId() == m.getRecipient().getId());

		this.chirpRepository.delete(m.getId());

	}

	public void deleteSent(final Chirp m) {
		Assert.notNull(m);
		final Chorbi principal = this.chorbiService.findByPrincipal();
		Assert.isTrue(principal.getId() == m.getSender().getId());

		this.chirpRepository.delete(m.getId());

	}

	//Other methods ---------------------------------------------------------

	public Collection<Chirp> mySendedMessages(final int actorId) {

		final Collection<Chirp> sm = this.chirpRepository.mySendedMessages(actorId);

		return sm;
	}

	public Collection<Chirp> myRecivedMessages(final int actorId) {

		final Collection<Chirp> sm = this.chirpRepository.myRecivedMessages(actorId);

		return sm;
	}

	public Collection<Chirp> misRecibidos(final Actor actor) {

		final Collection<Chirp> sm = actor.getChirpReceives();

		final Collection<Chirp> sm2 = new ArrayList<Chirp>();

		for (final Chirp c : sm)
			if (c.isCopy().equals(false))
				sm2.add(c);

		return sm2;
	}

	public void flush() {
		this.chirpRepository.flush();

	}

	// Dashboard

	public Collection<Object[]> minAvgMaxChirpsReceived() {
		final Collection<Object[]> res = this.chirpRepository.minAvgMaxChirpsReceived();
		return res;
	}

	public Collection<Object[]> minAvgMaxChirpsSent() {
		final Collection<Object[]> res = this.chirpRepository.minAvgMaxChirpsSent();
		return res;
	}
}
