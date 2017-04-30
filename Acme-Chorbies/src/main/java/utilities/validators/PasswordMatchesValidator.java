
package utilities.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import form.RegistrationForm;
	
public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, RegistrationForm> {

	@Override
	public void initialize(final PasswordMatches constraintAnnotation) {
	}
	@Override
	public boolean isValid(final RegistrationForm form, final ConstraintValidatorContext context) {
		return form.getPassword().equals(form.getPasswordCheck());
	}
}

