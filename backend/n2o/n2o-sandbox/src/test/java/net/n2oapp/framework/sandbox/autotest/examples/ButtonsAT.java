package net.n2oapp.framework.sandbox.autotest.examples;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.api.collection.Toolbar;
import net.n2oapp.framework.autotest.api.component.Tooltip;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.sandbox.autotest.SandboxAutotestApplication;
import net.n2oapp.framework.sandbox.autotest.SandboxAutotestBase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {"n2o.sandbox.project-id=examples_buttons"},
        classes = SandboxAutotestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ButtonsAT extends SandboxAutotestBase {

    private SimplePage page;

    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();

        page = open(SimplePage.class);
        page.shouldExists();
        page.header().brandNameShouldBe("N2O");
        page.breadcrumb().titleShouldHaveText("Кнопки");
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.sources(new CompileInfo("net/n2oapp/framework/config/default/default.application.xml"));
    }

    @Test
    public void testButtonsLeft() {
        Toolbar topLeft = page.widget(FormWidget.class).toolbar().topLeft();
        topLeft.shouldHaveSize(7);

        StandardButton button = topLeft.button(0, StandardButton.class);
        button.shouldExists();
        button.shouldHaveLabel("Кнопка");
        button.click();

        StandardButton buttonWithIcon = topLeft.button(1, StandardButton.class);
        buttonWithIcon.shouldExists();
        buttonWithIcon.shouldBeEnabled();
        buttonWithIcon.shouldHaveLabel("Кнопка  с иконкой");
        buttonWithIcon.shouldHaveIcon("fa-plus");
        buttonWithIcon.click();

        StandardButton buttonIcon = topLeft.button(2, StandardButton.class);
        buttonIcon.shouldExists();
        buttonIcon.shouldBeEnabled();
        buttonIcon.shouldHaveIcon("fa-pencil");
        buttonIcon.click();

        StandardButton buttonMenu = topLeft.button(3, StandardButton.class);
        buttonMenu.shouldExists();
        buttonMenu.shouldBeEnabled();
        buttonMenu.shouldHaveLabel("Меню");
        buttonMenu.click();
        topLeft.dropdown().menuItem("Один").shouldExists();
        topLeft.dropdown().menuItem("Два").shouldExists();

        StandardButton buttonMenuItem1 = topLeft.button(4, StandardButton.class);
        buttonMenuItem1.shouldExists();
        buttonMenuItem1.shouldBeEnabled();
        buttonMenuItem1.shouldHaveLabel("Один");
        buttonMenuItem1.click();

        buttonMenu.shouldHaveLabel("Меню");
        buttonMenu.click();

        StandardButton buttonMenuItem2 = topLeft.button(5, StandardButton.class);
        buttonMenuItem2.shouldExists();
        buttonMenuItem2.shouldBeEnabled();
        buttonMenuItem2.shouldHaveLabel("Два");
        buttonMenuItem2.click();

        StandardButton buttonWithTooltip = topLeft.button(6, StandardButton.class);
        buttonWithTooltip.shouldExists();
        buttonWithTooltip.shouldBeEnabled();
        buttonWithTooltip.shouldHaveLabel("Подсказка");
        buttonWithTooltip.element().hover();
        Tooltip tooltip = buttonWithTooltip.tooltip();
        tooltip.shouldExists();
        tooltip.shouldHaveText("Эта подсказка появляется сверху");
        buttonWithTooltip.click();
    }

    @Test
    public void testButtonsRight() {
        Toolbar topRight = page.widget(FormWidget.class).toolbar().topRight();
        topRight.shouldHaveSize(3);

        StandardButton buttonMain = topRight.button(0, StandardButton.class);
        buttonMain.shouldExists();
        buttonMain.shouldBeEnabled();
        buttonMain.shouldHaveLabel("Главная");
        buttonMain.shouldHaveColor(Colors.PRIMARY);
        buttonMain.element().shouldHave(Condition.cssClass("btn-primary"));
        buttonMain.click();

        StandardButton buttonDanger = topRight.button(1, StandardButton.class);
        buttonDanger.shouldExists();
        buttonDanger.shouldBeEnabled();
        buttonDanger.shouldHaveLabel("Опасная");
        buttonDanger.element().shouldHave(Condition.cssClass("btn-danger"));
        buttonDanger.shouldHaveColor(Colors.DANGER);
        buttonDanger.click();

        StandardButton buttonLink = topRight.button(2, StandardButton.class);
        buttonLink.shouldExists();
        buttonLink.shouldBeEnabled();
        buttonLink.shouldHaveLabel("Ссылка");
        buttonLink.click();
    }

}
