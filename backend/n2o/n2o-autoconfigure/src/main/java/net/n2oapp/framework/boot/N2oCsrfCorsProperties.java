package net.n2oapp.framework.boot;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.server.Cookie.SameSite;

import java.util.ArrayList;
import java.util.List;

/**
 * Настройки безопасности N2O Framework
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "n2o.security")
public class N2oCsrfCorsProperties {

    /**
     * Настройки CSRF защиты
     */
    private CsrfProperties csrf = new CsrfProperties();

    /**
     * Настройки Cookie
     */
    private CookieProperties cookie = new CookieProperties();

    /**
     * Настройки Cors
     */
    private CorsProperties cors = new CorsProperties();

    @Getter
    @Setter
    public static class CsrfProperties {
        /**
         * Включить CSRF защиту
         * По умолчанию: false
         */
        private boolean enabled = false;

        /**
         * Имя cookie для CSRF токена
         * По умолчанию: XSRF-TOKEN
         */
        private String cookieName = "XSRF-TOKEN";

        /**
         * Имя HTTP заголовка для CSRF токена
         * По умолчанию: X-XSRF-TOKEN
         */
        private String headerName = "X-XSRF-TOKEN";

        /**
         * Имя параметра для CSRF токена
         * По умолчанию: _csrf
         */
        private String parameterName = "_csrf";

        /**
         * Путь для CSRF cookie
         * По умолчанию: /
         */
        private String cookiePath = "/";

        /**
         * Список URL patterns, которые исключаются из CSRF защиты
         * Поддерживает Ant-style patterns (например: /api/public/**)
         */
        private List<String> ignoringRequestMatchers = new ArrayList<>();

        /**
         * Использовать Secure flag для cookies (только HTTPS)
         * По умолчанию: true
         */
        private boolean secure = true;
    }

    @Getter
    @Setter
    public static class CorsProperties {
        /**
         * Разрешенные origins для CORS
         */
        private List<String> allowedOrigins = null;

        /**
         * Разрешенные методы для CORS
         */
        private List<String> allowedMethods = List.of("GET", "OPTIONS");

        /**
         * Разрешенные заголовки для CORS
         */
        private List<String> allowedHeaders = List.of("*");

        /**
         * Список заголовков, которые может содержать фактический ответ и могут быть доступны.
         */
        private List<String> exposedHeaders = null;
        /**
         * Должен ли браузер отправлять учетные данные, такие как файлы cookie, вместе с междоменными запросами в аннотированную конечную точку.
         */
        private Boolean allowCredentials = false;
    }

    @Getter
    @Setter
    public static class CookieProperties {
        /**
         * SameSite политика для session cookie
         * Возможные значения: Strict, Lax, None
         * По умолчанию: Lax
         */
        private SameSite sameSite = SameSite.LAX;

    }
}
