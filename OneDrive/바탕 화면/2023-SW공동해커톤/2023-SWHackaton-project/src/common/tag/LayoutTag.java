package common.tag;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.springframework.context.ApplicationContext;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;

import common.util.CommUtils;
import common.util.properties.ApplicationProperty;
import egovframework.com.cmm.EgovMessageSource;

/**
 * Program Name 	: LayoutTag
 * Description 		: 화면레이어아웃 Velocity 처리 (Header, Footer)
 * Programmer Name 	: ntarget
 * Creation Date 	: 2021-02-08
 * Used Table 		:
 */

@SuppressWarnings("all")
public class LayoutTag extends TagSupport {

    private final Log logger = LogFactory.getLog(getClass());


    private EgovMessageSource message;

    private String mode 	= "header";
    private String type 	= "normal";
    private String sys 		= "";
    private String title 	= "";
    private String style	= "";		//화면 레이아웃   STYLE CLASS KEY
    private String icon		= "";   	//화면 제목아이콘 CLASS KEY
    private String TEMPLATE = "";

    private final String DEFAULT_TEMPLATE_HEADER 		= "common/templates/layout_header.vm";
    private final String DEFAULT_TEMPLATE_FOOTER 		= "common/templates/layout_footer.vm";
    private final String DEFAULT_TEMPLATE_HEADER_POPUP 	= "common/templates/layout_header_popup.vm";
    private final String DEFAULT_TEMPLATE_FOOTER_POPUP 	= "common/templates/layout_footer_popup.vm";
    private final String DEFAULT_TEMPLATE_STYLESCRIPT 	= "common/templates/layout_stylescript.vm";

    public int doStartTag() throws JspException {
        try {
            // message 객체 얻기
            ApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(pageContext.getServletContext());
            this.message =  wac.getBean(EgovMessageSource.class);

            if (mode == null) {
                return SKIP_BODY; // Nothing to output
            }
            HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
            Map pageMap 		= (HashMap)request.getSession().getAttribute(ApplicationProperty.get("SESS.PAGEINFO"));	// 페이지정보(DB정보) 및 세션정보(userInfo)
            List menuList 		= (List)request.getSession().getAttribute(ApplicationProperty.get("SESS.MENUINFO"));	// 메뉴정보(DB정보)

            Map params = new HashMap();

            if (pageMap != null) 	params.putAll(pageMap);
            if (menuList != null)	params.put("menuList", 	menuList);

            String sysCd 		= toLower(ApplicationProperty.get("system.code"));	// 시스템코드
            params.put("sysCd", sysCd);

            String str = "";
            if		("header".equals(mode))				str = header(params);
            else if	("headerPopup".equals(mode))		str = headerPopup(params);
            else if	("footer".equals(mode))				str = footer(params);
            else if	("footerPopup".equals(mode))		str = footerPopup(params);
            else if	("stylescript".equals(mode)) 		str = stylescript(params);

            JspWriter out = pageContext.getOut();
            out.println(str);

        } catch (Exception e) {
            throw new Error("LayoutTag("+mode+") Error!", e);
        }
        return SKIP_BODY;
    }

    private String header(Map params) {
        params.put("contextPath", ((HttpServletRequest) pageContext.getRequest()).getContextPath());
        params.put("type" , this.type);
        params.put("icon" , this.icon);
        params.put("style", this.style);
        params.put("title", _getHeaderTitle(params));

        TEMPLATE = DEFAULT_TEMPLATE_HEADER;

        return _merge(params, TEMPLATE);
    }

    private String headerPopup(Map params) {
        params.put("contextPath", ((HttpServletRequest) pageContext.getRequest()).getContextPath());
        params.put("type" , this.type);
        params.put("icon" , this.icon);
        params.put("style", this.style);
        params.put("title", _getHeaderTitle(params));

        TEMPLATE = DEFAULT_TEMPLATE_HEADER_POPUP;

        return _merge(params, TEMPLATE);
    }

    private String footerPopup(Map params) {
        params.put("contextPath", ((HttpServletRequest) pageContext.getRequest()).getContextPath());

        TEMPLATE = DEFAULT_TEMPLATE_FOOTER_POPUP;

        return _merge(params, TEMPLATE);
    }

    private String footer(Map params) {
        params.put("contextPath", ((HttpServletRequest) pageContext.getRequest()).getContextPath());
        params.put("type", this.type);
        params.put("footerTxt", this.message.getMessage("title.com.footerTxt"));

        TEMPLATE = DEFAULT_TEMPLATE_FOOTER;

        return _merge(params, TEMPLATE);
    }

    private String stylescript(Map params) {
        HttpServletRequest request  = (HttpServletRequest)pageContext.getRequest();
        String requestUri           = request.getRequestURI();
        String currPage             = requestUri.substring(requestUri.lastIndexOf("/"));

        if (!"".equals(currPage) && !"/".equals(currPage) )
            currPage			= currPage.substring(0, currPage.lastIndexOf("."));

        String currPath = requestUri.substring(0, requestUri.lastIndexOf("/"));
        currPath        = currPath.replaceAll("/WEB-INF/jsp/", "");

        params.put("currPage", 	    currPath + currPage);
        params.put("contextPath", 	((HttpServletRequest) pageContext.getRequest()).getContextPath());
        params.put("type", 		   this.type);

        params.put("ver",           CommUtils.getCurrDateTime2());

        TEMPLATE = DEFAULT_TEMPLATE_STYLESCRIPT;

        return _merge(params, TEMPLATE);
    }

    //VELOCITY 템플릿과 MERGE
    private String _merge(Map params, String template) {
        StringWriter writer	= new StringWriter();

        try {
            VelocityEngine velocityEngine = (VelocityEngine) WebApplicationContextUtils
            .getRequiredWebApplicationContext(pageContext.getServletContext()).getBean("velocityEngine");

            VelocityEngineUtils.mergeTemplate(velocityEngine, template, params, writer);
        } catch (VelocityException e) {
            //e.printStackTrace();
        }
        return writer.toString();
    }

    private String _getHeaderTitle(Map params) {
        String titleNm = "";

        if ((Map)params.get("pageInfo") != null)
                titleNm = (String)((Map)params.get("pageInfo")).get("titleNm");

        if (this.title != null && !"".equals(this.title)) {
            if ((String)this.message.getMessage(this.title) == null)
                titleNm = this.title;
            else
                titleNm = (String)this.message.getMessage(this.title);
        }

        return titleNm;
    }

    private String toLower(String str) {
        if (str == null)
            return null;
        return str.toLowerCase();
    }

    public String getMode() {
        return mode;
    }
    public void setMode(String mode) {
        this.mode = mode;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getStyle() {
        return style;
    }
    public void setStyle(String style) {
        this.style = style;
    }
    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
    public String getSys() {
        return sys;
    }
    public void setSys(String sys) {
        this.sys = sys;
    }


}
