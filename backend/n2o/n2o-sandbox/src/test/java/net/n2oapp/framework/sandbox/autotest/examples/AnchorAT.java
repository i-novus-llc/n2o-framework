package net.n2oapp.framework.sandbox.autotest.examples;

import com.codeborne.selenide.Selenide;
import net.n2oapp.framework.autotest.api.component.button.Button;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.sandbox.autotest.SandboxAutotestApplication;
import net.n2oapp.framework.sandbox.autotest.SandboxAutotestBase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "n2o.engine.test.classpath=/examples/modal/",
        "n2o.sandbox.project-id=examples_anchor"},
        classes = SandboxAutotestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AnchorAT extends SandboxAutotestBase {

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
    }

    @Test
    public void externalLinkTest() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.header().shouldHaveBrandName("N2O");
        page.breadcrumb().crumb(0).shouldHaveLabel("Поиск в яндексе");

        Button button = page.widget(FormWidget.class).toolbar().bottomLeft()
                .button("Найти");
        button.shouldExists();

        InputText inputText = page.widget(FormWidget.class).fields().field("Поиск")
                .control(InputText.class);
        inputText.shouldExists();
        inputText.shouldBeEmpty();

        inputText.click();
        inputText.setValue("yandex");
        inputText.shouldHaveValue("yandex");
        button.click();

        Selenide.switchTo().window(1);
        page.shouldHaveUrlLike("https://yandex.ru.*");
        Selenide.closeWindow();
    }
}
