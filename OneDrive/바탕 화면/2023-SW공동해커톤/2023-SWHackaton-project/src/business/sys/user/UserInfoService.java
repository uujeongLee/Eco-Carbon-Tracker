package business.sys.user;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import commf.dao.CommonMapperImpl;
import common.base.BaseService;
import common.user.UserInfo;


@Service
@SuppressWarnings({"all" })
public class UserInfoService extends BaseService {

	@Autowired
	private CommonMapperImpl dao;

	/* USERINFO GET */
	public UserInfo getUserInfo(Map user) {
		UserInfo userInfo = (UserInfo)dao.view("UserInfo.getUserInfo", user);
		return userInfo;
	}

	/* 패스워드 실패시 카운터 업데이트 및 잠금시간 등록 */
	public int updtPasswordFail(Map userMap) throws Exception {
		return (Integer)dao.update("UserInfo.updtPasswordFail", userMap);
	}

    /* 패스워드 실패시 카운터 업데이트 및 잠금시간 등록 */
    public int updtPwdErrCnt(Map userMap) throws Exception {
        return (Integer)dao.update("UserInfo.updtPwdErrCnt", userMap);
    }
    
	/* 로그인시간 등록 */
	public int updtLoginTime(Map userMap) throws Exception {
		return (Integer)dao.update("UserInfo.updtLoginTime", userMap);
	}
	
}