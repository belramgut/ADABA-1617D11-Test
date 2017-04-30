
package controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ChorbiService;
import services.CreditCardService;
import services.EventService;
import services.ManagerService;
import domain.Actor;
import domain.Chorbi;
import domain.Event;
import domain.Manager;

@Controller
@RequestMapping("/event")
public class EventController extends AbstractController {

	// Constructors -----------------------------------------------------------

	public EventController() {
		super();
	}


	// Services -----------------------------------------------------------

	@Autowired
	private EventService		eventService;

	@Autowired
	private ManagerService		managerService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private ChorbiService		chorbiService;

	@Autowired
	private CreditCardService	creditCardService;


	//Browse a listing that includes every event that was registered in the system.
	//Past events must be greyed out; events that are going to be organised in less than one month and have seats available must also be somewhat highlighted; 
	//the rest of events must be displayed normally.

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {

		ModelAndView result;
		Collection<Event> eventsToShow;
		final Collection<Event> aux = new ArrayList<Event>();
		final Collection<Event> toGray = new ArrayList<Event>();

		eventsToShow = this.eventService.findAll();

		final Calendar current = new GregorianCalendar();
		for (final Event tmp : eventsToShow) {
			final Calendar fecha = new GregorianCalendar();
			fecha.setTime(tmp.getMoment());
			if (fecha.before(current))
				toGray.add(tmp);
			else {

				final long dif = this.eventService.difDiasEntre2fechas(current, fecha);
				if (fecha.after(current) && dif <= 30 && tmp.getNumberSeatsOffered() > 0)
					aux.add(tmp);
			}

		}

		Actor principal;

		try {

			principal = this.actorService.findByPrincipal();

		} catch (final Throwable oops) {

			principal = null;
		}

		result = new ModelAndView("event/list");
		result.addObject("events", eventsToShow);
		result.addObject("requestURI", "event/list.do");
		result.addObject("current", new Date());
		result.addObject("tohighlight", aux);
		result.addObject("togray", toGray);
		result.addObject("principal", principal);

		return result;

	}

	//Browse the listing of events that are going to be organised in less than one month and have seats available
	@RequestMapping(value = "/listEventOfferMonth", method = RequestMethod.GET)
	public ModelAndView listEventOfferMonth() {
		ModelAndView result;
		final Collection<Event> events = this.eventService.listEventMonthSeatsFree();

		result = new ModelAndView("event/listEventOfferMonth");
		result.addObject("events", events);
		result.addObject("requestURI", "event/listEventOfferMonth.do");

		return result;
	}

	//List chorbies registered in this event
	@RequestMapping(value = "/listsRegisteredFrom", method = RequestMethod.GET)
	public ModelAndView listsRegisteredFrom(@RequestParam final int eventId) {
		ModelAndView result;
		Collection<Chorbi> chorbies;
		Event event;
		event = this.eventService.findOne(eventId);
		chorbies = event.getRegistered();

		result = new ModelAndView("chorbi/list");
		result.addObject("chorbies", chorbies);
		result.addObject("requestURI", "event/listsRegisteredFrom.do?chorbiId=" + eventId);

		return result;

	}

	@RequestMapping(value = "/myEvents", method = RequestMethod.GET)
	public ModelAndView myEvents() {

		ModelAndView result;
		Collection<Event> eventsToShow;
		final Manager m = this.managerService.findByPrincipal();
		final Collection<Event> aux = new ArrayList<Event>();
		final Collection<Event> toGray = new ArrayList<Event>();

		eventsToShow = m.getEvents();

		final Calendar current = new GregorianCalendar();
		for (final Event tmp : eventsToShow) {
			final Calendar fecha = new GregorianCalendar();
			fecha.setTime(tmp.getMoment());
			if (fecha.before(current))
				toGray.add(tmp);
			else {

				final long dif = this.eventService.difDiasEntre2fechas(current, fecha);
				if (fecha.after(current) && dif <= 30 && tmp.getNumberSeatsOffered() > 0)
					aux.add(tmp);
			}

		}

		result = new ModelAndView("event/list2");
		result.addObject("events", eventsToShow);
		result.addObject("requestURI", "event/myEvents.do");
		result.addObject("current", new Date());
		result.addObject("tohighlight", aux);
		result.addObject("togray", toGray);
		result.addObject("principal", m);

		return result;

	}

	//Create a new Event

	@RequestMapping(value = "/manager/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		final Event event;

		event = new Event();

		result = new ModelAndView("event/edit");
		result.addObject("event", event);
		result.addObject("requestURI", "event/manager/create.do");

		return result;

	}

	@RequestMapping(value = "/manager/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Event event, final BindingResult bindingResult) {
		ModelAndView result;

		final Manager m = this.managerService.findByPrincipal();

		if (event.getMoment() != null)
			try {
				final Date current = new Date();
				Assert.isTrue(event.getMoment().after(current));
			} catch (final IllegalArgumentException momenterrors) {
				return result = this.createEditModelAndView(event, "event.dates.error");
			}

		event = this.eventService.reconstruct(event, bindingResult);

		if (bindingResult.hasErrors())

			if (bindingResult.getGlobalError() != null)
				result = this.createEditModelAndView(event, bindingResult.getGlobalError().getCode());
			else
				result = this.createEditModelAndView(event);

		else
			try {

				Assert.notNull(m.getCreditCard());
				Assert.isTrue(this.creditCardService.validateDate(m.getCreditCard().getExpirationMonth(), m.getCreditCard().getExpirationYear()));

				this.eventService.save2(event);
				result = new ModelAndView("redirect:/event/myEvents.do");

			} catch (final IllegalArgumentException ccerror) {
				result = this.createEditModelAndView(event, "event.valid.creditCard");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(event, "event.commit.error");
			}

		return result;

	}

	//Edit an Event

	@RequestMapping(value = "/manager/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int eventId) {

		ModelAndView result;
		Event event;

		event = this.eventService.findOne(eventId);
		Assert.notNull(event);

		try {
			final Manager principal = this.managerService.findByPrincipal();
			Assert.isTrue(principal.getId() == event.getManager().getId());
		} catch (final Exception e) {
			result = new ModelAndView("event/forboperation");
			result.addObject("forbiddenOperation", "event.not.your.event");
			result.addObject("cancelURL", "event/myEvents.do");
			return result;

		}

		try {
			final Date current = new Date();
			Assert.isTrue(event.getMoment().after(current));
		} catch (final Exception e) {
			result = new ModelAndView("event/forboperation");
			result.addObject("forbiddenOperation", "event.in.past");
			result.addObject("cancelURL", "event/myEvents.do");
			return result;

		}

		result = this.createEditModelAndView(event);
		result.addObject("requestURI", "event/manager/edit.do?eventId=" + eventId);

		return result;

	}

	@RequestMapping(value = "/manager/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView saveEdit(Event event, final BindingResult bindingResult) {
		ModelAndView result;

		try {
			event = this.eventService.reconstruct(event, bindingResult);
		} catch (final IllegalArgumentException momenterrors) {
			result = this.createEditModelAndView(event, "event.plazas.error");
			result.addObject("requestURI", "event/manager/edit.do?eventId=" + event.getId());
			return result;

		} catch (final Exception e) {
			result = this.createEditModelAndView(event);
			result.addObject("requestURI", "event/manager/edit.do?eventId=" + event.getId());
		}

		if (event.getMoment() != null)
			try {
				final Date current = new Date();
				Assert.isTrue(event.getMoment().after(current));
			} catch (final IllegalArgumentException momenterrors) {
				result = this.createEditModelAndView(event, "event.dates.error");
				result.addObject("requestURI", "event/manager/edit.do?eventId=" + event.getId());
				return result;
			}

		if (bindingResult.hasErrors()) {

			if (bindingResult.getGlobalError() != null) {
				result = this.createEditModelAndView(event, bindingResult.getGlobalError().getCode());
				result.addObject("requestURI", "event/manager/edit.do?eventId=" + event.getId());
			} else {
				result = this.createEditModelAndView(event);
				result.addObject("requestURI", "event/manager/edit.do?eventId=" + event.getId());
			}

		} else
			try {

				this.eventService.saveEdit(event);
				result = new ModelAndView("redirect:/event/myEvents.do");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(event, "event.commit.error");
				result.addObject("requestURI", "event/manager/edit.do?eventId=" + event.getId());
			}

		return result;

	}

	//Delete an event

	@RequestMapping(value = "/manager/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int eventId) {
		ModelAndView result;

		Event event;

		event = this.eventService.findOne(eventId);
		Assert.notNull(event);

		try {
			final Manager principal = this.managerService.findByPrincipal();
			Assert.isTrue(principal.getId() == event.getManager().getId());
		} catch (final Exception e) {
			result = new ModelAndView("event/forboperation");
			result.addObject("forbiddenOperation", "event.not.your.event.delete");
			result.addObject("cancelURL", "event/myEvents.do");
			return result;

		}

		try {
			final Date current = new Date();
			Assert.isTrue(event.getMoment().after(current));
		} catch (final Exception e) {
			result = new ModelAndView("event/forboperation");
			result.addObject("forbiddenOperation", "event.in.past.delete");
			result.addObject("cancelURL", "event/myEvents.do");
			return result;

		}

		try {

			this.eventService.delete(event);
			result = new ModelAndView("redirect:/event/myEvents.do");

		} catch (final Throwable th) {

			result = new ModelAndView("event/forboperation");
			result.addObject("forbiddenOperation", "event.commit.error");
			result.addObject("cancelURL", "event/myEvents.do");
			return result;
		}

		return result;

	}

	//Register to an event

	@RequestMapping(value = "/registerEvent", method = RequestMethod.GET)
	public ModelAndView registerEvent(@RequestParam final int eventId) {
		ModelAndView result;
		Chorbi chorbi;
		Event e;
		e = this.eventService.findOne(eventId);
		chorbi = this.chorbiService.findByPrincipal();
		try {
			this.eventService.registerEvent(chorbi, e);
		} catch (final Exception e2) {
		}

		result = this.list();
		return result;
	}

	//unregister from an event

	@RequestMapping(value = "/unregisterEvent", method = RequestMethod.GET)
	public ModelAndView unregisterEvent(@RequestParam final int eventId) {
		ModelAndView result;
		Chorbi chorbi;
		Event e;
		e = this.eventService.findOne(eventId);
		chorbi = this.chorbiService.findByPrincipal();
		try {
			this.eventService.unregisterEvent(chorbi, e);
		} catch (final Exception e2) {
		}
		result = this.list();

		return result;
	}

	//-------------------------------------------------------------------------------------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Event event, final String message) {
		ModelAndView result;

		result = new ModelAndView("event/edit");
		result.addObject("event", event);
		result.addObject("requestURI", "event/manager/create.do");
		result.addObject("message", message);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Event event) {
		ModelAndView result;

		result = this.createEditModelAndView(event, null);

		return result;
	}

}
