package net.n2oapp.framework.access.api;

import net.n2oapp.framework.access.api.chained.AuthorizationApiCaller;

import static net.n2oapp.context.StaticSpringContext.getBean;
import static net.n2oapp.properties.StaticProperties.getProperty;

/**
 * User: operehod
 * Date: 26.09.2014
 * Time: 14:20
 */
public class AuthorizationApiProvider {

    protected transient AuthorizationApi authorizationApi;

    public AuthorizationApiProvider(AuthorizationApi authorizationApi) {
        this.authorizationApi = authorizationApi;
    }

    public AuthorizationApi getAuthorizationApi() {
        return authorizationApi;
    }

    public AuthorizationApiCaller createCaller() {
        return new AuthorizationApiCaller(authorizationApi);
    }

    public void setAuthorizationApi(AuthorizationApi authorizationApi) {
        this.authorizationApi = authorizationApi;
    }
}
