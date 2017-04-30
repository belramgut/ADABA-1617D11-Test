
package controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ChorbiService;
import services.EventService;
import services.TasteService;
import services.TemplateService;
import domain.Chorbi;
import domain.CreditCard;
import domain.Event;
import domain.Taste;
import domain.Template;
import form.RegistrationForm;

@Controller
@RequestMapping("/chorbi")
public class ChorbiController extends AbstractController {

	// Constructors -----------------------------------------------------------

	public ChorbiController() {
		super();
	}


	//Services -----------------------------------------------------------

	@Autowired
	private ChorbiService	chorbiService;

	@Autowired
	private TasteService	tasteService;

	@Autowired
	private TemplateService	templateService;

	@Autowired
	private EventService	eventService;


	//Browse the chorbies who has registered to the system
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {

		ModelAndView result;
		Collection<Chorbi> chorbiesToShow;

		chorbiesToShow = this.chorbiService.findAllNotBannedChorbies();

		result = new ModelAndView("chorbi/list");
		result.addObject("chorbies", chorbiesToShow);
		result.addObject("requestURI", "chorbi/list.do");

		return result;

	}

	@RequestMapping(value = "/listMyEvents", method = RequestMethod.GET)
	public ModelAndView myEvents() {

		ModelAndView result;
		Collection<Event> eventsToShow;
		final Chorbi m = this.chorbiService.findByPrincipal();
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

		result = new ModelAndView("chorbi/listMyEvents");
		result.addObject("events", eventsToShow);
		result.addObject("requestURI", "chorbi/listMyEvents.do");
		result.addObject("current", new Date());
		result.addObject("tohighlight", aux);
		result.addObject("togray", toGray);
		result.addObject("principal", m);

		return result;

	}
	//See Chorbi Profile
	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int chorbiId) {
		ModelAndView result;
		Chorbi chorbi;
		chorbi = this.chorbiService.findOne(chorbiId);
		boolean toLike;
		boolean toCreditCard = false;
		final Chorbi principal = this.chorbiService.findByPrincipal();

		Assert.isTrue(chorbi.isBan() == false);

		if (principal != null) {
			toLike = true;
			for (final Taste t : principal.getGivenTastes())
				if (t.getChorbi().getId() == chorbiId) {
					toLike = false;
					break;
				}
		} else
			toLike = false;

		final CreditCard creditCard = principal.getCreditCard();
		if (creditCard != null)
			toCreditCard = true;

		result = new ModelAndView("chorbi/displayProfile");
		result.addObject("chorbi", chorbi);
		result.addObject("principal", principal);
		result.addObject("toLike", toLike);
		result.addObject("creditCard", creditCard);
		result.addObject("toCreditCard", toCreditCard);
		result.addObject("requestURI", "chorbi/profile.do?chorbiId=" + chorbiId);

		return result;
	}

	//See Chorbi Profile
	@RequestMapping(value = "/viewProfile", method = RequestMethod.GET)
	public ModelAndView viewProfile() {
		return this.display(this.chorbiService.findByPrincipal().getId());

	}

	//See people who like him/her

	@RequestMapping(value = "/listWhoLikeThem", method = RequestMethod.GET)
	public ModelAndView listChorbiesWhoLike(@RequestParam final int chorbiId) {
		ModelAndView result;
		Collection<Chorbi> chorbies;
		Chorbi chorbi;
		chorbi = this.chorbiService.findOne(chorbiId);
		chorbies = this.chorbiService.findAllChorbiesWhoLikeThem(chorbi);

		result = new ModelAndView("chorbi/list");
		result.addObject("chorbies", chorbies);
		result.addObject("requestURI", "chorbi/listWhoLikeThem.do?chorbiId=" + chorbiId);

		return result;
	}

	//See people who like to him/her

	@RequestMapping(value = "/listWhoLikedThis", method = RequestMethod.GET)
	public ModelAndView listWhoLikedThis(@RequestParam final int chorbiId) {
		ModelAndView result;
		Collection<Chorbi> chorbies;
		Chorbi chorbi;

		chorbi = this.chorbiService.findOne(chorbiId);
		chorbies = this.chorbiService.findAllChorbiesWhoLikedByThisUser(chorbi);

		result = new ModelAndView("chorbi/list");
		result.addObject("chorbies", chorbies);
		result.addObject("requestURI", "chorbi/listWhoLikedThis.do?chorbiId=" + chorbiId);

		return result;
	}

	// Terms of Use -----------------------------------------------------------
	@RequestMapping("/dataProtection")
	public ModelAndView dataProtection() {
		ModelAndView result;
		result = new ModelAndView("chorbi/dataProtection");
		return result;

	}

	// Creation ---------------------------------------------------------------
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		RegistrationForm chorbi;

		chorbi = new RegistrationForm();
		result = this.createEditModelAndView(chorbi);

		return result;

	}

	@RequestMapping(value = "/register", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("chorbi") @Valid final RegistrationForm form, final BindingResult binding) {

		ModelAndView result;
		Chorbi chorbi;
		if (binding.hasErrors()) {
			if (binding.getGlobalError() != null)
				result = this.createEditModelAndView(form, binding.getGlobalError().getCode());
			else
				result = this.createEditModelAndView(form);
		} else
			try {
				final Template t = this.templateService.create();
				chorbi = this.chorbiService.reconstruct(form);
				chorbi = this.chorbiService.saveAndFlush(chorbi, t);

				result = new ModelAndView("redirect:../security/login.do");
			} catch (final DataIntegrityViolationException exc) {
				result = this.createEditModelAndView(form, "chorbi.duplicated.user");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(form, "chorbi.commit.error");
			}
		return result;
	}

	// Other methods

	protected ModelAndView createEditModelAndView(final RegistrationForm chorbi) {
		ModelAndView result;
		result = this.createEditModelAndView(chorbi, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final RegistrationForm chorbi, final String message) {
		ModelAndView result;
		result = new ModelAndView("chorbi/register");
		result.addObject("chorbi", chorbi);
		result.addObject("message", message);
		return result;

	}

	protected ModelAndView createEditModelAndView(final Event event) {
		ModelAndView result;

		result = this.createEditModelAndView(event, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Event event, final String message) {
		ModelAndView result;

		result = new ModelAndView("chorbi/registerEvent");
		result.addObject("event", event);
		result.addObject("message", message);

		return result;
	}

}
