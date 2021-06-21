package net.n2oapp.framework.autotest.button;

import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.cell.ToolbarCell;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.field.ButtonField;
import net.n2oapp.framework.autotest.api.component.field.StandardField;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.api.component.widget.table.TableWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static net.n2oapp.framework.autotest.N2oSelenide.page;

/**
 * Автотест для проверки корректной работы кнопок в разных местах
 */
public class ButtonBindingAT extends AutoTestBase {

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
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oApplicationPack(), new N2oAllPagesPack(), new N2oAllDataPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/button/path_binding/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/button/path_binding/test.object.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/button/path_binding/test.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/button/path_binding/test.page.xml"));
    }

    @Test
    public void testButtons() {
        FormWidget form = page.widget(FormWidget.class);
        StandardField field = form.fields().field("Put name");
        field.shouldExists();
        field.control(InputText.class).val("Ivan");
        StandardButton button = form.toolbar().bottomLeft().button("Press");
        button.shouldExists();
        button.click();

        StandardPage openPage = page(StandardPage.class);
        openPage.shouldExists();
        SimpleRegion region = openPage.regions().region(0, SimpleRegion.class);
        FormWidget widget1 = region.content().widget(0, FormWidget.class);
        ButtonField buttonField = widget1.fields().field("Кнопка в поле", ButtonField.class);
        buttonField.shouldExists();
        buttonField.click();
        openPage.alerts().alert(0).shouldHaveText("Hello, Ivan");

        ButtonField controlButton = widget1.fields().field("Кнопка в контроле", ButtonField.class);
        controlButton.shouldExists();
        controlButton.click();
        openPage.alerts().alert(0).shouldHaveText("Hello, Ivan");

        StandardField fieldWithButton = widget1.fields().field("Кнопка в тулбаре поля", StandardField.class);
        StandardButton fieldToolbarButton = fieldWithButton.toolbar().button("Кнопка в тулбаре поля");
        fieldToolbarButton.shouldExists();
        fieldToolbarButton.click();
        openPage.alerts().alert(0).shouldHaveText("Hello, Ivan");

        TableWidget widget2 = region.content().widget(1, TableWidget.class);
        ToolbarCell toolbarCell = widget2.columns().rows().row(0).cell(0, ToolbarCell.class);
        StandardButton toolbarCellButton = toolbarCell.toolbar().button("Кнопка в ячейке");
        toolbarCellButton.shouldExists();
        toolbarCellButton.click();
        openPage.alerts().alert(0).shouldHaveText("Hello, Ivan");
    }
}