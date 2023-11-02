package common.view;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.AbstractView;

import common.util.FileUtils;
import common.util.properties.ApplicationProperty;
import net.sf.jxls.transformer.XLSTransformer;

/**
 * Program Name 	: ExcelTempView
 * Description 		:
 * Programmer Name 	: ntarget
 * Creation Date 	: 2021-02-08
 * Used Table 		:
 */

@SuppressWarnings({"rawtypes"})
public class ExcelTempView extends AbstractView {
    /** Logger for this class */
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response)
    throws Exception {

        // 20200812 변경 : 경로를 context기반으로 /WEB-INF/부터 명시하게 함.
//		String tempPath		= ApplicationProperty.get("excel.templete.dir");
        String tempPath		= ApplicationProperty.get("excel.templete.servletContextDir");
        String contextPath  = request.getServletContext().getRealPath("");
        tempPath            = contextPath + File.separator + tempPath;

        String pcFilename	= (String)model.get("pcFilename");

        if (pcFilename == null || "".equals(pcFilename))
            pcFilename	= (String)model.get("filename");

        SecureRandom random = new SecureRandom();
        StringBuffer sb = null;
        sb = new StringBuffer();
        sb.append(String.valueOf(System.currentTimeMillis()));
        sb.append(String.valueOf(random.nextLong()));

        String fileName		= sb.toString() +"_"+pcFilename+".xls";

        String realPcFileName	= pcFilename+".xls";

        String tempFileName = (String)model.get("filename")+"_Template.xls";
        List   excelList	= (List)model.get("excelList");
        Map    paramMap		= (HashMap)model.get("paramMap");

        // Excel File Create
        Map<String, Object> beans = new HashMap<String, Object>();
        beans.put("excelList",  excelList);
        beans.put("paramMap", 	paramMap);

        XLSTransformer transformer = new XLSTransformer();
        transformer.transformXLS(tempPath +"/"+ tempFileName, beans, tempPath +"/"+ fileName);

        // File Download
        File f = new File(tempPath +"/"+ fileName);

        if (f.exists()) {
            logger.info("response charset : " + response.getCharacterEncoding());

            String mimetype = "application/x-msdownload";
            response.setContentType(mimetype);

            // 파일명 인코딩 처리
            FileUtils.setDisposition(realPcFileName, request, response);

            byte[] buffer = new byte[1024];
            BufferedInputStream ins = new BufferedInputStream(new FileInputStream(f));
            BufferedOutputStream outs = new BufferedOutputStream(response.getOutputStream());

            try {
                int read = 0;
                // while ((read = ins.read(buffer)) != -1) {
                // outs.write(buffer, 0, read);
                // }
                while (true) {
                    read = ins.read(buffer);
                    if (read == -1) {
                        break;
                    }
                    outs.write(buffer, 0, read);
                }
                outs.close();
                ins.close();
            } catch (IOException e) {
                logger.info("EXCEL DOWNLOAD ERROR : FILE NAME = "+tempPath +"/"+  fileName+" ");
            } finally {
                // Download Excel : File Remove
                FileUtils.deleteFile(tempPath +"/"+ fileName);
                if(outs!=null) outs.close();
                if(ins!=null) ins.close();
            }
        }
    }

}
