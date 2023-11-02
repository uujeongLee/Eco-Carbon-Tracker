package common.base;

import java.lang.reflect.Array;

import common.user.UserInfo;

/**
 * Program Name 	: BaseModel
 * Description 		:
 * Programmer Name 	: ntarget
 * Creation Date 	: 2021-02-08
 * Used Table 		:
 *
 *   수정일      수정자                     수정내용
 *  -------    --------    ---------------------------
 *
 */

public class BaseModel {

    /* paging */
    protected String page		= null;
    protected String pageSize   = null;
    protected String idx		= "0";
    protected int startIndex	= 0;
    protected int firstIndex 	= 1;
    protected int lastIndex 	= 1;

    /* combo box */
    private String comboValue	= null;
    private String comboText 	= null;

    /* 기타 */
    private String mode         = null;
    private String act          = null;

    private UserInfo userInfo   = null;

    public UserInfo getUserInfo() {
        if(this.userInfo == null) {
            this.userInfo = new UserInfo();
        }
        return userInfo;
    }
    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    @SuppressWarnings("unchecked")
    public <T> T[] getArrayForSecure(T[] memberArray) {
        /* 시큐어코딩 적용 (배열사용방법) */
        T[] ret = null;
        if(memberArray != null){
            // ret = new T[memberArray.length];
            ret = (T[])Array.newInstance(memberArray.getClass().getComponentType(), memberArray.length);
            for(int i = 0; i < memberArray.length; i++){
                ret[i] = memberArray[i];
            }
        }
        return ret;
    }

    @SuppressWarnings("unchecked")
    public <T> T[] getArraySetForSecure(T[] dataArray) {
        /* 시큐어코딩 적용 (배열사용방법) */
        //this.arrChkAddr = new String[arrChkAddr.length];
        T[] rear = (T[])Array.newInstance(dataArray.getClass().getComponentType(), dataArray.length);
        for (int i = 0; i < dataArray.length; i++) {
            rear[i] = dataArray[i];
        }
        return rear;
    }

    public String getPage() {
        return page;
    }
    public void setPage(String page) {
        this.page = page;
    }
    public String getPageSize() {
        return pageSize;
    }
    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }
    public String getIdx() {
        return idx;
    }
    public void setIdx(String idx) {
        this.idx = idx;
    }
    public int getStartIndex() {
        return startIndex;
    }
    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }
    public int getFirstIndex() {
        return firstIndex;
    }
    public void setFirstIndex(int firstIndex) {
        this.firstIndex = firstIndex;
    }
    public int getLastIndex() {
        return lastIndex;
    }
    public void setLastIndex(int lastIndex) {
        this.lastIndex = lastIndex;
    }

    public String getComboValue() {
        return comboValue;
    }
    public void setComboValue(String comboValue) {
        this.comboValue = comboValue;
    }
    public String getComboText() {
        return comboText;
    }
    public void setComboText(String comboText) {
        this.comboText = comboText;
    }

    public String getMode() {
        return mode;
    }
    public void setMode(String mode) {
        this.mode = mode;
    }
    public String getAct() {
        return act;
    }
    public void setAct(String act) {
        this.act = act;
    }


}