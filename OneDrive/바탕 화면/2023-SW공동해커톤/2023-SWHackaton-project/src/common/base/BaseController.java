package common.base;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import commf.paging.PaginatedArrayList;
import common.user.UserInfo;
import common.util.CommUtils;
import egovframework.com.cmm.EgovMessageSource;

/**
 * Program Name 	: BaseController
 * Description 		:
 * Programmer Name 	: ntarget
 * Creation Date 	: 2021-02-08
 * Used Table 		:
 *
 *   수정일     수정자              수정내용
 *  -------    --------    ---------------------------
 *
 */

@Aspect
@SuppressWarnings({"rawtypes", "unchecked"})
public class BaseController {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected UserInfo userInfo;

    @Resource(name="message")
    //protected Message message;
    protected EgovMessageSource message;

    public HttpServletRequest request = null;

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    protected int baseCurrPage = 1;
    protected int basePageSize = 10;

    protected HashMap getParameterMap(HttpServletRequest req, boolean sessionFlag) {
        setRequest(req);

        HashMap map = new HashMap();

        map = getParameterMap(req);

        if (sessionFlag)
            setGlobalSession(map);

        return map;
    }

    protected HashMap getParameterMap(HttpServletRequest req) {
        setRequest(req);

        HashMap map = new HashMap();

        Enumeration enm = req.getParameterNames();

        String name = null;
        String value = null;
        String[] arr = null;

        while (enm.hasMoreElements()) {
            name = (String) enm.nextElement();
            arr = req.getParameterValues(name);

            // 배열로 받기 -> arr로 시작하는 변수명 ex) arrSysCode 는 배열로 받음.
            if (name.startsWith("arr")) {
                map.put(name, arr);
            } else {
                if (arr != null && arr.length > 0) {
                    value = arr[0];
                } else {
                    value = req.getParameter(name);
                }
                map.put(name, value);
            }
        }

        return map;
    }

    // Get Method Name
    protected String getMethodName(Throwable trb) {
        StackTraceElement[] stacks = trb.getStackTrace();
        StackTraceElement currentStack = stacks[0];
        return currentStack.getMethodName();
    }

    /**
     * 글로벌세션으로 만들기(UserInfo -> MAP)
     * ex) userId   -> gsUserId
     */
    protected void setGlobalSession(Map map) {
        setGlobalSession(map, null);
    }

    /**
     * 글로벌세션으로 만들기(UserInfo를 BaseModel에 추가)
     */
    protected void setGlobalSession(BaseModel baseModel) {
        setGlobalSession(null, baseModel);
    }

    /**
     * 글로벌세션으로 만들기
     *  - UserInfo -> MAP
     *  - UserInfo를 BaseModel에 추가
     *
     * @param map
     * @param baseModel
     */
    protected void setGlobalSession(Map map, BaseModel baseModel) {
        try {
            Map userMap = new HashMap();
            userMap = PropertyUtils.describe(userInfo);
            // Serializable Object excluded.
            userMap.remove("advisors");
            userMap.remove("class");
            userMap.remove("callbacks");
            userMap.remove("exposeProxy");
            userMap.remove("frozen");
            userMap.remove("preFiltered");
            userMap.remove("proxiedInterfaces");
            userMap.remove("proxyTargetClass");
            userMap.remove("targetClass");
            userMap.remove("targetObject");
            userMap.remove("targetSource");

            if( map != null ) {
                Iterator k = userMap.keySet().iterator();
                String key = "";
                while (k.hasNext()) {
                    key = (String) k.next();
                    map.put("gs"+CommUtils.toUpper(CommUtils.substring(key, 0, 1))+CommUtils.substring(key, 1)
                            , userMap.get(key));
                }
            }

            // userInfo 추가
            if(baseModel != null) {
                baseModel.setUserInfo( (userInfo==null)? new UserInfo() : userInfo);
            }
        } catch (IllegalAccessException iae) {
            //iae.printStackTrace();
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public void resultFlag(String msg) {
        request.getSession().setAttribute("PROCFLAG", msg);
    }

	/**
     * 페이지 처리한 내용을 DataTables에 맞는 결과 객체로 리턴
     * @param paramMap
     * @param list
     * @return
     * @throws Exception
     */
    protected Map getPagingResult(Map paramMap, PaginatedArrayList list) throws Exception {
        Map returnMap = new HashMap();

        // DataTables 객체에 필요한 항목 설정
        returnMap.put("data",             list);
        returnMap.put("recordsTotal",     list.getTotalSize());
        returnMap.put("recordsFiltered",  list.getTotalSize());
        returnMap.put("startNo",          list.getStartNo());
        returnMap.put("draw",             paramMap.get("draw"));

        return returnMap;
    }
    
}
