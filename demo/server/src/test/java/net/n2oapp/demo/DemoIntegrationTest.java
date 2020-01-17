package net.n2oapp.demo;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static com.codeborne.selenide.Configuration.browser;
import static com.codeborne.selenide.Configuration.headless;
import static com.codeborne.selenide.Selenide.open;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DemoIntegrationTest {

    @LocalServerPort
    private int port;

    private ProtoPage protoPage;

    @BeforeClass
    public static void configure() {
        browser = "chrome";
        headless = true;
    }

    @Before
    public void openProtoPage() {
        protoPage = open("http://localhost:" + port, ProtoPage.class);
    }

    /**
     * Тест поиска людей по фамилии
     */
    @Test
    public void testSurname() {
        protoPage.findBySurname("Иванов");
        protoPage.tableShouldHaveSize(1);
        protoPage.assertSurname(0, "Иванова");
    }

    /**
     * Тест очистки поиска
     */
    @Test
    public void testClear() {
        protoPage.findByName("Мария")
                .findByGender("Женский")
                .findByVip();
        protoPage.tableShouldHaveSize(1);
        protoPage.clearFilters().assertClearFilterFields();
    }
}
