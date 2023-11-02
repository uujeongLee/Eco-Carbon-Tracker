package common.resolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

public class BaseExceptionResolver extends SimpleMappingExceptionResolver {
	
    private static Logger logger = LoggerFactory.getLogger(BaseExceptionResolver.class);

	@Override
	public ModelAndView resolveException(HttpServletRequest req, HttpServletResponse res, Object handler,
			Exception ex) {
		
		logger.error("BASE ERROR :", ex);
		return super.resolveException(req, res, handler, ex);
	}


}
