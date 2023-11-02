package business.usr.temp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import commf.dao.CommonMapperImpl;
import commf.paging.PaginatedArrayList;
import common.base.BaseService;
import common.util.CommUtils;


@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class SampleService extends BaseService {

	/**
	 * 사용 가능한  DAO Statement Method
	 * 1. list 			: 리스트 조회시 사용함.
	 * 2. pageList		: 페이징처리용 리스트조회시 사용함.
	 *    pageListOra12	: 페이징처리용 (오라클12 쿼리사용 조회시)
	 * 3. view			: 단건조회, 상세조회시 사용함.
	 * 4. save 			: INSERT, UPDATE, DELETE 모두 사용가능. (Return Type : Integer)
	 * 5. insert		: INSERT (Return String : Key 채번 사용함.)
	 * 6. update		: UPDATE (Return Type : Integer)
	 * 7. delete		: DELETE (Return Type : Integer)
	 * 8. batch			: Batch 등록 사용시
	 */

	@Autowired
	private CommonMapperImpl dao;

    /**
     * 샘플 리스트
     */
	public List listSample(Map paramMap) throws Exception {
		List list = null;

		list = dao.list("Sample.listSample", paramMap);

    	if (list == null) {
            throw processException("exception.NoResult");
    	}

		return list;
	}

    /**
     * 샘플 조회
     */
	public Map viewSample(Map paramMap) throws Exception {
		return (HashMap)dao.view("Sample.viewSample", paramMap);
	}

    /**
     * 샘플 페이징리스트 (기존)
     */
	public PaginatedArrayList listSample(Map paramMap, int currPage, int pageSize) throws Exception {
		return dao.pageList("Sample.listSample", paramMap, currPage, pageSize);
	}


    /**
     * 샘플 등록
     */
	public String regiSample(Map paramMap) throws Exception {
 		dao.insert("Sample.regiSample", paramMap);
		String keyNo = (String)paramMap.get("keyNo");

		logger.info("KEY NO ============= "+ keyNo);

		return keyNo;
	}

    /**
     * 샘플 일괄등록
     */
	public int regiSampleMulti(Map paramMap) throws Exception {
		int n = 0;
		String startTime	= "";
		String endTime		= "";
		String regiMode		= CommUtils.nvlTrim((String)paramMap.get("regiMode"));

		int totrows			= 10000;

		startTime 	= CommUtils.getCurrDateTime();

		// 일반등록처리 (mode : 1)
		if (regiMode.equals("1")) {
			paramMap.put("gsUserId", 	"ntarget");
			paramMap.put("userNm", 		CommUtils.nvlTrim((String)paramMap.get("userNm")));
			paramMap.put("title", 		CommUtils.nvlTrim((String)paramMap.get("title")) + " - 일괄");
			paramMap.put("content",		CommUtils.nvlTrim((String)paramMap.get("content")));
			paramMap.put("userType",	"01");

			for (int i = 0; i < totrows; i++) {
				n += dao.save("Sample.regiSampleMulti", paramMap);
			}

	    	if (n == 0) {
	            throw processException("exception.NoResult");
	    	}
		}
		// 배치등록처리
		else if (regiMode.equals("2")) {
			Map rowmap		= new HashMap(paramMap);
			List list		= new ArrayList();

			for (int r = 0; r < totrows; r++) {
				rowmap.put("gsUserId", 		"ntarget");
				rowmap.put("userNm", 		CommUtils.nvlTrim((String)paramMap.get("userNm")));
				rowmap.put("title", 		CommUtils.nvlTrim((String)paramMap.get("title")) + " - 배치");
				rowmap.put("content",		CommUtils.nvlTrim((String)paramMap.get("content")));
				rowmap.put("userType",		"09");

				list.add(rowmap);

				// Out of Memory Error로 짤라서 처리.
	    		if (list.size() == 1000 || r == totrows-1) {
					regiSampleBatchBatis(list, "Sample.regiSampleMulti");
					list.clear();
	    		}
			}

		}

		endTime		= CommUtils.getCurrDateTime();

		logger.info("★ ★ ★ ★ ★ ★ ★ ★ ★ ★ ★ ★        시작시간 : "+ startTime +", 종료시간 : "+ endTime);

		return 1;
	}

    /**
     * 샘플 수정
     */
	public int updtSample(Map paramMap) throws Exception {
		return (Integer)dao.update("Sample.updtSample", paramMap);
	}

    /**
     * 샘플 삭제
     */
	public int deltSample(Map paramMap) throws Exception {
		return (Integer)dao.delete("Sample.deltSample", paramMap);
	}

	/**
	 * 배치 등록
	 */
	public int regiSampleBatchBatis(final List list, final String statement) throws Exception {
		try {
			for (int i = 0; i < list.size(); i++) {
				dao.getSqlBatchSession().insert(statement, list.get(i));
			}
			dao.getSqlBatchSession().flushStatements();
			dao.getSqlBatchSession().commit();
			return 1;
		} catch(Exception ex) {
			return 0;
		}
	}
}