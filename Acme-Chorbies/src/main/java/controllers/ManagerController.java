
package controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ManagerService;
import domain.CreditCard;
import domain.Manager;
import form.RegistrationFormManager;

@Controller
@RequestMapping("/manager")
public class ManagerController extends AbstractController {

	// Constructors -----------------------------------------------------------

	public ManagerController() {
		super();
	}


	//Services -----------------------------------------------------------

	@Autowired
	private ManagerService	managerService;


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
		RegistrationFormManager manager;

		manager = new RegistrationFormManager();
		result = this.createEditModelAndView(manager);

		return result;

	}

	@RequestMapping(value = "/register", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@ModelAttribute("manager") @Valid final RegistrationFormManager form, final BindingResult binding) {

		ModelAndView result;
		Manager manager;
		if (binding.hasErrors()) {
			if (binding.getGlobalError() != null)
				result = this.createEditModelAndView(form, binding.getGlobalError().getCode());
			else
				result = this.createEditModelAndView(form);
		} else
			try {
				manager = this.managerService.reconstruct(form);
				manager = this.managerService.save(manager);

				result = new ModelAndView("redirect:..");
			} catch (final DataIntegrityViolationException exc) {
				result = this.createEditModelAndView(form, "manager.duplicated.user");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(form, "manager.commit.error");
			}
		return result;
	}

	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public ModelAndView display(@RequestParam final int managerId) {
		ModelAndView result;
		Manager manager;
		Manager principal;

		manager = this.managerService.findOne(managerId);
		principal = this.managerService.findByPrincipal();

		boolean toCreditCard = false;

		final CreditCard creditCard = manager.getCreditCard();
		if (creditCard != null)
			toCreditCard = true;

		result = new ModelAndView("manager/displayProfile");
		result.addObject("manager", manager);
		result.addObject("principal", principal);
		result.addObject("creditCard", creditCard);
		result.addObject("toCreditCard", toCreditCard);
		result.addObject("requestURI", "manager/profile.do?managerId=" + managerId);

		return result;
	}

	@RequestMapping(value = "/viewProfile", method = RequestMethod.GET)
	public ModelAndView viewProfile() {
		return this.display(this.managerService.findByPrincipal().getId());

	}

	// Other methods

	protected ModelAndView createEditModelAndView(final RegistrationFormManager manager) {
		ModelAndView result;
		result = this.createEditModelAndView(manager, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final RegistrationFormManager manager, final String message) {
		ModelAndView result;
		result = new ModelAndView("manager/register");
		result.addObject("manager", manager);
		result.addObject("message", message);
		return result;

	}

}
