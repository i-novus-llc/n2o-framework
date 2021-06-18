package net.n2oapp.framework.autotest.action;

import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.field.StandardField;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.RegionItems;
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

/**
 * Автотест для действия обновления данных виджета
 */
public class RefreshActionAT extends AutoTestBase {

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
        builder.packs(new N2oAllPagesPack(), new N2oApplicationPack(), new N2oAllDataPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/refresh/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/refresh/test.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/refresh/test.object.xml"));
    }

    @Test
    public void testRefresh() {
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        RegionItems content = page.regions().region(0, SimpleRegion.class).content();
        FormWidget form = content.widget(FormWidget.class);
        TableWidget table = content.widget(1, TableWidget.class);

        TableWidget.Rows rows = table.columns().rows();
        rows.shouldHaveSize(2);

        StandardField nameField = form.fields().field("name");
        StandardButton sendBtn = nameField.toolbar().button("Send");
        InputText name = nameField.control(InputText.class);
        name.shouldBeEmpty();

        name.val("test3");
        sendBtn.click();
        rows.shouldHaveSize(2);
        form.toolbar().bottomLeft().button("Refresh table").click();
        rows.shouldHaveSize(3);
        rows.row(0).cell(1).textShouldHave("test3");

        name.val("test4");
        sendBtn.click();
        rows.shouldHaveSize(3);
        table.toolbar().topRight().button("Refresh").click();
        rows.shouldHaveSize(4);
        rows.row(0).cell(1).textShouldHave("test4");
    }
}
