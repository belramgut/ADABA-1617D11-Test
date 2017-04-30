package form;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;


import utilities.validators.PasswordMatches;

@PasswordMatches
public class RegistrationForm {



	// Constructors -----------------------------------------------------------
	public RegistrationForm() {
		super();

	}


	// Attributes -------------------------------------------------------------



	private String	username;
	private String	password;
	private String	passwordCheck;
	
	private boolean	termsOfUse;
	private String	name;
	private String	surName;
	private String	email;
	private String	phone;

	
	private String relation;
	

	private String			picture;
	private String			description;
	private Date			birthDate;
	private String			genre;

	private String	country;
	private String	state;
	private String	province;
	private String	city;
	
	
	public String getRelation() {
		return relation;
	}
	public void setRelation(String relation) {
		this.relation = relation;
	}

	
	@NotBlank
	@URL
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}

	@NotBlank
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	public Date getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	
	
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	

	@NotBlank
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	

	@NotBlank
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
	
	@NotNull
	@Size(min = 5, max = 32)
	@Column(unique = true)
	public String getUsername() {
		return username;

	}
	public void setUsername(String username) {
		this.username = username;
	}



	@NotNull
	@Size(min = 5, max = 32)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@NotNull
	@Size(min = 5, max = 32)
	public String getPasswordCheck() {
		return passwordCheck;

	}

	public void setPasswordCheck(String passwordCheck) {
		this.passwordCheck = passwordCheck;
	}

	@AssertTrue
	public boolean getTermsOfUse() {
		return termsOfUse;

	}

	public void setTermsOfUse(boolean termsOfUse) {
		this.termsOfUse = termsOfUse;

	}

	@NotBlank
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;

	}

	@NotBlank
	public String getSurName() {
		return surName;
	}

	public void setSurName(String surName) {
		this.surName = surName;

	}

	
	@NotBlank
	@Email
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	@NotBlank
	@Pattern(regexp = "^\\+?\\d{1,3}?[- .]?\\d+$")
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}





}
	

