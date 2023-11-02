package business.com.cmm;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.icu.text.SimpleDateFormat;
import com.sun.star.io.IOException;

import commf.dao.CommonMapperImpl;
import common.base.BaseService;
import common.user.UserInfo;
import common.util.CommUtils;

@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class ExcelUploadService extends BaseService {

	private final int LIST_LIMIT = 150;		// DB등록처리 Group 구분

	@Autowired
	private CommonMapperImpl dao;

	@Autowired
	private UserInfo userInfo;
	
	@SuppressWarnings("deprecation")
	public long regiExcelLoadImport(Map reqMap, Map fileMap, String updateName, String insertName, String[] colName , String typeGbun)
	throws Exception, FileNotFoundException, IOException {
		
		
		String filePath = (String)fileMap.get("realPath");
		String fileName = (String)fileMap.get("fileSvrNm");
		
	
		
		long loopcnt = 0;
		List listLoadData	= new ArrayList();

		if (colName == null || colName.length == 0)
			throw processException("정의된 Column정보가 없습니다. 확인바랍니다.[FileManager.getColumnInfo]");

		FileInputStream fis = new FileInputStream(filePath+ "/" +fileName); 
		
        XSSFWorkbook workbook = new XSSFWorkbook(fis);

        XSSFSheet sheet = null;
        XSSFRow row     = null;
        XSSFCell cell   = null;

        int sheetNum	= workbook.getNumberOfSheets();

        try {
        	// LOG
        	logger.info("");
        	logger.info("Excel Upload Start ----- [exclUploadCodeMgmt] ");

        	int totInsertCnt = 0;
        	int totUpdateCnt = 0;

        	for (int i = 0; i < sheetNum; i++) {
        		logger.info("Sheet Num : "+ i);
        		logger.info("Sheet Name : "+ workbook.getSheetName(i));

        		sheet = workbook.getSheetAt(i);
        		// 생성된 시트를 이용하여 그 행의 수만큼
        		int rows = sheet.getPhysicalNumberOfRows();

        		logger.info("Row Total Num : "+ (rows-1));

        		for (int r = 0; r < rows; r++) {
        			if (r == 0) continue;	// Head Line 은 Skip
        			
        			row	= sheet.getRow(r);

        			Map cellMap	= new HashMap();
/*        			Map cellMap	= new HashMap(reqMap);*/

        			// 생성된 Row를 이용하여 그 셀의 수만큼
        			// 20221101 -tank
        			// no 컬럼 제외하므로 -1
        			// 배기량 -2개중복이므로 5번째꺼 제거 
        			// -2
        			int cells = row.getPhysicalNumberOfCells() -1;
        			
        			System.out.println(colName.length);
        			if (cells != colName.length-1)
        				throw processException("Excel 데이터 포맷이 맞지 않습니다. 확인바랍니다.");
        			//221101
        			// 각라인 첫번째 셀은 no 값이라 건너 뛰기 c= 0 -> c=1
        			for (short c = 1; c < cells; c++) {	// short형. 255개 Max 
        				
        				cell = row.getCell(c);

						if (colName != null) {
							switch (cell.getCellType()) {
							case XSSFCell.CELL_TYPE_NUMERIC:
								//엑셀 셀에서 yyyy-mm-dd의 형태의 데이터가 [날짜]셀서식일때
								if(DateUtil.isCellDateFormatted(cell)) {
									Date date = cell.getDateCellValue();
									cellMap.put(colName[c-1], new SimpleDateFormat("yyyy-MM-dd").format(date));
								//엑셀 셀에서 yyyy-mm-dd의 형태의 데이터가 [일반]셀서식일때
								} else {
									cellMap.put(colName[c-1], cell.getNumericCellValue());
							}
								break;
							case XSSFCell.CELL_TYPE_STRING:
								cellMap.put(colName[c-1], cell.getStringCellValue());
								break;
							}
						}
        			}
        			cellMap.put("typeGbun", typeGbun);
        			listLoadData.add(cellMap);

        			// DB저장
        			// out of memory 때문에 짤라서 처리하는게 좋음.
        			int icnt = 0;
        			int ucnt = 0;

        			if (listLoadData.size() == LIST_LIMIT || r == rows-1) {
        				/*if (CommUtils.nvlTrim(batch).equalsIgnoreCase("Y")) {*/
        					/*int batchCnt = regiBatch(listLoadData, insertName);
        					if (batchCnt == 0)
        						throw processException("ERROR] 데이터가 정상적으로 저장되지 않았습니다. [regiExcelLoadImport]");*/

        					/*logger.info("Saving Count : "+ listLoadData.size()+" [Insert : "+batchCnt+"]");*/
        		/*		} else {*/
        					for (int n = 0; n < listLoadData.size(); n++) {
        						Map importMap = (HashMap)listLoadData.get(n);
        						/*importMap.put("gsUserId", userInfo.getUserId());*/
        						importMap.put("colArr", colName);

        						if (updateName != null) {
        							if (dao.update(updateName, importMap) == 0) {
        								// Insert
        								icnt += dao.update(insertName, importMap);
        							}
        						} else {
        							// Insert
        							icnt += dao.update(insertName, importMap);
        						}
        					}
        					logger.info("Saving Count : "+ listLoadData.size()+" [Insert : "+icnt+", Update : "+ucnt+"]");

        			/*	}*/
        				listLoadData.clear();
        			}
        			totInsertCnt+=icnt;
        			totUpdateCnt+=ucnt;

        			loopcnt++;
        		}
        	}

        	logger.info("Total Count (Insert, Update) : "+totInsertCnt+", "+totUpdateCnt);
        	logger.info("Excel Upload End ----- [exclUploadCodeMgmt] ");
        	logger.info("");

        	// 처리 후 파일 삭제.
        	/*FileUtils.deleteFile(filePath +"/"+ fileName);*/
        } catch (Exception e) {
        	throw processException("엑셀을 읽는 중 오류가 발생하였습니다. 확인바랍니다.");
        } finally {
        	// FileInput CLOSE
        	if(workbook!=null)
        		fis.close();
        		
        }


		return loopcnt;
	}
	
	/**
	 * EXCEL SERVICE
	 * 221101
	 * 차량가액관리 엑셀업로드 기준가액 
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	@SuppressWarnings("deprecation")
	public long regiExcelLoadImportPrice(Map reqMap, Map fileMap, String updateName, String insertName)
	throws Exception, FileNotFoundException, IOException {
		
		String filePath = (String) fileMap.get("realPath");
		String fileName = (String) fileMap.get("fileSvrNm");
		// 국산
		String[] korCol = { "vhcl_mnft", "vhcl_model", "vhcl_nm", "vhcl_type", "vhcl_dvsn", "vhcl_dsplc", "vhcl_cpct",
				"cmpt_no", "calcul_year"};
		// 외산
		String[] forignCol = { "vhcl_mnft", "vhcl_nm", "subcategory", "model_nm", "vhcl_type", "vhcl_dvsn",
				"vhcl_dsplc", "vhcl_cpct", "cmpt_no", "calcul_year"};
		
		int korColCnt = korCol.length -1;
		int forignColCnt = forignCol.length -1;
		
		long loopcnt = 0;
		List listLoadData = new ArrayList();
		// String[] colName = FileManager.getColumnInfo(method);
		// colNam 저장 해야함 . 221031

		/*
		 * if (colName == null || colName.length == 0) throw
		 * processException("정의된 Column정보가 없습니다. 확인바랍니다.[FileManager.getColumnInfo]");
		 */
		FileInputStream fis = new FileInputStream(filePath + "/" + fileName);

		XSSFWorkbook workbook = new XSSFWorkbook(fis);

		XSSFSheet sheet = null;
		XSSFRow row = null;
		XSSFCell cell = null;

		// 생성된 시트 수만큼..
		int sheetNum = workbook.getNumberOfSheets();

		try {
			// LOG
			logger.info("");
			logger.info("Excel Upload Start ----- [exclUploadCodeMgmt] ");

			int totInsertCnt = 0;
			int totUpdateCnt = 0;
			String sheetNm = "";
			List years = new ArrayList();

			for (int i = 0; i < sheetNum; i++) {
				sheetNm = workbook.getSheetName(i);
				logger.info("Sheet Num : " + i);
				logger.info("Sheet Name : " + workbook.getSheetName(i));

				sheet = workbook.getSheetAt(i);
				// 생성된 시트를 이용하여 그 행의 수만큼
				int rows = sheet.getPhysicalNumberOfRows();

				logger.info("Row Total Num : " + (rows - 1));

				for (int r = 0; r < rows; r++) {
					row = sheet.getRow(r);

					Map cellMap = new HashMap();
					
					/* Map cellMap = new HashMap(reqMap); */

					// 생성된 Row를 이용하여 그 셀의 수만큼
					// 20221101 -tank

					int cells = row.getPhysicalNumberOfCells();

					// header line
					// 년도 갯수 및 년도 조회
					if (r == 0) {
						if (sheetNm.equals("국산")) {
							for (short c = (short) korColCnt; c < cells; c++) { // 국산은 kor
								cell = row.getCell(c);
								String cellString = cell.getStringCellValue();
								
								years.add(cellString.substring(0, 5).replaceAll("[^0-9a-zA-Z]", ""));
							}
						} else if (sheetNm.equals("외산")) {
							for (short c = (short) forignColCnt; c < cells; c++) { // 국산은 kor
								cell = row.getCell(c);
								String cellString = cell.getStringCellValue();
							
								years.add(cellString.substring(0, 5).replaceAll("[^0-9a-zA-Z]", ""));
							}
						}
						continue;
					} else {
						// 헤더 다음 라인 
						/*
						 * if (cells != colName.length) throw
						 * processException("Excel 데이터 포맷이 맞지 않습니다. 확인바랍니다.");
						 */
						// 221101
						// 각라인 첫번째 셀은 no 값이라 건너 뛰기 c= 0 -> c=1
						if (sheetNm.equals("국산")) {
							// 차량가액 헤더정보 전산번호 까지
							short cellCnt = 0;
							for (short c = 0; c < korColCnt; c++) { // 국산은 kor
								cell = row.getCell(c);
								switch (cell.getCellType()) {
								case XSSFCell.CELL_TYPE_NUMERIC:
									// 엑셀 셀에서 yyyy-mm-dd의 형태의 데이터가 [날짜]셀서식일때
									if (DateUtil.isCellDateFormatted(cell)) {
										Date date = cell.getDateCellValue();
										cellMap.put(korCol[c], new SimpleDateFormat("yyyy-MM-dd").format(date));
										// 엑셀 셀에서 yyyy-mm-dd의 형태의 데이터가 [일반]셀서식일때
									} else {
										cellMap.put(korCol[c], cell.getNumericCellValue());
									}
									break;
								case XSSFCell.CELL_TYPE_STRING:
									cellMap.put(korCol[c], cell.getStringCellValue());
									break;
								}

								cellCnt++;
							} // for end
							int yearsCnt = 0;
							for (short c = cellCnt; c < cells; c++) { // 국산은 kor
								Map yearMap = cellMap;
								// 산정년도
								yearMap.put("calcul_year", years.get(yearsCnt));

								// 기준가액
								cell = row.getCell(c);
								yearMap.put("basic_price", cell.getNumericCellValue());

								// 국산구분
								yearMap.put("domestic_dvsn", "국산");
								
								listLoadData.add(yearMap);
								
								yearsCnt++;
								dao.update(insertName, yearMap);
							
							}

						} else {

							short cellCnt = 0;
							for (short c = 0; c < forignColCnt; c++) { // 외산
								cell = row.getCell(c);
								switch (cell.getCellType()) {
								case XSSFCell.CELL_TYPE_NUMERIC:
									// 엑셀 셀에서 yyyy-mm-dd의 형태의 데이터가 [날짜]셀서식일때
									if (DateUtil.isCellDateFormatted(cell)) {
										Date date = cell.getDateCellValue();
										cellMap.put(forignCol[c], new SimpleDateFormat("yyyy-MM-dd").format(date));
										// 엑셀 셀에서 yyyy-mm-dd의 형태의 데이터가 [일반]셀서식일때
									} else {
										cellMap.put(forignCol[c], cell.getNumericCellValue());
									}
									break;
								case XSSFCell.CELL_TYPE_STRING:
									cellMap.put(forignCol[c], cell.getStringCellValue());
									break;
								}

								cellCnt++;
							} // for end
								int yearsCnt = 0;
							for (short c = cellCnt; c < cells; c++) { // 국산은 kor
								Map yearMap = cellMap;
								// 산정년도
								yearMap.put("calcul_year", years.get(yearsCnt));
								
								// 기준가액
								cell = row.getCell(c);
								yearMap.put("basic_price", cell.getNumericCellValue());

								// 국산구분
								yearMap.put("domestic_dvsn", "외산");
								
								listLoadData.add(yearMap);
								yearsCnt++;
								dao.update(insertName, yearMap);
							
							}

						}

						

						// DB저장
						// out of memory 때문에 짤라서 처리하는게 좋음.
						int icnt = 0;
						int ucnt = 0;

						/* if (CommUtils.nvlTrim(batch).equalsIgnoreCase("Y")) { */
						/*
						 * int batchCnt = regiBatch(listLoadData, insertName); if (batchCnt == 0) throw
						 * processException("ERROR] 데이터가 정상적으로 저장되지 않았습니다. [regiExcelLoadImport]");
						 */

						/*
						 * logger.info("Saving Count : "+
						 * listLoadData.size()+" [Insert : "+batchCnt+"]");
						 */
						/* } else { */
						
						/*for (int n = 0; n < listLoadData.size(); n++) {
							Map importMap = (HashMap) listLoadData.get(n);
							 importMap.put("gsUserId", userInfo.getUserId()); 
							 221121 국산 외산 구분 
							
							 * if (sheetNm.equals("국산")) { importMap.put("colArr", korCol); } else {
							 * importMap.put("colArr", forignCol); }
							 

							if (updateName != null) {
								if (dao.update(updateName, importMap) == 0) {
									// Insert
									icnt += dao.update(insertName, importMap);
								}
							} else {
								// Insert
								icnt += dao.update(insertName, importMap);
							}
						}
*/
						/* } */
						listLoadData.clear();
				

						/*
						 * totInsertCnt += icnt; totUpdateCnt += ucnt;
						 * 
						 * loopcnt++;
						 */
					}
				}
				years.clear();

			}

			// 처리 후 파일 삭제.
			/* FileUtils.deleteFile(filePath +"/"+ fileName); */
		} catch (Exception e) {
			throw processException("엑셀을 읽는 중 오류가 발생하였습니다. 확인바랍니다.");
		} finally {
			// FileInput CLOSE
			if (workbook != null)
				fis.close();

		}

		return loopcnt;
	}

	
	
	
	/**
	 * EXCEL SERVICE
	 * 221128
	 * 차량가액관리 시가표준액 엑셀업로드 
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	@SuppressWarnings("deprecation")
	public long regiExcelLoadImportMarketPrice(Map reqMap, Map fileMap, String updateName, String insertName)
	throws Exception, FileNotFoundException, IOException {
		
		String filePath = (String) fileMap.get("realPath");
		String fileName = (String) fileMap.get("fileSvrNm");
		// 시가표준액 컬럼
		String[] marketCol = {"vhcl_model","vhcl_mnft","vhcl_nm","vhcl_type","basic_price" };
		
		int marketColCnt = marketCol.length ;
		
		long loopcnt = 0;
		List listLoadData = new ArrayList();
		// String[] colName = FileManager.getColumnInfo(method);
		// colNam 저장 해야함 . 221031

		/*
		 * if (colName == null || colName.length == 0) throw
		 * processException("정의된 Column정보가 없습니다. 확인바랍니다.[FileManager.getColumnInfo]");
		 */
		FileInputStream fis = new FileInputStream(filePath + "/" + fileName);

		XSSFWorkbook workbook = new XSSFWorkbook(fis);

		XSSFSheet sheet = null;
		XSSFRow row = null;
		XSSFCell cell = null;

		// 생성된 시트 수만큼..
		int sheetNum = workbook.getNumberOfSheets();

		try {
			// LOG
			logger.info("");
			logger.info("Excel Upload Start ----- [exclUploadCodeMgmt] ");

			int totInsertCnt = 0;
			int totUpdateCnt = 0;
			String sheetNm = "";
			String domesticType = "";
			List years = new ArrayList();

			for (int i = 0; i < sheetNum; i++) {
				sheetNm = workbook.getSheetName(i);
				logger.info("Sheet Num : " + i);
				logger.info("Sheet Name : " + workbook.getSheetName(i));
				// sheetNm 자르기 
				sheetNm = workbook.getSheetName(i); 
				domesticType = sheetNm.substring(0, 2);
				System.out.println(domesticType);
				// 형식 구분 
			
				
				if(sheetNm.contains("형식")) {
					if(sheetNm.contains("원동기변경")) {
						continue;
					}
					sheet = workbook.getSheetAt(i);
					// 생성된 시트를 이용하여 그 행의 수만큼
					int rows = sheet.getPhysicalNumberOfRows()-4;

					logger.info("Row Total Num : " + (rows));

					for (int r = 0; r < rows; r++) {
						row = sheet.getRow(r);

						Map cellMap = new HashMap();
						
						/* Map cellMap = new HashMap(reqMap); */

						// 생성된 Row를 이용하여 그 셀의 수만큼
						// 20221101 -tank

						int cells = row.getPhysicalNumberOfCells()-1;

						// header line
						// 년도 갯수 및 년도 조회
						if (r == 0 || r== 1 || r== 2 || r==3) {
							continue;
						} else {
							// 헤더 다음 라인 
							/*
							 * if (cells != colName.length) throw
							 * processException("Excel 데이터 포맷이 맞지 않습니다. 확인바랍니다.");
							 */
							// 221101
							// 각라인 첫번째 셀은 no 값이라 건너 뛰기 c= 0 -> c=1
					
								// 차량가액 헤더정보 전산번호 까지
							short cellCnt = 0;
							for (short c = 0; c < marketCol.length; c++) { // 국산은 kor
								cell = row.getCell(c+1);
								switch (cell.getCellType()) {
								case XSSFCell.CELL_TYPE_NUMERIC:
									// 엑셀 셀에서 yyyy-mm-dd의 형태의 데이터가 [날짜]셀서식일때
									if (DateUtil.isCellDateFormatted(cell)) {
										Date date = cell.getDateCellValue();
										cellMap.put(marketCol[cellCnt], new SimpleDateFormat("yyyy-MM-dd").format(date));
										// 엑셀 셀에서 yyyy-mm-dd의 형태의 데이터가 [일반]셀서식일때
									} else {
										cellMap.put(marketCol[cellCnt], cell.getNumericCellValue());
									}
									break;
								case XSSFCell.CELL_TYPE_STRING:
									cellMap.put(marketCol[cellCnt], cell.getStringCellValue());
									break;
								}
		
								cellCnt++;
							} // for end
						
					
							if(sheetNm.contains("국산")) {
								cellMap.put("domestic_dvsn", "국산");
							}else if (sheetNm.contains("외산")) {
								cellMap.put("domestic_dvsn", "외산");
							}
							
							//sheet nm 추가 
							cellMap.put("sheet_dvsn",sheetNm);
		
							listLoadData.add(cellMap);
							
							dao.update(insertName, cellMap);
						}
					}
				
					
				
					
				}else {
					continue;
				}

				listLoadData.clear();
			}// sheet for end 
				
				
				

	

			// 처리 후 파일 삭제.
			/* FileUtils.deleteFile(filePath +"/"+ fileName); */
		} catch (Exception e) {
			throw processException("엑셀을 읽는 중 오류가 발생하였습니다. 확인바랍니다.");
		} finally {
			// FileInput CLOSE
			if (workbook != null)
				fis.close();

		}

		return loopcnt;
	}



    /**
     * 엑셀 업로드 세션 정보 초기화
     */
    public Map<String,Object> initExclUpldJobInfo(String exclJobId, HttpSession session) throws Exception {
        Map<String,Object> exclJobInfo   = null;

        if(!CommUtils.isEmpty(exclJobId)) {

            if(session != null) {
                // [#session] 엑셀 진행 내용 세션 진행
                exclJobInfo = (Map)session.getAttribute(exclJobId);
                if(exclJobInfo == null) {
                    session.setAttribute(exclJobId, (new HashMap()));

                    exclJobInfo = (Map)session.getAttribute(exclJobId);
                    exclJobInfo.put("failRows",     new ArrayList());
                    exclJobInfo.put("processRate",  0);
                    exclJobInfo.put("successCount", 0);
                    exclJobInfo.put("failureCount", 0);
                    exclJobInfo.put("totalCount",   0);
                }
            }
        }

        return exclJobInfo;
    }

    /**
     * 엑셀업로드 작업 정보 얻기
     * @param exclJobId
     * @param session
     * @return
     * @throws Exception
     */
    public Map<String, Object> getExclUpldJobInfo(String exclJobId, HttpSession session) throws Exception {
        // 초기화
        Map<String, Object> exclJobInfo = null;
        
        if(!CommUtils.isEmpty(exclJobId) && session != null) {
            exclJobInfo = (Map<String,Object>)session.getAttribute(exclJobId);
        
            if(exclJobInfo == null) {
                exclJobInfo = initExclUpldJobInfo(exclJobId, session);
            }
        }

        return exclJobInfo;
    }

    /**
     * 엑셀 업로드 세션 정보 제거
     */
    public void removeExclUpldJobInfo(String exclJobId, HttpSession session) throws Exception {

        if (!CommUtils.isEmpty(exclJobId) && session != null) {
            session.removeAttribute(exclJobId);
        }
        
    }

    /**
     * 성공수 저장
     * @param exclJobId     엑셀업로드 작업 id
     * @param successCount  성공수
     * @param session
     * @return
     * @throws Exception
     */
    public Map<String, Object> setSuccessCountInJobInfo(String exclJobId, int successCount, HttpSession session) throws Exception {
        // 초기화
        Map<String, Object> exclJobInfo = null;
        
        if(!CommUtils.isEmpty(exclJobId) && session != null) {
            exclJobInfo = (Map<String,Object>)session.getAttribute(exclJobId);
        
            if (exclJobInfo == null) {
                exclJobInfo = initExclUpldJobInfo(exclJobId, session);
            }
            
            // 성공수 저장
            exclJobInfo.put("successCount", successCount);

            // 처리율 계산
            setProcessRateInJobInfo(exclJobInfo);
        }

        return exclJobInfo;
    }

    /**
     * 실패수 저장
     * @param exclJobId     엑셀업로드 작업 id
     * @param failureCount  실패수
     * @param regiInfo      실패 row 정보
     * @param session
     * @return
     * @throws Exception
     */
    public Map<String, Object> setFailureCountInJobInfo(String exclJobId, int failureCount, Map regiInfo, HttpSession session) throws Exception {
        // 초기화
        Map<String, Object> exclJobInfo = null;

        if (!CommUtils.isEmpty(exclJobId) && session != null) {
            exclJobInfo = (Map<String,Object>)session.getAttribute(exclJobId);
        
            if(exclJobInfo == null) {
                exclJobInfo = initExclUpldJobInfo(exclJobId, session);
            }
            
            // 실패수 저장
            exclJobInfo.put("failureCount", failureCount);

            // 실패객체 추가
            ((List)exclJobInfo.get("failRows")).add(regiInfo);

            // 처리율 계산
            setProcessRateInJobInfo(exclJobInfo);
        }

        return exclJobInfo;
    }

    /**
     * 처리율 조회
     */
    public int getProcessRateInJobInfo(String exclJobId, HttpSession session) throws Exception {
        int processRate = 0;
        Map<String, Object> exclJobInfo = null;
        
        if (!CommUtils.isEmpty(exclJobId) && session != null) {
            exclJobInfo = (Map<String,Object>)session.getAttribute(exclJobId);
           
            if(exclJobInfo != null) {
                processRate = ((Integer)exclJobInfo.get("processRate") == null)? 0:(Integer)exclJobInfo.get("processRate");
            }
        }

        return processRate;
    }

    /**
     * 처리율 계산
     * @param exclJobId     엑셀업로드 작업 id
     * @param exclJobInfo   엑셀업로드 작업 정보 저장 map
     * @return
     * @throws Exception
     */
    private Map<String, Object> setProcessRateInJobInfo(Map<String,Object> exclJobInfo) throws Exception {

        if (exclJobInfo != null && !exclJobInfo.isEmpty()) {
            int succCnt  = ((Integer)exclJobInfo.get("successCount")==null)? 0:(Integer)exclJobInfo.get("successCount");
            int failCnt  = ((Integer)exclJobInfo.get("failureCount")==null)? 0:(Integer)exclJobInfo.get("failureCount");
            int totalCnt = ((Integer)exclJobInfo.get("totalCount")  ==null)? 0:(Integer)exclJobInfo.get("totalCount");
            
            //[#session] 엑셀처리 진행률
            int processRate = (totalCnt == 0)? 100 :  Integer.parseInt( String.valueOf(  Math.round((succCnt+failCnt)*100/totalCnt)  ) );
            exclJobInfo.put("processRate", processRate);
        }

        return exclJobInfo;
    }

}
