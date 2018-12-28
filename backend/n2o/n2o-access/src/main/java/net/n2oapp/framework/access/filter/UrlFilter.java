package net.n2oapp.framework.access.filter;

import net.n2oapp.framework.access.api.AuthorizationApi;
import net.n2oapp.framework.api.user.StaticUserContext;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * фильтр проверяющий доступен ли url текущему пользователю
 */
public class UrlFilter extends DelegatingFilterProxy {

    private AuthorizationApi authorizationApi;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String url = getUrl(httpServletRequest);
        boolean isAllowed = authorizationApi.getPermissionForUrlPattern(StaticUserContext.getUserContext(), url).isAllowed();
        if (isAllowed)
            filterChain.doFilter(request, response);
        else {
            ((HttpServletResponse) response).sendError(403);
        }
    }

    private static String getUrl(HttpServletRequest request) {
        String url = request.getServletPath();
        String pathInfo = request.getPathInfo();
        String query = request.getQueryString();

        if (pathInfo != null || query != null) {
            StringBuilder sb = new StringBuilder(url);

            if (pathInfo != null) {
                sb.append(pathInfo);
            }

            if (query != null) {
                sb.append('?').append(query);
            }
            url = sb.toString();
        }
        return url;
    }

    public void setAuthorizationApi(AuthorizationApi authorizationApi) {
        this.authorizationApi = authorizationApi;
    }
}
