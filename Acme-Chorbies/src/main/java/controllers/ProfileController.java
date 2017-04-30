/*
 * ProfileController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import javax.validation.Valid;

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
import services.ManagerService;
import services.TasteService;
import services.TemplateService;
import domain.Chorbi;
import domain.Coordinate;
import domain.CreditCard;
import domain.Manager;

@Controller
@RequestMapping("/profile")
public class ProfileController extends AbstractController {

	@Autowired
	private ChorbiService		chorbiService;

	@Autowired
	private TasteService		tasteService;

	@Autowired
	private TemplateService		templateService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private CreditCardService	creditCardService;

	@Autowired
	private ManagerService		managerService;


	//Edit profile

	@RequestMapping(value = "/editProfile", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int chorbiId) {
		ModelAndView res;
		Chorbi chorbi;
		int principal;
		chorbi = this.chorbiService.findOne(chorbiId);
		principal = this.actorService.findByPrincipal().getId();
		Assert.isTrue(principal == chorbiId);

		try {
			Assert.isTrue(principal == chorbiId);
		} catch (final Throwable th) {
			res = this.createEditModelAndViewError(chorbi);
			return res;
		}
		Assert.notNull(chorbi);
		res = this.createEditModelAndView(chorbi);
		return res;
	}

	@RequestMapping(value = "/editProfile", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final Chorbi chorbi, final BindingResult binding) {
		ModelAndView result;
		if (binding.hasErrors()) {
			if (binding.getGlobalError() != null)
				result = this.createEditModelAndView(chorbi, binding.getGlobalError().getCode());
			else
				result = this.createEditModelAndView(chorbi);
		} else
			try {
				final Chorbi chorbi1 = this.chorbiService.reconstruct(chorbi, binding);
				this.chorbiService.save(chorbi1);
				result = new ModelAndView("redirect:../chorbi/profile.do?chorbiId=" + chorbi.getId());
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(chorbi, "chorbi.commit.error");
			}
		return result;
	}

	@RequestMapping(value = "/createCreditCard", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		CreditCard creditCard;

		creditCard = this.creditCardService.create();

		result = this.editCreditCardModelAndView(creditCard);

		return result;
	}

	@RequestMapping(value = "/createCreditCard", method = RequestMethod.POST, params = "save")
	public ModelAndView save(final CreditCard creditCard, final BindingResult binding) {

		ModelAndView result;
		CreditCard res;
		final Chorbi chorbi = this.chorbiService.findByPrincipal();
		res = this.creditCardService.reconstruct(creditCard, binding);
		if (binding.hasErrors())
			result = this.editCreditCardModelAndView(res);
		else
			try {
				this.chorbiService.saveAndFlush2(chorbi, creditCard);
				result = new ModelAndView("redirect:../chorbi/profile.do?chorbiId=" + chorbi.getId());

			} catch (final Throwable th) {
				result = this.editCreditCardModelAndView(res, "creditCard.commit.error");

			}

		return result;
	}

	// Edit CreditCard ----------------------------------

	@RequestMapping(value = "/editCreditCard", method = RequestMethod.GET)
	public ModelAndView editCreditCard(@RequestParam final int chorbiId) {
		ModelAndView res;
		CreditCard creditCard;
		Chorbi principal;
		principal = this.chorbiService.findByPrincipal();
		creditCard = this.chorbiService.findOne(chorbiId).getCreditCard();
		try {
			Assert.isTrue(principal.getId() == chorbiId);
		} catch (final Throwable th) {
			res = this.createEditCreditCardModelAndViewError(creditCard);
			return res;
		}
		Assert.notNull(creditCard);
		res = this.createEditCreditCardModelAndView(creditCard);
		return res;
	}

	@RequestMapping(value = "/editCreditCard", method = RequestMethod.POST, params = "save")
	public ModelAndView saveCreditCard(@Valid final CreditCard creditCard, final BindingResult binding) {
		ModelAndView result;
		if (binding.hasErrors()) {
			result = new ModelAndView("profile/editCreditCard");
			result.addObject("creditCard", creditCard);
			result.addObject("forbiddenOperation", false);
		} else
			try {
				final Chorbi t = this.chorbiService.findByPrincipal();
				t.setCreditCard(creditCard);
				this.chorbiService.save(t);
				this.creditCardService.save(creditCard);
				result = new ModelAndView("redirect:../chorbi/profile.do?chorbiId=" + t.getId());
			} catch (final Throwable oops) {
				result = new ModelAndView("profile/editCreditCard");
				result.addObject("creditCard", creditCard);
				result.addObject("message", "chorbi.commit.error");
			}
		return result;

	}

	//Create creditCard manager ------------------------------------

	@RequestMapping(value = "/createCreditCardManager", method = RequestMethod.GET)
	public ModelAndView createCreditCardManager() {
		ModelAndView result;
		CreditCard creditCard;

		creditCard = this.creditCardService.create();

		result = this.editCreditCardManagerModelAndView(creditCard);

		return result;
	}

	@RequestMapping(value = "/createCreditCardManager", method = RequestMethod.POST, params = "save")
	public ModelAndView saveCreateCreditCardManager(final CreditCard creditCard, final BindingResult binding) {

		ModelAndView result;
		CreditCard res;
		final Manager manager = this.managerService.findByPrincipal();
		res = this.creditCardService.reconstruct(creditCard, binding);
		if (binding.hasErrors())
			result = this.editCreditCardManagerModelAndView(res);
		else
			try {
				this.managerService.saveAndFlush2(manager, creditCard);
				result = new ModelAndView("redirect:../manager/profile.do?managerId=" + manager.getId());

			} catch (final Throwable th) {
				result = this.editCreditCardManagerModelAndView(res, "creditCard.commit.error");

			}

		return result;
	}

	// Edit CreditCard manager ----------------------------------

	@RequestMapping(value = "/editCreditCardManager", method = RequestMethod.GET)
	public ModelAndView editCreditCardmanager(@RequestParam final int managerId) {
		ModelAndView res;
		CreditCard creditCard;
		Manager principal;
		principal = this.managerService.findByPrincipal();
		creditCard = this.managerService.findOne(managerId).getCreditCard();
		try {
			Assert.isTrue(principal.getId() == managerId);
		} catch (final Throwable th) {
			res = this.createEditCreditCardManagerModelAndViewError(creditCard);
			return res;
		}
		Assert.notNull(creditCard);
		res = this.createEditCreditCardManagerModelAndView(creditCard);
		return res;
	}

	@RequestMapping(value = "/editCreditCardManager", method = RequestMethod.POST, params = "save")
	public ModelAndView saveCreditCardManager(@Valid final CreditCard creditCard, final BindingResult binding) {
		ModelAndView result;
		if (binding.hasErrors()) {
			result = new ModelAndView("profile/editCreditCard");
			result.addObject("creditCard", creditCard);
			result.addObject("forbiddenOperation", false);
		} else
			try {
				final Manager t = this.managerService.findByPrincipal();
				t.setCreditCard(creditCard);
				this.managerService.save(t);
				this.creditCardService.save(creditCard);
				result = new ModelAndView("redirect:../manager/profile.do?managerId=" + t.getId());
			} catch (final Throwable oops) {
				result = new ModelAndView("profile/editCreditCardManager");
				result.addObject("creditCard", creditCard);
				result.addObject("message", "manager.commit.error");
			}
		return result;

	}

	// Edit LocationInformation ----------------------------------

	@RequestMapping(value = "/editLocationInformation", method = RequestMethod.GET)
	public ModelAndView editLocationInformation(@RequestParam final int chorbiId) {
		ModelAndView res;
		Chorbi principal;
		principal = this.chorbiService.findByPrincipal();
		final Coordinate c = principal.getCoordinate();
		try {
			Assert.isTrue(principal.getId() == chorbiId);
		} catch (final Throwable th) {
			res = this.createEditModelAndView(c);
			return res;
		}
		Assert.notNull(c);
		res = this.createEditModelAndView(c);
		return res;
	}

	@RequestMapping(value = "/editLocationInformation", method = RequestMethod.POST, params = "save")
	public ModelAndView saveCreditCard(@Valid final Coordinate coordinate, final BindingResult binding) {
		ModelAndView result;
		if (binding.hasErrors()) {
			result = new ModelAndView("profile/editLocationInformation");
			result.addObject("coordinate", coordinate);
			result.addObject("forbiddenOperation", false);
		} else
			try {
				final Chorbi t = this.chorbiService.findByPrincipal();
				t.setCoordinate(coordinate);
				this.chorbiService.save(t);
				result = new ModelAndView("redirect:../chorbi/profile.do?chorbiId=" + t.getId());
			} catch (final Throwable oops) {
				result = new ModelAndView("profile/editLocationInformation");
				result.addObject("coordinate", coordinate);
				result.addObject("message", "chorbi.commit.error");
			}
		return result;

	}

	//Other methods 
	protected ModelAndView createEditModelAndView(final Coordinate c) {
		ModelAndView result;
		result = this.createEditModelAndView(c, null);
		return result;
	}

	protected ModelAndView createEditModelAndViewError(final Coordinate c) {
		ModelAndView res;
		res = this.createEditModelAndViewError(c, null);
		return res;

	}

	protected ModelAndView createEditModelAndView(final Coordinate c, final String message) {
		ModelAndView result;
		result = new ModelAndView("profile/editLocationInformation");
		result.addObject("coordinate", c);
		result.addObject("message", message);
		result.addObject("forbiddenOperation", false);
		return result;
	}

	protected ModelAndView createEditModelAndViewError(final Coordinate c, final String message) {
		ModelAndView res;
		res = new ModelAndView("profile/editProfile");
		res.addObject("coordinate", c);
		res.addObject("message", message);
		res.addObject("forbiddenOperation", true);
		return res;

	}

	protected ModelAndView createEditModelAndView(final Chorbi chorbi) {
		ModelAndView result;
		result = this.createEditModelAndView(chorbi, null);
		return result;
	}

	protected ModelAndView createEditModelAndViewError(final Chorbi chorbi) {
		ModelAndView res;
		res = this.createEditModelAndViewError(chorbi, null);
		return res;

	}

	protected ModelAndView createEditModelAndView(final Chorbi chorbi, final String message) {
		ModelAndView result;
		result = new ModelAndView("profile/editProfile");
		result.addObject("chorbi", chorbi);
		result.addObject("message", message);
		result.addObject("forbiddenOperation", false);
		return result;
	}

	protected ModelAndView createEditModelAndViewError(final Chorbi chorbi, final String message) {
		ModelAndView res;
		res = new ModelAndView("profile/editProfile");
		res.addObject("chorbi", chorbi);
		res.addObject("message", message);
		res.addObject("forbiddenOperation", true);
		return res;

	}

	protected ModelAndView createEditCreditCardModelAndView(final CreditCard creditCard) {
		ModelAndView result;
		result = this.createEditCreditCardModelAndView(creditCard, null);
		return result;

	}

	protected ModelAndView createEditCreditCardModelAndViewError(final CreditCard creditCard) {
		ModelAndView res;
		res = this.createEditCreditCardModelAndViewError(creditCard, null);
		return res;
	}

	protected ModelAndView createEditCreditCardModelAndView(final CreditCard creditCard, final String message) {
		ModelAndView result;

		result = new ModelAndView("profile/editCreditCard");
		result.addObject("creditCard", creditCard);
		result.addObject("message", message);
		result.addObject("forbiddenOperation", false);

		return result;
	}

	protected ModelAndView createEditCreditCardModelAndViewError(final CreditCard creditCard, final String message) {
		ModelAndView res;
		res = new ModelAndView("profile/editCreditCard");
		res.addObject("creditCard", creditCard);
		res.addObject("message", message);
		res.addObject("forbiddenOperation", true);
		return res;

	}

	protected ModelAndView createEditCreditCardManagerModelAndView(final CreditCard creditCard) {
		ModelAndView result;
		result = this.createEditCreditCardManagerModelAndView(creditCard, null);
		return result;

	}

	protected ModelAndView createEditCreditCardManagerModelAndViewError(final CreditCard creditCard) {
		ModelAndView res;
		res = this.createEditCreditCardManagerModelAndViewError(creditCard, null);
		return res;
	}

	protected ModelAndView createEditCreditCardManagerModelAndView(final CreditCard creditCard, final String message) {
		ModelAndView result;

		result = new ModelAndView("profile/editCreditCardManager");
		result.addObject("creditCard", creditCard);
		result.addObject("message", message);
		result.addObject("forbiddenOperation", false);

		return result;
	}

	protected ModelAndView createEditCreditCardManagerModelAndViewError(final CreditCard creditCard, final String message) {
		ModelAndView res;
		res = new ModelAndView("profile/editCreditCardManager");
		res.addObject("creditCard", creditCard);
		res.addObject("message", message);
		res.addObject("forbiddenOperation", true);
		return res;

	}

	//----------------------

	protected ModelAndView editCreditCardModelAndView(final CreditCard creditCard) {
		ModelAndView res;
		res = this.editCreditCardModelAndView(creditCard, null);
		return res;
	}

	protected ModelAndView editCreditCardModelAndView(final CreditCard creditCard, final String message) {
		ModelAndView result;

		result = new ModelAndView("profile/createCreditCard");
		result.addObject("creditCard", creditCard);
		result.addObject("message", message);
		result.addObject("forbiddenOperation", false);

		return result;
	}

	protected ModelAndView editCreditCardManagerModelAndView(final CreditCard creditCard) {
		ModelAndView res;
		res = this.editCreditCardManagerModelAndView(creditCard, null);
		return res;
	}

	protected ModelAndView editCreditCardManagerModelAndView(final CreditCard creditCard, final String message) {
		ModelAndView result;

		result = new ModelAndView("profile/createCreditCardManager");
		result.addObject("creditCard", creditCard);
		result.addObject("message", message);
		result.addObject("forbiddenOperation", false);

		return result;
	}

}
