
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Chirp;

@Component
@Transactional
public class ChirpToStringConverter implements Converter<Chirp, String> {

	@Override
	public String convert(final Chirp source) {
		String res;

		if (source == null)
			res = null;
		else
			res = String.valueOf(source.getId());

		return res;
	}

}
