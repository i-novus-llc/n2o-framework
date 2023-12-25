package net.n2oapp.framework.autotest.action;

import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.page.Page;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.api.component.widget.StandardWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Автотест для подтверждения действия кнопки
 */
public class ConfirmActionAT extends AutoTestBase {

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
        builder.packs(new N2oApplicationPack(), new N2oAllPagesPack(), new N2oAllDataPack());
    }

    @Test
    void testDialog() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/action/confirm/type/index.page.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        StandardWidget.WidgetToolbar toolbar = page.widget(FormWidget.class).toolbar();

        StandardButton button = toolbar.bottomLeft().button("dialog");
        button.shouldBeEnabled();
        InputText test1 = page.widget(FormWidget.class).fields().field("test1").control(InputText.class);
        InputText test2 = page.widget(FormWidget.class).fields().field("test2").control(InputText.class);

        button.click();
        Page.Dialog dialog = page.dialog("Предупреждение");
        dialog.shouldBeVisible();
        dialog.shouldHaveText("confirm-text");
        dialog.button(1).shouldHaveLabel("Нет");
        dialog.button(1).click();
        test1.shouldBeEmpty();
        test2.shouldBeEmpty();

        button.click();
        dialog.shouldBeVisible();
        dialog.shouldHaveText("confirm-text");
        dialog.button(0).shouldHaveLabel("Да");
        dialog.button(0).click();
        test1.shouldHaveValue("qwerty2");
        test2.shouldHaveValue("zxc2");
    }

    @Test
    void testPopover() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/action/confirm/type/index.page.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        StandardWidget.WidgetToolbar toolbar = page.widget(FormWidget.class).toolbar();

        StandardButton button = toolbar.bottomLeft().button("popover");
        button.shouldBeEnabled();
        InputText test1 = page.widget(FormWidget.class).fields().field("test1").control(InputText.class);
        InputText test2 = page.widget(FormWidget.class).fields().field("test2").control(InputText.class);


        button.click();
        Page.Popover popover = page.popover("burn");
        popover.shouldBeVisible();
        popover.shouldHaveText("Going to hell?");
        popover.button(1).shouldHaveLabel("No no no");
        popover.button(1).click();
        test1.shouldBeEmpty();
        test2.shouldBeEmpty();

        button.click();
        popover.shouldBeVisible();
        popover.shouldHaveText("Going to hell?");
        popover.button(0).shouldHaveLabel("Hell, yes");
        popover.button(0).click();
        test1.shouldHaveValue("qwerty");
        test2.shouldHaveValue("zxc");

        //Проверка закрытия окна при клике вне
        button.click();
        popover.shouldBeVisible();
        test1.click();
        popover.shouldBeClosed();
    }

    @Test
    void testReverse() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/action/confirm/reverse/index.page.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        StandardWidget.WidgetToolbar toolbar = page.widget(FormWidget.class).toolbar();

        StandardButton popoverButton = toolbar.bottomLeft().button("popover");
        StandardButton dialogButton = toolbar.bottomLeft().button("dialog");

        InputText test1 = page.widget(FormWidget.class).fields().field("test1").control(InputText.class);

        popoverButton.click();
        Page.Popover popover = page.popover("burn");
        popover.shouldBeVisible();
        popover.button(1).shouldHaveLabel("No no no");
        popover.button(1).click();
        test1.shouldBeEmpty();

        popoverButton.click();
        popover.shouldBeVisible();
        popover.button(0).shouldHaveLabel("Hell, yes");
        popover.button(0).click();
        test1.shouldHaveValue("qwerty");

        dialogButton.click();
        Page.Dialog dialog = page.dialog("burn");
        dialog.shouldBeVisible();
        dialog.shouldHaveText("Going to hell?");
        dialog.button(1).shouldHaveLabel("No no no");
        dialog.button(1).click();
        test1.shouldHaveValue("qwerty");

        dialogButton.click();
        dialog.shouldBeVisible();
        dialog.shouldHaveText("Going to hell?");
        dialog.button(0).shouldHaveLabel("Hell, yes");
        dialog.button(0).click();
        test1.shouldHaveValue("qwerty2");
    }
}
