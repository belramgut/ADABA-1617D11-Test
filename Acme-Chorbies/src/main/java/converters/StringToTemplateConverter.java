
package converters;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import repositories.TemplateRepository;
import domain.Template;

@Component
@Transactional
public class StringToTemplateConverter implements Converter<String, Template> {

	@Autowired
	TemplateRepository	templateRepository;


	@Override
	public Template convert(final String text) {
		Template result;
		int id;

		try {
			if (StringUtils.isEmpty(text))
				result = null;
			else {
				id = Integer.valueOf(text);
				result = this.templateRepository.findOne(id);
			}
		} catch (final Throwable oops) {
			throw new IllegalArgumentException(oops);
		}

		return result;
	}

}
