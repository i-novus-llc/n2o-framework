package net.n2oapp.framework.autotest.button;

import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.cell.ToolbarCell;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.field.ButtonField;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.api.component.widget.table.TableWidget;
import net.n2oapp.framework.autotest.impl.component.region.N2oSimpleRegion;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oHeaderPack;
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
        builder.packs(new N2oHeaderPack(), new N2oAllPagesPack(), new N2oAllDataPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/button/path_binding/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/button/path_binding/test.object.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/button/path_binding/test.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/button/path_binding/test.page.xml"));
    }

    @Test
    public void testDialog() {
        FormWidget form = page.widget(FormWidget.class);
        form.fields().field("Put name").control(InputText.class).val("Ivan");
        StandardButton button = form.toolbar().bottomLeft().button("Press");
        button.click();

        StandardPage newPage = page(StandardPage.class);
        N2oSimpleRegion region = newPage.regions().region(0, N2oSimpleRegion.class);
        FormWidget widget1 = region.content().widget(0, FormWidget.class);
        widget1.fields().field("Кнопка в поле", ButtonField.class).click();
        page.alerts().alert(0).shouldHaveText("Hello, Ivan");
        widget1.fields().field("Кнопка в контроле", ButtonField.class).click();
        page.alerts().alert(0).shouldHaveText("Hello, Ivan");
        TableWidget widget2 = region.content().widget(1, TableWidget.class);
        ToolbarCell toolbarCell = widget2.columns().rows().row(0).cell(0, ToolbarCell.class);
        toolbarCell.toolbar().button("Кнопка в ячейке").click();
        page.alerts().alert(0).shouldHaveText("Hello, Ivan");
    }
}