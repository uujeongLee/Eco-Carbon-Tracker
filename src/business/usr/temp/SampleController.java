package business.usr.temp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.map.LinkedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import business.com.cmm.CommService;
import business.com.cmm.FileService;
import business.usr.temp.domain.SampleVO;
import commf.paging.PaginatedArrayList;
import common.base.BaseController;
import common.file.FileManager;
import common.util.CommUtils;
import common.util.properties.ApplicationProperty;
import egovframework.com.utl.sim.service.EgovFileScrty;
import egovframework.rte.fdl.cmmn.exception.EgovBizException;


@Controller
@SuppressWarnings({"rawtypes","unchecked","static-access","unused"})
public class SampleController extends BaseController {

	@Autowired
	private SampleService sampleService;

	@Autowired
	private CommService commService;

	@Autowired
	private FileService fileService;

	@Resource(name="fileManager")
	FileManager fileManager;

    /**
     * 샘플 리스트
     *
     * 예제 샘플)
	 * 1. 리스트 조회
	 * 2. 파라메터 Map 으로 받기
	 * 3. Logging 처리 방법
	 * 4. Return Result 방법(ModelMap 으로)
	 * 5. Message 사용법 getMessage("");
     * 6. Paging 처리
     *
     * getParameterMap : 배열로 받기 -> arr로 시작하는 변수명.  ex)arrSysCode 는 배열로 받음.
     *
     */
	@RequestMapping("/usr/temp/listSample.do")
	public String listSample(HttpServletRequest request, ModelMap model
			, @ModelAttribute SampleVO sampleVO) throws Exception {

		Map paramMap = getParameterMap(request, true);
		setMappingValues(paramMap, getMethodName(new Throwable()));
		BeanUtils.copyProperties(sampleVO, paramMap);
		// -------------------- Default Setting End -----------------------//

		baseCurrPage = CommUtils.strToInt((String)paramMap.get("page"), 		1);
		basePageSize = CommUtils.strToInt((String)paramMap.get("pageSize"), 	10);

        paramMap.put("pageIndex", baseCurrPage);
        paramMap.put("pageSize",  basePageSize);

    	PaginatedArrayList list	= null;
    	list = sampleService.listSample(paramMap, baseCurrPage, basePageSize);

    	//코드조회
    	/*
    	List listCode = commService.listCode(paramMap);
    	if (listCode != null && listCode.size() > 0)
    		logger.info("LIST CODE =============== "+ listCode.get(0));
		*/
    	
    	// 로그 처리 & 메시지 처리
    	logger.info("Message =============== "+ message.getMessage("title.sysname"));

    	// Exception 처리
    	// Controller 에서 Exception 처리 -> EgovBizException 사용
    	if (1 > 1) {
    		throw new EgovBizException("EgovBizException 예외사항 처리입니다.");
    	}

    	// 콤보 테스트1
    	Map lmap = new LinkedMap();
    	lmap.put("1", "접수");
    	lmap.put("2", "진행");
    	lmap.put("3", "완료");
		model.addAttribute("cmbCompStat", lmap);

		// 콤보 테스트2
		Map lmap2 = new LinkedMap();
		lmap2.put("1", "선택1");
		lmap2.put("2", "선택2");
		lmap2.put("3", "선택3");
		model.addAttribute("chkInstType", lmap2);

		// 일반페이지 페이징 항목 설정
		model.addAttribute("model", 		sampleVO);
		model.addAttribute("map", 			paramMap);
		model.addAttribute("pageList", 		list);
		model.addAttribute("startNo",  		list.getStartNo());  	// 순서
		//model.addAttribute("startRevNo",	list.getStartRevNo());  // 역순

		return "usr/temp/listSample";
	}

	/**
	 * 샘플 엑셀 다운로드
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/usr/temp/downloadExcelSample.do")
	public String downloadExcelSample(HttpServletRequest request, ModelMap model
			, @ModelAttribute SampleVO sampleVO) throws Exception {

		Map paramMap = getParameterMap(request, true);
		setMappingValues(paramMap, getMethodName(new Throwable()));
		BeanUtils.copyProperties(sampleVO, paramMap);
		// -------------------- Default Setting End -----------------------//

		baseCurrPage = CommUtils.strToInt((String)paramMap.get("page"), 		baseCurrPage);
		basePageSize = CommUtils.strToInt((String)paramMap.get("pageSize"), 	basePageSize);

    	PaginatedArrayList list	= null;
    	list = sampleService.listSample(paramMap, baseCurrPage, basePageSize);

    	List           titleList = new ArrayList();
    	LinkedHashMap  titleMap  = new LinkedHashMap();

    	//타이틀을위한 LinkedHashMap 객체 구성(순서 유지)
    	titleMap.put("tempSeq",  "No");
    	titleMap.put("userId",   "사용자아이디");
    	titleMap.put("userNm",   "사용자명");
    	titleMap.put("title",    "제목");
    	titleMap.put("regiDttm", "등록일시");
    	//List 객체에 추가하여 List 객체를 담아야 함
    	titleList.add(titleMap);

    	model.addAttribute("titleList", titleList);        // 타이
    	model.addAttribute("excelList", list);
    	model.addAttribute("filename",  "sampleExcel");

		return "excelView";
	}

	/**
	 * 샘플 엑셀(Template) 다운로드
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/usr/temp/downExclTempSample.do")
	public String downExclTempSample(HttpServletRequest request, ModelMap model
			, @ModelAttribute SampleVO sampleVO) throws Exception {

		Map paramMap = getParameterMap(request, true);
		setMappingValues(paramMap, getMethodName(new Throwable()));
		BeanUtils.copyProperties(sampleVO, paramMap);
		// -------------------- Default Setting End -----------------------//

		baseCurrPage = CommUtils.strToInt((String)paramMap.get("page"), 		baseCurrPage);
		basePageSize = CommUtils.strToInt((String)paramMap.get("pageSize"), 	basePageSize);

		PaginatedArrayList list	= null;
		list = sampleService.listSample(paramMap, baseCurrPage, basePageSize);

		paramMap.put("fromDate", 	"2019-03-04");
		paramMap.put("toDate", 		"2019-06-30");
		paramMap.put("compNm", 		"테스트");
		paramMap.put("currPage", 	""+baseCurrPage);
		paramMap.put("totCnt", 		""+list.getTotalSize());

		model.addAttribute("filename", 		"tempSample");		// Template 엑셀파일명
		model.addAttribute("pcFilename", 	"샘플엑셀다운로드");		// 다운로드 파일명
		model.addAttribute("excelList", 	list);
		model.addAttribute("paramMap",  	paramMap);

		return "excelTempView";
	}

    /**
     * 샘플 조회
     */
	@RequestMapping("/usr/temp/viewSample.do")
	public String viewSample(HttpServletRequest request, ModelMap model
			, @ModelAttribute SampleVO sampleVO) throws Exception {

		Map paramMap = getParameterMap(request, true);
		setMappingValues(paramMap, getMethodName(new Throwable()));
		BeanUtils.copyProperties(sampleVO, paramMap);
		// -------------------- Default Setting End -----------------------//

    	// 상세조회
    	Map viewMap = sampleService.viewSample(paramMap);
    	if (viewMap != null) {
    		BeanUtils.copyProperties(sampleVO, viewMap);
    	}

    	paramMap.put("rootNo",	paramMap.get("tempSeq"));

    	// 첨부파일 조회
    	List listFile	= null;
    	listFile = fileService.listFile(paramMap);

    	// Return Values
		model.addAttribute("model", 	sampleVO);
		model.addAttribute("map", 		paramMap);
		model.addAttribute("listFile", 	listFile);

		return "usr/temp/viewSample";
	}


	/**
	 * 샘플 Ajax 리스트 테스트
	 */
	@RequestMapping("/usr/temp/listSampleJson.do")
	@ResponseBody
	public Map listSampleJson(HttpServletRequest request, ModelMap model
			, @ModelAttribute SampleVO sampleVO) throws Exception {

		Map paramMap = getParameterMap(request, true);
		// -------------------- Default Setting End -----------------------//

        String rtnCode = "1";
        String rtnMsg  = "";
        List   list    = null;

        try {
            // DB] 목록 조회
            list = sampleService.listSample(paramMap);
        } catch (EgovBizException ebe) {
            rtnMsg = ebe.getMessage();
            rtnCode= "0";
        } catch (Exception e) {
            rtnMsg = message.getMessage("error.comm.fail");
            rtnCode= "0";
        }

        Map returnMap = new HashMap();
        returnMap.put("rtnMsg",  rtnMsg);
        returnMap.put("rtnCode", rtnCode);
        returnMap.put("list",    list);

		return returnMap;
	}
	
	
	/**
	 * 샘플 Ajax 조회 테스트
	 */
	@RequestMapping("/usr/temp/viewSampleJson.do")
	@ResponseBody
	public Map viewSampleJson(HttpServletRequest request, ModelMap model
			, @ModelAttribute SampleVO sampleVO) throws Exception {

		Map paramMap = getParameterMap(request, true);
		// -------------------- Default Setting End -----------------------//

		// 상세조회
		Map viewMap = sampleService.viewSample(paramMap);

		return viewMap;
	}

	/**
	 * 샘플 등록 오픈
	 */
	@RequestMapping("/usr/temp/openRegiSample.do")
	public String openRegiSample(HttpServletRequest request, ModelMap model
			, @ModelAttribute SampleVO sampleVO) throws Exception {

		Map paramMap = getParameterMap(request, true);
		setMappingValues(paramMap, getMethodName(new Throwable()));
		BeanUtils.copyProperties(sampleVO, paramMap);
		// -------------------- Default Setting End -----------------------//

		String viewMode = "REGI";

		// 상세에서 '수정'을 위해 이동한 경우일 때
		String tempSeq = (String)paramMap.get("tempSeq");
		if(CommUtils.nvl(tempSeq).length() > 0) {
    		// 상세조회
            Map viewMap = sampleService.viewSample(paramMap);
            if (viewMap != null) {
                BeanUtils.copyProperties(sampleVO, viewMap);
            }

            paramMap.put("rootNo",  tempSeq);
            // 첨부파일 조회
            List listFile   = null;
            listFile        = fileService.listFile(paramMap);

            model.addAttribute("listFile",  listFile);

            viewMode = "UPDT";
		}

		// Return Values
		model.addAttribute("model", 	sampleVO);
		model.addAttribute("map", 		paramMap);
		model.addAttribute("viewMode", 	viewMode);

		return "usr/temp/regiSample";
	}

     /**
     * 샘플 등록 처리
     */
    @RequestMapping("/usr/temp/regiSample.do")
    public String regiSample(HttpServletRequest request, ModelMap model) throws Exception {

        // 등록 공통 내용 수행
        regiSampleComm(request, model);

    	return "redirect:/usr/temp/listSample.do";
    }

    /**
     * 등록 수정
     * ajax일 방법 샘플
     *
     * @param request
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/usr/temp/regiSampleJson.do")
    @ResponseBody
    public Map regiSampleJson(HttpServletRequest request, ModelMap model) throws Exception {

        String rtnCode = "1";   // 성공
        try {
            regiSampleComm(request, model);
        } catch (Exception e) {
            rtnCode = "0";      // 오류 발생
            logger.error("오류발생", e);
        }

        Map returnMap   = new HashMap();
        returnMap.put("rtnCode", rtnCode);

        return returnMap;
    }

    /**
     * 등록 공통 내용
     *
     * @param request
     * @param model
     * @throws Exception
     */
    private void regiSampleComm(HttpServletRequest request, ModelMap model)  throws Exception {

        SampleVO sampleVO   = new SampleVO();
        Map paramMap = getParameterMap(request, true);
        setMappingValues(paramMap, getMethodName(new Throwable()));
        BeanUtils.copyProperties(sampleVO, paramMap);
        // -------------------- Default Setting End -----------------------//

        paramMap.put("gsUserId",    CommUtils.nvlTrim((String)paramMap.get("gsUserId"),     "ntarget"));

        String regiMode = CommUtils.nvlTrim((String)paramMap.get("regiMode"), "0");

        // 파일 첨부
        List listNewFile = fileManager.multiFileUpload(request);

        String keyNo = "";
        // 단건처리
        if (regiMode.equals("0")) {
            keyNo = sampleService.regiSample(paramMap);
        }
        // 멀티처리
        else {
            keyNo = ""+sampleService.regiSampleMulti(paramMap);
        }

        // 첨부파일 DB등록
        if (!CommUtils.isEmpty(keyNo)) {
            paramMap.put("rootNo",  keyNo); // Key
            paramMap.put("subDir",  ApplicationProperty.get("upload.sub.tmp"));

            //fileService.fileManagement(paramMap, listNewFile);
        }
    }

    /**
     * 샘플 수정
     */
    @RequestMapping("/usr/temp/updtSample.do")
    public String updtSample(HttpServletRequest request, ModelMap model) throws Exception {

        // 수정 공통 내용 수행
        updtSampleComm(request, model);

   		return "redirect:/usr/temp/listSample.do";
    }

    /**
     * 샘플 수정
     * ajax일 방법 샘플
     *
     * @param request
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/usr/temp/updtSampleJson.do")
    @ResponseBody
    public Map updtSampleJson(HttpServletRequest request, ModelMap model) throws Exception {

        String rtnCode = "1";   // 성공
        try {
            updtSampleComm(request, model);
        } catch (Exception e) {
            rtnCode = "0";      // 오류 발생
            logger.error("오류발생", e);
        }

        Map returnMap   = new HashMap();
        returnMap.put("rtnCode", rtnCode);

        return returnMap;
    }

    /**
     * 수정 공통 내용
     * @param request
     * @param model
     * @throws Exception
     */
    private void updtSampleComm(HttpServletRequest request, ModelMap model)  throws Exception {
        SampleVO sampleVO   = new SampleVO();
        Map paramMap = getParameterMap(request, true);
        setMappingValues(paramMap, getMethodName(new Throwable()));
        BeanUtils.copyProperties(sampleVO, paramMap);
        // -------------------- Default Setting End -----------------------//

        // 파일 첨부
        List listNewFile    = fileManager.multiFileUpload(request);

        int  cnt = sampleService.updtSample(paramMap);

        // 첨부파일 DB 처리
        if (!CommUtils.nvlTrim((String)paramMap.get("tempSeq")).equals("") && cnt > 0) {
            paramMap.put("rootNo",  (String)paramMap.get("tempSeq"));   // Key
            paramMap.put("subDir",  ApplicationProperty.get("upload.sub.tmp"));

            //fileService.fileManagement(paramMap, listNewFile);
        } else {
            resultFlag("0");
        }

    }

    /**
     * 샘플 삭제
     */
    @RequestMapping("/usr/temp/deltSample.do")
    public String deltSample(HttpServletRequest request, ModelMap model) throws Exception {

        // 삭제 공통 내용 수행
        deltSampleComm(request, model);

    	return "redirect:/usr/temp/listSample.do";
    }

    /**
     * 샘플 삭제
     * ajax일 방법 샘플
     * @param request
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/usr/temp/deltSampleJson.do")
    @ResponseBody
    public Map deltSampleJson(HttpServletRequest request, ModelMap model) throws Exception {

        String rtnCode = "1";   // 성공
        try {
            // 삭제 공통 내용 수행
            deltSampleComm(request, model);
        } catch (Exception e) {
            rtnCode = "0";      // 오류 발생
            logger.error("오류발생", e);
        }

        Map returnMap   = new HashMap();
        returnMap.put("rtnCode", rtnCode);

        return returnMap;
    }

    /**
     * 삭제 공통내용
     * @param request
     * @param model
     * @return
     * @throws Exception
     */
    private void deltSampleComm(HttpServletRequest request, ModelMap model) throws Exception {
        SampleVO sampleVO   = new SampleVO();
        Map paramMap = getParameterMap(request, true);
        setMappingValues(paramMap, getMethodName(new Throwable()));
        BeanUtils.copyProperties(sampleVO, paramMap);
        // -------------------- Default Setting End -----------------------//

        int cnt = sampleService.deltSample(paramMap);

        // 첨부파일 DB 처리
        if (!CommUtils.nvlTrim((String)paramMap.get("tempSeq")).equals("") && cnt > 0) {

            paramMap.put("rootNo",  (String)paramMap.get("tempSeq"));   // Key
            paramMap.put("subDir",  ApplicationProperty.get("upload.sub.tmp"));

            // 삭제일 때는 아래와 같이 설정해주면 해당 글에 소개 있는 모든 파일에 관한 정보가 제거 된다.
            paramMap.put("arrFileNo", null);
            // paramMap.put("status"   , "D");       // 물리적인 파일을 삭제할 때
            paramMap.put("atthType" , "DELTALL");   // null이 아닌 아무값을 넣어도 상관없음.

            //fileService.fileManagement(paramMap, null);
        } else {
            resultFlag("0");
        }
    }


	/**
	 * 샘플 Ajax 리스트 테스트
	 */
	@RequestMapping("/usr/temp/getPassword.do")
	@ResponseBody
	public String getPassword(HttpServletRequest request, ModelMap model
			, @ModelAttribute SampleVO sampleVO) throws Exception {

		Map paramMap = getParameterMap(request, true);
		// -------------------- Default Setting End -----------------------//

		String salt 	= CommUtils.nvlTrim((String)paramMap.get("salt"));
		String passwd  	= CommUtils.nvlTrim((String)paramMap.get("passwd"));
		
		String strPwd = "";
		
		if (CommUtils.isNotEmpty(salt) && CommUtils.isNotEmpty(passwd)) {
			strPwd = EgovFileScrty.encryptPassword(passwd, salt);
		}
						
		return strPwd;
	}
	
    
    
    /**
     * 파라메터 디폴트값 셋팅 및 파라메터 데이터 핸들링.
     */
	private void setMappingValues(Map paramMap, String method) {
		paramMap.put("page", 		CommUtils.nvlTrim((String)paramMap.get("page"), 		"1"));

		if (method.equalsIgnoreCase("regiSample")) {
			paramMap.put("userId",		CommUtils.nvlTrim((String)paramMap.get("userId"), 		"ntarget"));
			paramMap.put("userNm",		CommUtils.nvlTrim((String)paramMap.get("userNm"), 		"엔타겟"));
			paramMap.put("title",		CommUtils.nvlTrim((String)paramMap.get("title"), 		"타이틀"));
			paramMap.put("content",		CommUtils.nvlTrim((String)paramMap.get("content"), 		"내용"));
			paramMap.put("userType",	CommUtils.nvlTrim((String)paramMap.get("userType"), 	"00"));
		}
		else if (method.equalsIgnoreCase("updtSample")) {
			paramMap.put("userId",		CommUtils.nvlTrim((String)paramMap.get("userId"), 		"ntarget"));
			paramMap.put("userNm",		CommUtils.nvlTrim((String)paramMap.get("userNm"), 		"엔타겟"));
			paramMap.put("title",		CommUtils.nvlTrim((String)paramMap.get("title"), 		"타이틀"));
			paramMap.put("content",		CommUtils.nvlTrim((String)paramMap.get("content"), 		"내용"));
			paramMap.put("userType",	CommUtils.nvlTrim((String)paramMap.get("userType"), 	"00"));
		}
		else if (method.equalsIgnoreCase("listSample")) {
			String[] arrChkAddr = {"1", "2"};

			paramMap.put("arrChkAddr",		arrChkAddr);
		}
	}
	
}