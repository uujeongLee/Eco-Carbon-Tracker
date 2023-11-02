package business.usr.temp.domain;

import common.base.BaseModel;

public class SampleVO extends BaseModel {
    private String tempSeq             = null;
    private String userId              = null;
    private String userNm              = null;
    private String title               = null;
    private String content             = null;
    private String exclNm              = null;
    private String regiDt              = null;
    private String regiDttm            = null;
    private String regiDate            = null;

    private String fromDate            = null;
    private String toDate              = null;

    private String arrChkAddr[]        = null;

    private String statusCd            = null;
    private String statusRadioCd       = null;
    private String statusCheckCd       = null;
    private String arrCheckCd[]        = null;
    private String srchUserId          = null;
    private String srchUserNm          = null;
    private String srchType            = null;
    private String srchKeyword         = null;

    private String exVal               = null;
    private String keyNo               ;

    private String userNo              = null;
    private String passwd              = null;
    private String salt                = null;

    private String exclJobId;
    private String procType;

    private String[] arrUserId;
    private String[] arrUserNm;
    private String[] arrTitle;
    private String[] arrContent;

    

    public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public String getUserNo() {
		return userNo;
	}
	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	
	/* 시큐어코딩 적용 (배열 getter 사용방법) */
    public String[] getArrChkAddr() {
//        String[] ret = null;
//        if(this.arrChkAddr != null){
//            ret = new String[arrChkAddr.length];
//            for(int i = 0; i < arrChkAddr.length; i++){
//                ret[i] = this.arrChkAddr[i];
//            }
//        }
//        return ret;
        return getArrayForSecure(this.arrChkAddr);
    }
    /* 시큐어코딩 적용 (배열 setter 사용방법) */
    public void setArrChkAddr(String[] arrChkAddr) {
//        this.arrChkAddr = new String[arrChkAddr.length];
//        for (int i = 0; i < arrChkAddr.length; i++)
//            this.arrChkAddr[i] = arrChkAddr[i];
        this.arrChkAddr = getArraySetForSecure(arrChkAddr);
    }
    public String[] getArrUserId() {
        return getArrayForSecure(this.arrUserId);
    }
    public void setArrUserId(String[] arrUserId) {
        this.arrUserId = getArraySetForSecure(arrUserId);
    }
    public String[] getArrUserNm() {
        return getArrayForSecure(this.arrUserNm);
    }
    public void setArrUserNm(String[] arrUserNm) {
        this.arrUserNm = getArraySetForSecure(arrUserNm);
    }
    public String[] getArrTitle() {
        return getArrayForSecure(this.arrTitle);
    }
    public void setArrTitle(String[] arrTitle) {
        this.arrTitle = getArraySetForSecure(arrTitle);
    }
    public String[] getArrContent() {
        return getArrayForSecure(this.arrContent);
    }
    public void setArrContent(String[] arrContent) {
        this.arrContent = getArraySetForSecure(arrContent);
    }

    public String getTempSeq() {
        return tempSeq;
    }
    public void setTempSeq(String tempSeq) {
        this.tempSeq = tempSeq;
    }
    public String getFromDate() {
        return fromDate;
    }
    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }
    public String getToDate() {
        return toDate;
    }
    public void setToDate(String toDate) {
        this.toDate = toDate;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
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
    public String getStatusCd() {
        return statusCd;
    }
    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
    public String getStatusRadioCd() {
        return statusRadioCd;
    }
    public void setStatusRadioCd(String statusRadioCd) {
        this.statusRadioCd = statusRadioCd;
    }
    public String getStatusCheckCd() {
        return statusCheckCd;
    }
    public void setStatusCheckCd(String statusCheckCd) {
        this.statusCheckCd = statusCheckCd;
    }
    public String getSrchType() {
        return srchType;
    }
    public void setSrchType(String srchType) {
        this.srchType = srchType;
    }
    public String getSrchKeyword() {
        return srchKeyword;
    }
    public void setSrchKeyword(String srchKeyword) {
        this.srchKeyword = srchKeyword;
    }
    public String getExVal() {
        return exVal;
    }
    public void setExVal(String exVal) {
        this.exVal = exVal;
    }
    public String getSrchUserId() {
        return srchUserId;
    }
    public void setSrchUserId(String srchUserId) {
        this.srchUserId = srchUserId;
    }
    public String getSrchUserNm() {
        return srchUserNm;
    }
    public void setSrchUserNm(String srchUserNm) {
        this.srchUserNm = srchUserNm;
    }
    public String getExclNm() {
        return exclNm;
    }
    public void setExclNm(String exclNm) {
        this.exclNm = exclNm;
    }
    public String getRegiDt() {
        return regiDt;
    }
    public void setRegiDt(String regiDt) {
        this.regiDt = regiDt;
    }
    public String getRegiDttm() {
        return regiDttm;
    }
    public void setRegiDttm(String regiDttm) {
        this.regiDttm = regiDttm;
    }
    public String getRegiDate() {
        return regiDate;
    }
    public void setRegiDate(String regiDate) {
        this.regiDate = regiDate;
    }
    public String getKeyNo() {
        return keyNo;
    }
    public void setKeyNo(String keyNo) {
        this.keyNo = keyNo;
    }
    public String getExclJobId() {
        return exclJobId;
    }
    public void setExclJobId(String exclJobId) {
        this.exclJobId = exclJobId;
    }
    public String getProcType() {
        return procType;
    }
    public void setProcType(String procType) {
        this.procType = procType;
    }
    public String[] getArrCheckCd() {
        return getArrayForSecure(this.arrCheckCd);
    }
    public void setArrCheckCd(String[] arrCheckCd) {
        this.arrCheckCd = getArraySetForSecure(arrCheckCd);
    }
    
    


}