package net.n2oapp.framework.sandbox.autotest.examples;

import net.n2oapp.framework.autotest.api.component.button.Button;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.snippet.Alert;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestApplication;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = AutoTestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GreetingAT extends AutoTestBase {

    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack(), new N2oApplicationPack(), new N2oAllDataPack());
        builder.sources(new CompileInfo("/examples/greeting/index.page.xml"),
                new CompileInfo("/examples/greeting/greeting.object.xml"));
    }

    @Test
    public void greetingTest() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.header().brandNameShouldBe("N2O");
        page.breadcrumb().crumb(0).shouldHaveLabel("Приветственное сообщение");

        Button button = page.widget(FormWidget.class).toolbar().bottomLeft()
                .button("Отправить");
        button.shouldExists();

        InputText inputText = page.widget(FormWidget.class).fields().field("Имя")
                .control(InputText.class);
        inputText.shouldExists();

        inputText.val("name");
        inputText.shouldHaveValue("name");

        button.click();
        page.alerts(Alert.Placement.top).alert(0).shouldHaveText("Привет, " + "name");
    }

}
