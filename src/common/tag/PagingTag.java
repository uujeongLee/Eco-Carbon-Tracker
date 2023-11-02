package common.tag;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;

import commf.paging.PaginatedArrayList;

/**
 * Program Name 	: PagingTag
 * Description 		: 페이징 Velocity 처리
 * Programmer Name 	: ntarget
 * Creation Date 	: 2021-02-08
 * Used Table 		:
 */

@SuppressWarnings({"all"})
public class PagingTag extends TagSupport {

    private final static int DEFAULT_PAGE_INDEX_SIZE = 10;
    private int indexSize = -1;
    private String name;
    private String property;
    private String jsFunction;
    private String scope;
    private String type = "";
    private String sys = "";
    private String template = "";

    private final static String DEFAULT_TEMPLATE_PAGINGLIST = "common/templates/paging_list.vm";

    public int doStartTag() throws JspException {
        int page = 1;

        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();

        PaginatedArrayList paginatedList = (PaginatedArrayList)request.getAttribute(name);

        Map params = new HashMap();

        try {
            if (paginatedList == null) {
                return SKIP_BODY; // Nothing to output
            }

            if (paginatedList.getCurrPage() > 0) {
                page = paginatedList.getCurrPage();
            }

            String pagingIndex = generate(page, paginatedList.getTotalSize(), paginatedList.getPageSize(), params);

            JspWriter out = pageContext.getOut();
            out.println(pagingIndex);

        } catch (Exception e) {
            throw new JspException(e);
        }

        return SKIP_BODY;
    }

    public String generate(int page, int totalSize, int pageSize, Map params) throws Exception {

        if ((page <= 0) || (pageSize <= 0) || (getIndexSize() <= 0)) {
            return "";
        }

        if (indexSize < 1) {
            indexSize = DEFAULT_PAGE_INDEX_SIZE;
        }

        int totalPage = totalSize / pageSize;

        if ((totalSize % pageSize) > 0) {
            totalPage++;
        }

        if (totalPage <= 0) {
            totalPage = 1;
        }

        int currentPageCount = page / indexSize;

        if ((page % indexSize) > 0) {
            currentPageCount++;
        }

        int lastPageCount = totalPage / indexSize;

        if ((totalPage % indexSize) > 0) {
            lastPageCount++;
        }

        int startPage = ((currentPageCount - 1) * indexSize) + 1;
        int endPage = ((currentPageCount - 1) * indexSize) + indexSize;

        if (endPage > totalPage) {
            endPage = totalPage;
        }

        params.put("startPage", 		new Integer(startPage));
        params.put("page", 				new Integer(page));
        params.put("endPage", 			new Integer(endPage));
        params.put("contextPath", 		((HttpServletRequest) pageContext.getRequest()).getContextPath());
        params.put("indexSize", 		new Integer(getIndexSize()));
        params.put("currentPageCount", 	new Integer(currentPageCount));
        params.put("lastPageCount", 	new Integer(lastPageCount));
        params.put("totalPage", 		new Integer(totalPage));
        params.put("totalSize", 		new Integer(totalSize));
        params.put("jsFunction", 		jsFunction);
        params.put("type", 				type);

        StringWriter writer = new StringWriter();

        VelocityEngine velocityEngine = (VelocityEngine) WebApplicationContextUtils.getRequiredWebApplicationContext(pageContext.getServletContext()).getBean("velocityEngine");

        try {
            template = DEFAULT_TEMPLATE_PAGINGLIST;

            VelocityEngineUtils.mergeTemplate(velocityEngine, template, params, writer);
        } catch (VelocityException e) {
            //e.printStackTrace();
        }

        return writer.toString();
    }

    public String getName() {
        return name;
    }

    public String getMessageerty() {
        return property;
    }

    public String getScope() {
        return scope;
    }

    public void setName(String string) {
        name = string;
    }

    public void setProperty(String string) {
        property = string;
    }

    public void setScope(String string) {
        scope = string;
    }

    public String getJsFunction() {
        return jsFunction;
    }

    public void setJsFunction(String jsFunction) {
        this.jsFunction = jsFunction;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIndexSize() {
        if (indexSize == -1) {
            return DEFAULT_PAGE_INDEX_SIZE;
        }
        return indexSize;
    }

    public void setIndexSize(int indexSize) {
        this.indexSize = indexSize;
    }

    public String getSys() {
        return sys;
    }

    public void setSys(String sys) {
        this.sys = sys;
    }


}
