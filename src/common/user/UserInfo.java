package common.user;

import java.io.Serializable;

/**
 * Program Name 	: UserInfo
 * Description 		:
 * Programmer Name 	: ntarget
 * Creation Date 	: 2022-09-26
 * Used Table 		:
 */

@SuppressWarnings({"serial"})
public class UserInfo implements Serializable {
    // 시스템 공통정보
    private String userId      = null; // 사용자아이디
    private String userNm      = null; // 사용자명
    private String password    = null; // 비밀번호
    private String roleId      = null; // 역할ID
    private String roleNm      = null; // 역할명
    private String ipAddr      = null; // IP주소
    private String useYn       = null; // 사용여부
    private String enabled     = null;

    // 차주 
    private String carNum	   = null; // 차량번호
    private String carPhoneNum = null; // 차주휴대전화
    private String[] prgsStts  = null; // 차주 진행상황
    private	String userPrgsStts	=null;
    
    
    private int    diffDays    = 0;    // 비밀번호변경일수
    private int    diffNextDays= 0;    // 비밀번호 다음에변경 일수

    /* 사용자정보 */
    private String userNo      = null; //사용자번호
    private String useIp       = null; //사용IP

    private String eml         = null; // 이메일
    private String mblTelno    = null; // 휴대전화번호
    private String brdt        = null; // 생년웡일
    private String sxdst       = null; // 성별
    private String orgId       = null; // 조직ID
    private String orgNm       = null; // 조직명
    private String topDeptCd   = null; // 최상위부서코드
    private String topDeptNm   = null; // 최상위부서명
    private String deptCd      = null; // 부서코드
    private String deptNm      = null; // 부서명
    private String fullDeptNm  = null; // 전체부서명
    private String offmZipcd   = null; // 사무실우편번호
    private String offmAddr    = null; // 사무실주소
    private String offmTelno   = null; // 사무실전화번호
    private String offmFax     = null; // 사무실팩스번호
    private String joinYmd     = null; // 가입일자
    private String pswdLockYmd = null; // 비밀번호잠금일자
    private String pswdErrCnt  = null; // 비밀번호오류횟수
    private String pswdChgYmd  = null; // 비밀번호변경일자
    private String pswdNextYmd = null; // 비밀번호다음일자
    private String lstLgnDt    = null; // 마지막로그인일시
    private String useStusCd   = null; // 사용상태
    private String testUseYn   = null; // 테스트사용여부
    private String regstrNo    = null; // 등록자번호
    private String regYmd      = null; // 등록일자
    private String upusrNo     = null; // 수정자번호
    private String mdfcnYmd    = null; // 수정일자

    private String zip    		= null; // 우편번호
    private String addr    		= null; // 주소
    private String daddr    	= null; // 상세주소
    
    
    
	public String getUserPrgsStts() {
		return userPrgsStts;
	}
	public void setUserPrgsStts(String userPrgsStts) {
		this.userPrgsStts = userPrgsStts;
	}
	public String[] getPrgsStts() {
		return prgsStts;
	}
	public void setPrgsStts(String[] prgsStts) {
		this.prgsStts = prgsStts;
	}
	public String getCarNum() {
		return carNum;
	}
	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}
	public String getCarPhoneNum() {
		return carPhoneNum;
	}
	public void setCarPhoneNum(String carPhoneNum) {
		this.carPhoneNum = carPhoneNum;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserNm() {
		return userNm;
	}
	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getRoleNm() {
		return roleNm;
	}
	public void setRoleNm(String roleNm) {
		this.roleNm = roleNm;
	}
	public String getIpAddr() {
		return ipAddr;
	}
	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
	public String getEnabled() {
		return enabled;
	}
	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}
	public int getDiffDays() {
		return diffDays;
	}
	public void setDiffDays(int diffDays) {
		this.diffDays = diffDays;
	}
	public int getDiffNextDays() {
		return diffNextDays;
	}
	public void setDiffNextDays(int diffNextDays) {
		this.diffNextDays = diffNextDays;
	}
	public String getUserNo() {
		return userNo;
	}
	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}
	public String getUseIp() {
		return useIp;
	}
	public void setUseIp(String useIp) {
		this.useIp = useIp;
	}
	public String getEml() {
		return eml;
	}
	public void setEml(String eml) {
		this.eml = eml;
	}
	public String getMblTelno() {
		return mblTelno;
	}
	public void setMblTelno(String mblTelno) {
		this.mblTelno = mblTelno;
	}
	public String getBrdt() {
		return brdt;
	}
	public void setBrdt(String brdt) {
		this.brdt = brdt;
	}
	public String getSxdst() {
		return sxdst;
	}
	public void setSxdst(String sxdst) {
		this.sxdst = sxdst;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getOrgNm() {
		return orgNm;
	}
	public void setOrgNm(String orgNm) {
		this.orgNm = orgNm;
	}
	public String getTopDeptCd() {
		return topDeptCd;
	}
	public void setTopDeptCd(String topDeptCd) {
		this.topDeptCd = topDeptCd;
	}
	public String getTopDeptNm() {
		return topDeptNm;
	}
	public void setTopDeptNm(String topDeptNm) {
		this.topDeptNm = topDeptNm;
	}
	public String getDeptCd() {
		return deptCd;
	}
	public void setDeptCd(String deptCd) {
		this.deptCd = deptCd;
	}
	public String getDeptNm() {
		return deptNm;
	}
	public void setDeptNm(String deptNm) {
		this.deptNm = deptNm;
	}
	public String getFullDeptNm() {
		return fullDeptNm;
	}
	public void setFullDeptNm(String fullDeptNm) {
		this.fullDeptNm = fullDeptNm;
	}
	public String getOffmZipcd() {
		return offmZipcd;
	}
	public void setOffmZipcd(String offmZipcd) {
		this.offmZipcd = offmZipcd;
	}
	public String getOffmAddr() {
		return offmAddr;
	}
	public void setOffmAddr(String offmAddr) {
		this.offmAddr = offmAddr;
	}
	public String getOffmTelno() {
		return offmTelno;
	}
	public void setOffmTelno(String offmTelno) {
		this.offmTelno = offmTelno;
	}
	public String getOffmFax() {
		return offmFax;
	}
	public void setOffmFax(String offmFax) {
		this.offmFax = offmFax;
	}
	public String getJoinYmd() {
		return joinYmd;
	}
	public void setJoinYmd(String joinYmd) {
		this.joinYmd = joinYmd;
	}
	public String getPswdLockYmd() {
		return pswdLockYmd;
	}
	public void setPswdLockYmd(String pswdLockYmd) {
		this.pswdLockYmd = pswdLockYmd;
	}
	public String getPswdErrCnt() {
		return pswdErrCnt;
	}
	public void setPswdErrCnt(String pswdErrCnt) {
		this.pswdErrCnt = pswdErrCnt;
	}
	public String getPswdChgYmd() {
		return pswdChgYmd;
	}
	public void setPswdChgYmd(String pswdChgYmd) {
		this.pswdChgYmd = pswdChgYmd;
	}
	public String getPswdNextYmd() {
		return pswdNextYmd;
	}
	public void setPswdNextYmd(String pswdNextYmd) {
		this.pswdNextYmd = pswdNextYmd;
	}
	public String getLstLgnDt() {
		return lstLgnDt;
	}
	public void setLstLgnDt(String lstLgnDt) {
		this.lstLgnDt = lstLgnDt;
	}
	public String getUseStusCd() {
		return useStusCd;
	}
	public void setUseStusCd(String useStusCd) {
		this.useStusCd = useStusCd;
	}
	public String getTestUseYn() {
		return testUseYn;
	}
	public void setTestUseYn(String testUseYn) {
		this.testUseYn = testUseYn;
	}
	public String getRegstrNo() {
		return regstrNo;
	}
	public void setRegstrNo(String regstrNo) {
		this.regstrNo = regstrNo;
	}
	public String getRegYmd() {
		return regYmd;
	}
	public void setRegYmd(String regYmd) {
		this.regYmd = regYmd;
	}
	public String getUpusrNo() {
		return upusrNo;
	}
	public void setUpusrNo(String upusrNo) {
		this.upusrNo = upusrNo;
	}
	public String getMdfcnYmd() {
		return mdfcnYmd;
	}
	public void setMdfcnYmd(String mdfcnYmd) {
		this.mdfcnYmd = mdfcnYmd;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getDaddr() {
		return daddr;
	}
	public void setDaddr(String daddr) {
		this.daddr = daddr;
	}

    

}

