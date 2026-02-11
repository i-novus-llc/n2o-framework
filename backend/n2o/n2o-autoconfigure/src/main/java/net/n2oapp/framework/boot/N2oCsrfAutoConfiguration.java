package net.n2oapp.framework.boot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.server.CookieSameSiteSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfAuthenticationStrategy;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Автоконфигурация безопасности N2O Framework
 */
@Slf4j
@AutoConfiguration
@ConditionalOnWebApplication
@ConditionalOnClass(name = "org.springframework.security.config.annotation.web.configuration.EnableWebSecurity")
@EnableConfigurationProperties(N2oCsrfCorsProperties.class)
public class N2oCsrfAutoConfiguration {

    private final N2oCsrfCorsProperties properties;

    public N2oCsrfAutoConfiguration(N2oCsrfCorsProperties properties) {
        this.properties = properties;
    }

    /**
     * Конфигурация SameSite для cookies
     * Применяется всегда, независимо от CSRF
     */
    @Bean
    @ConditionalOnProperty(prefix = "n2o.security.cookie", name = "same-site")
    public CookieSameSiteSupplier cookieSameSiteSupplier(N2oCsrfCorsProperties properties) {
        return CookieSameSiteSupplier.of(properties.getCookie().getSameSite());
    }

    /**
     * Настройка CORS для междоменных запросов
     */
    @Bean
    @ConditionalOnProperty(value = "n2o.security.cors.allowed-origins")
    public Customizer<CorsConfigurer<HttpSecurity>> n2oCorsCustomizer() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(properties.getCors().getAllowedOrigins());
        config.setAllowedMethods(properties.getCors().getAllowedMethods());
        config.setAllowedHeaders(properties.getCors().getAllowedHeaders());
        config.setAllowCredentials(properties.getCors().getAllowCredentials());
        config.setExposedHeaders(properties.getCors().getExposedHeaders());
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return cors -> cors.configurationSource(source);
    }

    /**
     * Конфигурация CSRF защиты
     * Активируется только если n2o.security.csrf.enabled=true
     */
    @Configuration
    @EnableWebSecurity
    @ConditionalOnProperty(prefix = "n2o.security.csrf", name = "enabled", havingValue = "true")
    public class CsrfSecurityConfiguration {

        @Bean
        public CookieCsrfTokenRepository csrfTokenRepository() {
            CookieCsrfTokenRepository tokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
            tokenRepository.setCookieName(properties.getCsrf().getCookieName());
            tokenRepository.setHeaderName(properties.getCsrf().getHeaderName());
            tokenRepository.setParameterName(properties.getCsrf().getParameterName());
            tokenRepository.setCookiePath(properties.getCsrf().getCookiePath());
            tokenRepository.setCookieCustomizer(responseCookieBuilder -> {
                responseCookieBuilder.httpOnly(false);
                responseCookieBuilder.secure(properties.getCsrf().isSecure());
            });
            return tokenRepository;
        }

        @Bean
        public Customizer<CsrfConfigurer<HttpSecurity>> n2oCsrfCustomizer(CookieCsrfTokenRepository csrfTokenRepository) {
            return csrf -> {
                csrf.csrfTokenRepository(csrfTokenRepository);

//                 xor как настроить???
                csrf.csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler());

                List<String> ignoringPatterns = properties.getCsrf().getIgnoringRequestMatchers();
                if (!ignoringPatterns.isEmpty())
                    csrf.ignoringRequestMatchers(ignoringPatterns.toArray(new String[0]));

                csrf.sessionAuthenticationStrategy(new CsrfAuthenticationStrategy(csrfTokenRepository));
            };
        }
    }
}
