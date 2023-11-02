package common.base;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import common.user.UserInfo;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * Program Name 	: BaseService
 * Description 		:
 * Programmer Name 	: ntarget
 * Creation Date 	: 2021-02-08
 * Used Table 		:
 *
 *   수정일      수정자                     수정내용
 *  -------    --------    ---------------------------
 *
 */
public class BaseService extends EgovAbstractServiceImpl  {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	public BaseService() {
	}

	@Autowired
	protected UserInfo userInfo;

    @Resource(name="message")
    protected EgovMessageSource message;

}
