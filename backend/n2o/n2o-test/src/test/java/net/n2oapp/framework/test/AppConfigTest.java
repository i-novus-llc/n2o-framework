package net.n2oapp.framework.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.n2oapp.framework.api.context.ContextEngine;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Locale;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.main.allow-bean-definition-overriding=true")
public class AppConfigTest {
    @LocalServerPort
    private int port;

    @Autowired
    ContextEngine contextEngine;

    @Autowired
    @Qualifier("n2oMessageSource")
    private MessageSource messageSource;

    @BeforeEach
    void setUp() {
        contextEngine.set("username", "testuser");
    }

    @Test
    public void servletTest() throws IOException, JSONException {
        ObjectMapper objectMapper = new ObjectMapper();
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format("http://localhost:%d/n2o/config", port);
        HttpEntity<?> entity = new HttpEntity<>(new HttpHeaders());

        ObjectNode expectedNode = (ObjectNode) objectMapper.readTree(this.getClass().getClassLoader().getResourceAsStream("META-INF/config.json"));
        expectedNode.put("username", "testuser");

        ResponseEntity<ObjectNode> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, ObjectNode.class);
        ObjectNode dataNode = responseEntity.getBody();

        String expected = objectMapper.writeValueAsString(expectedNode);
        String data = objectMapper.writeValueAsString(dataNode);

        JSONAssert.assertEquals(expected, data, false);
    }

    @Test
    public void testMessageSourceConfig() {
        assert "серверный".equals(messageSource.getMessage("n2o.server", null, Locale.forLanguageTag("ru")));
        assert "несистемный".equals(messageSource.getMessage("n2o.system", null, Locale.forLanguageTag("ru")));
    }
}
