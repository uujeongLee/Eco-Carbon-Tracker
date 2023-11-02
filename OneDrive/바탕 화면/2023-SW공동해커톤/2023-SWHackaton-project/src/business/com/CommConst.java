package business.com;

import common.util.properties.ApplicationProperty;

/**
 * [상수클래스] - 공통 상수
 *
 * @class   : CommConst
 * @author  :
 * @since   : 2022.09.26
 * @version : 1.0
 *
 *   수정일     수정자             수정내용
 *  --------   --------    ---------------------------
 *
 */
public class CommConst {

    private CommConst() {};

    // COMBO ALL Value
    public static final String COMBO_VAL_ALL = "all";
    public static final String COMBO_TXT_ALL = "::: 전체 :::";
    public static final String COMBO_TXT_SEL = "::: 선택하세요 :::";


    public static final String SERVER_NAME               = ApplicationProperty.get("system.servername");         // 서버명
}
