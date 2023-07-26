package net.n2oapp.framework.autotest.widget.table;

import net.n2oapp.framework.autotest.api.component.control.OutputText;
import net.n2oapp.framework.autotest.api.component.field.ButtonField;
import net.n2oapp.framework.autotest.api.component.fieldset.SimpleFieldSet;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.RegionItems;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.region.TabsRegion;
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
 * Автотест для проверки работы свойства fetch-on-init у виджетов
 */

public class FetchOnInitAT extends AutoTestBase {
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
    public void test() {
        setJsonPath("net/n2oapp/framework/autotest/widget/table/fetch_on_init/simple");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/fetch_on_init/simple/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/fetch_on_init/simple/form.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/fetch_on_init/simple/table.query.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        RegionItems regionItems = page.regions().region(0, SimpleRegion.class).content();
        FormWidget formWidget = regionItems.widget(0, FormWidget.class);
        formWidget.shouldExists();
        OutputText firstFieldName = formWidget.fieldsets().fieldset(1, SimpleFieldSet.class).fields().field("Имя").control(OutputText.class);
        firstFieldName.shouldExists();
        firstFieldName.shouldBeEmpty();
        ButtonField refreshField = formWidget.fieldsets().fieldset(0, SimpleFieldSet.class).fields().field("Обновить", ButtonField.class);
        refreshField.shouldExists();
        refreshField.click();
        firstFieldName.shouldHaveValue("test1");

        FormWidget secondFormWidget = regionItems.widget(1, FormWidget.class);
        formWidget.shouldExists();
        OutputText secondFieldName = secondFormWidget.fieldsets().fieldset(0, SimpleFieldSet.class).fields().field("Имя").control(OutputText.class);
        secondFieldName.shouldExists();
        secondFieldName.shouldHaveValue("test1");

        TableWidget tableWidget = regionItems.widget(2, TableWidget.class);
        tableWidget.shouldExists();
        TableWidget.Rows rows = tableWidget.columns().rows();
        rows.shouldNotHaveRows();
        tableWidget.filters().toolbar().button("Найти").click();
        rows.shouldHaveSize(4);
    }

    @Test
    public void testOnTabs() {
        setJsonPath("net/n2oapp/framework/autotest/widget/table/fetch_on_init/tabs");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/fetch_on_init/tabs/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/fetch_on_init/tabs/form.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/fetch_on_init/tabs/table.query.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        TabsRegion tabsRegion = page.regions().region(0, TabsRegion.class);
        tabsRegion.tab(0).content().widget(0, TableWidget.class).columns().rows().shouldHaveSize(0);
        tabsRegion.tab(1).click();
        tabsRegion.tab(1).content().widget(0, TableWidget.class).columns().rows().shouldHaveSize(0);
        tabsRegion.tab(1).content().widget(1, TableWidget.class).columns().rows().shouldHaveSize(4);
    }
}
