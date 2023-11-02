package common.advice;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import egovframework.com.cmm.EgovMessageSource;
import egovframework.rte.fdl.cmmn.exception.EgovBizException;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class ExceptionController {

    @Resource(name="message")
    protected EgovMessageSource message;

    @ExceptionHandler({EgovBizException.class})
    public Map<String, Object> handleEgovBizException(final EgovBizException ex) {
        log.warn("error", ex);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("Code",    -1);
        result.put("Message", ex.getMessage());
        return result;
    }

    @ExceptionHandler({Exception.class})
    public Map<String, Object> handleException(final Exception ex) {
        log.warn("error", ex);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("Code",    -2);
        result.put("Message", message.getMessage("error.comm.fail"));
        return result;
    }
}
