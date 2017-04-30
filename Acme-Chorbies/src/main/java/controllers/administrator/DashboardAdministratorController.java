
package controllers.administrator;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import services.ChirpService;
import services.ChorbiService;
import services.ManagerService;
import services.TasteService;
import controllers.AbstractController;
import domain.Chorbi;
import domain.Manager;

@Controller
@RequestMapping("/administrator")
public class DashboardAdministratorController extends AbstractController {

	// Services -----------------------------------------------

	@Autowired
	private ChorbiService	chorbiService;

	@Autowired
	private ChirpService	chirpService;

	@Autowired
	private ManagerService	managerService;

	@Autowired
	private TasteService	tasteService;


	// Constructors -------------------------------------------
	public DashboardAdministratorController() {
		super();
	}

	// List Category ------------------------------------------

	@RequestMapping(value = "/dashboard")
	public ModelAndView dashboard() {
		final ModelAndView result;

		final Collection<Object[]> listingWithTheNumberOfChorbiesPerCountryAndCity = this.chorbiService.listingWithTheNumberOfChorbiesPerCountryAndCity();
		final Collection<Object[]> minimumMaximumAverageAgesOfTheChorbies = this.chorbiService.minimumMaximumAverageAgesOfTheChorbies();
		final Double ratioChorbiesWhoHaveNoRegisteredACreditCardOrHaveRegisteredAnInvalidCreditCard = this.chorbiService.ratioChorbiesWhoHaveNoRegisteredACreditCardOrHaveRegisteredAnInvalidCreditCard();
		final Double ratiosOfChorbiesWhoSearchActivities = this.chorbiService.ratiosOfChorbiesWhoSearchActivities();
		final Double ratiosOfChorbiesWhoSearchFriendship = this.chorbiService.ratiosOfChorbiesWhoSearchFriendship();
		final Double ratiosOfChorbiesWhoSearchLove = this.chorbiService.ratiosOfChorbiesWhoSearchLove();
		final Collection<Object[]> listOfChorbiesCortedByTheNumberOfLikesTheyHaveGot = this.chorbiService.listOfChorbiesCortedByTheNumberOfLikesTheyHaveGot();
		final Collection<Double> minOfLikesPerChorbie = this.chorbiService.minOfLikesPerChorbie();
		final Collection<Double> maxOfLikesPerChorbie = this.chorbiService.maxOfLikesPerChorbie();
		final Double averageLikesPerChorbi = this.chorbiService.averageLikesPerChorbi();
		final Collection<Chorbi> theChorbiesWhoHaveGotMoreChirps = this.chorbiService.theChorbiesWhoHaveGotMoreChirps();
		final Collection<Chorbi> theChorbiesWhoHaveSentMoreChirps = this.chorbiService.theChorbiesWhoHaveSentMoreChirps();
		final Collection<Object[]> minAvgMaxChirpsReceived = this.chirpService.minAvgMaxChirpsReceived();
		final Collection<Object[]> minAvgMaxChirpsSent = this.chirpService.minAvgMaxChirpsSent();

		//Dashboard 2.0

		final Collection<Manager> listingManagersSortedByNumbeOfEventsTheyOrganise = this.managerService.listingManagersSortedByNumbeOfEventsTheyOrganise();
		final Collection<Object[]> listingManagersAndAmountTheyDueInFees = this.managerService.listingManagersAndAmountTheyDueInFees();
		final Collection<Chorbi> listingChorbiesSortedByNumberOfEventsRegistered = this.chorbiService.listingChorbiesSortedByNumberOfEventsRegistered();
		final Collection<Object[]> listingChorbiesAndAmountTheyDueInFees = this.chorbiService.listingChorbiesAndAmountTheyDueInFees();
		final Collection<Object[]> listChorbiesSortedByAverageNumberStarsThatTheyHaveeGot = this.chorbiService.listChorbiesSortedByAverageNumberStarsThatTheyHaveeGot();
		final Long minStars = this.tasteService.minStars();
		final Long maxStars = this.tasteService.maxStars();
		final Double avgStars = this.tasteService.avgStars();

		result = new ModelAndView("administrator/dashboard");
		result.addObject("listingWithTheNumberOfChorbiesPerCountryAndCity", listingWithTheNumberOfChorbiesPerCountryAndCity);
		result.addObject("minimumMaximumAverageAgesOfTheChorbies", minimumMaximumAverageAgesOfTheChorbies);
		result.addObject("ratioChorbiesWhoHaveNoRegisteredACreditCardOrHaveRegisteredAnInvalidCreditCard", ratioChorbiesWhoHaveNoRegisteredACreditCardOrHaveRegisteredAnInvalidCreditCard);
		result.addObject("ratiosOfChorbiesWhoSearchActivities", ratiosOfChorbiesWhoSearchActivities);
		result.addObject("ratiosOfChorbiesWhoSearchFriendship", ratiosOfChorbiesWhoSearchFriendship);
		result.addObject("ratiosOfChorbiesWhoSearchLove", ratiosOfChorbiesWhoSearchLove);
		result.addObject("listOfChorbiesCortedByTheNumberOfLikesTheyHaveGot", listOfChorbiesCortedByTheNumberOfLikesTheyHaveGot);
		result.addObject("minOfLikesPerChorbie", minOfLikesPerChorbie);
		result.addObject("maxOfLikesPerChorbie", maxOfLikesPerChorbie);
		result.addObject("averageLikesPerChorbi", averageLikesPerChorbi);
		result.addObject("theChorbiesWhoHaveGotMoreChirps", theChorbiesWhoHaveGotMoreChirps);
		result.addObject("theChorbiesWhoHaveSentMoreChirps", theChorbiesWhoHaveSentMoreChirps);
		result.addObject("minAvgMaxChirpsReceived", minAvgMaxChirpsReceived);
		result.addObject("minAvgMaxChirpsSent", minAvgMaxChirpsSent);
		result.addObject("listingManagersSortedByNumbeOfEventsTheyOrganise", listingManagersSortedByNumbeOfEventsTheyOrganise);
		result.addObject("listingManagersAndAmountTheyDueInFees", listingManagersAndAmountTheyDueInFees);
		result.addObject("listingChorbiesSortedByNumberOfEventsRegistered", listingChorbiesSortedByNumberOfEventsRegistered);
		result.addObject("listingChorbiesAndAmountTheyDueInFees", listingChorbiesAndAmountTheyDueInFees);
		result.addObject("listChorbiesSortedByAverageNumberStarsThatTheyHaveeGot", listChorbiesSortedByAverageNumberStarsThatTheyHaveeGot);
		result.addObject("minStars", minStars);
		result.addObject("maxStars", maxStars);
		result.addObject("avgStars", avgStars);

		return result;
	}
}
