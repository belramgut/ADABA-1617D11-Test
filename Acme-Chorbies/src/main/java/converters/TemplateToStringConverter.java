
package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Template;

@Component
@Transactional
public class TemplateToStringConverter implements Converter<Template, String> {

	@Override
	public String convert(final Template source) {
		String res;

		if (source == null)
			res = null;
		else
			res = String.valueOf(source.getId());

		return res;
	}

}
