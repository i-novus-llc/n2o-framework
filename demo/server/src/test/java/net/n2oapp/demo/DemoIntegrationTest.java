package net.n2oapp.demo;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.junit4.SpringRunner;

import static com.codeborne.selenide.Configuration.*;
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

    @Test
    @Primary
    public void checkAllElementsExists() {
        protoPage.checkAllElementsExists();
    }

    /**
     * Тест поиска людей по полу
     */
    @Test
    public void testFilterByGender() {
        protoPage.testFilterByGender();
    }

    @Test
    public void testTableSorting() {
        protoPage.testTableSorting();
    }

    @Test
    public void testTableEditBirthday() {
        protoPage.testTableEditBirthday();
    }

    /**
     * Тест поиска людей по фимилии и имени
     */
    @Test
    public void testFilterByNameAndSurname() {
        protoPage.testFilterByNameAndSurname();
    }
}
