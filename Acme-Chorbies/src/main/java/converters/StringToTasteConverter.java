
package converters;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import repositories.TasteRepository;
import domain.Taste;

@Component
@Transactional
public class StringToTasteConverter implements Converter<String, Taste> {

	@Autowired
	TasteRepository	tasteRepository;


	@Override
	public Taste convert(final String text) {
		Taste result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.tasteRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
