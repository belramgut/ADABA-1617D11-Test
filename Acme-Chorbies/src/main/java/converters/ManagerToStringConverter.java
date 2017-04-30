
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Manager;

@Component
@Transactional
public class ManagerToStringConverter implements Converter<Manager, String> {

	@Override
	public String convert(final Manager source) {
		String res;

		if (source == null)
			res = null;
		else
			res = String.valueOf(source.getId());

		return res;
	}

}
