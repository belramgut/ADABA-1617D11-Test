package converters;

import javax.transaction.Transactional;

import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;

import domain.Actor;

@Component
@Transactional
public class ActorToStringConverter implements Converter<Actor, String> {

	@Override
	public String convert(Actor source) {
		String res;

		if (source == null) {
			res = null;
		} else {
			res = String.valueOf(source.getId());
		}

		return res;
	}

}