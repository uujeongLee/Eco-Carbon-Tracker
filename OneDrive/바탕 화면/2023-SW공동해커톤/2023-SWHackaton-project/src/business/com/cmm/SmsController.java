package business.com.cmm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

import business.com.cmm.domain.CommVO;
import common.base.BaseController;



@Controller
@SuppressWarnings( {"all"})
public class SmsController extends BaseController {

	@Autowired
	private SmsService smsService;

	
	 
    /**
     * sms 전송 
     */
    @RequestMapping("/sms/messageCert.do")
    @ResponseBody
    public int messagemeAPI(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
    	Map paramMap = getParameterMap(request, false);
    	/*setMappingValues(paramMap, getMethodName(new Throwable()));*/
    	
    	int result = smsService.insertSmsCert(paramMap);
    	
		
		
		return result;
		
        
        
        
    }
	
	

  
}
