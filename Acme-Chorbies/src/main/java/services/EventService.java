
package services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.EventRepository;
import domain.Chorbi;
import domain.Event;
import domain.Manager;

@Service
@Transactional
public class EventService {

	// Managed Repository -------------------------------------------

	@Autowired
	private EventRepository			eventRepository;

	// Supporting services ----------------------------------------------------

	@Autowired
	private ChorbiService			chorbiService;

	@Autowired
	private ManagerService			managerService;

	@Autowired
	private ConfigurationService	configurationService;

	@Autowired
	private CreditCardService		creditCardService;

	@Autowired
	private ChirpService			chirpService;


	// Constructors -----------------------------------------------------------

	public EventService() {
		super();
	}

	// Simple CRUD methods ----------------------------------------------------

	public Event create() {
		Event result;

		result = new Event();

		return result;

	}

	public Event registerEvent(final Chorbi chorbi, final Event event) {
		Assert.notNull(chorbi);
		Assert.notNull(event);
		final Collection<Chorbi> coll = event.getRegistered();

		final int seats = event.getNumberSeatsOffered();
		Assert.isTrue(seats > 0);

		//Añadir chorbi al listado del evento
		final List<Chorbi> list = new ArrayList<Chorbi>(coll);
		//No debe de haber sido registrado este chorbi.
		Assert.isTrue(!list.contains(chorbi));
		list.add(chorbi);
		event.setRegistered(list);
		//Quitamos una plaza

		event.setNumberSeatsOffered(seats - 1);

		//Añadir el evento al chorbi
		final Collection<Event> events = chorbi.getEvents();
		final List<Event> listEvents = new ArrayList<Event>(events);
		listEvents.add(event);
		chorbi.setEvents(listEvents);

		this.save(event);
		this.chorbiService.save(chorbi);
		return event;

	}

	public Event unregisterEvent(final Chorbi chorbi, final Event event) {
		Assert.notNull(chorbi);
		Assert.notNull(event);
		final Collection<Chorbi> coll = event.getRegistered();
		final List<Chorbi> list = new ArrayList<Chorbi>(coll);

		Assert.isTrue(list.contains(chorbi));
		final int index = list.indexOf(chorbi);
		list.remove(index);
		event.setRegistered(list);

		final Collection<Event> collEvent = chorbi.getEvents();
		final List<Event> listEvent = new ArrayList<Event>(collEvent);
		Assert.isTrue(listEvent.contains(event));
		final int index2 = listEvent.indexOf(event);
		listEvent.remove(index2);
		chorbi.setEvents(listEvent);

		event.setNumberSeatsOffered(event.getNumberSeatsOffered() + 1);

		this.save(event);
		this.chorbiService.save(chorbi);
		return event;

	}

	public Collection<Event> findAll() {
		Collection<Event> result;
		result = this.eventRepository.findAll();
		Assert.notNull(result);
		return result;
	}

	public Event findOne(final int eventId) {
		Event result;
		result = this.eventRepository.findOne(eventId);
		Assert.notNull(result);
		return result;
	}

	public Event save(final Event event) {
		return this.eventRepository.save(event);
	}

	public Event save2(Event event) {
		Assert.notNull(event);
		final Date current = new Date();
		final Manager m = this.managerService.findByPrincipal();
		Assert.isTrue(event.getMoment().after(current));
		Assert.notNull(m.getCreditCard());
		Assert.isTrue(this.creditCardService.validateDate(m.getCreditCard().getExpirationMonth(), m.getCreditCard().getExpirationYear()));
		final double feeCurrent = this.configurationService.findConfiguration().getManagersFee();
		event.setTotalChargedFee(feeCurrent);
		m.setTotalChargedFee(m.getTotalChargedFee() + feeCurrent);
		event = this.eventRepository.save(event);
		this.managerService.save(m);
		return event;

	}

	public Event saveEdit(Event event) {
		Assert.notNull(event);
		final Date current = new Date();
		final Manager m = this.managerService.findByPrincipal();
		Assert.isTrue(m.getId() == event.getManager().getId());
		Assert.isTrue(event.getMoment().after(current));
		event = this.eventRepository.save(event);

		this.chirpService.editEventChirp(event, m);

		return event;

	}
	public void delete(final Event event) {
		Assert.notNull(event);
		final Manager principal = this.managerService.findByPrincipal();
		Assert.isTrue(event.getManager().getId() == principal.getId());
		final Date current = new Date();
		Assert.isTrue(event.getMoment().after(current));
		this.chirpService.deleteEventChirp(event, principal);
		principal.getEvents().remove(event);
		this.eventRepository.delete(event);

	}

	// Other business methods ----------------------------------------------

	public void flush() {
		this.eventRepository.flush();
	}

	public long difDiasEntre2fechas(final Calendar current, final Calendar fecha) {
		final long difms = fecha.getTimeInMillis() - current.getTimeInMillis();
		final long difd = difms / (1000 * 60 * 60 * 24);
		return difd;
	}


	@Autowired
	private Validator	validator;


	public Event reconstruct(final Event event, final BindingResult bindingResult) {
		Event result;

		if (event.getId() == 0) {
			result = event;

			if (!(result.getTitle() == "") || !(result.getTitle() == null))
				result.setTitle(this.checkContactInfo(result.getTitle()));

			if (!(result.getDescription() == "") || !(result.getDescription() == null))
				result.setDescription(this.checkContactInfo(result.getDescription()));

			final Manager manager = this.managerService.findByPrincipal();
			Collection<Chorbi> registered;
			registered = new ArrayList<Chorbi>();

			result.setManager(manager);
			result.setRegistered(registered);

			this.validator.validate(event, bindingResult);

		} else {

			result = this.eventRepository.findOne(event.getId());
			//Este assert también se comprueba aquí, porque si no, el validator lo persiste igual, aunque no se cumpla la condición en controlador
			Assert.isTrue(result.getRegistered().size() <= event.getNumberSeatsOffered());

			result.setTitle(this.checkContactInfo(event.getTitle()));
			result.setDescription(this.checkContactInfo(event.getDescription()));
			result.setMoment(event.getMoment());
			result.setNumberSeatsOffered(event.getNumberSeatsOffered());
			result.setPicture(event.getPicture());
			this.validator.validate(result, bindingResult);

		}

		return result;

	}

	public String checkContactInfo(final String cadena) {

		String res = "";

		if (cadena.contains("@")) {
			final String[] trozos = cadena.split(" ");
			for (String t : trozos)
				if (t.contains("@")) {
					t = "***@***";
					res = res + " " + t;
				} else
					res = res + " " + t;
		} else
			res = cadena;

		final char[] res2 = cadena.toCharArray();
		int contador = 0;
		for (int i = 0; i < res.length(); i++)
			if (Character.isDigit(res2[i]))
				contador = contador + 1;

		if (contador >= 6) {
			String copia = "";
			final char c_aux = '*';
			for (int i = 0; i < res.length(); i++)
				if (Character.isDigit(res.charAt(i)))
					copia = copia + c_aux;
				else
					copia = copia + res.charAt(i);

			res = copia;

		}

		return res;

	}

	public Collection<Event> listEventMonthSeatsFree() {
		final Collection<Event> res = this.eventRepository.listEventMonthSeatsFree();
		return res;
	}

}
