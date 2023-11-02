package business.com.cmm;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import commf.dao.CommonMapperImpl;
import common.base.BaseService;


@Service
@SuppressWarnings({"rawtypes"})
public class CommService extends BaseService {

	@Autowired
	private CommonMapperImpl dao;

	/**
     * 코드 리스트 조회
     */
    public List listCode(Map paramMap) throws Exception {
    	return dao.list("Comm.listCode", paramMap);
    }

    /**
     * 코드 정보 조회
     */
    public Map viewCode(Map paramMap) throws Exception {
        return (Map)dao.view("Comm.viewCode", paramMap);
    }

	public List searchSidoList() {
		// TODO Auto-generated method stub
		return dao.selectList("Comm.searchSidoList");
	}

	public List searchSigunguList(Map paramMap) {
		// TODO Auto-generated method stub
		return dao.selectList("Comm.searchSigunguList",paramMap);
	}

}
