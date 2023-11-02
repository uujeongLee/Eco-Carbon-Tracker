package business.sys.log;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import business.sys.log.domain.LogVO;
import commf.paging.PaginatedArrayList;
import common.base.BaseController;
import common.util.CommUtils;


@Controller
@SuppressWarnings({ "all"})
public class LogController extends BaseController {

	@Autowired
    private LogService  logService;

    /**
     * 배치로그조회 리스트 페이지
     */
    @RequestMapping("/sys/log/listBatchLog.do")
    public String listBatchLog(HttpServletRequest request, ModelMap model) throws Exception {
        LogVO logVO   = new LogVO();

        Map paramMap = getParameterMap(request, true);

        setMappingValues(paramMap, getMethodName(new Throwable()));

        BeanUtils.copyProperties(logVO, paramMap);
        // -------------------- Default Setting End -----------------------//

        model.addAttribute("model",     logVO);

        return "sys/log/listBatchLog";
    }

    /**
     *  배치로그조회 리스트 조회
     */
    @RequestMapping("/sys/log/getListBatchLog.do")
    @ResponseBody
    public Map getListBatchLog(HttpServletRequest request, ModelMap model) throws Exception {
        LogVO logVO   = new LogVO();

        Map paramMap = getParameterMap(request, true);

        setMappingValues(paramMap, getMethodName(new Throwable()));

        BeanUtils.copyProperties(logVO, paramMap);
        // -------------------- Default Setting End -----------------------//

        baseCurrPage = CommUtils.strToInt((String)paramMap.get("start"),       0);
        basePageSize = CommUtils.strToInt((String)paramMap.get("length"),      10);
        baseCurrPage = baseCurrPage/basePageSize + 1;

        paramMap.put("pageIndex", baseCurrPage);
        paramMap.put("pageSize",  basePageSize);

        PaginatedArrayList list = null;
        list = logService.getListBatchLog(paramMap, baseCurrPage, basePageSize);

        Map returnMap = getPagingResult(paramMap, list);

        return returnMap;
    }


    /**
     * 파라메터 디폴트값 셋팅 및 파라메터 데이터 핸들링.
     */
    private void setMappingValues(Map paramMap, String method) {

        if (method.equalsIgnoreCase("listBatchLog") ) {

        }

    }
}

