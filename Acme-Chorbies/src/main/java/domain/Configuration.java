
package domain;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.validation.constraints.Digits;

@Entity
@Access(AccessType.PROPERTY)
public class Configuration extends DomainEntity {

	//Constructors ---------------------------------------------------------------------------

	public Configuration() {
		super();
	}


	//Attributes ---------------------------------------------------------------------------

	private int		hour;
	private int		minute;
	private int		second;
	private double	managersFee;
	private double	chorbiesFee;


	//Getters & Setters ----------------------------------------------------------------------

	public int getHour() {
		return this.hour;
	}

	public void setHour(final int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return this.minute;
	}

	public void setMinute(final int minute) {
		this.minute = minute;
	}

	public int getSecond() {
		return this.second;
	}

	public void setSecond(final int second) {
		this.second = second;
	}

	@Digits(integer = 32, fraction = 2)
	public double getManagersFee() {
		return this.managersFee;
	}

	public void setManagersFee(final double managersFee) {
		this.managersFee = managersFee;
	}

	@Digits(integer = 32, fraction = 2)
	public double getChorbiesFee() {
		return this.chorbiesFee;
	}

	public void setChorbiesFee(final double chorbiesFee) {
		this.chorbiesFee = chorbiesFee;
	}

}
