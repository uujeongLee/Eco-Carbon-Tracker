package business.main;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import business.board.BoardService;
import business.com.cmm.PagingVO;
import common.base.BaseController;

@Controller
public class MainController extends BaseController  {
	
	@Autowired
	private MainService mainService;
	
	@RequestMapping("/main.do")
	public String listBoard(HttpServletRequest request, ModelMap model) throws Exception {
		mainService.selectRegiInfo();

		
		
		return "main/main";
	}	
	
	@RequestMapping("/ajax/searchBuildingLoc.do")
	@ResponseBody
	public Map searchBuildingLoc(HttpServletRequest request, ModelMap model) throws Exception {
		request.setCharacterEncoding("UTF-8");
		Map paramMap = getParameterMap(request, true);
		// -------------------- Default Setting End -----------------------//

		
		List<Map> result = mainService.selectBuidingLoc(paramMap);
		
        Map returnMap = new HashMap();
        
        returnMap.put("boardList",  result);
        
        System.out.println(paramMap.get("sgg"));
        
		return returnMap;
	}
	
	
	@RequestMapping("/ajax/searchBuildingInfo.do")
	@ResponseBody
	public Map searchBuildingInfo(HttpServletRequest request, ModelMap model) throws Exception {
		request.setCharacterEncoding("UTF-8");
		Map paramMap = getParameterMap(request, true);
		// -------------------- Default Setting End -----------------------//

	
		Map result = mainService.searchBuildingInfo(paramMap);
		
        Map returnMap = new HashMap();
        
        returnMap.put("boardList",  result);
   
        
		return returnMap;
	}
	
	@RequestMapping("/ajax/getSggListBySd.do")
	@ResponseBody
	public Map getSggListBySd(HttpServletRequest request, ModelMap model) throws Exception {
		request.setCharacterEncoding("UTF-8");
		Map paramMap = getParameterMap(request, true);
		// -------------------- Default Setting End -----------------------//

	
		List<Map> result = mainService.getSggListBySd(paramMap);
		
        Map returnMap = new HashMap();
        
        returnMap.put("boardList",  result);
   
        
		return returnMap;
	}
	
	
}
