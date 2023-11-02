package common.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;

import commf.exception.BusinessException;

/**
 * Program Name 	: FileUtil
 * Description 		: 파일 처리 관련된 유틸리티 API.
 * Programmer Name 	: ntarget
 * Creation Date 	: 2021-02-08
 * Used Table 		:
 */

public class FileUtils {
    /**
     * Logging output for this class.
     */
    protected static Log log = LogFactory.getLog(FileUtils.class);

    public static final int BUFFER_SIZE = 4096;

    /**
     * path 파라미터의 파일이나 디렉터리가 없으면, 해당 디렉터리를 생성한다.
     * 만일 파일이나 디렉터리가 존재한다면 false를 반환한다.
     *
     * @param path
     *         	생성할 디렉터리 위치
     * @return boolean
     * 			<code>true</code> 성공적으로 디렉터리를 생성한 경우.
     *         	<code>false</code> 디렉터리를 생성하지 않은 경우.
     */
    public static boolean makeDirectories(String path) {
        if ( StringUtils.isBlank(path)) {
            throw new BusinessException("Given path parameter is blank. Thus can't make directory.");
        }

        File f = new File(path);

        if (f.exists()) {
            return false;
        } else {
            if (log.isDebugEnabled()) {
                log.debug(" Path does not exist on the file system. Creating folders...");
            }
            f.mkdirs();
            return true;

            /*
            f.setExecutable(false, true);
            f.setReadable(true);
            f.setWritable(false, true);

            if (log.isDebugEnabled()) {
                log.debug(" Path does not exist on the file system. Creating folders...");
            }

            return f.mkdirs();
            */
        }
    }

    /**
     * 파일시스템에서 지정된 디렉터리를 삭제한다.
     *
     * @param path
     *            삭제한 디렉터리
     * @todo implement flag
     */
    public static void removeDirectories(String path, boolean removeWithContents) {
        if (log.isDebugEnabled()) {
            log.debug(" Attempting to remove folders for path: " + path);
        }
        if (StringUtils.isBlank(path)) {
            throw new BusinessException("Given path is blank.");
        }

        File f = new File(path);

        try {
            org.apache.commons.io.FileUtils.deleteDirectory(f);
        } catch (IOException e) {
            throw new BusinessException(e);
        }
    }

    /**
     * 지정된 파일의 위치를 옮긴다.
     *
     * @param fromFile 원본 위치
     * @param toFile 대상 위치
     * @throws Exception
     */
    public static void moveFile(String fromFile, String toFile) throws Exception {
        if( StringUtils.isNotBlank(toFile)){
            String replacedPath = replacePathToSlash(toFile);
            makeDirectories(replacedPath.substring(0, replacedPath.lastIndexOf("/")));
        }
        else{
            throw new BusinessException("Given target file path is blank. Thus can't move source file.");
        }

        copyFile(fromFile, toFile);
        deleteFile(fromFile);
    }

    /**
     * 파일을 복사한다. 대상 파일이 이미 존재하는 경우, 런타임 예외를 발생시킨다.
     *
     * @param fromFile 원본 파일
     * @param toFile 대상 파일
     * @throws Exception
     * @throws Exception
     */
    public static void copyFile(String fromFile, String toFile) throws Exception {

        // retrieve the file data
        FileInputStream fis  = null;
        FileOutputStream fos = null;

        try {
            if (new File(toFile).exists()) { // 대상 파일이 이미 존재하면 예외 처리
                throw new BusinessException("Given target file exist already. : " + toFile);
            }

            fis = new FileInputStream(fromFile);
            fos = new FileOutputStream(toFile);

            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = 0;

//            while ((bytesRead = fis.read(buffer, 0, BUFFER_SIZE)) != -1) {
//                fos.write(buffer, 0, bytesRead);
//            }
            while (true) {
                bytesRead = fis.read(buffer, 0, BUFFER_SIZE);
                if (bytesRead == -1) {
                   break;
                }
                fos.write(buffer, 0, bytesRead);
            }

            //close the stream
            fis.close();
            fos.close();
        } catch (FileNotFoundException fnfe) {
            throw new BusinessException(fnfe);
        } catch (Exception ioe) {
            throw new BusinessException(ioe);
        } finally {
            if (fis != null) fis.close();
            if (fos != null) fos.close();
        }
    }

    /**
     * 파일을 복사한다.
     *
     * @param in byte[] 복사할 원본의 바이너리
     * @param outPathName String 목표 파일명
     * @throws IOException
     */
    public static void copyFile(byte[] in, String outPathName) throws IOException {
        Assert.notNull(in, "No input byte array specified");
        File out = new File(outPathName);
        if (out.exists()) {
            throw new BusinessException("Given target file exist already. : " + outPathName);
        }
        copyFile(in, out);
    }

    /**
     * 파일을 복사한다.
     *
     * @param in byte[]
     * @param out File
     * @throws IOException
     */
    public static void copyFile(byte[] in, File out) throws IOException {
        Assert.notNull(in, "No input byte array specified");
        Assert.notNull(out, "No output File specified");
        ByteArrayInputStream inStream = new ByteArrayInputStream(in);

        String replacedPath = replacePathToSlash(out.getPath());
        makeDirectories(replacedPath.substring(0, replacedPath.lastIndexOf("/")));

        OutputStream outStream = new BufferedOutputStream(new FileOutputStream(out));
        copyFile(inStream, outStream);
    }

    /**
     * 파일을 복사한다.
     *
     * @param in InputStream
     * @param out OutputStream
     * @return
     * @throws IOException
     */
    public static int copyFile(InputStream in, OutputStream out) throws IOException {
        Assert.notNull(in, "No InputStream specified");
        Assert.notNull(out, "No OutputStream specified");
        try {
            int byteCount = 0;
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = -1;
//			while ((bytesRead = in.read(buffer)) != -1) {
//				out.write(buffer, 0, bytesRead);
//				byteCount += bytesRead;
//			}
            while (true) {
                bytesRead = in.read(buffer);
                if(bytesRead == -1 ) {
                    break;
                }
                out.write(buffer, 0, bytesRead);
                byteCount += bytesRead;
            }
            out.flush();
            return byteCount;
        }
        finally {
            try {
                in.close();
            }
            catch (IOException ex) {
                log.warn("Could not close InputStream", ex);
            }
            try {
                out.close();
            }
            catch (IOException ex) {
                log.warn("Could not close OutputStream", ex);
            }
        }
    }

    /**
     * 지정된 위치의 파일을 byte[]로 반환한다.
     *
     * @param fullFilePath
     * @return
     */
    public static byte[] getFileToByteArray(String fullFilePath) throws IOException {
        Assert.notNull(fullFilePath, "No input byte array specified");

        FileInputStream fis = null;
        byte[] out = null;

        try {
            fis = new FileInputStream(fullFilePath);
            out = new byte[fis.available()];
            fis.read(out);

            fis.close();
        } catch (FileNotFoundException e) {
            throw new BusinessException(e);
        } catch (IOException e) {
            throw new BusinessException(e);
        } finally {
            if(fis!=null) fis.close();
        }
        return out;
    }

    /**
     * 지정된 파일을 삭제한다.
     * @param fullFilePath 파일 위치 문자열
     * @return true이면 제거 성공, false이면 실패
     */
    public static boolean deleteFile(String fullFilePath) {
        File file = new File(fullFilePath);
        boolean retn = false;
        try {
            if (file.exists()) {
                retn = file.delete();
            }
            else{
                log.debug("Given path's file do not exist. : " + fullFilePath);
            }
        } catch (SecurityException e) {
            throw new BusinessException(e);
        }
        return retn;
    }

    /**
     * 파일이나 디렉터리 명을 "/"(슬래시)기반으로 변경하여 반환.
     *
     * @param path 변경할 패스 문자열
     * @return
     */
    public static String replacePathToSlash(String path){
        return (StringUtils.isBlank(path)) ?
                path :
                path.replaceAll("[\\\\]+", "/").replaceAll("[/]{2,}", "/");
    }

    /**
     * 파일 Disposition 지정하기.
     */
    public static void setDisposition(String filename, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String browser = getBrowser(request);

        String dispositionPrefix = "attachment; filename=";
        String encodedFilename = null;

        if (browser.equals("MSIE")) {
            encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
        } else if (browser.equals("Trident")) { // IE11 문자열 깨짐 방지
            encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
        } else if (browser.equals("Firefox")) {
            encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
        } else if (browser.equals("Opera")) {
            encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
        } else if (browser.equals("Chrome")) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < filename.length(); i++) {
                char c = filename.charAt(i);
                if (c > '~') {
                    sb.append(URLEncoder.encode("" + c, "UTF-8"));
                } else {
                    sb.append(c);
                }
            }
            encodedFilename = sb.toString();
        } else {
            throw new BusinessException("Not supported browser");
        }

        response.setHeader("Content-Disposition", dispositionPrefix + encodedFilename);

        if ("Opera".equals(browser)) {
            response.setContentType("application/octet-stream;charset=UTF-8");
        }
    }

    /**
     * 브라우저 구분 얻기.
     */
    private static String getBrowser(HttpServletRequest request) {
        String header = request.getHeader("User-Agent");
        if (header.indexOf("MSIE") > -1) {
            return "MSIE";
        } else if (header.indexOf("Trident") > -1) { // IE11 문자열 깨짐 방지
            return "Trident";
        } else if (header.indexOf("Chrome") > -1) {
            return "Chrome";
        } else if (header.indexOf("Opera") > -1) {
            return "Opera";
        }
        return "Firefox";
    }

    // 첨부파일 확장자 가능 체크
    public static boolean isAtthAllowedFileType(String fileNm, String allowedExtNms) {
        String extNm = fileNm.substring(fileNm.toLowerCase().lastIndexOf(".") + 1);

        extNm = extNm.toLowerCase();

        if (allowedExtNms.indexOf(extNm) >= 0){
            return true;
        }

        return false;
    }

    public static String getFileExt( final String fileName ) {
        int index = fileName.lastIndexOf(".");

        if (index == -1) {
            return "";
        }
        else {
            return fileName.substring(index+1, fileName.length());
        }
    }
}