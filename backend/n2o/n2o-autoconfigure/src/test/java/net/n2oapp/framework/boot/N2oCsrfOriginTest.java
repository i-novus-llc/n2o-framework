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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Тест проверки Origin и Referer заголовков для защиты от CSRF
 * Spring Security автоматически проверяет эти заголовки
 */
@SpringBootTest(
        classes = TestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "n2o.security.csrf.enabled=true",
        "n2o.security.csrf.secure=false",
        "server.port=8080",
        "n2o.security.cors.allowed-origins=http://localhost:8080",
        "n2o.security.cors.allowed-methods=GET,POST"
})
class N2oCsrfOriginTest {

    private static final String CSRF_COOKIE_NAME = "XSRF-TOKEN";
    private static final String CSRF_HEADER_NAME = "X-XSRF-TOKEN";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CookieCsrfTokenRepository csrfTokenRepository;

    @Test
    void shouldAcceptRequestWithValidOrigin() throws Exception {
        Cookie csrfCookie = obtainCookieToken();
        String csrfToken = csrfCookie.getValue();

        mockMvc.perform(post("/api/test/data")
                        .cookie(csrfCookie)
                        .header(CSRF_HEADER_NAME, csrfToken)
                        .header("Origin", "http://localhost")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"test\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldRejectRequestWithInvalidOrigin() throws Exception {
        Cookie csrfCookie = obtainCookieToken();
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
    void shouldAcceptRequestWithValidReferer() throws Exception {
        Cookie csrfCookie = obtainCookieToken();
        String csrfToken = csrfCookie.getValue();

        mockMvc.perform(post("/api/test/data")
                        .cookie(csrfCookie)
                        .header(CSRF_HEADER_NAME, csrfToken)
                        .header("Referer", "http://localhost/page")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"test\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldAcceptRequestWithoutOrigin() throws Exception {
        Cookie csrfCookie = obtainCookieToken();
        String csrfToken = csrfCookie.getValue();

        mockMvc.perform(post("/api/test/data")
                        .cookie(csrfCookie)
                        .header(CSRF_HEADER_NAME, csrfToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"test\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldRejectCrossOriginRequestEvenWithValidToken() throws Exception {
        Cookie csrfCookie = obtainCookieToken();
        String csrfToken = csrfCookie.getValue();

        mockMvc.perform(post("/api/test/data")
                        .cookie(csrfCookie)
                        .header(CSRF_HEADER_NAME, csrfToken)
                        .header("Origin", "https://attacker.com")  // Браузер установит реальный Origin
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"attack\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldAcceptOriginWithPort() throws Exception {
        Cookie csrfCookie = obtainCookieToken();
        String csrfToken = csrfCookie.getValue();

        mockMvc.perform(post("/api/test/data")
                        .cookie(csrfCookie)
                        .header(CSRF_HEADER_NAME, csrfToken)
                        .header("Origin", "http://localhost:8080")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"test\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldRejectDifferentPort() throws Exception {
        Cookie csrfCookie = obtainCookieToken();
        String csrfToken = csrfCookie.getValue();

        mockMvc.perform(post("/api/test/data")
                        .cookie(csrfCookie)
                        .header(CSRF_HEADER_NAME, csrfToken)
                        .header("Origin", "http://localhost:9999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"test\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldRejectSubdomainAttack() throws Exception {
        Cookie csrfCookie = obtainCookieToken();
        String csrfToken = csrfCookie.getValue();

        mockMvc.perform(post("/api/test/data")
                        .cookie(csrfCookie)
                        .header(CSRF_HEADER_NAME, csrfToken)
                        .header("Origin", "http://evil.localhost")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"test\"}"))
                .andExpect(status().isForbidden());
    }

    private Cookie obtainCookieToken() {
        CsrfToken csrfToken1 = csrfTokenRepository.generateToken(new MockHttpServletRequest());
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockHttpServletRequest request = new MockHttpServletRequest();
        csrfTokenRepository.saveToken(csrfToken1, request, response);
        return response.getCookie(CSRF_COOKIE_NAME);
    }
}
