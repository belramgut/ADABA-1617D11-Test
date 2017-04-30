
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

@Entity
@Access(AccessType.PROPERTY)
public class Manager extends Actor {

	//Constructors ---------------------------------------------------------------------------

	public Manager() {
		super();
	}


	//Attributes ---------------------------------------------------------------------------

	private String	company;
	private String	vatNumber;
	private double	totalChargedFee;


	//Getters & Setters ----------------------------------------------------------------------

	@Min(0)
	@Digits(integer = 32, fraction = 2)
	public double getTotalChargedFee() {
		return this.totalChargedFee;
	}

	public void setTotalChargedFee(final double totalChargedFee) {
		this.totalChargedFee = totalChargedFee;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getCompany() {
		return this.company;
	}

	public void setCompany(final String company) {
		this.company = company;
	}

	@NotBlank
	@Pattern(regexp = "^ES[ABCDEFGHJNPQRSUVW]{1}[1-9]{8}$")
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getVatNumber() {
		return this.vatNumber;
	}

	public void setVatNumber(final String vatNumber) {
		this.vatNumber = vatNumber;
	}


	//Relationships

	Collection<Event>	events;


	@Valid
	@OneToMany(mappedBy = "manager")
	public Collection<Event> getEvents() {
		return this.events;
	}

	public void setEvents(final Collection<Event> events) {
		this.events = events;
	}

}
