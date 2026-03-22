package net.n2oapp.framework.ui.utils;

import jakarta.servlet.http.HttpServletRequest;
import net.n2oapp.framework.api.exception.N2oException;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;

public class UrlUtil {
    private UrlUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Преобразует относительный URL в абсолютный на основе параметров входящего HTTP-запроса.
     * Абсолютные URL возвращаются без изменений.
     *
     * @param url         URL для преобразования
     * @param httpRequest входящий HTTP-запрос, используемый для определения scheme, host, port и context path
     * @return абсолютный URL
     * @throws N2oException если URL имеет некорректный формат
     */
    public static String resolveAbsoluteUrl(String url, HttpServletRequest httpRequest) {
        try {
            URI uri = new URI(url);
            if (uri.isAbsolute()) {
                return url;
            }
        } catch (URISyntaxException e) {
            throw new N2oException(String.format("Неверный формат URL: %s", url));
        }

        String scheme = httpRequest.getScheme();
        int port = httpRequest.getServerPort();
        boolean isDefaultPort = ("http".equals(scheme) && port == 80) || ("https".equals(scheme) && port == 443);

        return UriComponentsBuilder.newInstance()
                .scheme(scheme)
                .host(httpRequest.getServerName())
                .port(isDefaultPort ? -1 : port)
                .replacePath(httpRequest.getContextPath())
                .path(url)
                .build()
                .toUriString();
    }
}
