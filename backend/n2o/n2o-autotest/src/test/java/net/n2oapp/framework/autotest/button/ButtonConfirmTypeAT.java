package net.n2oapp.framework.autotest.button;

import net.n2oapp.framework.autotest.api.component.button.StandardButton;
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
public class ButtonConfirmTypeAT extends AutoTestBase {

    private SimplePage page;
    private StandardWidget.WidgetToolbar toolbar;

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
        toolbar = page.widget(FormWidget.class).toolbar();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oApplicationPack(), new N2oAllPagesPack(), new N2oAllDataPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/button/confirm_type/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/button/confirm_type/myObject.object.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"));
    }

    @Test
    public void testDialog() {
        StandardButton button = toolbar.bottomLeft().button("dialog");
        button.shouldBeEnabled();

        button.click();
        Page.Dialog dialog = page.dialog("Предупреждение");
        dialog.shouldBeVisible();
        dialog.shouldHaveText("confirm-text");
        dialog.click("Нет");
        page.alerts().alert(0).shouldNotExists();

        button.click();
        dialog.shouldBeVisible();
        dialog.shouldHaveText("confirm-text");
        dialog.click("Да");
        page.alerts().alert(0).shouldHaveText("success");
    }

    @Test
    public void testPopover() {
        StandardButton button = toolbar.bottomLeft().button("popover");
        button.shouldBeEnabled();

        button.click();
        Page.Popover popover = page.popover("Предупреждение");
        popover.shouldBeVisible();
        popover.shouldHaveText("confirm-text");
        popover.click("Нет");
        page.alerts().alert(0).shouldNotExists();

        button.click();
        popover.shouldBeVisible();
        popover.shouldHaveText("confirm-text");
        popover.click("Да");
        page.alerts().alert(0).shouldHaveText("success");
    }

    @Test
    public void testCustomPopover() {
        StandardButton button = toolbar.bottomLeft().button("CustomPopover");
        button.shouldBeEnabled();

        button.click();
        Page.Popover popover = page.popover("burn");
        popover.shouldBeVisible();
        popover.shouldHaveText("Going to hell?");
        popover.click("No no no");
        page.alerts().alert(0).shouldNotExists();

        button.click();
        popover.shouldBeVisible();
        popover.shouldHaveText("Going to hell?");
        popover.click("Hell, yes");
        page.alerts().alert(0).shouldHaveText("success");
    }

}