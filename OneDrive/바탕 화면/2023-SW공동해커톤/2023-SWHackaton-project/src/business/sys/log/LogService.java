package business.sys.log;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import commf.dao.CommonMapperImpl;
import commf.paging.PaginatedArrayList;
import common.base.BaseService;


@Service
@SuppressWarnings({"all" })
public class LogService extends BaseService {

	@Autowired
	private CommonMapperImpl dao;

    /**
     * 리스트 조회
     */
	public PaginatedArrayList getListBatchLog(Map paramMap, int currPage, int pageSize) throws Exception {
		return dao.pageListOra12("Log.listBatchLog", paramMap, currPage, pageSize);
	}

}
