
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Event;

@Component
@Transactional
public class EventToStringConverter implements Converter<Event, String> {

	@Override
	public String convert(final Event source) {
		String res;

		if (source == null)
			res = null;
		else
			res = String.valueOf(source.getId());

		return res;
	}

}
