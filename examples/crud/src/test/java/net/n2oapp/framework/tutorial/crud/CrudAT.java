package net.n2oapp.framework.tutorial.crud;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.tutorial.crud.model.CreatePage;
import net.n2oapp.framework.tutorial.crud.model.CrudPage;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.codeborne.selenide.Configuration.browserSize;
import static com.codeborne.selenide.Configuration.headless;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CrudAT {
    @LocalServerPort
    private int port;

    private CrudPage crudPage;

    @BeforeAll
    public static void configure() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());

        System.setProperty("chromeoptions.args", "--no-sandbox,--verbose,--whitelisted-ips=''");

        headless = true;
        browserSize = "1920x1200";
    }

    @BeforeEach
    public void openProtoPage() {
        crudPage = Selenide.open("http://localhost:" + port, CrudPage.class);
        crudPage.table().shouldExists();
    }

    @Test
    @Order(1)
    public void checkCreate() {
        CreatePage createPage = crudPage.create();
        createPage.shouldHaveTitle("test - Создание");
        createPage.name().val("Иван");
        createPage.save();
        crudPage.shouldDialogClosed("test - Создание", 4000);
        crudPage.tableCellShouldHaveText(0,0,"5");
        crudPage.tableCellShouldHaveText(0,1,"Иван");
    }

    @Test
    @Order(1)
    public void checkUpdate() {
        CreatePage createPage = crudPage.update();
        createPage.shouldHaveTitle("test - Изменение");
        createPage.name().val("Артем");
        createPage.save();
        crudPage.shouldDialogClosed("test - Изменение", 4000);
        crudPage.tableCellShouldHaveText(0,0,"5");
        crudPage.tableCellShouldHaveText(0,1,"Артем");
    }

    @Test
    @Order(2)
    public void checkDelete() {
        crudPage.delete();
        crudPage.shouldBeDialog("Предупреждение");
        crudPage.acceptDialog("Предупреждение");
        crudPage.tableAlertColorShouldBe(Colors.SUCCESS);

        crudPage.tableCellShouldHaveText(0,0,"1");
        crudPage.tableCellShouldHaveText(0,1,"test1");
    }
}
