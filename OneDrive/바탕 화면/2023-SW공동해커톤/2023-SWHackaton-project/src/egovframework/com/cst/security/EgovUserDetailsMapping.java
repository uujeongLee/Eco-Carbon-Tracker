package egovframework.com.cst.security;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import egovframework.rte.fdl.security.userdetails.EgovUserDetails;
import egovframework.rte.fdl.security.userdetails.jdbc.EgovUsersByUsernameMapping;


public class EgovUserDetailsMapping extends EgovUsersByUsernameMapping {

    /**
     * EgovUserDetailsMapping 생성자
     * @param ds
     * @param usersByUsernameQuery
     */
    public EgovUserDetailsMapping(DataSource ds, String usersByUsernameQuery) {
        super(ds, usersByUsernameQuery);
    }

    /*
     * (non-Javadoc)
     * @see
     * egovframework.rte.fdl.security.userdetails.jdbc
     * .EgovUsersByUsernameMapping
     * #mapRow(java.sql.ResultSet, int)
     */
    /**
     * EgovUsersByUsernameMapping 클래스를 상속받아
     * jdbc-user-service 에서 지정된 users-by-username-query
     * 의 쿼리문을 조회하여 ResultSet에 매핑된다.
     */
    @Override
    protected EgovUserDetails mapRow(ResultSet rs, int rownum) throws SQLException {
        logger.debug("## EgovUsersByUsernameMapping mapRow ##");

        String userid 	= rs.getString(1);
        String password = rs.getString(2);
        boolean enabled = rs.getBoolean(3);
        String username = rs.getString(4);

        // TODO USERS 테이블 컬럼 변경
        // 세션에서 관리되는 항목 추가 - ntarget : not used. LoginController에서 User정보 다시 조회함.
        EgovUserDetailsVO egovUserDetailsVO = new EgovUserDetailsVO();

  		egovUserDetailsVO.setUserId(userid);
        egovUserDetailsVO.setPassword(password);
        egovUserDetailsVO.setUserName(username);

        return new EgovUserDetails(userid, password, enabled, egovUserDetailsVO);
    }

}

