package net.n2oapp.framework.autotest;

import com.codeborne.selenide.Selenide;
import net.n2oapp.framework.autotest.api.TestLeftRightPage;
import net.n2oapp.framework.autotest.api.TestRegion;
import net.n2oapp.framework.autotest.api.TestWidget;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.widget.Widget;
import net.n2oapp.framework.autotest.impl.N2oComponentLibrary;
import net.n2oapp.framework.autotest.page.TestPageObject;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SimpleTest.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SpringBootApplication
public class SimpleTest {
    @LocalServerPort
    private int port;

    @BeforeClass
    public static void beforeClass() throws Exception {
        N2oSelenide.setFactory(new ComponentFactory()
                .addLibrary(new N2oComponentLibrary())
                .addComponents(TestRegion.class, TestWidget.class, TestLeftRightPage.class));
    }

    @Test
    public void n2o() {
        TestLeftRightPage page = N2oSelenide.open("http://localhost:" + port + "/#/test", TestLeftRightPage.class);
        page.left().region(0, TestRegion.class).content().widget(TestWidget.class).textShouldBe("Left Region 0 Widget 0");
        page.left().region(0, TestRegion.class).content().widget(1, TestWidget.class).textShouldBe("Left Region 0 Widget 1");
        page.left().region(1, TestRegion.class).content().widget(TestWidget.class).textShouldBe("Left Region 1 Widget 0");
        page.right().region(0, SimpleRegion.class).content().widget(Widget.class).shouldExists();
    }

    @Test
    public void selenide() {
        TestPageObject page = Selenide.open("http://localhost:" + port + "/#/test", TestPageObject.class);
        page.getLeftWidget0().textShouldBe("Left Region 0 Widget 0");
        page.getLeftWidget1().textShouldBe("Left Region 0 Widget 1");
        page.getRightWidget0().textShouldBe("Right Region 0 Widget 0");
    }
}
