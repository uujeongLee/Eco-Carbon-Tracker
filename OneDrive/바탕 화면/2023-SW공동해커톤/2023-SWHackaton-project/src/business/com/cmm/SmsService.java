package business.com.cmm;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import commf.dao.CommonMapperImpl;
import common.base.BaseService;


@Service
@SuppressWarnings({"rawtypes"})
public class SmsService extends BaseService {

	@Autowired
	private CommonMapperImpl dao;


 
	public int insertSmsCert(Map paramMap) {
		// TODO Auto-generated method stub
		return dao.update("Sms.insertSmsCert",paramMap);
				
	}

}
