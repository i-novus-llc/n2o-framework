package net.n2oapp.framework.autotest.button;

import net.n2oapp.framework.autotest.Colors;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.control.OutputText;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.snippet.Alert;
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

/**
 * Автотест для кнопки {@code <clipboard-button>}
 */
class ClipboardButtonAT extends AutoTestBase {

    @BeforeAll
    static void beforeClass() {
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
        setJsonPath("net/n2oapp/framework/autotest/button/clipboard");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/button/clipboard/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/button/clipboard/test.query.xml"));
    }

    @Test
    void test() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        FormWidget form = page.widget(FormWidget.class);
        form.shouldExists();
        OutputText output = form.fields().field("СНИЛС").control(OutputText.class);
        output.shouldBeVisible();
        output.shouldHaveValue("123-456-789 01");

        StandardButton clipboardButton = form.fields().field("СНИЛС").toolbar().button(0, StandardButton.class);
        clipboardButton.shouldHaveIcon("fa fa-copy");
        clipboardButton.shouldHaveColor(Colors.LINK);

        clipboardButton.shouldHaveDescription("Копировать СНИЛС");
        clipboardButton.click();
        Alert alert = page.alerts(Alert.Placement.top).alert(0);
        alert.shouldExists();
        alert.shouldHaveText("Скопировано в буфер обмена");

        clipboardButton.hover();
        clipboardButton.tooltip().shouldExists();
        clipboardButton.tooltip().shouldHaveText(new String[]{"Копировать СНИЛС"});

        InputText input = form.fields().field("input").control(InputText.class);
        input.shouldBeEmpty();
        input.insert();
        input.shouldHaveValue("123-456-789 01");
    }
}