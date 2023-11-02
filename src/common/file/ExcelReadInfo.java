package common.file;

import java.util.ArrayList;
import java.util.List;

public class ExcelReadInfo {

    private int          headerRowCount  = 1;
    private List<String> cellMappingKeys ;
    private String       fileFullPathName;
    /**
     * 각 row가 정의된 컬럼수와 다름을 허용할지여부
     * 허용하지 않으면 Exception 발생함
     *
     * true : exception을 발생하지 않고 null로 적용
     * false : exception을 발행하고 parsing작업을 중지
     * default는 true
     */
    private boolean      isAllowExcelFormatError = true ;

    public ExcelReadInfo() {
        headerRowCount  = 0;
        cellMappingKeys = new ArrayList<String>();
    }

    public int getHeaderRowCount() {
        return headerRowCount;
    }
    public ExcelReadInfo setHeaderRowCount(int headerRowCount) {
        this.headerRowCount = headerRowCount;
        return this;
    }
    public List<String> getCellMappingKeys() {
        //return cellMappingKeys;
        List<String> retnList = new ArrayList<String>();
        retnList.addAll(this.cellMappingKeys);
        return retnList;
    }
    public ExcelReadInfo setCellMappingKeys(List<String> cellMappingKeys) {
        //this.cellMappingKeys = cellMappingKeys;
        this.cellMappingKeys = new ArrayList<String>();
        this.cellMappingKeys.addAll(cellMappingKeys);
        return this;
    }
    public String getFileFullPathName() {
        return this.fileFullPathName;
    }
    public ExcelReadInfo setFileFullPathName(String path) {
        this.fileFullPathName = path;
        return this;
    }

    public boolean isAllowExcelFormatError() {
        return isAllowExcelFormatError;
    }

    public ExcelReadInfo setAllowExcelFormatError(boolean isAllowExcelFormatError) {
        this.isAllowExcelFormatError = isAllowExcelFormatError;
        return this;
    }

    @Override
    public String toString() {
        return "ExcelReadInfo [headerRowCount=" + headerRowCount +
                            ", cellMappingKeys=" + cellMappingKeys +
                            ", fileFullPathName=" + fileFullPathName + "]";
    }
}
