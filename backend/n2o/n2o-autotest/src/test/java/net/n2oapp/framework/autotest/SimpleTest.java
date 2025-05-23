package net.n2oapp.framework.autotest;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import net.n2oapp.framework.autotest.api.component.widget.Widget;
import net.n2oapp.framework.autotest.impl.collection.N2oRegions;
import net.n2oapp.framework.autotest.impl.collection.N2oWidgets;
import net.n2oapp.framework.autotest.impl.component.region.N2oRegionItems;
import net.n2oapp.framework.autotest.run.TestLeftRightPage;
import net.n2oapp.framework.autotest.run.TestPageObject;
import net.n2oapp.framework.autotest.run.TestRegion;
import net.n2oapp.framework.autotest.run.TestWidget;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.server.LocalServerPort;

import static net.n2oapp.framework.autotest.run.AutoTestUtil.checkChromeDriver;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

//@SpringBootTest(classes = AutoTestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SimpleTest {

    @LocalServerPort
    private int port;

//    @BeforeAll
    static void beforeClass() {
        checkChromeDriver();
        System.setProperty("chromeoptions.args", "--no-sandbox,--verbose,--whitelisted-ips=''");
        Configuration.headless = true;

        N2oSelenide.setFactory(
                new ComponentFactory()
                        .addCollections(N2oRegions.class, N2oWidgets.class, N2oRegionItems.class)
                        .addComponents(TestRegion.class, TestWidget.class, TestLeftRightPage.class)
        );
    }

    @Test
    @Disabled
    void n2o() {
        TestLeftRightPage page = N2oSelenide.open("http://localhost:" + port + "/test.html", TestLeftRightPage.class);
        page.left().region(0, TestRegion.class).content().widget(TestWidget.class).textShouldBe("Left Region 0 Widget 0");
        page.left().region(0, TestRegion.class).content().widget(1, TestWidget.class).textShouldBe("Left Region 0 Widget 1");
        page.left().region(1, TestRegion.class).content().widget(TestWidget.class).textShouldBe("Left Region 1 Widget 0");
        page.right().region(0, TestRegion.class).content().widget(Widget.class).shouldExists();
    }

    @Test
    @Disabled
    void selenide() {
        TestPageObject page = Selenide.open("http://localhost:" + port + "/test.html", TestPageObject.class);
        assertDoesNotThrow(() -> page.getLeftWidget0().textShouldBe("Left Region 0 Widget 0"));
        page.getLeftWidget1().textShouldBe("Left Region 0 Widget 1");
        page.getRightWidget0().textShouldBe("Right Region 0 Widget 0");
    }
}
