package net.n2oapp.framework.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.main.allow-bean-definition-overriding=true")
public class AppConfigTest {
    @LocalServerPort
    private int port;

    @Test
    public void servletTest() throws IOException, JSONException {
        ObjectMapper objectMapper = new ObjectMapper();
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format("http://localhost:%d/n2o/config", port);
        HttpEntity<?> entity = new HttpEntity<>(new HttpHeaders());

        ObjectNode expectedNode = (ObjectNode) objectMapper.readTree(this.getClass().getClassLoader().getResourceAsStream("META-INF/config.json"));

        ResponseEntity<ObjectNode> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, ObjectNode.class);
        ObjectNode dataNode = responseEntity.getBody();

        String expected = objectMapper.writeValueAsString(expectedNode);
        String data = objectMapper.writeValueAsString(dataNode);

        JSONAssert.assertEquals(expected, data, false);
    }
}
