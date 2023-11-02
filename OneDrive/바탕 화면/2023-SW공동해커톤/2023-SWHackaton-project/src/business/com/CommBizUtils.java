package business.com;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import common.util.CommUtils;

/**
 * [유틸클래스] - 공통 업무용 유틸리티
 *
 * @class   : CommBizUtils
 * @author  : LSH
 * @since   : 2021.09.09
 * @version : 1.0
 *
 *   수정일     수정자             수정내용
 *  --------   --------    ---------------------------
 *
 */
public class CommBizUtils {
	
    private static final Logger logger = LoggerFactory.getLogger(CommBizUtils.class);

	 /**
	  * 목록을 구조화된 TREE 형태로 반환한다.
	  * 
	  * @param  list CONNECT BY로 검색된 목록
	  * @param  keys 필요한 키항목 맵
	  *         - level : 항목의 레벨에 해당되는 키값 예) menuLvl
	  *         - item  : 항목의 고유키에 해당되는 키값 예) menuId
	  *         - parent: 항목의 상위항목 고유키에 해당되는 키값 예) upMenuId
	  * @return TREE형태의 계층구조화된 목록
	  */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<Map> buildTree(List<Map> list, Map keys) {
		 
        String levelId  = (String)keys.get("level"); // "menuLvl"
        String itemId   = (String)keys.get("itemId");// "menuId"
        String itemNm   = (String)keys.get("itemNm");// "menuNm"
        String parentId = (String)keys.get("parentId");// "upMenuId"
        
		List<Map> rootLst = new ArrayList<Map>();
        Map rootMap = new HashMap();
        
        int level = -1;
        
		for (Map item : list) {

			int itemLvl = CommUtils.getInt(item, levelId);
			String itemKey = (String)item.get(itemId);
			String parentKey = (String)item.get(parentId);
			
			// TREE 구조에 필요한 키값 생성
			item.put("id", item.get(itemId));
			item.put("text", item.get(itemNm));
			
			rootMap.put(itemKey, item);
			
			if (level < 0)
				level = itemLvl;

			// ROOT 레벨인 경우
			if (itemLvl == level) {
				rootLst.add(item);
				continue;
			}
			// 상위 항목을 찾아 현재 항목을 하위 항목으로 담는다.
			Map parent = (Map)rootMap.get(parentKey);
			if (parent != null) {
				if(parent.get("childMap") == null) {
					parent.put("childMap", new HashMap());
					parent.put("children", new ArrayList<Map>());
				}
				((Map)parent.get("childMap")).put(itemKey, item);
				((List)parent.get("children")).add(item);
			} else {
				rootLst.add(item);
			}
		}
		return rootLst;
	}
	
    /**
     * 2021.11.04 LSH 현재 URL 정보 가져오기
     * SecurityInterceptor 참조
     */
    public static String getRequestURL(HttpServletRequest request) {
        String url  = request.getRequestURI().substring(request.getContextPath().length());
        if ("/".equals(url) && 
        	request.getPathInfo() != null) {
        	return request.getPathInfo();
        }
        return url;
    }
	
    /**
     * 2021.11.04 LSH 현재 서버명칭 가져오기
     * SecurityInterceptor 참조
     */
    public static String getServerName(HttpServletRequest request) {
    	return CommConst.SERVER_NAME 
    			+ ":"  + request.getServerPort()
    			+ "__" + request.getLocalName()
    			+ "__"+ request.getLocale().toString();
    }
	
    /**
     * 2021.11.16 LSH 현재 도메인 URL 가져오기 (http://www.test.com)
     */
    public static String getDomain(HttpServletRequest request) {
    	//return request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
    	return request.getRequestURL().toString().replace(request.getRequestURI(),"");
    }
    
    /**
     * OPEN API 연결하여 JSON String 으로 반환하는 함수
     * @param apiUrl  API URL  (ex. https://www.openapi.com )
     * @param charset 결과 캐릭터셋 (ex. UTF-8 )
     * @return JSON String
     */
    public static String getJsonOpenApi(String apiUrl, String charset) {
    	BufferedReader br = null;
    	StringBuffer sb = null;
    	try {
    		logger.debug("OPEN API URL : " + apiUrl);
    		logger.debug("OPEN API CHARSET : " + charset);
    		
    		URL url = new URL(apiUrl);
        	br = new BufferedReader(new InputStreamReader(url.openStream(),charset));
        	sb = new StringBuffer();
        	String tempStr = null;
        	while(true){
        		tempStr = br.readLine();
        		if(tempStr == null) break;
        		sb.append(tempStr); // 응답결과 JSON 저장
        	}
        	return sb.toString();
        	
        	//response.setCharacterEncoding(charset);
    		//response.setContentType("text/xml");
        	//response.getWriter().write(sb.toString());			// 응답결과 반환
        	
    	}
    	catch(Exception e) {
    		logger.error("OPEN API ERROR : ", e);
    	}
    	finally {
    		try {
        		if (br != null)
        			br.close();
    		} catch(Exception ex) {}
    	}
    	return null;
    }

}
