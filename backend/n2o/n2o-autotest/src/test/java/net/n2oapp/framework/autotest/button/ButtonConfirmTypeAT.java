package net.n2oapp.framework.autotest.button;

import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.field.StandardField;
import net.n2oapp.framework.autotest.api.component.page.Page;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oHeaderPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Автотест для подтверждения действия кнопки
 */
public class ButtonConfirmTypeAT extends AutoTestBase {

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
        builder.packs(new N2oHeaderPack(), new N2oAllPagesPack(), new N2oAllDataPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/button/confirm_type/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/button/confirm_type/myObject.object.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"));
    }

    @Test
    public void testDialog() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        FormWidget form = page.widget(FormWidget.class);
        form.fields().shouldHaveSize(1);

        StandardField input = form.fields().field("Наименование");
        input.shouldExists();
        input.control(InputText.class).val("testDialog");

        StandardButton button = form.toolbar().bottomLeft().button("dialog");
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
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        FormWidget form = page.widget(FormWidget.class);
        form.fields().shouldHaveSize(1);

        StandardField input = form.fields().field("Наименование");
        input.shouldExists();
        input.control(InputText.class).val("testPopover");

        StandardButton button = form.toolbar().bottomLeft().button("popover");
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
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        FormWidget form = page.widget(FormWidget.class);
        form.fields().shouldHaveSize(1);

        StandardField input = form.fields().field("Наименование");
        input.shouldExists();
        input.control(InputText.class).val("testCustomPopover");

        StandardButton button = form.toolbar().bottomLeft().button("CustomPopover");
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