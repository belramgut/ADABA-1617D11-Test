package converters;

import javax.transaction.Transactional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import domain.Banner;

@Component
@Transactional
public class BannerToStringConverter implements Converter<Banner, String> {

	@Override
	public String convert(final Banner source) {
		String res;

		if (source == null)
			res = null;
		else
			res = String.valueOf(source.getId());

		return res;
	}

}