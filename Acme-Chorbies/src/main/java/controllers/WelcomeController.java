/*
 * WelcomeController.java
 * 
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.BannerService;
import domain.Banner;

@Controller
@RequestMapping("/welcome")
public class WelcomeController extends AbstractController {

	// Constructors -----------------------------------------------------------

	public WelcomeController() {
		super();
	}


	@Autowired
	private BannerService	bannerService;


	// Index ------------------------------------------------------------------		

	@RequestMapping(value = "/index")
	public ModelAndView index(@RequestParam(required = false, defaultValue = "John Doe") final String name) {
		ModelAndView result;
		SimpleDateFormat formatter;
		String moment;
		String banner;

		formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		moment = formatter.format(new Date());

		final List<Banner> banners = (List<Banner>) this.bannerService.findAll();

		if (banners.size() != 0) {
			final Random rand = new Random();
			final int aux = rand.nextInt(banners.size());
			banner = banners.get(aux).getUrl();

		} else
			banner = "";

		result = new ModelAndView("welcome/index");
		result.addObject("name", name);
		result.addObject("moment", moment);
		result.addObject("banner", banner);

		return result;
	}

	@RequestMapping(value = "/cookiesPolicy")
	public ModelAndView cookiesPolicy() {

		ModelAndView result;

		result = new ModelAndView("welcome/cookiesPolicy");

		return result;

	}

	@RequestMapping(value = "/about")
	public ModelAndView about() {
		ModelAndView result;
		result = new ModelAndView("welcome/about");
		return result;

	}

}
