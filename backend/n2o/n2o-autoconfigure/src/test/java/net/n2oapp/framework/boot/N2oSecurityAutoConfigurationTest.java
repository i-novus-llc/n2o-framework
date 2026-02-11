package net.n2oapp.framework.boot;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.boot.web.server.Cookie;
import org.springframework.boot.web.servlet.server.CookieSameSiteSupplier;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Тесты для N2oSecurityAutoConfiguration
 */
class N2oSecurityAutoConfigurationTest {

    private final WebApplicationContextRunner contextRunner = new WebApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(
                    WebMvcAutoConfiguration.class,
                    N2oCsrfAutoConfiguration.class
            ));

    @Test
    void csrfDisabledByDefault() {
        contextRunner
                .run(context -> {
                    assertThat(context).hasSingleBean(N2oCsrfCorsProperties.class);
                    
                    N2oCsrfCorsProperties properties = context.getBean(N2oCsrfCorsProperties.class);
                    assertThat(properties.getCsrf().isEnabled()).isFalse();
                    
                    // Security configuration should not be created when CSRF is disabled
                    assertThat(context).doesNotHaveBean(EnableWebSecurity.class);
                });
    }

    @Test
    void csrfEnabledWhenPropertySet() {
        contextRunner
                .withPropertyValues("n2o.security.csrf.enabled=true")
                .run(context -> {
                    assertThat(context).hasSingleBean(N2oCsrfCorsProperties.class);
                    
                    N2oCsrfCorsProperties properties = context.getBean(N2oCsrfCorsProperties.class);
                    assertThat(properties.getCsrf().isEnabled()).isTrue();
                });
    }

    @Test
    void csrfPropertiesConfigured() {
        contextRunner
                .withPropertyValues(
                        "n2o.security.csrf.enabled=true",
                        "n2o.security.csrf.cookie-name=CUSTOM-TOKEN",
                        "n2o.security.csrf.header-name=X-CUSTOM-TOKEN",
                        "n2o.security.csrf.ignoring-request-matchers[0]=/public/**",
                        "n2o.security.csrf.ignoring-request-matchers[1]=/api/health"
                )
                .run(context -> {
                    N2oCsrfCorsProperties properties = context.getBean(N2oCsrfCorsProperties.class);
                    
                    assertThat(properties.getCsrf().getCookieName()).isEqualTo("CUSTOM-TOKEN");
                    assertThat(properties.getCsrf().getHeaderName()).isEqualTo("X-CUSTOM-TOKEN");
                    assertThat(properties.getCsrf().getIgnoringRequestMatchers())
                            .containsExactly("/public/**", "/api/health");
                });
    }

    @Test
    void cookiePropertiesConfigured() {
        contextRunner
                .withPropertyValues(
                        "n2o.security.cookie.same-site=Strict",
                        "n2o.security.csrf.secure=false",
                        "n2o.security.csrf.http-only=false"
                )
                .run(context -> {
                    N2oCsrfCorsProperties properties = context.getBean(N2oCsrfCorsProperties.class);
                    
                    assertThat(properties.getCookie().getSameSite()).isEqualTo(Cookie.SameSite.STRICT);
                    assertThat(properties.getCsrf().isSecure()).isFalse();
                });
    }

    @Test
    void sameSiteCookieSupplierConfigured() {
        contextRunner
                .withPropertyValues("n2o.security.cookie.same-site=Lax")
                .run(context -> {
                    assertThat(context).hasSingleBean(CookieSameSiteSupplier.class);
                });
    }

    @Test
    void corsOriginsConfigured() {
        contextRunner
                .withPropertyValues(
                        "n2o.security.cors.allowed-origins[0]=https://trusted.com",
                        "n2o.security.cors.allowed-origins[1]=https://another.com"
                )
                .run(context -> {
                    N2oCsrfCorsProperties properties = context.getBean(N2oCsrfCorsProperties.class);
                    
                    assertThat(properties.getCors().getAllowedOrigins())
                            .containsExactly("https://trusted.com", "https://another.com");
                });
    }

    @Test
    void defaultValues() {
        contextRunner
                .run(context -> {
                    N2oCsrfCorsProperties properties = context.getBean(N2oCsrfCorsProperties.class);
                    
                    // CSRF defaults
                    assertThat(properties.getCsrf().isEnabled()).isFalse();
                    assertThat(properties.getCsrf().getCookieName()).isEqualTo("XSRF-TOKEN");
                    assertThat(properties.getCsrf().getHeaderName()).isEqualTo("X-XSRF-TOKEN");
                    assertThat(properties.getCsrf().getParameterName()).isEqualTo("_csrf");
                    assertThat(properties.getCsrf().getCookiePath()).isEqualTo("/");

                    // Cookie defaults
                    assertThat(properties.getCookie().getSameSite()).isEqualTo(Cookie.SameSite.LAX);
                    assertThat(properties.getCsrf().isSecure()).isTrue();

                    // CORS defaults
                    assertThat(properties.getCors().getAllowedOrigins()).isNullOrEmpty();
                });
    }
}
