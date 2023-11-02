package common.view;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import common.util.FileUtils;

/**
 * Program Name 	: ExcelView
 * Description 		: AbstractExcelView -> AbstractXlsView 수정
 * Programmer Name 	: ntarget
 * Creation Date 	: 2021-02-08
 * Used Table 		:
 *
 * 수정이력 :
 * -------------------------
 *
 */

@SuppressWarnings({"rawtypes", "unchecked"})
public class ExcelView extends AbstractXlsView {
	/** Logger for this class */
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	protected void buildExcelDocument(Map model, Workbook workbook, HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String filename	= (String)model.get("filename");

		if (filename == null || "".equals(filename))
			filename	= (String)model.get("filename");

		List<LinkedHashMap>   titleList	= (List)model.get("titleList");
		List                  excelList	= (List)model.get("excelList");

		Sheet worksheet = null;
		Row row = null;

		CellStyle cellHeadStyle = workbook.createCellStyle();

		cellHeadStyle.setAlignment(CellStyle.ALIGN_CENTER);
		//****
		cellHeadStyle.setFillForegroundColor(HSSFColor.LIGHT_CORNFLOWER_BLUE.index);
		cellHeadStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		cellHeadStyle.setBorderTop(CellStyle.BORDER_THIN);
		cellHeadStyle.setBorderBottom(CellStyle.BORDER_THIN);
		cellHeadStyle.setBorderLeft(CellStyle.BORDER_THIN);
		cellHeadStyle.setBorderRight(CellStyle.BORDER_THIN);

		worksheet = workbook.createSheet("WorkSheet");
		row = worksheet.createRow(0);

		if (titleList != null) {
			List listKey	= new ArrayList();
			LinkedHashMap titleMap	= (LinkedHashMap)titleList.get(0);

			// EXCEL TITLE Create
			Iterator k = titleMap.keySet().iterator();
			String key = "";
			int n = 0;
			while (k.hasNext()) {
				key = (String) k.next();

				Cell cell = row.createCell(n);

				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellStyle(cellHeadStyle);
				cell.setCellValue((String)titleMap.get(key));

				listKey.add(n, key);
				n++;
			}

			// EXCEL Data Create
			if (excelList != null) {
				CellStyle cellLeftStyle = workbook.createCellStyle();
				cellLeftStyle.setBorderTop(CellStyle.BORDER_THIN);
				cellLeftStyle.setBorderBottom(CellStyle.BORDER_THIN);
				cellLeftStyle.setBorderLeft(CellStyle.BORDER_THIN);
				cellLeftStyle.setBorderRight(CellStyle.BORDER_THIN);
				cellLeftStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
				//****
				cellLeftStyle.setFillForegroundColor(HSSFColor.WHITE.index);
				cellLeftStyle.setAlignment(CellStyle.ALIGN_LEFT);

				CellStyle cellRightStyle = workbook.createCellStyle();
				cellRightStyle.setBorderTop(CellStyle.BORDER_THIN);
				cellRightStyle.setBorderBottom(CellStyle.BORDER_THIN);
				cellRightStyle.setBorderLeft(CellStyle.BORDER_THIN);
				cellRightStyle.setBorderRight(CellStyle.BORDER_THIN);
				cellRightStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
				//****
				cellRightStyle.setFillForegroundColor(HSSFColor.WHITE.index);
				cellRightStyle.setAlignment(CellStyle.ALIGN_RIGHT);

				for(int i = 0; i < excelList.size(); i++){
					row = worksheet.createRow(i+1);
					Map map = (HashMap)excelList.get(i);

					for (int r = 0; r < listKey.size(); r++) {
						Cell cell = row.createCell(r);
						if (map.get(listKey.get(r)) instanceof BigDecimal) {
							cell.setCellStyle(cellRightStyle);
							//****
							cell.setCellValue(new HSSFRichTextString( ((BigDecimal)map.get(listKey.get(r))).toString()));
						}else if (map.get(listKey.get(r)) instanceof Long) {
							cell.setCellStyle(cellRightStyle);
							//****
							cell.setCellValue(new HSSFRichTextString( ((Long)map.get(listKey.get(r))).toString()));
						}else {
							cell.setCellStyle(cellLeftStyle);
							cell.setCellValue((String)map.get(listKey.get(r)));
						}
					}
				}

				for(int i = 0; i < listKey.size(); i++){
					worksheet.autoSizeColumn((short)i);
					worksheet.setColumnWidth(i, ((worksheet.getColumnWidth(i))+512)>65280? 65280 : ((worksheet.getColumnWidth(i))));  // 윗줄만으로는 컬럼의 width 가 부족하여 더 늘려야 함.
				}

			}
		}

        response.setContentType("Application/Msexcel");

        // 파일명 인코딩 처리
        FileUtils.setDisposition(filename+".xls", request, response);

        //response.setHeader("Content-Disposition", "ATTachment; Filename="+filename+".xls");

	}

}
