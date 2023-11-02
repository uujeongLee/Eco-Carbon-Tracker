package business.sys.log;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import commf.dao.CommonMapperImpl;

@Service
@SuppressWarnings({"all"})
public class AccessControlService {

	@Autowired
	private CommonMapperImpl dao;

	public int regiAccessLog(Map map){
		return dao.update("AccessControl.regiAccessLog", map);
	}

	public int regiLoginLog(Map map) {
		return dao.insert("AccessControl.regiLoginLog", map);
	}

}
