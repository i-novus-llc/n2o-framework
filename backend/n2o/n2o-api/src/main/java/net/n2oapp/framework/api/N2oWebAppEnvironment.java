package net.n2oapp.framework.api;

import net.n2oapp.properties.web.WebApplicationProperties;
import org.springframework.web.context.support.StandardServletEnvironment;

/**
 * @author operehod
 * @since 08.06.2015
 */
public class N2oWebAppEnvironment extends StandardServletEnvironment {

    private WebApplicationProperties n2oProperties;

    public WebApplicationProperties getN2oProperties() {
        return n2oProperties;
    }

    public void setN2oProperties(WebApplicationProperties n2oProperties) {
        this.n2oProperties = n2oProperties;
    }
}
