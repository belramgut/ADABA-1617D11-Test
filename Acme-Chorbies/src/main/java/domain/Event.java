
package domain;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "moment,numberSeatsOffered")
})
public class Event extends DomainEntity {

	//Constructors ---------------------------------------------------------------------------

	public Event() {
		super();
	}


	//Attributes ---------------------------------------------------------------------------

	private String	title;
	private Date	moment;
	private String	description;
	private String	picture;
	private int		numberSeatsOffered;
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
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	public Date getMoment() {
		return this.moment;
	}

	//Habrá que controlar en la implementación que este momento sea futuro, pues no se pueden organizar eventos en el pasado

	public void setMoment(final Date moment) {
		this.moment = moment;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@URL
	@NotBlank
	public String getPicture() {
		return this.picture;
	}

	public void setPicture(final String picture) {
		this.picture = picture;
	}

	@Min(1)
	public int getNumberSeatsOffered() {
		return this.numberSeatsOffered;
	}

	public void setNumberSeatsOffered(final int numberSeatsOffered) {
		this.numberSeatsOffered = numberSeatsOffered;
	}


	//Relationships

	private Manager				manager;
	private Collection<Chorbi>	registered;


	@Valid
	@NotNull
	@ManyToOne(optional = false)
	public Manager getManager() {
		return this.manager;
	}

	public void setManager(final Manager manager) {
		this.manager = manager;
	}

	@Valid
	@NotNull
	@ManyToMany
	public Collection<Chorbi> getRegistered() {
		return this.registered;
	}

	public void setRegistered(final Collection<Chorbi> registered) {
		this.registered = registered;
	}

}
