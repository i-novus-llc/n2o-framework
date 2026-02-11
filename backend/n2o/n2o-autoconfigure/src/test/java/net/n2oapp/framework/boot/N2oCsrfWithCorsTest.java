package net.n2oapp.framework.boot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import jakarta.servlet.http.Cookie;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Тест CSRF защиты с CORS конфигурацией
 * Проверяет что CSRF токен требуется даже когда CORS разрешен
 */
@SpringBootTest(
    classes = TestApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc
@TestPropertySource(properties = {
    "n2o.security.csrf.enabled=true",
    "n2o.security.csrf.secure=false",
    "n2o.security.cors.allowed-origins=https://trusted-domain.com,https://another-trusted.com",
    "n2o.security.cors.allowed-methods=GET,POST,OPTION",
    "n2o.security.cors.allow-credentials=true"
})
class N2oCsrfWithCorsTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CookieCsrfTokenRepository csrfTokenRepository;

    private static final String CSRF_COOKIE_NAME = "XSRF-TOKEN";
    private static final String CSRF_HEADER_NAME = "X-XSRF-TOKEN";

    @Test
    void shouldRequireCsrfTokenEvenWithAllowedOrigin() throws Exception {
        Cookie csrfCookie = obtainCookieToken() ;
        String csrfToken = csrfCookie.getValue();

        // 2. POST с разрешенного домена БЕЗ CSRF токена - должен вернуть 403
        mockMvc.perform(post("/api/test/data")
                .header("Origin", "https://trusted-domain.com")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"test\"}"))
            .andExpect(status().isForbidden());

        // 3. POST с разрешенного домена С CSRF токеном - должен пройти
        mockMvc.perform(post("/api/test/data")
                .cookie(csrfCookie)
                .header(CSRF_HEADER_NAME, csrfToken)
                .header("Origin", "https://trusted-domain.com")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"test\"}"))
            .andExpect(status().isOk())
            // Проверить CORS заголовки в ответе
            .andExpect(header().string("Access-Control-Allow-Origin", "https://trusted-domain.com"))
            .andExpect(header().string("Access-Control-Allow-Credentials", "true"));
    }

    @Test
    void shouldRejectUnauthorizedOriginEvenWithCsrfToken() throws Exception {
        Cookie csrfCookie = obtainCookieToken() ;
        String csrfToken = csrfCookie.getValue();

        mockMvc.perform(post("/api/test/data")
                .cookie(csrfCookie)
                .header(CSRF_HEADER_NAME, csrfToken)
                .header("Origin", "https://evil.com")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"test\"}"))
            .andExpect(status().isForbidden());
    }

    @Test
    void shouldHandlePreflightRequest() throws Exception {
        // Preflight OPTIONS запрос не требует CSRF токен
        mockMvc.perform(options("/api/test/data")
                .header("Origin", "https://trusted-domain.com")
                .header("Access-Control-Request-Method", "POST")
                .header("Access-Control-Request-Headers", CSRF_HEADER_NAME))
            .andExpect(status().isOk())
            .andExpect(header().string("Access-Control-Allow-Origin", "https://trusted-domain.com"))
            .andExpect(header().string("Access-Control-Allow-Methods", org.hamcrest.Matchers.containsString("POST")))
            .andExpect(header().string("Access-Control-Allow-Credentials", "true"));
    }


    @Test
    void shouldRejectWildcardOriginSubstitution() throws Exception {
        Cookie csrfCookie = obtainCookieToken() ;
        String csrfToken = csrfCookie.getValue();

        // Атакующий пытается использовать wildcard
        mockMvc.perform(post("/api/test/data")
                .cookie(csrfCookie)
                .header(CSRF_HEADER_NAME, csrfToken)
                .header("Origin", "https://evil-trusted-domain.com")  // Похоже на разрешенный
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"test\"}"))
            .andExpect(status().isForbidden());
    }

    private Cookie obtainCookieToken(){
        CsrfToken csrfToken1 = csrfTokenRepository.generateToken(new MockHttpServletRequest());
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockHttpServletRequest request = new MockHttpServletRequest();
        csrfTokenRepository.saveToken(csrfToken1, request, response);
        return response.getCookie(CSRF_COOKIE_NAME);
    }
}
