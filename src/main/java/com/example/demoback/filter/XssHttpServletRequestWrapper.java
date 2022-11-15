package com.example.demoback.filter;

import com.example.demoback.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
    private static final Logger logger = LoggerFactory.getLogger(XssHttpServletRequestWrapper.class);
    /**
     * Constructs a request object wrapping the given request.
     *
     * @param request The request to wrap
     * @throws IllegalArgumentException if the request is null
     */
    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getHeader(String name) {
        String strHeader = super.getHeader(name);
        if(StringUtils.isEmpty(strHeader)){
            return strHeader;

        }
        return HtmlUtils.htmlEscape(super.getHeader(name));
    }

    @Override
    public String getParameter(String name) {
        String strParameter = super.getParameter(name);
        if(StringUtils.isEmpty(strParameter)){
            return strParameter;
        }
        return HtmlUtils.htmlEscape(super.getParameter(name));
    }


    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if(values==null){
            return values;
        }
        int length = values.length;
        String[] escapseValues = new String[length];
        for(int i = 0;i<length;i++){
            //过滤一切可能的xss攻击字符串
            escapseValues[i] = HtmlUtils.htmlEscape(values[i]);
            if(!StringUtils.equals(escapseValues[i],values[i])){
                logger.info("xss字符串过滤前："+values[i]+"\r\n"+"过滤后："+escapseValues[i]);
            }
        }
        return escapseValues;
    }
}