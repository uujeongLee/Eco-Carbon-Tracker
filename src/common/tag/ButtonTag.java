package common.tag;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import common.user.UserInfo;
import common.util.properties.ApplicationProperty;

/**
 * Program Name     : ButtonTag
 * Description      : 버튼 디자인 및 권한체크
 * Programmer Name  : ntarget
 * Creation Date    : 2022-09-26
 * Used Table       :
 */

@SuppressWarnings({"all"})
public class ButtonTag extends TagSupport {
    private String title        = "";   // 버튼명
    private String id           = "";   // html 태그 id
    private String style        = "";   // html 태그 style
    private String cls          = "";   // html 태그 class
    private String type         = "";   // 버튼구분 [태그가 button인경우 type='button' 으로 설정]
    private String auth         = "";   // 권한롤 [ , 로 구분해서 다중롤 가능 ]

    private String TEMPLATE_BUTTON = "common/templates/button_list.vm";

    /*
     * (non-Javadoc)
     *
     * @see javax.servlet.jsp.tagext.Tag#doEndTag()
     */
    public int doStartTag() throws JspException {
        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();

        try {
            if (id == null) {
                return SKIP_BODY; // Nothing to output
            }

            JspWriter out = pageContext.getOut();
            out.println(buttonListStr());

        } catch (Exception e) {
            throw new JspException(e);
        }

        return SKIP_BODY;
    }

    public String buttonListStr() throws Exception {
        HashMap model = new HashMap();
        model.put("type",               this.type);
        model.put("id",                 this.id);
        model.put("title",              this.title);
        model.put("cls",                this.cls);
        model.put("style",              this.style);

        String authBtnCheck = ApplicationProperty.get("auth.btn.check");  // 버튼권한체크

        if (nvlTrim(authBtnCheck).equals("true"))
            model.put("isBtnAuth",         isAuthBtn(nvlTrim(this.auth)));
        else
            model.put("isBtnAuth",         true);


        StringWriter writer = new StringWriter();

        VelocityEngine velocityEngine = (VelocityEngine) WebApplicationContextUtils.getRequiredWebApplicationContext(pageContext.getServletContext()).getBean("velocityEngine");

        try {
            VelocityEngineUtils.mergeTemplate(velocityEngine, TEMPLATE_BUTTON, "UTF-8", model, writer);
        } catch (VelocityException e) {
            //e.printStackTrace();
        }

        return writer.toString();
    }

    // 버튼 권한 체크
    private boolean isAuthBtn(String strAuth) {
        HttpServletRequest request  = (HttpServletRequest)pageContext.getRequest();

        boolean isAuth = true;

        WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(((HttpServletRequest) request).getSession().getServletContext());

        UserInfo userInfo = (UserInfo)wac.getBean("userInfo");

        String[] gsRoleIds   = split((String)userInfo.getRoleId(), ",");

        if (strAuth != null && !strAuth.equals("")) {
            if (gsRoleIds == null)
                isAuth = false;
            else
                isAuth = Arrays.asList(gsRoleIds).contains(strAuth);
        }

        return isAuth;
    }

    private String nvl(String str) {
        String value = "";
        if (str != null && str.length() > 0) {
            value = str;
        }
        return value;
    }

    private String nvlTrim(String str) {
        return nvl(str).trim();
    }

    private String[] split(String src, String delim) {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getCls() {
        return cls;
    }

    public void setCls(String cls) {
        this.cls = cls;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getTEMPLATE_BUTTON() {
        return TEMPLATE_BUTTON;
    }

    public void setTEMPLATE_BUTTON(String tEMPLATE_BUTTON) {
        TEMPLATE_BUTTON = tEMPLATE_BUTTON;
    }

}
