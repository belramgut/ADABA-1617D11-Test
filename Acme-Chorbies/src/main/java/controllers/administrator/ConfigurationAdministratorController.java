
package controllers.administrator;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ConfigurationService;
import controllers.AbstractController;
import domain.Configuration;

@Controller
@RequestMapping("/configuration/administrator")
public class ConfigurationAdministratorController extends AbstractController {

	// Services ---------------------------------------------------------------

	@Autowired
	private ConfigurationService	configurationService;


	// Constructors -----------------------------------------------------------

	public ConfigurationAdministratorController() {
		super();
	}

	// Listing ----------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Configuration> configurations;

		configurations = this.configurationService.findAll();

		result = new ModelAndView("configuration/list");
		result.addObject("configurations", configurations);
		result.addObject("requestURI", "configurations/administrator/list.do");

		return result;
	}

	// Edition ----------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int configurationId) {
		ModelAndView result;
		Configuration config;

		config = this.configurationService.findOne(configurationId);
		result = this.createEditModelAndView(config);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Configuration config, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(config);
		else
			try {
				this.configurationService.save(config);
				result = new ModelAndView("redirect:list.do");

			} catch (final Throwable oops) {

				result = this.createEditModelAndView(config, "configuration.commit.error");
			}

		return result;
	}

	//-------------------------------------------------------------------------------------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Configuration config) {
		ModelAndView result;

		result = this.createEditModelAndView(config, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Configuration configuration, final String message) {
		ModelAndView result;

		result = new ModelAndView("configuration/edit");
		result.addObject("configuration", configuration);
		result.addObject("message", message);

		return result;
	}

}
