/*
 * CustomerController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ChirpService;
import services.ChorbiService;
import services.EventService;
import domain.Actor;
import domain.Chirp;
import domain.Chorbi;
import domain.Event;

@Controller
@RequestMapping("/chirp")
public class ChirpController extends AbstractController {

	@Autowired
	private ChirpService	chirpService;

	@Autowired
	private ChorbiService	chorbiService;

	@Autowired
	private EventService	eventService;

	@Autowired
	private ActorService	actorService;


	// Constructors -----------------------------------------------------------

	public ChirpController() {
		super();
	}

	@RequestMapping(value = "/listReceivedMessages", method = RequestMethod.GET)
	public ModelAndView listReceivedMessages() {
		final ModelAndView result;

		final Actor c = this.chorbiService.findByPrincipal();

		final Collection<Chirp> receivedMessages = this.chirpService.misRecibidos(c);

		result = new ModelAndView("chirp/listReceivedMessages");
		result.addObject("chirps", receivedMessages);
		result.addObject("requestURI", "chirp/listReceivedMessages.do");

		return result;
	}
	@RequestMapping(value = "/listSentMessages", method = RequestMethod.GET)
	public ModelAndView listSentMessages() {
		ModelAndView result;

		final Chorbi c = this.chorbiService.findByPrincipal();

		final Collection<Chirp> sendMessages = this.chirpService.mySendedMessages(c.getId());

		result = new ModelAndView("chirp/listSentMessages");
		result.addObject("chirps", sendMessages);
		result.addObject("requestURI", "chirp/listSentMessages.do");

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Chirp chirp;

		chirp = this.chirpService.create();

		result = this.createEditModelAndView(chirp);

		return result;
	}

	@RequestMapping(value = "/response/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int actorId) {
		ModelAndView result;
		Chirp chirp;

		final Chorbi c = this.chorbiService.findOneToSent(actorId);
		chirp = this.chirpService.create(c);

		result = this.createEditModelAndView2(chirp);

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Chirp chirpToEdit, final BindingResult binding) {

		ModelAndView result;

		chirpToEdit = this.chirpService.reconstruct(chirpToEdit, binding);
		if (binding.hasErrors())
			result = this.createEditModelAndView(chirpToEdit);
		else
			try {
				this.chirpService.save(chirpToEdit);
				result = new ModelAndView("redirect:/chirp/listSentMessages.do");

			} catch (final Throwable th) {
				result = this.createEditModelAndView(chirpToEdit, "chirp.commit.error");

			}

		return result;
	}

	@RequestMapping(value = "/response/create", method = RequestMethod.POST, params = "save")
	public ModelAndView save2(Chirp chirpToEdit, final BindingResult binding) {

		ModelAndView result;

		chirpToEdit = this.chirpService.reconstruct(chirpToEdit, binding);
		if (binding.hasErrors()) {
			result = this.createEditModelAndView2(chirpToEdit);

			result.addObject("chirp", chirpToEdit);

		} else {
			try {

				this.chirpService.save(chirpToEdit);

			} catch (final Throwable th) {
				result = this.createEditModelAndView2(chirpToEdit, "chirp.commit.error");

				result.addObject("chirp", chirpToEdit);

				return result;
			}

			result = new ModelAndView("redirect:/chirp/listReceivedMessages.do");

		}

		return result;
	}

	@RequestMapping(value = "/bulkChirp", method = RequestMethod.GET)
	public ModelAndView createBulk(@RequestParam final int eventId) {
		ModelAndView result;
		Chirp chirp;

		final Event c = this.eventService.findOne(eventId);
		chirp = this.chirpService.createBulk(c);

		result = this.createEditModelAndView3(chirp);
		result.addObject("requestURI", "chirp/bulkChirp.do?eventId=" + eventId);
		result.addObject("eventId", eventId);

		return result;
	}

	@RequestMapping(value = "/bulkChirp", method = RequestMethod.POST, params = "save")
	public ModelAndView saveBulk(Chirp chirpToEdit, final BindingResult binding, @RequestParam final int eventId) {

		ModelAndView result;

		chirpToEdit = this.chirpService.reconstruct2(chirpToEdit, binding);
		if (binding.hasErrors()) {
			result = this.createEditModelAndView3(chirpToEdit);

			result.addObject("chirp", chirpToEdit);

		} else {
			try {

				final Event event = this.eventService.findOne(eventId);
				this.chirpService.saveBulk(chirpToEdit, event);

			} catch (final Throwable th) {
				result = this.createEditModelAndView3(chirpToEdit, "chirp.commit.error");

				result.addObject("chirp", chirpToEdit);

				return result;
			}

			result = new ModelAndView("redirect:/event/myEvents.do");

		}

		return result;
	}

	// REPLY ----------------------------------------------------

	@RequestMapping(value = "/reply", method = RequestMethod.GET)
	public ModelAndView reply(@RequestParam final int chirpId) {
		ModelAndView result;

		final Chirp chirp = this.chirpService.findOne(chirpId);

		result = this.createEditModelAndView(chirp);

		return result;
	}

	@RequestMapping(value = "/reply", method = RequestMethod.POST, params = "save")
	public ModelAndView reply(Chirp chirpToEdit, final BindingResult binding) {

		ModelAndView result;

		chirpToEdit = this.chirpService.reply(chirpToEdit, binding);
		if (binding.hasErrors())
			result = this.createEditModelAndView(chirpToEdit);
		else
			try {

				this.chirpService.save(chirpToEdit);
				result = new ModelAndView("redirect:/chirp/listSentMessages.do");

			} catch (final Throwable th) {
				result = this.createEditModelAndView(chirpToEdit, "chirp.commit.error");

			}

		return result;
	}

	// DELETE ---------------------------------------------------

	@RequestMapping(value = "/deleteReceived", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int chirpId) {

		ModelAndView result;
		Chirp chirp;

		chirp = this.chirpService.findOne(chirpId);
		try {
			this.chirpService.deleteReceived(chirp);
		} catch (final Throwable th) {

		}

		result = new ModelAndView("redirect:/chirp/listReceivedMessages.do");

		return result;

	}

	@RequestMapping(value = "/deleteSent", method = RequestMethod.GET)
	public ModelAndView delete2(@RequestParam final int chirpId) {

		ModelAndView result;
		Chirp chirp;

		chirp = this.chirpService.findOne(chirpId);
		try {
			this.chirpService.deleteSent(chirp);
		} catch (final Throwable th) {

		}

		result = new ModelAndView("redirect:/chirp/listSentMessages.do");

		return result;

	}

	//CREATE EDIT MODEL AND VIEW

	protected ModelAndView createEditModelAndView(final Chirp chirpToEdit) {
		ModelAndView result;

		result = this.createEditModelAndView(chirpToEdit, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Chirp chirpToEdit, final String message) {
		ModelAndView result;

		Collection<Chorbi> actors;
		final Chorbi c = this.chorbiService.findByPrincipal();

		actors = this.chorbiService.findAllNotBannedChorbies();
		actors.remove(c);

		result = new ModelAndView("chirp/create");
		result.addObject("chirp", chirpToEdit);
		result.addObject("actors", actors);
		result.addObject("message", message);

		return result;
	}

	protected ModelAndView createEditModelAndView2(final Chirp chirpToEdit) {
		ModelAndView result;

		result = this.createEditModelAndView2(chirpToEdit, null);

		return result;
	}

	protected ModelAndView createEditModelAndView2(final Chirp chirpToEdit, final String message) {
		ModelAndView result;

		result = new ModelAndView("chirp/response/create");
		result.addObject("chirp", chirpToEdit);
		result.addObject("message", message);

		return result;
	}

	protected ModelAndView createEditModelAndView3(final Chirp chirpToEdit) {
		ModelAndView result;

		result = this.createEditModelAndView3(chirpToEdit, null);

		return result;
	}

	protected ModelAndView createEditModelAndView3(final Chirp chirpToEdit, final String message) {
		ModelAndView result;

		result = new ModelAndView("chirp/bulkChirp");
		result.addObject("chirp", chirpToEdit);
		result.addObject("message", message);

		return result;
	}

}
