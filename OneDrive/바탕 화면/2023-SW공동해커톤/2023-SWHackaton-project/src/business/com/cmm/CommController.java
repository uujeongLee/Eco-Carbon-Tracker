package business.com.cmm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import business.com.cmm.domain.CommVO;
import common.base.BaseController;


@Controller
@SuppressWarnings( {"all"})
public class CommController extends BaseController {

	@Autowired
	private CommService commService;

	private void setMappingValues(Map paramMap, String method) {

	}
}
