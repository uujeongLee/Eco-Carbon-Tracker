package common.file;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 엑셀파일 파싱 클래스
 *
 * 사용예)
 * new ExcelReadHelper(
 *          new ExcelReadInfo()
 *              .setHeaderRowCount(1)                           // header row 개수 설정 (default : 1)
 *              .setCellMappingKeys(buildExcelMappingKeys())    // 엑셀 mapping 키 리스트 설정
 *              .setFileFullPathName(fullFilePath)              // 엑셀파일 경로(full path) 설정
 *     ).excelToDataList();
 * @author
 *
 */
@SuppressWarnings({"rawtypes","unchecked"})
public class ExcelReadHelper {

    private static final Logger logger = LoggerFactory.getLogger(ExcelReadHelper.class);

    private ExcelReadInfo mExcelReadInfo;

    public ExcelReadHelper(ExcelReadInfo excelReadVo) {
        mExcelReadInfo = excelReadVo;
    }


    /**
     * 엑셀 파일을 읽어서 List객체로 반환하는 method (xls/xlsx 구별없이 수행)
     *
     * @param filePath
     * @return
     */
    public List<Map> excelToDataList() throws Exception {

        List<Map> list = new ArrayList<Map>();

        FileInputStream fis   = null;
        Workbook mWorkbook    = null;

        try {
            fis = new FileInputStream(mExcelReadInfo.getFileFullPathName());

            // Workbook은 엑셀파일 전체 내용을 담고 있는 객체
            // xls/xlsx를 구별하지 않고 처리하기 위해 다음과 같이 작업.
            mWorkbook = WorkbookFactory.create(fis);

            // 엑셀파일의 모든 내용을 읽어서 list 객체로 반환
            list = loadWorkbook(mWorkbook);

        } catch (FileNotFoundException e) {
            //e.printStackTrace();
            throw e;
        } catch (IOException e) {
            //e.printStackTrace();
            throw e;
        } catch (IllegalArgumentException e) {
            //e.printStackTrace();
            throw e;
        } catch (Exception e) {
            //e.printStackTrace();
            throw e;
        } finally {
            try {
                // 사용한 자원은 finally에서 해제
                if( fis!= null) fis.close();
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }

        return list;
    }


    //++++++++++++++++++++++++++++++++++++++++++++++++
    // private methods...
    //++++++++++++++++++++++++++++++++++++++++++++++++

    /**
     * Workbook객체를 넙겨 받아 파일 내용을 파싱하여 List 객체로 반환하는 method
     *
     * @param workbook
     * @return
     * @throws Exception
     */
    private List<Map> loadWorkbook(Workbook workbook) throws Exception {

        // 반환할 객체를 생성
        List<Map> list = new ArrayList<Map>();

        if(workbook != null) {

            // 탐색에 사용할 Sheet, Row, Cell 객체
            Sheet curSheet;
            Map   map;

            // 내용이 시작되는 row index (header 개수를 기준으로 자동 판단)
            int contentStartRwoIndex = mExcelReadInfo.getHeaderRowCount();

            // Sheet 탐색 for문
            for(int sheetIndex = 0 ; sheetIndex < workbook.getNumberOfSheets(); sheetIndex++) {
                // 현재 Sheet 반환
                curSheet = workbook.getSheetAt(sheetIndex);
                // row 탐색 for문
                for(int rowIndex=0; rowIndex < curSheet.getPhysicalNumberOfRows(); rowIndex++) {
                    // 헤더를 제외하고 내용이 시작되는 부분부터 parsing 함.
                    if(rowIndex >= contentStartRwoIndex) {
                        // 현재 row 반환
                        map = readRow(curSheet.getRow(rowIndex));

                        // 결과가 null을 반환하지 않았으면 list에 추가한다.
                        if(map != null) list.add(map);
                    }
                }
            }

        }

        return list;
    }

    /**
     * Row 객체를 parsing하여 map 객체로 반환
     *
     * @param rowObj
     * @return
     * @throws Exception
     */
    private Map readRow(Row rowObj) throws Exception {

        Cell  cellObj = null;
        Map   map     = null;

        if(rowObj != null) {
            // Row 정보 객체
            map = new LinkedHashMap();
            String value;

// 제거 : 빈 컬럼이 존재할 수 있음
//            // row의 첫번째 cell값이 비어있지 않은 경우 만 cell탐색
//            if(!"".equals(rowObj.getCell(0).getStringCellValue())) {

            // 해당 row의 작성된 cell 개수
//                int numOfCells = rowObj.getPhysicalNumberOfCells();
            // 위 코드에서는 셀 중간에 빈값이 있을때 셀의 갯수를 정확하게 가져오지 못한다.
            // 만약 10개의 셀이 있을 때 중간 3번째 셀이 비어있다면 9개를 가져오는 것이다.
            // 그래서 저 함수가 아닌 getLastCellNum()을 사용하여 데이터가 있는 마지막 셀의 숫자를 가지고 오게 된다.
            int numOfCells = rowObj.getLastCellNum();

            // 데이터 입력한 내용이 1개라도 존재할 때 수행 (TODO : 추후 확인해야 함)
            if(numOfCells > 0) {

                logger.debug("***** 물리적 cell 개수 : " + numOfCells);

                // 정의된 header col 수
                int definedColCount = mExcelReadInfo.getCellMappingKeys().size();

                //** 정의된 컬럼 수와 맞지 않으면 format exception 발생 (정의된 format와 다른 것으로 간주)
                if( numOfCells != definedColCount) {
                    logger.error("## NOT_ALLOWED_EXCEL_FORMAT");

                    if(mExcelReadInfo.isAllowExcelFormatError()) {
                        numOfCells = definedColCount;
                    }
                    else {
                        throw new Exception("NOT_ALLOWED_EXCEL_FORMAT");
                    }
                }

                // cell 탐색 for 문
                for(int cellIndex=0;cellIndex<numOfCells; cellIndex++) {

                    // 정의된 col이 아니면 parsing 수행하지 않고 넘어감.(정의되지 않은 cell은 대상에서 제외함)
                    if(cellIndex >= definedColCount) continue;

                    // cell 객체.
                    cellObj = rowObj.getCell(cellIndex);

                    if(true) {
                        value = "";
                        if(cellObj == null) {
                            value = null;
                        }
                        else {
                            // cell 스타일이 다르더라도 String으로 반환 받음
                            switch (cellObj.getCellType()){
                                case Cell.CELL_TYPE_FORMULA:
                                    value = cellObj.getCellFormula();
                                    break;
                                case Cell.CELL_TYPE_NUMERIC:
                                    value = cellObj.getNumericCellValue()+"";
                                    break;
                                case Cell.CELL_TYPE_STRING:
                                    value = cellObj.getStringCellValue()+"";
                                    break;
                                case Cell.CELL_TYPE_BLANK:
                                    value = "";
                                    break;
                                case Cell.CELL_TYPE_ERROR:
                                    value = cellObj.getErrorCellValue()+"";
                                    break;
                                default:
                                    value = new String();
                                    break;
                                }
                        }

                        // 정의된 cell key 값과 매핑되는 cell 순서대로 읽어서 map에 추가.
                        map.put(mExcelReadInfo.getCellMappingKeys().get(cellIndex), value);

                    }
                }
            } // end of 'if(numOfCells > 0) {'
//            } // end of 'if(!"".equals(rowObj.getCell(0).getStringCellValue())) {'
        }

        logger.debug("*** excel read result map : " + map);

        return map;
    }
}
