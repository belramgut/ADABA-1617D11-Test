package converters;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import security.UserAccount;
import security.UserAccountRepository;



@Component
@Transactional
public class StringToUserAccountConverter implements Converter<String,UserAccount>{
	
	@Autowired
	UserAccountRepository userAccountRepository;

	@Override
	public UserAccount convert(String text) {
		UserAccount res;
		int id;
		
		try{
			if(StringUtils.isEmpty(text)){
				res=null;
			}else{
				id = Integer.valueOf(text);
				res = userAccountRepository.findOne(id);
			}
		}catch (Throwable th){
			throw new IllegalArgumentException(th);
		}
		
		return res;
		
		
	}

}
