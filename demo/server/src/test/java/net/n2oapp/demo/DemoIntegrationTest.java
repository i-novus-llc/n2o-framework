package net.n2oapp.demo;

import net.n2oapp.demo.model.ProtoPage;
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
        browserSize = "1920x1200";
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

    @Test
    public void testFilterByGender() {
        protoPage.testFilterByGender();
    }

    @Test
    public void testTableSorting() {
        protoPage.testTableSorting();
    }

    @Test
    public void testSurnameCell() {
        protoPage.testSurnameCell();
    }

    @Test
    public void testPatronymicCell() {
        protoPage.testPatronymicCell();
    }

    @Test
    public void testTableEditBirthday() {
        protoPage.testTableEditBirthday();
    }

    @Test
    public void testAddClient() {
        protoPage.assertAddClient();
    }

    @Test
    public void testCreateClient() {
        protoPage.assertCreateClient();
    }

    @Test
    public void testUpdateClient() {
        protoPage.assertUpdateClient();
    }

    @Test
    public void testUpdateClientFromToolbarCell() {
        protoPage.assertUpdateClientFromToolbarCell();
    }

    @Test
    public void testViewClient() {
        protoPage.assertViewClient();
    }

    @Test
    public void testTableInPlaceDelete() {
        protoPage.testTableInPlaceDelete();
    }

    @Test
    public void testTableRowDelete() {
        protoPage.testTableRowDelete();
    }
}
