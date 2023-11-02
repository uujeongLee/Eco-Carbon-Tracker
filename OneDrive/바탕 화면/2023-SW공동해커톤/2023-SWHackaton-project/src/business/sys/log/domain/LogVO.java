package business.sys.log.domain;

import common.base.BaseModel;

public class LogVO extends BaseModel {

    private String batchSeq;
    private String batchCode;
    private String batchNm;
    private String batchDe;
    private String batchProc;
    private String batchSttus;
    private String batchSttusNm;
    private String batchBeginTime;
    private String batchEndTime;
    private String errorCode;
    private String errorCn;

	private String srchFromDate;
	private String srchToDate;


	public String getBatchSeq() {
        return batchSeq;
    }
    public void setBatchSeq(String batchSeq) {
        this.batchSeq = batchSeq;
    }
    public String getBatchCode() {
        return batchCode;
    }
    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }
    public String getBatchNm() {
        return batchNm;
    }
    public void setBatchNm(String batchNm) {
        this.batchNm = batchNm;
    }
    public String getBatchDe() {
        return batchDe;
    }
    public void setBatchDe(String batchDe) {
        this.batchDe = batchDe;
    }
    public String getBatchProc() {
        return batchProc;
    }
    public void setBatchProc(String batchProc) {
        this.batchProc = batchProc;
    }
    public String getBatchSttus() {
        return batchSttus;
    }
    public void setBatchSttus(String batchSttus) {
        this.batchSttus = batchSttus;
    }
    public String getBatchSttusNm() {
        return batchSttusNm;
    }
    public void setBatchSttusNm(String batchSttusNm) {
        this.batchSttusNm = batchSttusNm;
    }
    public String getBatchBeginTime() {
        return batchBeginTime;
    }
    public void setBatchBeginTime(String batchBeginTime) {
        this.batchBeginTime = batchBeginTime;
    }
    public String getBatchEndTime() {
        return batchEndTime;
    }
    public void setBatchEndTime(String batchEndTime) {
        this.batchEndTime = batchEndTime;
    }
    public String getErrorCode() {
        return errorCode;
    }
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    public String getErrorCn() {
        return errorCn;
    }
    public void setErrorCn(String errorCn) {
        this.errorCn = errorCn;
    }
    public String getSrchFromDate() {
		return srchFromDate;
	}
	public void setSrchFromDate(String srchFromDate) {
		this.srchFromDate = srchFromDate;
	}
	public String getSrchToDate() {
		return srchToDate;
	}
	public void setSrchToDate(String srchToDate) {
		this.srchToDate = srchToDate;
	}

}