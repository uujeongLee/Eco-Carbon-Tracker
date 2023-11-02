package common.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import commf.exception.BusinessException;
import common.util.CommUtils;
import common.util.FileUtils;
import common.util.properties.ApplicationProperty;

/**
 * Program Name 	: FileManager
 * Description 		: Common File Management
 * Programmer Name 	: ntarget
 * Creation Date 	: 2021-02-08
 * Used Table 		:
 */

@SuppressWarnings({"rawtypes", "unchecked"})
public class FileManager {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 첨부파일 업로드
     * 첨부파일 FORM Name : upfile로사용. ex) upfile1, upfile2 로 넘겨준다.
     * -- file[] 처럼 배열로 사용할경우 EgovFileMngUtil.java 참조.
     * 저장 디렉토리는 기본값을 사용
     *
     * @param request
     * @return
     * @throws IOException
     */
    public List<FileInfo> multiFileUpload(HttpServletRequest request) throws IOException {
        return multiFileUploadDetail(request, null);
    }

    /**
     * 첨부파일 업로드
     * 첨부파일 FORM Name : upfile로사용. ex) upfile1, upfile2 로 넘겨준다.
     * -- file[] 처럼 배열로 사용할경우 EgovFileMngUtil.java 참조.
     *
     * @param request
     * @param saveDir  파일 저장 디렉토리
     * @return
     * @throws IOException
     */
    public List<FileInfo> multiFileUpload(HttpServletRequest request, String saveDir) throws IOException {
        return multiFileUploadDetail(request, saveDir);
    }

    /**
     * 첨부파일 FORM 명을 원하는 대로 구성하여 처리 (단 name은 모두 달라야 함)
     * 저장 디렉토리는 기본값을 사용
     * @param request
     * @return
     * @throws IOException
     */
    public List<FileInfo> multiFileUploadArray(HttpServletRequest request) throws IOException {
        return multiFileUploadArrayDetail(request, null);
    }

    /**
     * 첨부파일 FORM 명을 원하는 대로 구성하여 처리 (단 name은 모두 달라야 함)
     * @param request
     * @param saveDir
     * @return
     * @throws IOException
     */
    public List<FileInfo> multiFileUploadArray(HttpServletRequest request, String saveDir) throws IOException {
        return multiFileUploadArrayDetail(request, saveDir);
    }


    /**
     * 첨부파일 업로드
     * 첨부파일 FORM Name : upfile로사용. ex) upfile1, upfile2 로 넘겨준다.
     * -- file[] 처럼 배열로 사용할경우 EgovFileMngUtil.java 참조.
     */
    private List<FileInfo> multiFileUploadDetail(HttpServletRequest request, String saveDir) throws IOException {
        List listFile = new ArrayList();

        MultipartHttpServletRequest multipartRequest = null;
        try {
            multipartRequest = (MultipartHttpServletRequest) request;
        } catch(ClassCastException e) {
            // ClassCastException이면 첨부파일 내용이 없는 것으로 확인하여 빈 객체를 리턴
            return listFile;
        }
        Map<String, MultipartFile> files = multipartRequest.getFileMap();
        
        String tempDir = saveDir;
        if(tempDir == null || tempDir.trim().length() == 0) {
            tempDir = ApplicationProperty.get("upload.temp.dir");
        }

        //디렉토리 생성
        FileUtils.makeDirectories(tempDir);
        
        // 파일이름이 중복되면 spring에서 에러가 나므로 각각 다른 이름으로 받음.
        // 저장 파일이 1개 일때와 여러개일때 구분
        if(files.size() == 1) {
        	int i = 0;
        	MultipartFile inVideo 	= files.get("upfile5");
        	
        	if(inVideo != null) {
                // 파일을 디렉토리에 저장후 파일정보 리턴
                Map fileInfo = getFileSaveNInfoMap(inVideo, tempDir, i);
                listFile.add(fileInfo);
        	}else {
        		String upfileNm 		= "upfile";
            	MultipartFile inFile 	= files.get(upfileNm);

                // 파일을 디렉토리에 저장후 파일정보 리턴
                Map fileInfo = getFileSaveNInfoMap(inFile, tempDir, i);
                listFile.add(fileInfo);
        	}
        	/*String upfileNm 		= "upfile";
        	MultipartFile inFile 	= files.get(upfileNm);

            // 파일을 디렉토리에 저장후 파일정보 리턴
            Map fileInfo = getFileSaveNInfoMap(inFile, tempDir, i);
            listFile.add(fileInfo);*/
        }else {
        	for (int i = 0; i < files.size(); i++) {
        		String upfileNm 		= "upfile" + i;
        		MultipartFile inFile 	= files.get(upfileNm);
        		String seqNo			= request.getParameter("seqNo"+i);
        		
        		// 파일을 디렉토리에 저장후 파일정보 리턴
        		if(seqNo != null) {
        			Map fileInfo = getFileSaveNInfoMapSeq(inFile, tempDir, i, seqNo);
        			listFile.add(fileInfo);
        		}else {
        			Map fileInfo = getFileSaveNInfoMap(inFile, tempDir, i);
            		listFile.add(fileInfo);
        		}
        	}
        }
        return listFile;
    }

    /**
     * 첨부파일 FORM 명을 원하는 대로 구성하여 처리 (단 name은 모두 달라야 함)
     * @param request
     * @return
     * @throws IOException
     */
    private List<FileInfo> multiFileUploadArrayDetail(HttpServletRequest request, String saveDir) throws IOException {
        List listFile = new ArrayList();
        MultipartHttpServletRequest multipartRequest = null;
        try {
            multipartRequest = (MultipartHttpServletRequest) request;
        } catch(ClassCastException e) {
            // ClassCastException이면 첨부파일 내용이 없는 것으로 확인하여 빈 객체를 리턴
            return listFile;
        }

        String tempDir = saveDir;
        if(tempDir == null || tempDir.trim().length() == 0) {
            tempDir = ApplicationProperty.get("upload.temp.dir");
        }

        //디렉토리 생성
        FileUtils.makeDirectories(tempDir);

        Iterator<String> iterator = multipartRequest.getFileNames();

        int i= 0;
        while(iterator.hasNext()) {
            MultipartFile inFile    = multipartRequest.getFile(iterator.next());

            // 파일을 디렉토리에 저장후 파일정보 리턴
            FileInfo fileInfo = getFileSaveNInfo(inFile, tempDir, i);
            listFile.add(fileInfo);

            i++;
        }

        return listFile;
    }

    /**
     * Multipart에서 파일 1개에 대해 지정된 디렉토리에 저장후 해당 파일 정보를 fileInfo으로 구성하여 리턴
     * @param inFile
     * @param saveDir
     * @param index
     * @return
     */
    private FileInfo getFileSaveNInfo(MultipartFile inFile, String saveDir, int index) {

        String saveFileNm = getFileName(saveDir, inFile.getOriginalFilename());
        FileInfo fileInfo = null;

        try {
            if (!saveFileNm.equals("")) {
                // 파일을 폴더에 저장함
                FileUtils.copyFile(inFile.getInputStream(), new FileOutputStream(saveDir + saveFileNm));

                fileInfo = new FileInfo().setFileName(saveFileNm)
                                         .setOrgFileName(CommUtils.nvlTrim(inFile.getOriginalFilename()))
                                         .setFileSize( String.valueOf(inFile.getSize()))
                                         .setPath(saveDir)
                                         .setType(inFile.getContentType())
                                         .setIndex(index);
                                         ;
            }
            return fileInfo;

        } catch (FileNotFoundException e) {
            throw new BusinessException(e);
        } catch (IOException e) {
            throw new BusinessException(e);
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }
    /**
     * Multipart에서 파일 1개에 대해 지정된 디렉토리에 저장후 해당 파일 정보를 Map으로 구성하여 리턴
     * @param inFile
     * @param saveDir
     * @param index
     * @return
     */
    private Map getFileSaveNInfoMap(MultipartFile inFile, String saveDir, int index) {

        String saveFileNm = getFileName(saveDir, inFile.getOriginalFilename());
        Map fileMap = new HashMap();

        try {
            if (!saveFileNm.equals("")) {
                // 파일을 폴더에 저장함
                FileUtils.copyFile(inFile.getInputStream(), new FileOutputStream(saveDir + saveFileNm));

                fileMap.put("fileSvrNm", saveFileNm); 
                fileMap.put("fileOrgNm", CommUtils.nvlTrim(inFile.getOriginalFilename())); 
                fileMap.put("tempDir", ApplicationProperty.get("upload.temp.dir")); 
                fileMap.put("fileSize", inFile.getSize()); 
                fileMap.put("saveDir", saveDir); 
                fileMap.put("atchmnflTy", inFile.getContentType()); 
                fileMap.put("idx", index); 
                		
                
            }
            return fileMap;

        } catch (FileNotFoundException e) {
            throw new BusinessException(e);
        } catch (IOException e) {
            throw new BusinessException(e);
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }
    
    
    
    
    

    /**
     * 서버에 저장될 파일명 생성 가져오기
     */
    public String getFileName(String dir, String originalFileName) {
        if (CommUtils.nvlTrim(originalFileName).equals(""))
            return "";

        String dotextension = originalFileName.substring(originalFileName.lastIndexOf("."));
        java.io.File currentPath = new java.io.File(dir);
        String[] fileList = null;

        SecureRandom random = new SecureRandom();
        FileNameFilter fileNameFilter = new FileNameFilter();

        StringBuffer sb = null;
        do {
            sb = new StringBuffer();
            sb.append(String.valueOf(System.currentTimeMillis()));
            sb.append(String.valueOf(random.nextLong()));
            sb.append(dotextension);
            fileNameFilter.setFileName(sb.toString());

            fileList = currentPath.list(fileNameFilter);
        } while (fileList.length > 0);

        return sb.toString();
    }

    static class FileNameFilter implements FilenameFilter {
        String sFileName = null;

        public void setFileName(String sFileName) {
            this.sFileName = sFileName;
        }

        public boolean accept(java.io.File directory, String name) {
            if (name.equals(sFileName)) {
                return true;
            }
            return false;
        }
    }

    /**
     * 파일 다운로드 처리 (파일정보를 map 객체로 전달받아 처리)
     * @param request
     * @param response
     * @param fileInfo
     * @throws Exception
     */
    public void procFileDownload(HttpServletRequest request, HttpServletResponse response, Map fileInfo) throws Exception {
        String saveFileNm = "";
        String serverPath = "";
        String orgFileNm  = "";

        if(fileInfo != null) {
            saveFileNm = (String)fileInfo.get("fileServerNm");
            serverPath = (String)fileInfo.get("filePath");
            orgFileNm  = (String)fileInfo.get("fileOrginlNm");
        }
        else {
            logger.info("### FILE DOWNLOAD ERROR : Not server File.");
            throw new Exception("### FILE DOWNLOAD ERROR");
        }

        // 파일 풀경로
        String fullFileName = serverPath + saveFileNm;

        // 파일명 orgFileNm의 이름으로 다운로드 함
        File f = new File(fullFileName);

        if(f.exists()) {
            logger.info("response charset : " +  response.getCharacterEncoding());

            String mimetype = "application/x-msdownload";
            response.setContentType(mimetype);

            // 파일명 인코딩
            FileUtils.setDisposition(orgFileNm, request, response);

            byte[] buffer = new byte[1024];
            BufferedInputStream ins = new BufferedInputStream(new FileInputStream(f));
            BufferedOutputStream outs = new BufferedOutputStream(response.getOutputStream());

            try {
                int read = 0;
                while((read = ins.read(buffer)) != -1) {
                    outs.write(buffer, 0, read);
                }
                outs.close();
                ins.close();
            }catch(IOException e) {
                logger.info("### FILE DOWNLOAD ERROR");
            }finally {
                if(outs!=null) outs.close();
                if(ins!=null) ins.close();
            }
        }
        else {
            logger.info("### FILE DOWNLOAD ERROR");
            throw new Exception("### FILE DOWNLOAD ERROR");
        }
    }
    
    private Map getFileSaveNInfoMapSeq(MultipartFile inFile, String saveDir, int index, String seqNo) {

        String saveFileNm = getFileName(saveDir, inFile.getOriginalFilename());
        Map fileMap = new HashMap();

        try {
            if (!saveFileNm.equals("")) {
                // 파일을 폴더에 저장함
                FileUtils.copyFile(inFile.getInputStream(), new FileOutputStream(saveDir + saveFileNm));

                fileMap.put("fileSvrNm", saveFileNm); 
                fileMap.put("fileOrgNm", CommUtils.nvlTrim(inFile.getOriginalFilename())); 
                fileMap.put("tempDir", ApplicationProperty.get("upload.temp.dir")); 
                fileMap.put("fileSize", inFile.getSize()); 
                fileMap.put("saveDir", saveDir); 
                fileMap.put("atchmnflTy", inFile.getContentType()); 
                fileMap.put("idx", index); 
                fileMap.put("seq", seqNo); 		
                
            }
            return fileMap;

        } catch (FileNotFoundException e) {
            throw new BusinessException(e);
        } catch (IOException e) {
            throw new BusinessException(e);
        } catch (Exception e) {
            throw new BusinessException(e);
        }
    }
    
    public List<FileInfo> modifyMultiFileUpload(HttpServletRequest request) throws IOException {
        return modifyMultiFileUploadDetail(request, null);
    }
    
    private List<FileInfo> modifyMultiFileUploadDetail(HttpServletRequest request, String saveDir) throws IOException {
        List listFile = new ArrayList();

        MultipartHttpServletRequest multipartRequest = null;
        try {
            multipartRequest = (MultipartHttpServletRequest) request;
        } catch(ClassCastException e) {
            // ClassCastException이면 첨부파일 내용이 없는 것으로 확인하여 빈 객체를 리턴
            return listFile;
        }
        Map<String, MultipartFile> files = multipartRequest.getFileMap();
        
        String tempDir = saveDir;
        if(tempDir == null || tempDir.trim().length() == 0) {
            tempDir = ApplicationProperty.get("upload.temp.dir");
        }

        //디렉토리 생성
        FileUtils.makeDirectories(tempDir);

        // 파일이름이 중복되면 spring에서 에러가 나므로 각각 다른 이름으로 받음.
        // 저장 파일이 1개 일때와 여러개일때 구분       
        	for (int i = 0; i < 6; i++) {
        		String upfileNm 		= "upfile" + i;
        		MultipartFile inFile 	= files.get(upfileNm);
        		if(inFile == null) {
        			continue;
        		}else {
        			String seqNo			= request.getParameter("seqNo"+i);
        			System.out.println(seqNo);
            		// 파일을 디렉토리에 저장후 파일정보 리턴
            		if(seqNo != "") {
            			Map fileInfo = getFileSaveNInfoMapSeq(inFile, tempDir, i, seqNo);
            			listFile.add(fileInfo);
            		}else {
            			Map fileInfo = getFileSaveNInfoMap(inFile, tempDir, i);
                		listFile.add(fileInfo);
            		}
        		}
        	}       
        return listFile;
    }
    
    public List<FileInfo> multiFileUploadFax(HttpServletRequest request) throws IOException {
        return multiFileUploadDetailFax(request, null);
    }
    
    private List<FileInfo> multiFileUploadDetailFax(HttpServletRequest request, String saveDir) throws IOException {
        List listFile = new ArrayList();

        MultipartHttpServletRequest multipartRequest = null;
        try {
            multipartRequest = (MultipartHttpServletRequest) request;
        } catch(ClassCastException e) {
            // ClassCastException이면 첨부파일 내용이 없는 것으로 확인하여 빈 객체를 리턴
            return listFile;
        }
        Map<String, MultipartFile> files = multipartRequest.getFileMap();
        
        String tempDir = saveDir;
        if(tempDir == null || tempDir.trim().length() == 0) {
            tempDir = ApplicationProperty.get("upload.temp.dir");
        }

        //디렉토리 생성
        FileUtils.makeDirectories(tempDir);

        // 파일이름이 중복되면 spring에서 에러가 나므로 각각 다른 이름으로 받음.
        // 저장 파일이 1개 일때와 여러개일때 구분
        if(files.size() == 1) {
        	int i = 0;       	
        	String upfileNm 		= "upfile0";
        	MultipartFile inFile 	= files.get(upfileNm);

            // 파일을 디렉토리에 저장후 파일정보 리턴
            Map fileInfo = getFileSaveNInfoMap(inFile, tempDir, i);
            listFile.add(fileInfo);
        }else {
        	for (int i = 0; i < files.size(); i++) {
        		String upfileNm 		= "upfile" + i;
        		MultipartFile inFile 	= files.get(upfileNm);
        		
        		String seqNo			= request.getParameter("seqNo"+i);
        		
        		// 파일을 디렉토리에 저장후 파일정보 리턴
        		if(seqNo != null && seqNo != "") {
        			Map fileInfo = getFileSaveNInfoMapSeq(inFile, tempDir, i, seqNo);
        			listFile.add(fileInfo);
        		}else {
        			Map fileInfo = getFileSaveNInfoMap(inFile, tempDir, i);
            		listFile.add(fileInfo);
        		}
        	}
        }
        return listFile;
    }
}

