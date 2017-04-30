
package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ChorbiService;
import services.CreditCardService;
import services.TasteService;
import domain.Chorbi;
import domain.Taste;

@Controller
@RequestMapping("/chorbi/chorbi")
public class TasteController {

	// Constructors -----------------------------------------------------------

	public TasteController() {
		super();
	}


	//Services -----------------------------------------------------------

	@Autowired
	private ChorbiService		chorbiService;

	@Autowired
	private TasteService		tasteService;

	@Autowired
	private CreditCardService	creditCardService;


	//MyLikes ------------------------------------------------

	@RequestMapping(value = "/myLikes", method = RequestMethod.GET)
	public ModelAndView myLikes() {

		ModelAndView result;
		final Collection<Taste> tastes;
		Chorbi principal;

		try {

			principal = this.chorbiService.findByPrincipal();
			Assert.isTrue(principal.getCreditCard() != null);
			Assert.isTrue(this.creditCardService.validateDate(principal.getCreditCard().getExpirationMonth(), principal.getCreditCard().getExpirationYear()));
			tastes = this.chorbiService.findAllMyTastesWithoutBannedChorbies(principal);
			result = new ModelAndView("taste/list");
			result.addObject("tastes", tastes);
			result.addObject("requestURI", "chorbi/chorbi/myLikes.do");

		} catch (final Throwable th) {
			principal = this.chorbiService.findByPrincipal();
			result = new ModelAndView("invalidCreditCard");
			result.addObject("creditCard", principal.getCreditCard());
		}

		return result;

	}

	//People who i like  ------------------------------------------------

	@RequestMapping(value = "/likesToMe", method = RequestMethod.GET)
	public ModelAndView likestoMe() {

		ModelAndView result;
		final Collection<Chorbi> tastes;
		Chorbi principal;

		principal = this.chorbiService.findByPrincipal();
		tastes = this.chorbiService.findAllTastesToMeWithoutBannedChorbies(principal);

		result = new ModelAndView("taste/list2");
		result.addObject("chorbies", tastes);
		result.addObject("principal", principal);
		result.addObject("requestURI", "chorbi/chorbi/likesToMe.do");

		return result;

	}

	// Create ------------------------------------------------

	@RequestMapping(value = "/like", method = RequestMethod.GET)
	public ModelAndView like(@RequestParam final int chorbiId) {
		ModelAndView result;
		Taste taste;
		final Chorbi principal = this.chorbiService.findByPrincipal();
		final Chorbi chorbiToLike = this.chorbiService.findOne(chorbiId);

		if (principal.getId() == chorbiToLike.getId()) {

			result = new ModelAndView("taste/forboperation");
			result.addObject("forbiddenOperation", "taste.not.like.yourself");
			result.addObject("cancelURL", "chorbi/profile.do?chorbiId=" + chorbiId);
			return result;

		} else
			for (final Taste t : principal.getGivenTastes())
				if (t.getChorbi().getId() == chorbiToLike.getId()) {
					result = new ModelAndView("taste/forboperation");
					result.addObject("forbiddenOperation", "taste.not.twice");
					result.addObject("cancelURL", "chorbi/profile.do?chorbiId=" + chorbiId);
					return result;
				}

		taste = this.tasteService.create(chorbiToLike);
		result = this.createEditModelAndView(taste);
		result.addObject("requestURI", "chorbi/chorbi/like.do?chorbiId=" + chorbiId);
		result.addObject("cancelURL", "chorbi/profile.do?chorbiId=" + chorbiId);
		result.addObject("chorbiId", chorbiId);

		return result;
	}

	//Save ------------------------------------------------

	@RequestMapping(value = "/like", method = RequestMethod.POST, params = "save")
	public ModelAndView save(Taste taste, final BindingResult binding, @RequestParam final int chorbiId) {
		ModelAndView result;

		final Chorbi chorbiToLike = this.chorbiService.findOne(chorbiId);
		final Chorbi principal = this.chorbiService.findByPrincipal();

		taste = this.tasteService.reconstruct(taste, binding, chorbiToLike);
		if (binding.hasErrors()) {
			result = this.createEditModelAndView(taste);
			result.addObject("requestURI", "chorbi/chorbi/like.do?chorbiId=" + chorbiId);
			result.addObject("cancelURL", "chorbi/profile.do?chorbiId=" + chorbiId);
			result.addObject("chorbiId", chorbiId);
		} else
			try {
				this.tasteService.save(taste);
				result = new ModelAndView("redirect:/chorbi/listWhoLikedThis.do?chorbiId=" + principal.getId());
			} catch (final Throwable th) {
				result = this.createEditModelAndView(taste, "taste.commit.error");
			}

		return result;

	}

	// Delete  --------------------------------------------------------------

	@RequestMapping(value = "/cancelLike", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int chorbiId) {

		ModelAndView result;
		Chorbi chorbi;

		chorbi = this.chorbiService.findOne(chorbiId);
		final Chorbi principal = this.chorbiService.findByPrincipal();

		if (chorbi.isBan() == true) {
			result = new ModelAndView("taste/forboperation");
			result.addObject("forbiddenOperation", "not.cancel.like.to.banned");
			result.addObject("cancelURL", "chorbi/profile.do?chorbiId=" + principal.getId());
			return result;
		}

		if (principal.getId() == chorbi.getId()) {

			result = new ModelAndView("taste/forboperation");
			result.addObject("forbiddenOperation", "taste.not.cancel.like.yourself");
			result.addObject("cancelURL", "chorbi/profile.do?chorbiId=" + chorbiId);
			return result;

		} else if (!this.chorbiService.findAllChorbiesWhoLikedByThisUserForNotDoubleLike(principal).contains(chorbi)) {
			result = new ModelAndView("taste/forboperation");
			result.addObject("forbiddenOperation", "operacion.prohibida");
			result.addObject("cancelURL", "chorbi/profile.do?chorbiId=" + chorbiId);
			return result;
		}

		this.tasteService.delete(chorbi);

		result = new ModelAndView("redirect:/chorbi/listWhoLikedThis.do?chorbiId=" + principal.getId());

		return result;

	}

	// Ancillary methods ------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Taste taste) {
		ModelAndView result;

		result = this.createEditModelAndView(taste, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Taste taste, final String message) {
		ModelAndView result;

		result = new ModelAndView("taste/edit");
		result.addObject("taste", taste);
		result.addObject("message", message);

		return result;
	}

}
