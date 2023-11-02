package business.main;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import commf.dao.CommonMapperImpl;
import common.base.BaseService;

@Service
public class MainService extends BaseService{

	@Autowired
	private CommonMapperImpl dao;
	
	public List<Map> selectRegiInfo() {
		return dao.selectList("Main.selectTest");
		// TODO Auto-generated method stub
		
	}
	
	public List<Map> selectBuidingLoc(Map paramMap){
		
		return dao.selectList("Main.selectBuidingLoc", paramMap);
	}
	
	public Map searchBuildingInfo(Map paramMap) {
		return dao.selectOne("Main.searchBuildingInfo", paramMap);
	}
	
	public List<Map> getSggListBySd(Map paramMap) {
		return dao.selectList("Main.getSggListBySd", paramMap);
	}
}
