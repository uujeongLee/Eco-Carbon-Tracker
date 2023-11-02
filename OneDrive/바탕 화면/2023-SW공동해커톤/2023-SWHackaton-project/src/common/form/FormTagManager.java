package common.form;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.LinkedMap;

import common.util.CommUtils;

/**
 * [유틸클래스] 스프링폼태그 사용을 위한 배열성 데이터 작성
 *              라디오, 체크박스, 콤보박스 항목데이터 정의
 * @class   : FormTagManager
 * @author  :
 * @since   : 2021-02-08
 * @version : 1.0
 */

@SuppressWarnings({"rawtypes","unchecked"})
public class FormTagManager {

    /**
     * ComboBox List 생성. (리턴 MAP)
     * @param list
     * @param allFlag
     *          - N        : "전체" 사용 안함.
     *          - Y        : Tag값 예) value=all,    text=::: 전체 :::
     *          - E        : Tag값 예) value=  ,     text=::: 전체 :::
     *          - {입력값} : Tag값 예) value=  ,     text={입력값}
     * @return Map
     */
    public static Map listToMapAll(List list, String allFlag) {
        return listToMapAll(list, "", "", allFlag);
    }

    public static Map listToMapAll(List list, String code, String codeNm, String allFlag) {
        Map map = new LinkedMap();

        // 전체사용 할경우
        if (!CommUtils.nvlTrim(allFlag).equalsIgnoreCase("N")) {
            // (Y)
            if (CommUtils.nvlTrim(allFlag).equalsIgnoreCase("Y")) {
                map.put(FormConst.COMBO_VAL_ALL, FormConst.COMBO_TXT_ALL);
            // (E)
            } else if (CommUtils.nvlTrim(allFlag).equalsIgnoreCase("E")) {
                map.put("", FormConst.COMBO_TXT_ALL);
            // (입력값)
            } else {
                map.put("", allFlag);
            }
        }

        // 코드 목록을 추가
        map.putAll(listToMap(list, code, codeNm));

        return map;
    }

    public static Map listToMap(List<Map> list) {
        return listToMap(list, null, null);
    }

    /**
     * list 객체를 Combo/Radio/Check를 위해 Map으로 변경
     * @param list
     * @param code      기본값 "code"
     * @param codeNm    기본값 "codeNm"
     * @return
     */
    public static Map listToMap(List<Map> list, String code, String codeNm) {
        Map map = new LinkedMap();
        String codeKey = (CommUtils.isEmpty(code))?   "code"  :code;
        String valueKey= (CommUtils.isEmpty(codeNm))? "codeNm":codeNm;

        if (list != null && list.size() > 0) {
            for (Map dataMap : list) {
                map.put(dataMap.get(codeKey), dataMap.get(valueKey));
            }
        }
        return map;
    }

}