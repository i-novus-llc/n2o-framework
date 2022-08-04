package net.n2oapp.framework.autotest.button;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import net.n2oapp.framework.autotest.api.component.button.Button;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DialogsButtonsAT extends AutoTestBase {
    private static final String BTN_LABEL = "Кнопка";
    private static final String DIALOG_LABEL = "Подтвердите действие";
    private static final String CONFIRM_BTN_LABEL = "Подтвердить";
    private static final String CANCEL_BTN_LABEL = "Отменить";
    private static final String ATTRIBUTE_CLASS = "class";
    private static final String BUTTONS_BLOCK_SELECTOR = "[class*='flex-row-reverse']";
    private static final String PRIMARY = "btn-primary";
    private static final String DANGER = "btn-danger";

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
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/button/dialogs_buttons/index.page.xml"));
        builder.properties("n2o.api.button.confirm.reverse_buttons=true", "n2o.api.button.disable_on_empty_model=false");
    }

    @Test
    public void testChangeBtnColor() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        FormWidget form = page.widget(FormWidget.class);
        Button btn = form.toolbar().bottomRight().button(BTN_LABEL);
        btn.shouldExists();
        btn.click();

        Button confirmBtn = page.dialog(DIALOG_LABEL).button(CONFIRM_BTN_LABEL);
        Button cancelBtn = page.dialog(DIALOG_LABEL).button(CANCEL_BTN_LABEL);

        Assertions.assertTrue(confirmBtn.element().getAttribute(ATTRIBUTE_CLASS).contains(PRIMARY), "Attribute class of button confirm isn't match expected");
        Assertions.assertTrue(cancelBtn.element().getAttribute(ATTRIBUTE_CLASS).contains(DANGER), "Attribute class of button cancel isn't match expected");
    }

    @Test
    public void testReverseButtons() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        FormWidget form = page.widget(FormWidget.class);
        Button btn = form.toolbar().bottomRight().button(BTN_LABEL);
        btn.shouldExists();
        btn.click();

        Assertions.assertTrue(Selenide.$(BUTTONS_BLOCK_SELECTOR).exists(), "Block with reversed buttons isn't exist");
    }
}
