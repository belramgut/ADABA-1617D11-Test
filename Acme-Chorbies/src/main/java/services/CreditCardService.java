
package services;

import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.CreditCardRepository;
import domain.CreditCard;

@Service
@Transactional
public class CreditCardService {

	// Managed repository -----------------------------------------------------

	@Autowired
	private CreditCardRepository	creditCardRepository;

	@Autowired
	private Validator				validator;


	// Constructors -----------------------------------------------------------

	public CreditCardService() {
		super();
	}

	// Simple CRUD -------------------------------------------------------------

	public CreditCard create() {
		CreditCard res;
		res = new CreditCard();
		return res;
	}

	public CreditCard reconstruct(final CreditCard creditCard, final BindingResult binding) {
		CreditCard result;

		if (creditCard.getId() == 0)
			result = creditCard;
		else {
			result = this.creditCardRepository.findOne(creditCard.getId());
			result.setBrandName(creditCard.getBrandName());
			result.setNumber(creditCard.getNumber());

			if (this.validateCreditCardNumber(result.getNumber()) == false)
				result.setNumber(null);

			result.setCvvCode(creditCard.getCvvCode());
			result.setExpirationMonth(creditCard.getExpirationMonth());
			result.setExpirationYear(creditCard.getExpirationYear());
			if (this.validateDate(result.getExpirationMonth(), result.getExpirationYear())) {
				result.setNumber(null);
				result.setBrandName(null);

			}
			result.setHolderName(creditCard.getHolderName());
			this.validator.validate(result, binding);
		}
		return result;
	}

	public Collection<CreditCard> findAll() {
		Collection<CreditCard> res;
		res = this.creditCardRepository.findAll();
		Assert.notNull(res);
		return res;
	}

	public CreditCard findOne(final int creditCardId) {
		CreditCard res;
		res = this.creditCardRepository.findOne(creditCardId);
		Assert.notNull(res);
		return res;
	}

	public CreditCard save(final CreditCard c) {
		Assert.notNull(c);
		return this.creditCardRepository.save(c);

	}

	public CreditCard saveAndFlush(final CreditCard c) {
		Assert.notNull(c);
		return this.creditCardRepository.saveAndFlush(c);

	}

	public void delete(final CreditCard c) {
		Assert.notNull(c);
		this.creditCardRepository.delete(c);
	}

	// Other business methods --------------------------------------

	public boolean validateCreditCardNumber(final String number) {

		int sum = 0;
		boolean alternate = false;
		for (int i = number.length() - 1; i >= 0; i--) {
			int n = Integer.parseInt(number.substring(i, i + 1));
			if (alternate) {
				n *= 2;
				if (n > 9)
					n = (n % 10) + 1;
			}
			sum += n;
			alternate = !alternate;

		}
		return (sum % 10 == 0);

	}

	public boolean validateDate(final int month, final int year) {
		boolean res = false;

		final Calendar cal = new GregorianCalendar();
		final Integer yearActual = cal.get(Calendar.YEAR);
		final Integer monthActual = cal.get(Calendar.MONTH) + 1;

		//Primero comprobamos el año que nos pase sea mayor que el año actual
		if (year == yearActual) {
			//Comprobamos ahora que el mes que nos pase sea mayor que el mes actual
			if (month > monthActual)
				res = true;
		} else if (year > yearActual)
			res = true;

		return res;
	}
}
