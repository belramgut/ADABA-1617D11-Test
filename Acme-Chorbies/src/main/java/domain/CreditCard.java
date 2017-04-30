
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.validator.constraints.CreditCardNumber;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

@Entity
@Access(AccessType.PROPERTY)
@Table(indexes = {
	@Index(columnList = "expirationYear")
})
public class CreditCard extends DomainEntity {

	//Constructors ---------------------------------------------------------------------------

	public CreditCard() {
		super();
	}


	//Attributes ---------------------------------------------------------------------------

	private String		holderName;
	private BrandName	brandName;
	private String		number;
	private int			expirationMonth;
	private int			expirationYear;
	private int			cvvCode;


	//Getters & Setters ----------------------------------------------------------------------

	@NotBlank
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getHolderName() {
		return this.holderName;
	}

	public void setHolderName(final String holderName) {
		this.holderName = holderName;
	}

	public BrandName getBrandName() {
		return this.brandName;
	}

	public void setBrandName(final BrandName brandName) {
		this.brandName = brandName;
	}

	@NotBlank
	@CreditCardNumber
	@SafeHtml(whitelistType = WhiteListType.NONE)
	public String getNumber() {
		return this.number;
	}

	public void setNumber(final String number) {
		this.number = number;
	}

	@Range(min = 1, max = 12)
	public int getExpirationMonth() {
		return this.expirationMonth;
	}

	public void setExpirationMonth(final int expirationMonth) {
		this.expirationMonth = expirationMonth;
	}

	public int getExpirationYear() {
		return this.expirationYear;
	}

	public void setExpirationYear(final int expirationYear) {
		this.expirationYear = expirationYear;
	}

	@Range(min = 100, max = 999)
	public int getCvvCode() {
		return this.cvvCode;
	}

	public void setCvvCode(final int cvvCode) {
		this.cvvCode = cvvCode;
	}

}
