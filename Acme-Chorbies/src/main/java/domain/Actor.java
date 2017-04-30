
package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

import security.UserAccount;

@Entity
@Access(AccessType.PROPERTY)
public class Actor extends DomainEntity {

	//Constructors ---------------------------------------------------------------------------

	public Actor() {
		super();
	}


	//Attributes ---------------------------------------------------------------------------

	private String	name;
	private String	surName;
	private String	email;
	private String	phone;


	//Getters & Setters ----------------------------------------------------------------------

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getSurName() {
		return this.surName;
	}

	public void setSurName(final String surName) {
		this.surName = surName;
	}

	@NotBlank
	@Email
	public String getEmail() {
		return this.email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	@NotBlank
	@Pattern(regexp = "^\\+?\\d{1,3}?[- .]?\\d+$")
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(final String phone) {
		this.phone = phone;
	}


	//Relationships

	private CreditCard	creditCard;


	@OneToOne(optional = true)
	public CreditCard getCreditCard() {
		return this.creditCard;
	}

	public void setCreditCard(final CreditCard creditCard) {
		this.creditCard = creditCard;
	}


	private UserAccount	userAccount;


	@NotNull
	@Valid
	@OneToOne(cascade = CascadeType.ALL, optional = false)
	public UserAccount getUserAccount() {
		return this.userAccount;
	}

	public void setUserAccount(final UserAccount userAccount) {
		this.userAccount = userAccount;
	}


	private Collection<Chirp>	chirpReceives;
	private Collection<Chirp>	chirpWrites;


	@OneToMany(mappedBy = "recipient")
	public Collection<Chirp> getChirpReceives() {
		return this.chirpReceives;
	}

	public void setChirpReceives(final Collection<Chirp> chirpReceives) {
		this.chirpReceives = chirpReceives;
	}

	@OneToMany(mappedBy = "sender")
	public Collection<Chirp> getChirpWrites() {
		return this.chirpWrites;
	}

	public void setChirpWrites(final Collection<Chirp> chirpWrites) {
		this.chirpWrites = chirpWrites;
	}

}
