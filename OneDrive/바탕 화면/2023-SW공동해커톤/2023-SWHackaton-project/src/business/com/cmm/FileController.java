package business.com.cmm;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import common.base.BaseController;
import common.file.FileManager;
import common.util.FileUtils;
import common.util.properties.ApplicationProperty;
import egovframework.rte.fdl.cmmn.exception.EgovBizException;

@Controller
@SuppressWarnings("all")
public class FileController extends BaseController {

	@Resource(name="fileManager")
	FileManager fileManager;

	@Autowired
	FileService fileService;

	/**
	 * 첨부파일 업로드 (첨부파일만 단독으로 등록시 사용)
	 * 첨부파일 FORM Name : upfile1, upfile2 로 넘겨준다.
	 */
	@RequestMapping("/comm/fileUpload.do")
	public void fileUpload(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List listFile = new ArrayList();

		// 파일 멀티로 받음
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> files = multipartRequest.getFileMap();

		String tempDir = ApplicationProperty.get("upload.temp.dir");

		//디렉토리 생성
		FileUtils.makeDirectories(tempDir);

		// 파일이름이 중복되면 spring에서 에러가 나므로 각각 다른 이름으로 받음
		for (int i = 0; i < files.size(); i++) {
			String upfileNm 		= "upfile" + i;
			MultipartFile inFile 	= files.get(upfileNm);

			String saveFileNm 		= fileManager.getFileName(tempDir, inFile.getOriginalFilename());

			// 가능 확장자 체크
			if (!FileUtils.isAtthAllowedFileType(inFile.getOriginalFilename(), ApplicationProperty.get("file.all.allow.exts")))
				throw new EgovBizException("["+inFile.getOriginalFilename()+"]"+message.getMessage("fail.common.notExtFile"));

			try {
				if (!saveFileNm.equals("")) {
					// 파일을 폴더에 저장함
					FileUtils.copyFile(inFile.getInputStream(), new FileOutputStream(tempDir + saveFileNm));

					HashMap fmap = new HashMap();
					fmap.put(upfileNm, 		saveFileNm);
					fmap.put("tempDir", 	tempDir);

					listFile.add(fmap);
				}
			} catch (FileNotFoundException e) {
				response.setStatus(500);
				throw new RuntimeException(e);
			} catch (IOException e) {
				response.setStatus(500);
				throw new RuntimeException(e);
			} catch (Exception e) {
				response.setStatus(500);
				throw new RuntimeException(e);
			}
		}

		// 파일이름을 세션에 저장
		request.getSession().setAttribute("FILELIST", listFile);
	}

	/**
     * 파일 다운로드
     */
    @RequestMapping("/comm/fileDownload.do")
    public void fileDownload(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String fileNo = request.getParameter("fileNo");

        // 사용처에 따라서 커스터마이징해서 사용.
        Map params = new HashMap();
        params.put("fileNo", 	fileNo);

        String saveFileNm  		= "";
        String serverDirPath 	= "";
        String orgFileNm   		= "";

        Map fileInfo = fileService.viewFile(params);

        // TODO : WAS 상태경로로 다운로드하는 method (실운영에서는 사용 안함)
        // procFileDownloadEx(request, response, fileInfo);
        
        // 절대경로 기반 다운로드 method (실운영에서 사용할 대상)
        procFileDownload(request, response, fileInfo);
    }

    /**
     * 사용자쪽 업로드 처리에 맞게 변경한 파일 다운로드 처리
     * @param request
     * @param response
     * @param fileInfo
     * @throws Exception
     */
    private void procFileDownloadEx(HttpServletRequest request, HttpServletResponse response, Map fileInfo) throws Exception {

        String saveFileNm   = "";
        String serverPath   = "";
        String orgFileNm    = "";

        //-----------------------------
        // 사용자 업로드 부분에서 가져옴
        //  => UploadController.java
        //-----------------------------
        String contextRealPath = request.getSession().getServletContext().getRealPath("");
        File directory = new File(contextRealPath);
        String fileUploadRootPath = directory.getParent();

        if (fileInfo != null) {
            saveFileNm = (String)fileInfo.get("fileServerNm");
            serverPath = (String)fileInfo.get("filePath");
            orgFileNm  = (String)fileInfo.get("fileOrginlNm");
        } else {
            logger.info("$$$$$$$$$$$$$$$$$  FILE DOWNLOAD ERROR : Not Server File.");
            throw new EgovBizException(message.getMessage("exception.notExistAttachFile"));
        }

        String fullFileName = fileUploadRootPath + serverPath;

        //파일을 orgFileNm의 이름으로 다운로드 함
        File f = new File(fullFileName);

        if (f.exists()) {
            logger.info("response charset : " + response.getCharacterEncoding());

            String mimetype = "application/x-msdownload";
            response.setContentType(mimetype);

            // 파일명 인코딩 처리
            FileUtils.setDisposition(orgFileNm, request, response);

            byte[] buffer = new byte[1024];
            BufferedInputStream ins = new BufferedInputStream(new FileInputStream(f));
            BufferedOutputStream outs = new BufferedOutputStream(response.getOutputStream());

            try {
                int read = 0;
                while ((read = ins.read(buffer)) != -1) {
                    outs.write(buffer, 0, read);
                }
                outs.close();
                ins.close();
            } catch (IOException e) {
                logger.info("$$$$$$$$$$$$$$$$$  : FILE DOWNLOAD ERROR : $$$$$$$$$$$$$$$$$$");
            } finally {
                if(outs!=null) outs.close();
                if(ins!=null) ins.close();
            }
        } else {
            logger.info("$$$$$$$$$$$$$$$$$  FILE DOWNLOAD ERROR : Not Server File.");
            throw new EgovBizException(message.getMessage("exception.notExistAttachFile"));
        }
    }

    /**
     * 파일 다운로드 처리 (파일정보를 map 객체로 전달받아 처리.)
     */
	private void procFileDownload(HttpServletRequest request, HttpServletResponse response, Map fileInfo) throws Exception {
        String saveFileNm  		= "";
        String serverPath 	= "";
        String orgFileNm   		= "";

        if (fileInfo != null) {
            saveFileNm = (String)fileInfo.get("fileServerNm");
            serverPath = (String)fileInfo.get("filePath");
            orgFileNm  = (String)fileInfo.get("fileOrginlNm");
        } else {
            logger.info("$$$$$$$$$$$$$$$$$  FILE DOWNLOAD ERROR : Not Server File.");
            throw new EgovBizException(message.getMessage("exception.notExistAttachFile"));
        }

        //실제 디렉토리
        String realDir= ApplicationProperty.get("upload.real.dir");

        //파일 풀경로 가져옴
        String fullFileName = realDir + serverPath;


        //파일을 orgFileNm의 이름으로 다운로드 함
        File f = new File(fullFileName);

        if (f.exists()) {
            logger.info("response charset : " + response.getCharacterEncoding());

            String mimetype = "application/x-msdownload";
            response.setContentType(mimetype);

            // 파일명 인코딩 처리
            FileUtils.setDisposition(orgFileNm, request, response);

            byte[] buffer = new byte[1024];
            BufferedInputStream ins = new BufferedInputStream(new FileInputStream(f));
            BufferedOutputStream outs = new BufferedOutputStream(response.getOutputStream());

            try {
                int read = 0;
                while ((read = ins.read(buffer)) != -1) {
                    outs.write(buffer, 0, read);
                }
                outs.close();
                ins.close();
            } catch (IOException e) {
                logger.info("$$$$$$$$$$$$$$$$$  : FILE DOWNLOAD ERROR : $$$$$$$$$$$$$$$$$$");
            } finally {
            	if(outs!=null) outs.close();
            	if(ins!=null) ins.close();
            }
        } else {
        	logger.info("$$$$$$$$$$$$$$$$$  FILE DOWNLOAD ERROR : Not Server File.");
        	throw new EgovBizException(message.getMessage("exception.notExistAttachFile"));
        }
    }

}

