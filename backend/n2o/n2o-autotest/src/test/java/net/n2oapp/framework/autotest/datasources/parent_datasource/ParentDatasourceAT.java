package net.n2oapp.framework.autotest.datasources.parent_datasource;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.cell.CheckboxCell;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.modal.Modal;
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

import java.util.List;

/**
 * Тестирование parent-datasource
 */
public class ParentDatasourceAT extends AutoTestBase {

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

    /**
     * Тестирование фильтров от parent-datasource
     */
    @Test
    public void testFiltersByParent() {
        setJsonPath("net/n2oapp/framework/autotest/datasources/parent_datasource/filter_by_parent");
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/datasources/parent_datasource/filter_by_parent/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/parent_datasource/filter_by_parent/modal.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/parent_datasource/filter_by_parent/test.query.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        StandardButton showModalBtn = page.regions().region(0, SimpleRegion.class).content()
                .widget(0, FormWidget.class).toolbar().topLeft().button("История изменений");
        showModalBtn.shouldExists();
        showModalBtn.click();
        Modal modalPage = N2oSelenide.modal();
        modalPage.shouldExists();
        modalPage.shouldHaveTitle("История изменений");
        TableWidget.Rows rows = modalPage.content(StandardPage.class).regions().region(0, SimpleRegion.class).content()
                .widget(1, TableWidget.class).columns().rows();
        rows.shouldHaveSize(3);
        rows.row(0).cell(0).textShouldHave("1");
        rows.row(1).cell(0).textShouldHave("2");
        rows.row(2).cell(0).textShouldHave("3");
    }
}

