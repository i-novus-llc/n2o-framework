package net.n2oapp.demo;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selectors;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Primary;

import static com.codeborne.selenide.Configuration.browser;
import static com.codeborne.selenide.Configuration.headless;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = DemoApplication.class,
//        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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
        protoPage = open("https://n2o.i-novus.ru/next/demo", ProtoPage.class);
    }

    @Test
    @Primary
    public void checkAllElementsExists() {
        protoPage.checkAllElementsExists();
    }

    @Test
    public void testGender() {
        protoPage.assertGender();
    }

    @Test
    public void testSorting() {
        protoPage.assertSorting();
    }

    @Test
    public void name() {
        $(".n2o-page");
                $(".layout") //standard page
                .$$(".col-md-6").first(); //left
                $(".n2o-panel-region") //panel
        .$(Selectors.byText("Нет данных для отображения")).should(Condition.exist);
    }
}
