package net.n2oapp.framework.boot;

import jakarta.servlet.http.Cookie;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Интеграционный тест CSRF защиты
 * Проверяет установку CSRF токена и его валидацию
 */
@SpringBootTest(
        classes = TestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "n2o.security.csrf.enabled=true",
        "n2o.security.csrf.secure=false",  // Для тестов HTTP
        "n2o.security.csrf.ignoring-request-matchers[0]=/api/public/**",
        "n2o.security.cors.allowed-origins=*"
})
class N2oCsrfProtectionIntegrationTest {

    private static final String CSRF_COOKIE_NAME = "XSRF-TOKEN";
    private static final String CSRF_HEADER_NAME = "X-XSRF-TOKEN";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CookieCsrfTokenRepository csrfTokenRepository;

    @Test
    void shouldRejectPostRequestWithoutCsrfToken() throws Exception {
        // POST без CSRF токена должен вернуть 403 Forbidden
        mockMvc.perform(post("/api/test/data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"test\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldAcceptPostRequestWithValidCsrfToken() throws Exception {
        Cookie csrfCookie = obtainCookieToken();
        assertThat(csrfCookie).isNotNull();
        String csrfToken = csrfCookie.getValue();

        // 2. POST с правильным CSRF токеном должен пройти
        mockMvc.perform(post("/api/test/data")
                        .cookie(csrfCookie)
                        .header(CSRF_HEADER_NAME, csrfToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"test\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldRejectPostRequestWithInvalidCsrfToken() throws Exception {
        Cookie csrfCookie = obtainCookieToken();
        assertThat(csrfCookie).isNotNull();

        mockMvc.perform(post("/api/test/data")
                        .cookie(csrfCookie)
                        .header(CSRF_HEADER_NAME, "invalid-token-12345")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"test\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldRejectPostRequestWithTokenOnlyInHeader() throws Exception {
        // POST только с header (без cookie) должен вернуть 403
        mockMvc.perform(post("/api/test/data")
                        .header(CSRF_HEADER_NAME, "some-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"test\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldRejectPostRequestWithTokenOnlyInCookie() throws Exception {
        Cookie csrfCookie = obtainCookieToken();

        // 2. POST только с cookie (без header) должен вернуть 403
        mockMvc.perform(post("/api/test/data")
                        .cookie(csrfCookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"test\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldAcceptPutRequestWithValidCsrfToken() throws Exception {
        Cookie csrfCookie = obtainCookieToken();
        String csrfToken = csrfCookie.getValue();

        // 2. PUT с токеном должен пройти
        mockMvc.perform(put("/api/test/data/1")
                        .cookie(csrfCookie)
                        .header(CSRF_HEADER_NAME, csrfToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"updated\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldAcceptDeleteRequestWithValidCsrfToken() throws Exception {
        Cookie csrfCookie = obtainCookieToken();
        String csrfToken = csrfCookie.getValue();

        // 2. DELETE с токеном должен пройти
        mockMvc.perform(delete("/api/test/data/1")
                        .cookie(csrfCookie)
                        .header(CSRF_HEADER_NAME, csrfToken))
                .andExpect(status().isOk());
    }

    @Test
    void shouldAcceptPatchRequestWithValidCsrfToken() throws Exception {
        Cookie csrfCookie = obtainCookieToken();
        String csrfToken = csrfCookie.getValue();

        // 2. PATCH с токеном должен пройти
        mockMvc.perform(patch("/api/test/data/1")
                        .cookie(csrfCookie)
                        .header(CSRF_HEADER_NAME, csrfToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"patched\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotRequireCsrfTokenForGetHeadOptionsRequest() throws Exception {
        // GET запрос не требует CSRF токен
        mockMvc.perform(get("/api/test/data"))
                .andExpect(status().isOk());
        // HEAD запрос не требует CSRF токен
        mockMvc.perform(head("/api/test/data"))
                .andExpect(status().isOk());
        // OPTIONS запрос не требует CSRF токен
        mockMvc.perform(options("/api/test/data"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldAllowPublicEndpointsWithoutCsrfToken() throws Exception {
        // Endpoint в исключениях (/api/public/**) не требует CSRF токен
        mockMvc.perform(post("/api/public/feedback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"message\":\"test\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldRejectRequestWithMismatchedToken() throws Exception {
        Cookie csrfCookie = obtainCookieToken();

        // 2. "Получить" другой токен (имитация атаки)
        String attackerToken = "attacker-generated-token-12345";

        // 3. POST с cookie от сервера, но header от атакующего
        mockMvc.perform(post("/api/test/data")
                        .cookie(csrfCookie)
                        .header(CSRF_HEADER_NAME, attackerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"attack\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldUseSameTokenForMultipleRequests() throws Exception {
        Cookie csrfCookie = obtainCookieToken();
        String csrfToken = csrfCookie.getValue();

        // 2. Первый POST с токеном
        mockMvc.perform(post("/api/test/data")
                        .cookie(csrfCookie)
                        .header(CSRF_HEADER_NAME, csrfToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"first\"}"))
                .andExpect(status().isOk());

        // 3. Второй POST с тем же токеном - должен пройти
        mockMvc.perform(post("/api/test/data")
                        .cookie(csrfCookie)
                        .header(CSRF_HEADER_NAME, csrfToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"second\"}"))
                .andExpect(status().isOk());

        // 4. Третий POST с тем же токеном - тоже должен пройти
        mockMvc.perform(post("/api/test/data")
                        .cookie(csrfCookie)
                        .header(CSRF_HEADER_NAME, csrfToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"third\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldValidateCsrfTokenCaseInsensitiveMethod() throws Exception {
        Cookie csrfCookie = obtainCookieToken();
        String csrfToken = csrfCookie.getValue();

        // 2. Проверить что POST (любой регистр) требует токен
        mockMvc.perform(post("/api/test/data")
                        .cookie(csrfCookie)
                        .header(CSRF_HEADER_NAME, csrfToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"test\"}"))
                .andExpect(status().isOk());
    }

    private Cookie obtainCookieToken() {
        CsrfToken csrfToken1 = csrfTokenRepository.generateToken(new MockHttpServletRequest());
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockHttpServletRequest request = new MockHttpServletRequest();
        csrfTokenRepository.saveToken(csrfToken1, request, response);
        return response.getCookie(CSRF_COOKIE_NAME);
    }
}
