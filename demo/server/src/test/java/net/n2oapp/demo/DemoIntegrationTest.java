package net.n2oapp.demo;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static com.codeborne.selenide.Configuration.browser;
import static com.codeborne.selenide.Configuration.headless;
import static com.codeborne.selenide.Selenide.open;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DemoApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DemoIntegrationTest {

    @LocalServerPort
    private int port;

    private ProtoPage protoPage;

    @BeforeAll
    public static void configure() {
        browser = "chrome";
        headless = true;
    }

    @BeforeEach
    public void openProtoPage() {
        protoPage = open("http://localhost:" + port, ProtoPage.class);
    }

    @Test
    @Order(1)
    public void checkStaticContent() throws IOException {
        HttpUriRequest request = new HttpGet("http://localhost:" + port + "/index.html");
        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);
        assertThat(httpResponse.getStatusLine().getStatusCode(), equalTo(HttpStatus.SC_OK));
    }

    @Test
    @Primary
    @Order(2)
    public void checkAllElementsExists() {
        protoPage.checkAllElementsExists();
    }

    @Test
    @Order(3)
    public void testFilterByGender() {
        protoPage.testFilterByGender();
    }

    @Test
    @Order(4)
    public void testTableSorting() {
        protoPage.testTableSorting();
    }

    @Test
    @Order(5)
    public void testTableEditBirthday() {
        protoPage.testTableEditBirthday();
    }
}
