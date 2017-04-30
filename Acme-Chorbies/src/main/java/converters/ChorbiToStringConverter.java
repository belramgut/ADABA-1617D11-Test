
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Chorbi;

@Component
@Transactional
public class ChorbiToStringConverter implements Converter<Chorbi, String> {

	@Override
	public String convert(final Chorbi source) {
		String res;

		if (source == null)
			res = null;
		else
			res = String.valueOf(source.getId());

		return res;
	}

}
