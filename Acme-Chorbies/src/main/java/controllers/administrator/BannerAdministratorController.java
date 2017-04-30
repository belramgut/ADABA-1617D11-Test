
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

import services.BannerService;
import controllers.AbstractController;
import domain.Banner;

@Controller
@RequestMapping("/banner/administrator")
public class BannerAdministratorController extends AbstractController {

	// Supporting services ----------------------------------------------------

	@Autowired
	private BannerService	bannerService;


	// Constructor ------------------------------------------------------------

	public BannerAdministratorController() {
		super();
	}

	// Listing ----------------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Banner> banners;

		banners = this.bannerService.findAll();

		result = new ModelAndView("banner/list");
		result.addObject("banners", banners);
		result.addObject("requestURI", "banner/administrator/list.do");

		return result;

	}

	// Creation ---------------------------------------------------------------

	// Edition ----------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int bannerId) {
		ModelAndView result;
		Banner banner;

		banner = this.bannerService.findOne(bannerId);
		result = this.createEditModelAndView(banner);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Banner banner, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(banner);
		else
			try {
				this.bannerService.save(banner);
				result = new ModelAndView("redirect:list.do");

			} catch (final Throwable oops) {

				result = this.createEditModelAndView(banner, "banner.commit.error");
			}

		return result;
	}

	//-------------------------------------------------------------------------------------------------------------------------------------

	protected ModelAndView createEditModelAndView(final Banner banner) {
		ModelAndView result;

		result = this.createEditModelAndView(banner, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Banner banner, final String message) {
		ModelAndView result;

		result = new ModelAndView("banner/edit");
		result.addObject("banner", banner);
		result.addObject("message", message);

		return result;
	}

}
