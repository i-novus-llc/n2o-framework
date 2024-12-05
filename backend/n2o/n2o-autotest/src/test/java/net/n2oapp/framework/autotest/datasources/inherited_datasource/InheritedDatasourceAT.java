package net.n2oapp.framework.autotest.datasources.inherited_datasource;

import net.n2oapp.framework.autotest.api.component.button.Button;
import net.n2oapp.framework.autotest.api.component.cell.CheckboxCell;
import net.n2oapp.framework.autotest.api.component.control.InputSelect;
import net.n2oapp.framework.autotest.api.component.control.InputText;
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
 * Тестирование inherited-datasource
 */
public class InheritedDatasourceAT extends AutoTestBase {

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
     * Тестирование fetch зависимости
     */
    @Test
    void testFetchData() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/datasources/inherited_datasource/simple/index.page.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        InputText source = page.regions().region(0, SimpleRegion.class).content()
                .widget(0, FormWidget.class).fields().field("test").control(InputText.class);
        InputText inher = page.regions().region(0, SimpleRegion.class).content()
                .widget(1, FormWidget.class).fields().field("test").control(InputText.class);

        source.click();
        source.setValue("test");
        inher.shouldHaveValue("test");
        source.click();
        source.clear();
        inher.shouldBeEmpty();

        inher.click();
        inher.setValue("test");
        source.shouldBeEmpty();
        inher.shouldHaveValue("test");
    }

    /**
     * Тестирование модели Resolve
     */
    @Test
    void testModelResolve() {
        setJsonPath("net/n2oapp/framework/autotest/datasources/inherited_datasource/resolve");
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/datasources/inherited_datasource/resolve/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/inherited_datasource/resolve/test.query.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(TableWidget.class);
        InputText id = page.regions().region(1, SimpleRegion.class).content()
                .widget(FormWidget.class).fields().field("id").control(InputText.class);
        InputText name = page.regions().region(1, SimpleRegion.class).content()
                .widget(FormWidget.class).fields().field("name").control(InputText.class);

        id.shouldHaveValue("1");
        name.shouldHaveValue("test1");
        table.columns().rows().row(2).click();
        id.shouldHaveValue("3");
        name.shouldHaveValue("test3");
        table.columns().rows().row(1).click();
        id.shouldHaveValue("2");
        name.shouldHaveValue("test2");
        table.columns().rows().row(3).click();
        id.shouldHaveValue("4");
        name.shouldHaveValue("test4");
        table.columns().rows().row(0).click();
        id.shouldHaveValue("1");
        name.shouldHaveValue("test1");
    }

    /**
     * Тестирование модели Multi
     */
    @Test
    void testModelMulti() {
        setJsonPath("net/n2oapp/framework/autotest/datasources/inherited_datasource/multi");
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/datasources/inherited_datasource/multi/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/inherited_datasource/multi/test.query.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        TableWidget sourceTable = page.regions().region(0, SimpleRegion.class).content().widget(0, TableWidget.class);
        TableWidget childTable = page.regions().region(0, SimpleRegion.class).content().widget(1, TableWidget.class);

        sourceTable.columns().rows().shouldHaveSize(4);
        childTable.columns().rows().shouldHaveSize(0);
        childTable.columns().headers().header(0).shouldHaveTitle("id");
        childTable.columns().headers().header(1).shouldHaveTitle("name");
        sourceTable.columns().rows().row(0).cell(0, CheckboxCell.class).setChecked(true);

        childTable.columns().rows().row(0).cell(1).shouldHaveText("test1");
        sourceTable.columns().rows().row(1).cell(0, CheckboxCell.class).setChecked(true);
        sourceTable.columns().rows().row(2).cell(0, CheckboxCell.class).setChecked(true);
        childTable.columns().rows().row(1).cell(1).shouldHaveText("test2");
        childTable.columns().rows().row(2).cell(1).shouldHaveText("test3");

        sourceTable.columns().rows().row(0).cell(0, CheckboxCell.class).setChecked(false);
        sourceTable.columns().rows().row(1).cell(0, CheckboxCell.class).setChecked(false);
        childTable.columns().rows().row(0).cell(1).shouldHaveText("test3");
    }

    /**
     * Тестирование модели Datasource
     */
    @Test
    void testModelDatasource() {
        setJsonPath("net/n2oapp/framework/autotest/datasources/inherited_datasource/datasource");
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/datasources/inherited_datasource/datasource/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/inherited_datasource/datasource/test.query.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        TableWidget sourceTable = page.regions().region(0, SimpleRegion.class).content().widget(0, TableWidget.class);
        TableWidget childTable = page.regions().region(0, SimpleRegion.class).content().widget(1, TableWidget.class);

        sourceTable.columns().rows().shouldHaveSize(4);
        childTable.columns().rows().shouldHaveSize(4);
        childTable.columns().rows().columnShouldHaveTexts(0, List.of("1", "2", "3", "4"));
        childTable.columns().rows().columnShouldHaveTexts(1, List.of("test1", "test2", "test3", "test4"));
    }

    /**
     * Тестирование при котором данные таблицы берутся из поля
     */
    @Test
    void testSourceFieldId() {
        setJsonPath("net/n2oapp/framework/autotest/datasources/inherited_datasource/source_field");
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/datasources/inherited_datasource/source_field/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/inherited_datasource/source_field/test.query.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(1, TableWidget.class);
        table.columns().rows().columnShouldHaveTexts(0, List.of("Иванов И.И.", "Петров П.П.", "Кузнецов А.И."));
    }

    @Test
    void testFetchAndSubmitValue() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/datasources/inherited_datasource/fetch_submit_value/index.page.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        InputText rub = page.regions().region(0, SimpleRegion.class).content().widget(0, FormWidget.class).fields()
                .field("rub").control(InputText.class);
        InputText rate = page.regions().region(0, SimpleRegion.class).content().widget(0, FormWidget.class).fields()
                .field("rate").control(InputText.class);
        InputText dollar = page.regions().region(0, SimpleRegion.class).content().widget(1, FormWidget.class).fields()
                .field("dollar").control(InputText.class);

        Button submit = page.toolbar().bottomRight().button("submit");

        rub.click();
        rub.setValue("5");
        rate.shouldHaveValue("2");
        dollar.shouldHaveValue("10");

        submit.click();
        rub.shouldHaveValue("10");
        dollar.shouldHaveValue("20");

        rub.click();
        rub.setValue("8");
        rate.shouldHaveValue("2");
        dollar.shouldHaveValue("16");

        submit.click();
        rub.shouldHaveValue("16");
        rate.shouldHaveValue("2");
        dollar.shouldHaveValue("32");

        rate.click();
        rate.setValue("3");
        rub.shouldHaveValue("16");
        dollar.shouldHaveValue("48");

        rub.click();
        rub.clear();
        dollar.shouldHaveValue("0");
    }

    @Test
    void testFetchValueSourceFieldId() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/datasources/inherited_datasource/source_field_fetch_value/index.page.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        InputText rub = page.regions().region(0, SimpleRegion.class).content().widget(0, FormWidget.class).fields()
                .field("rub").control(InputText.class);
        InputText dollar = page.regions().region(0, SimpleRegion.class).content().widget(1, FormWidget.class).fields()
                .field("dollar").control(InputText.class);

        rub.click();
        rub.setValue("5");
        dollar.shouldHaveValue("10");
        rub.click();
        rub.setValue("7");
        dollar.shouldHaveValue("14");
    }

    @Test
    void testSubmitTargetFieldId() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/datasources/inherited_datasource/submit/index.page.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        InputText rub = page.regions().region(0, SimpleRegion.class).content().widget(0, FormWidget.class).fields()
                .field("rub").control(InputText.class);
        InputText other = page.regions().region(0, SimpleRegion.class).content().widget(0, FormWidget.class).fields()
                .field("other").control(InputText.class);
        InputText dollar = page.regions().region(0, SimpleRegion.class).content().widget(1, FormWidget.class).fields()
                .field("dollar").control(InputText.class);

        Button submit = page.toolbar().bottomRight().button("submit");

        rub.click();
        rub.setValue("4");
        dollar.shouldHaveValue("8");
        submit.click();
        other.shouldHaveValue("1.6");
    }

    @Test
    void testFetchDependencies() {
        setJsonPath("net/n2oapp/framework/autotest/datasources/inherited_datasource/fetch_depend");
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/datasources/inherited_datasource/fetch_depend/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/inherited_datasource/fetch_depend/test.query.xml"));

        StandardPage page = open(StandardPage.class);
        page.shouldExists();
        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(0, TableWidget.class);
        FormWidget form = page.regions().region(0, SimpleRegion.class).content().widget(1, FormWidget.class);

        InputSelect input = form.fields().field("Тип документа").control(InputSelect.class);
        input.shouldExists();
        input.shouldHaveValue("Свидетельство о рождении");
        table.columns().rows().row(1).click();
        input.shouldHaveValue("Документ 1");
        table.columns().rows().row(2).click();
        input.shouldHaveValue("Документ 2");

    }
}

