package egovframework.com.cmm.service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import egovframework.com.cmm.EgovWebUtil;
import egovframework.com.cmm.util.EgovResourceCloseHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class EgovProperties {

    private static final Logger LOGGER = LoggerFactory.getLogger(EgovProperties.class);

    //파일구분자
    final static  String FILE_SEPARATOR = System.getProperty("file.separator");

    //프로퍼티 파일의 물리적 위치
    //public static final String GLOBALS_PROPERTIES_FILE = System.getProperty("user.home") + FILE_SEPARATOR + "egovProps" +FILE_SEPARATOR + "globals.properties";

    //public static final String RELATIVE_PATH_PREFIX = EgovProperties.class.getResource("").getPath().substring(0, EgovProperties.class.getResource("").getPath().lastIndexOf("com"));
    //public static final String RELATIVE_PATH_PREFIX = EgovProperties.class.getProtectionDomain().getCodeSource().getLocation().getPath().substring(0,EgovProperties.class.getProtectionDomain().getCodeSource().getLocation().getPath().indexOf("WEB-INF/classes/")+"WEB-INF/classes/".length())+"egovframework/";

    //public static final String GLOBALS_PROPERTIES_FILE = RELATIVE_PATH_PREFIX + "egovProps" + FILE_SEPARATOR + "globals.properties";

    public static final String RELATIVE_PATH_PREFIX = "/config/";
    public static final String GLOBALS_PROPERTIES_FILE = RELATIVE_PATH_PREFIX + "globals.properties";

    /**
     * 인자로 주어진 문자열을 Key값으로 하는 상대경로 프로퍼티 값을 절대경로로 반환한다(Globals.java 전용)
     * @param keyName String
     * @return String
     */
    public static String getPathProperty(String keyName) {
        String value = "";

        LOGGER.debug("getPathProperty : {} = {}", GLOBALS_PROPERTIES_FILE, keyName);

        FileInputStream fis = null;
        try {
            Properties props = new Properties();

            fis = new FileInputStream(EgovWebUtil.filePathBlackList(GLOBALS_PROPERTIES_FILE));
            props.load(new BufferedInputStream(fis));

            value = props.getProperty(keyName);
            value = (value == null) ? "" : value.trim();//KISA 보안약점 조치 (2018-10-29, 윤창원)
            //value = RELATIVE_PATH_PREFIX + "egovProps" + System.getProperty("file.separator") + value;
            value = RELATIVE_PATH_PREFIX + System.getProperty("file.separator") + value;
        } catch (FileNotFoundException fne) {
            LOGGER.debug("Property file not found.", fne);
            throw new RuntimeException("Property file not found", fne);
        } catch (IOException ioe) {
            LOGGER.debug("Property file IO exception", ioe);
            throw new RuntimeException("Property file IO exception", ioe);
        } finally {
            EgovResourceCloseHelper.close(fis);
        }

        return value;
    }

    /**
     * 인자로 주어진 문자열을 Key값으로 하는 프로퍼티 값을 반환한다(Globals.java 전용)
     * @param keyName String
     * @return String
     */
    public static String getProperty(String keyName) {
        String value = "";

        LOGGER.debug("===>>> getProperty"+EgovProperties.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        LOGGER.debug("getProperty : {} = {}", GLOBALS_PROPERTIES_FILE, keyName);

        FileInputStream fis = null;
        try {
            Properties props = new Properties();

            fis = new FileInputStream(EgovWebUtil.filePathBlackList(GLOBALS_PROPERTIES_FILE));

            props.load(new BufferedInputStream(fis));
            if (props.getProperty(keyName) == null) {
                return "";
            }
            value = props.getProperty(keyName).trim();
        } catch (FileNotFoundException fne) {
            LOGGER.debug("Property file not found.", fne);
            throw new RuntimeException("Property file not found", fne);
        } catch (IOException ioe) {
            LOGGER.debug("Property file IO exception", ioe);
            throw new RuntimeException("Property file IO exception", ioe);
        } finally {
            EgovResourceCloseHelper.close(fis);
        }

        return value;
    }

    /**
     * 주어진 파일에서 인자로 주어진 문자열을 Key값으로 하는 프로퍼티 상대 경로값을 절대 경로값으로 반환한다
     * @param fileName String
     * @param key String
     * @return String
     */
    public static String getPathProperty(String fileName, String key) {
        FileInputStream fis = null;
        try {
            Properties props = new Properties();

            fis = new FileInputStream(EgovWebUtil.filePathBlackList(fileName));
            props.load(new BufferedInputStream(fis));
            fis.close();

            String value = props.getProperty(key);
            //value = RELATIVE_PATH_PREFIX + "egovProps" + System.getProperty("file.separator") + value;
            value = RELATIVE_PATH_PREFIX + System.getProperty("file.separator") + value;

            return value;
        } catch (FileNotFoundException fne) {
            LOGGER.debug("Property file not found.", fne);
            throw new RuntimeException("Property file not found", fne);
        } catch (IOException ioe) {
            LOGGER.debug("Property file IO exception", ioe);
            throw new RuntimeException("Property file IO exception", ioe);
        } finally {
            EgovResourceCloseHelper.close(fis);
        }
    }

    /**
     * 주어진 파일에서 인자로 주어진 문자열을 Key값으로 하는 프로퍼티 값을 반환한다
     * @param fileName String
     * @param key String
     * @return String
     */
    public static String getProperty(String fileName, String key) {
        FileInputStream fis = null;
        try {
            Properties props = new Properties();

            fis = new FileInputStream(EgovWebUtil.filePathBlackList(fileName));
            props.load(new BufferedInputStream(fis));
            fis.close();

            String value = props.getProperty(key);

            return value;
        } catch (FileNotFoundException fne) {
            LOGGER.debug("Property file not found.", fne);
            throw new RuntimeException("Property file not found", fne);
        } catch (IOException ioe) {
            LOGGER.debug("Property file IO exception", ioe);
            throw new RuntimeException("Property file IO exception", ioe);
        } finally {
            EgovResourceCloseHelper.close(fis);
        }
    }

    /**
     * 주어진 프로파일의 내용을 파싱하여 (key-value) 형태의 구조체 배열을 반환한다.
     * @param property String
     * @return ArrayList
     */
    public static ArrayList<Map<String, String>> loadPropertyFile(String property) {

        // key - value 형태로 된 배열 결과
        ArrayList<Map<String, String>> keyList = new ArrayList<Map<String, String>>();

        String src = property.replace('\\', File.separatorChar).replace('/', File.separatorChar);
        FileInputStream fis = null;
        try {

            File srcFile = new File(EgovWebUtil.filePathBlackList(src));
            if (srcFile.exists()) {

                Properties props = new Properties();
                fis = new FileInputStream(src);
                props.load(new BufferedInputStream(fis));
                fis.close();

                Enumeration<?> plist = props.propertyNames();
                if (plist != null) {
                    while (plist.hasMoreElements()) {
                        Map<String, String> map = new HashMap<String, String>();
                        String key = (String) plist.nextElement();
                        map.put(key, props.getProperty(key));
                        keyList.add(map);
                    }
                }
            }
        } catch (IOException ex) {
            LOGGER.debug("IO Exception", ex);
            throw new RuntimeException(ex);
        } finally {
            EgovResourceCloseHelper.close(fis);
        }

        return keyList;
    }
}
