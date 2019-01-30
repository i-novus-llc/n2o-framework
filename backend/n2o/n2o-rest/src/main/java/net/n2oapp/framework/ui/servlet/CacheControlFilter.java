package net.n2oapp.framework.ui.servlet;

import net.n2oapp.properties.StaticProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * @author iryabov
 * @since 11.08.2015
 */
public class CacheControlFilter implements Filter {
    Logger logger = LoggerFactory.getLogger(CacheControlFilter.class);

    private int cacheAge = 60;

    @Override
    public void init(FilterConfig filterConfig) {
        try {
            cacheAge = StaticProperties.getInt("n2o.ui.cache");
        } catch (Exception e) {
            logger.error("Can't initialize init param 'max-age', set by default [60 sec]", e);
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {


        long expiry = new Date().getTime() + cacheAge * 1000;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        httpResponse.setDateHeader("Expires", expiry);
        httpResponse.setHeader("Cache-Control", "max-age=" + cacheAge);

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
