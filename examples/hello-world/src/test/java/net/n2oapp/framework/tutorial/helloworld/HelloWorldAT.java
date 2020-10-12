package net.n2oapp.framework.tutorial.helloworld;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import net.n2oapp.framework.tutorial.helloworld.model.HelloPage;
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
public class HelloWorldAT {
    @LocalServerPort
    private int port;

    private HelloPage helloPage;

    @BeforeAll
    public static void configure() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());

        System.setProperty("chromeoptions.args", "--no-sandbox,--verbose,--whitelisted-ips=''");

        headless = false;
        browserSize = "1920x1200";
    }

    @BeforeEach
    public void openProtoPage() {
        helloPage = Selenide.open("http://localhost:" + port, HelloPage.class);
        helloPage.form().shouldExists();
    }

    @Test
    @Order(1)
    public void checkHelloText() {
        helloPage.helloShouldHaveText("Привет Мир!");
    }

}
