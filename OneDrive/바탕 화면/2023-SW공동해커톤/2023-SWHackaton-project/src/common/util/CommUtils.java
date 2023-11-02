package common.util;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

/**
 * Program Name 	: CommUtils
 * Description 		:
 * Programmer Name 	: ntarget
 * Creation Date 	: 2022-09-26
 * Used Table 		:
 */

@SuppressWarnings("all")
public class CommUtils {

	public static boolean isEmpty(String str) {
		if (str == null || str.length() == 0)
			return true;
		return false;
	}
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}
	public static boolean isEmptyList(List list) {
		if (list == null || list.size() == 0)
			return true;
		return false;
	}
	public static boolean isNotEmptyList(List list) {
		return !isEmptyList(list);
	}

    public static boolean isEqual(String str1, String str2) {
        if (str1 == null && str2 == null)
            return true;
        if (str1 == null || str2 == null)
            return false;
        if (str1.equals(str2))
            return true;
        return false;
    }

	// Current Year (Param : Calendar )
	public static int getYear(Calendar cal) {
		int res = 0;
		if (cal != null)
			res = cal.get(Calendar.YEAR);
		return res;
	}

	// get month of year from calendar.
	public static int getMonth(Calendar cal) {
		int res = 0;
		if (cal != null)
			res = cal.get(Calendar.MONTH) + 1;
		return res;
	}

    // get day of month from calendar.
    public static int getDay(Calendar cal) {
        int res = 0;
        if (cal != null)
            res = cal.get(Calendar.DATE);
        return res;
    }

	// change null string to blank string //
	public static String nvl(String str) {
		String value = "";
		if (str != null && str.length() > 0) {
			value = str;
		}
		return value;
	}

	public static String nvlTrim(String str) {
		return nvl(str).trim();
	}

	public static String nvlTrim(String str, String defaultStr) {
		String rtn = "";
		if (nvl(str).trim().equals(""))
			rtn = defaultStr;
		else
			rtn = nvl(str).trim();

		return rtn;
	}

	public static String replace(String src, String find, String rep) {
		if (src == null)
			return null;
		if (find == null)
			return src;
		if (rep == null)
			rep = "";
		StringBuffer res = new StringBuffer();
		String sp[] = split(src, find);
		res.append(sp[0]);
		for (int i = 1; i < sp.length; i++) {
			res.append(rep);
			res.append(sp[i]);
		}
		return res.toString();
	}

	public static String[] split(String src, String delim) {
		if (src == null || delim == null)
			return null;
		ArrayList list = new ArrayList();
		int start = 0, last = 0;
		String term;
		while ((start = src.indexOf(delim, last)) > -1) {
			term = src.substring(last, start);
			list.add(term);
			last = start + delim.length();
		}
		term = src.substring(last, src.length());
		list.add(term);
		String[] res = new String[list.size()];
		list.toArray(res);
		return res;
	}

	public static String toLower(String str) {
		if (str == null)
			return null;
		return str.toLowerCase();
	}

	public static String toUpper(String str) {
		if (str == null)
			return null;
		return str.toUpperCase();
	}

	// 날짜 가져오기 (한국)
	public static String getToday(String gubun) {
		java.util.Date cdCurrent     = new java.util.Date();
		SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyyMMdd");
		String currDate = dateFormat2.format(cdCurrent);

		String strYear = currDate.substring(0,4);
		String strMonth = currDate.substring(4,6);;
		String strDay = currDate.substring(6,8);

		String strToday = getDateFmt(strYear+strMonth+strDay, gubun);

		return strToday;
	}

	// 날짜 구분
    public static String getDateFmt(String dt, String gubun) {
        String strYear = dt.substring(0, 4);
        String strMonth = dt.substring(4, 6);
        String strDay = dt.substring(6, 8);

        String strDate = strYear + gubun + strMonth + gubun + strDay;

        return strDate;
    }

    public static int strToInt(String str) {
        return strToInt(str, 0);
    }

    // converts string value to integer value. //
    public static int strToInt(String str, int defaultValue) {
        int res = 0;
        try {
            if (str == null || str.length() <= 0) {
                res = defaultValue;
            } else {
                res = Integer.parseInt(delNonNum(nvlTrim(str, "0")));
            }
        } catch (Exception e) {
            res = 0;
        }
        return res;
    }

    // remove non-numeric char from a string.
    public static String delNonNum(String str) {
        String s = "";
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c >= '0' && c <= '9') {
                s += c;
            }
        }
        return s;
    }

    public static Calendar getCurCalendar() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        return (cal);
    }

    // Current Year
    public static int getCurrYear() {
        return getYear(getCurCalendar());
    }
    // Current Year
    public static int getCurrMonth() {
        return getMonth(getCurCalendar());
    }
    // Current Day
    public static int getCurrDay() {
        return getDay(getCurCalendar());
    }
    // Current Date YYYY-MM-DD format Return
    public static String getCurDate() {
        return formatDate("yyyy-MM-dd", getCurCalendar());
    }
    // Current Date YYYYMMDD format Return
    public static String getCurDateString() {
        return formatDate("yyyyMMdd", getCurCalendar());
    }

    // Current Date YYYY-MM-DD HH24:MI:SS format Return
    public static String getCurrDateTime() {
        return formatDate("yyyy-MM-dd HH:mm:ss", getCurCalendar());
    }

    // Current Date YYYY-MM-DD HH24:MI:SS format Return
    public static String getCurrDateTime2() {
        return formatDate("yyyyMMddHHmmss", getCurCalendar());
    }
    
    public static String getCurrTimeStamp() {
        return formatDate("yyyy-MM-dd HH:mm:ss.SSS", getCurCalendar());
    }
    public static String getCurrTimeStampString() {
        return formatDate("yyyyMMddHHmmssSSS", getCurCalendar());
    }
    
    public static String formatDate(String pattern, Calendar cal) {
        SimpleDateFormat f = new SimpleDateFormat(pattern, Locale.KOREA);
        return f.format(cal.getTime());
    }
    public static String formatCurDate(String pattern) {
        return formatDate(pattern, getCurCalendar());
    }

    // substring(start, end) -> null check
    public static String substring (String str, int start, int end) {
        String rtnValue = "";

        str = nvlTrim(str);

        if (str.length() >= end) {
            rtnValue = str.substring(start,end);
        }

        return rtnValue;
    }

    // substring(start) -> null check
    public static String substring (String str, int start) {
        String rtnValue = "";

        str = nvlTrim(str);

        if (str.length() >= start) {
            rtnValue = str.substring(start);
        }

        return rtnValue;
    }

    // Map 객체에 put
    public static Map putMap(Map map, String key, Object value) {
        Map rtnMap = map;
        if(rtnMap == null) {
            rtnMap = new HashMap();
        }
        rtnMap.put(key, value);
        return rtnMap;
    }


    /**
     * 큰 실수 값을 반환한다.
     *
     * @param key 키
     * @param defaultValue 기본 값
     * @return 값
     */
    public static double getDouble(Object value, double defaultValue) {

        if (value == null) {
            return defaultValue;
        }
        if (value.toString().trim().length() == 0) {
            return defaultValue;
        }

        if(value instanceof Number)
        	return ((Number)value).doubleValue();
       	else
       		return Double.parseDouble(value.toString().trim());
    }


    /**
     * 실수 값을 반환한다.
     *
     * @param key 키
     * @param defaultValue 기본 값
     * @return 값
     */
    public static float getFloat(Object value, float defaultValue) {

        if (value == null) {
            return defaultValue;
        }
        if (value.toString().trim().length() == 0) {
            return defaultValue;
        }

        return Float.parseFloat(value.toString().trim());
    }

    /**
     * 정수 값을 반환한다.
     *
     * @param key 키
     * @param defaultValue 기본 값
     * @return 값
     */
    public static int getInt(Object value, int defaultValue) {

        if (value == null) {
            return defaultValue;
        }
        if (value.toString().trim().length() == 0) {
            return defaultValue;
        }
        String str = replace(value.toString().trim(), ",", "");
        return Integer.parseInt(str, 10);
    }



    /**
     * 큰 정수 값을 반환한다.
     *
     * @param key 키
     * @param defaultValue 기본 값
     * @return 값
     */
    public static long getLong(Object value, long defaultValue) {

        if (value == null) {
            return defaultValue;
        }
        if (value.toString().trim().length() == 0) {
            return defaultValue;
        }
        String str = replace(value.toString().trim(), ",", "");
        return Long.parseLong(str, 10);
    }

    public static boolean getBoolean(Object value, boolean defaultValue) {

        if (value == null) {
            return defaultValue;
        }
        if (String.valueOf(value).trim().length() == 0) {
            return defaultValue;
        }
        return Boolean.valueOf(String.valueOf(value)).booleanValue();
    }

    /**
     * 문자 값을 반환한다.
     *
     * @param key 키
     * @param defaultValue 기본 값
     * @return 값
     */
    public static String getString(Object value, String defaultValue) {

        if (value == null) {
            return defaultValue;
        }
        if (value.toString().trim().length() == 0) {
            return defaultValue;
        }
        return value.toString().trim();
    }
    
    public static double getDouble(Map map, Object key) {
        return getDouble(map.get(key), 0.0D);
    }
    public static float getFloat(Map map, Object key) {
        return getFloat(map.get(key), 0.0F);
    }
    public static int getInt(Map map, Object key) {
        return getInt(map.get(key), 0);
    }
    public static long getLong(Map map, Object key) {
        return getLong(map.get(key), 0L);
    }
    public static boolean getBoolean(Map map, Object key) {
        return getBoolean(map.get(key), false);
    }
    public static String getStr(Map map, Object key) {
    	Object value = map.get(key);
        if (value == null)
            return null;
        return value.toString();
    }
    public static String getStrNvl(Map map, Object key) {
    	return nvl(getStr(map, key));
    }
    
    /**
     * 2021.10.02 LSH 경로HTML 구성 함수
     * f.tld에 정의하여 사용됨.
     * 
     * @param path 전체 경로 문자열 (메뉴1,메뉴2)
     * @param delim 메뉴별 구분문자 (,)
     * @param sep   변환된 구분문자 (>)
     * @return HTML로 변환된 문자열
     */
    public static String getPathHtml(String path, String delim, String sep) {
    	
    	if (path == null)
    		return "";
    	
    	String[] arr = path.split(delim);
    	
    	String html = "";
    	
    	for (int i = 0; i < arr.length; i++) {
    		html += "<li> "+ arr[i] + " </li>";
    		
    		if (sep != null) {
	    		if (i < (arr.length-1))
	    			html += sep;
    		}
    	}
    	return html;
    }
    
    /**
     * 2021.10.12 LSH 짝수인지 판단
     */
    public static boolean isEven(String num) {
    	// 빈값은 0으로 인식
    	if (CommUtils.isEmpty(num))
    		return true;
    	return (Integer.parseInt(num) % 2 == 0);
    }
    
    /**
     * 2021.10.12 LSH 주민등록번호에서 생년월일 추출
     */
    public static String getBrdtByRno(String rno) {
    	if (rno == null || 
    		rno.length() != 13)
    		return null;
    	
    	char c = rno.charAt(6);
    	String dt = rno.substring(0,6);
    	if (c == '1' || c == '2' ||
    		c == '5' || c == '6') {
    		return "19"+dt;
    	}
    	else {
    		return "20"+dt;
    	}
    }
    
    /**
     * 2021.10.12 LSH 주민등록번호에서 성별 추출
     */
    public static String getSxdstByRno(String rno) {
    	if (rno == null || rno.length() != 13)
    		return "X";
    	char c = rno.charAt(6);
    	if (c == '1' || c == '3')
    		return "M"; // 남성
    	else if (c == '2' || c == '4')
    		return "F"; // 여성
    	else
    		return "G"; // 외국인
    }
    
    /**
     * 2021.10.12 LSH 주민등록번호에서 현재나이 추출
     */
    public static int getAgeByRno(String rno) {
    	if (rno == null || rno.length() != 13)
    		return 0;
    	char c = rno.charAt(6);
    	int  n = Integer.parseInt(rno.substring(0,2));
    	int year = getCurrYear();
    	
    	if (c == '1' || c == '2' ||
    		c == '5' || c == '6') {
    		return year - (1900+n) + 1;
    	}
    	else {
    		return year - (2000+n) + 1;
    	}
    }
    
    /**
     * 2021.10.15 LSH 구분자기준 split한 문자열배열을 List로 반환한다.
     */
	public static List<String> splitToList(String src, String delim) {
		if (src == null || delim == null)
			return null;
		String[] arr = src.split(delim);
		if (arr == null)
			return null;
		List<String> list = new ArrayList<String>();
		for (String s : arr) {
			list.add(nvlTrim(s));
		}
		return list;
	}
    
    /**
     * 2021.10.16 LSH List<String> 객체를 구분자로 병합된 문자열을 반환한다.
     */
	public static String mergeString(List<String> list, String delim) {
		if (list == null || delim == null)
			return null;
		if (list.size() == 0)
			return null;

		String ret = "";
		for (String s : list) {
			ret += (ret.length() > 0 ? delim : "") + s;
		}
		return ret;
	}

    /**
     * 2021.10.22 LSH 전화번호를 형식에맞게 변환하여 반환한다.
	 * @param phone 숫자만 있는 전화번호 (0211112222)
	 * @return 포맷된 전화번호 (02-1111-2222)
	 */
	public static String formatPhone(String phone) {
		if (phone == null)
			return "";
		if (phone.length() == 8)
			return phone.replaceFirst("^([0-9]{4})([0-9]{4})$", "$1-$2");
		else if (phone.length() == 12)
			return phone.replaceFirst("(^[0-9]{4})([0-9]{4})([0-9]{4})$", "$1-$2-$3");
		else
			return phone.replaceFirst("(^02|[0-9]{3})([0-9]{3,4})([0-9]{4})$", "$1-$2-$3");
	}

    /**
     * 2021.11.01 LSH yyyy[sep]MM[sep]dd 형식의 날짜문자열을 yyyyMMdd 로 변환
	 */
	public static String toShortDate(String date, String sep) {
		if (date == null)
			return null;
		return replace(date, sep, "");
	}
	
	/**
	 * 2021.11.30 LSH Map의 Value를 Bean에 Copy한다.
	 */
	public static void mapToBean(Map properties, Object bean) throws IllegalAccessException, InvocationTargetException {
		if (properties == null)
			return;
		BeanUtils.populate(bean, properties);
	}
	
	/**
	 * 2021.11.30 LSH Bean의 Value를 Map에 Copy한다.
	 */
	public static void beanToMap(Object bean, Map properties) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		Map map = PropertyUtils.describe(bean);
		map.remove("class");
		
		properties.putAll(map);
	}
	
	/**
	 * 2021.12.01 LSH double 형의 문자열을 int 형의 문자열로 변환
	 */
	public static String toIntString(String doubleStr) {
		if (doubleStr == null)
			return "0";
		Double d = Double.parseDouble(doubleStr);
		return String.valueOf( d.intValue() );
	}
	
	/**
	 * 2022.02.04 LSH double 형의 문자열을 long 형의 문자열로 변환
	 */
	public static String toLongString(String doubleStr) {
		if (doubleStr == null)
			return "0";
		Double d = Double.parseDouble(doubleStr);
		return String.valueOf( d.longValue() );
	}
	
	/**
	 * yyyyMMdd or yyyy-MM-dd 형식의 날짜문자열을 한글형으로 변환
	 * @param  str yyyyMMdd or yyyy-MM-dd 문자열
	 * @return "yyyy년 MM월 dd일"
	 */
	public static String toKorDate(String str) {
		if (str == null)
			return null;
		str = str.replaceAll("[^0-9]","");
		if (str.length() < 8)
			return null;
		return 	str.substring(0,4)+"년 "
			 +  str.substring(4,6)+"월 "
			 +  str.substring(6,8)+"일";
	}
	
	/**
	 * 2022.01.10 LSH 입력받은 배열에 해당 문자열이 있는지 확인
	 * @param arr 문자열 배열
	 * @param str 비교 문자열
	 * @return 있으면 true, 아니면 false
	 */
	public static boolean exist(String[] arr, String str) {
		if (str == null)
			return false;
		if (arr == null)
			return false;
		for (String token : arr) {
			if (CommUtils.isEqual(token, str))
				return true;
		}
		return false;
	}
	
	/**
	 * 2022.03.02 ntarget 구분자있는 문자열에 해당 문자열이 있는지 확인
	 * @param strs 구분자 문자열
	 * @param str  비교 문자열
	 * @return 있으면 true, 아니면 false
	 */
	public static boolean exist(String strs, String str) {
		if (str == null || str.length() == 0)
			return false;
		
		if (strs == null || strs.length() == 0)
			return false;
		
		if (strs.indexOf(str) > -1) 
			return true;
		
		return false;
	}	

	/**
	 * 2022.01.20 LSH YYYYMMDD 날짜의 Calendar를 반환
	 */
	public static Calendar toCalendar(String str) {
		
		if (str == null)
			return null;
		
		str = str.replaceAll("[^0-9]","");
		if (str.length() < 8)
			return null;
		
        Calendar cal = Calendar.getInstance();
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
			cal.setTime(formatter.parse(str.substring(0,8)));
        } 
		catch(ParseException e) {
            e.printStackTrace();
        }
		return cal;
	}

	/**
	 * 2022.01.20 LSH YYYYMMDD 날짜를 addDays만큼 날짜를 더한 후
	 *  특정포맷으로 변경하여 반환
	 */
	public static String formatDateAdd(String str, int addDays, String pattern) {
		Calendar cal = CommUtils.toCalendar(str);
		if (cal == null)
			return null;
		// 날짜 더하기
		cal.add(Calendar.DATE, addDays);
		return CommUtils.formatDate(pattern, cal);
	}
}
