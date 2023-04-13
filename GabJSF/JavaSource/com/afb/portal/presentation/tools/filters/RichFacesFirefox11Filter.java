/**
 * 
 */
package com.afb.portal.presentation.tools.filters;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * @author Francis K
 *
 */
public class RichFacesFirefox11Filter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(new HttpServletRequestWrapper((HttpServletRequest) request) {
            @Override
            public String getRequestURI() {
                try {
                    return URLDecoder.decode(super.getRequestURI(), "UTF-8");
                } catch (UnsupportedEncodingException e) {

                    throw new IllegalStateException("Cannot decode request URI.", e);
                }
            }
        }, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void destroy() {}

    /*
 
 <filter>
    <description>Filtre pour la gestion des erreurs de compatibilité avec les version de FireFox Supérieure à la V10</description>
    <display-name>richFacesFirefox11Filter</display-name>
    <filter-name>RichFacesFirefox11Filter</filter-name>
    <filter-class>com.afrikbrain.pme.ehr.presentation.tools.filters.RichFacesFirefox11Filter</filter-class>
   </filter>
   <filter-mapping>
    <filter-name>RichFacesFirefox11Filter</filter-name>
    <url-pattern>/a4j/*</url-pattern>
   </filter-mapping>
   
   */
    
    
}