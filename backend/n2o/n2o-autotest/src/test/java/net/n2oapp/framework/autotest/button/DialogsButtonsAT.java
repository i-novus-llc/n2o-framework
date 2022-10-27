package net.n2oapp.framework.autotest.button;

import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.api.component.button.Button;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.page.Page;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DialogsButtonsAT extends AutoTestBase {
    private static final String BUTTONS_BLOCK_SELECTOR = "[class*='flex-row-reverse']";

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
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/button/dialogs_buttons/index.page.xml"));
        builder.properties("n2o.api.button.confirm.reverse_buttons=true", "n2o.api.button.disable_on_empty_model=false");
    }

    @Test
    public void testDialogBtnColors() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        FormWidget form = page.widget(FormWidget.class);
        Button btn = form.toolbar().bottomRight().button("Кнопка");
        btn.shouldExists();
        btn.click();

        Page.Dialog dialog = page.dialog("Подтвердите действие");
        dialog.shouldHaveReversedButtons();

        StandardButton confirmBtn = dialog.button("Подтвердить");
        confirmBtn.shouldHaveColor(Colors.PRIMARY);
        StandardButton cancelBtn = dialog.button("Отменить");
        cancelBtn.shouldHaveColor(Colors.DANGER);
    }
}
