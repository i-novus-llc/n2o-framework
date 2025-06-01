package net.n2oapp.framework.autotest.datasources.cached;

import com.codeborne.selenide.Selenide;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.component.DropDown;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.*;
import net.n2oapp.framework.autotest.api.component.field.ButtonField;
import net.n2oapp.framework.autotest.api.component.modal.Modal;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
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
 * Тестирование cached-datasource
 */
class CachedDatasourceAT extends AutoTestBase {

    @BeforeAll
    static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oApplicationPack(), new N2oAllPagesPack(), new N2oAllDataPack());
    }

    /**
     * Тестирование простого кейса
     */
    @Test
    void testSimple() {
        setResourcePath("net/n2oapp/framework/autotest/datasources/cached_datasource/simple");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/datasources/cached_datasource/simple/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/cached_datasource/simple/test.query.xml")
        );
        StandardPage page = open(StandardPage.class);
        page.shouldExists();
        InputText input = page.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class)
                .fields().field("Имя").control(InputText.class);
        input.shouldExists();
        input.shouldHaveValue("test1");
        input.click();
        input.setValue("new value");

        Selenide.sleep(500);
        Selenide.refresh();
        input.shouldHaveValue("new value");

        Selenide.sessionStorage().clear();
        Selenide.refresh();

        page.shouldExists();
        input.shouldExists();
        input.shouldHaveValue("test1");

        Selenide.sessionStorage().clear();
    }

    /**
     * Тестирование кейса с параметрами, от которых зависит кэш
     */
    @Test
    void testPathParams() {
        setResourcePath("net/n2oapp/framework/autotest/datasources/cached_datasource/by_params");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/datasources/cached_datasource/by_params/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/cached_datasource/by_params/list.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/cached_datasource/by_params/modal.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/cached_datasource/by_params/person.query.xml")
        );
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(TableWidget.class);
        table.shouldExists();
        StandardButton button = table.toolbar().bottomLeft().button("Открыть");
        button.click();
        checkOpenPage("Opened Name 1");

        table.columns().rows().row(1).click();
        button.click();
        checkOpenPage("Opened Name 2");

        Selenide.sessionStorage().clear();
    }

    @Test
    void testFetchOnInit() {
        setResourcePath("net/n2oapp/framework/autotest/datasources/cached_datasource/fetch_on_init");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/datasources/cached_datasource/fetch_on_init/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/cached_datasource/fetch_on_init/person.query.xml")
        );

        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(0, TableWidget.class);
        table.columns().rows().shouldHaveSize(2);
        table.columns().rows().row(0).cell(0).shouldHaveText("1");
        table.columns().rows().row(0).cell(1).shouldHaveText("Opened Name 1");
        table.columns().rows().row(1).cell(0).shouldHaveText("2");
        table.columns().rows().row(1).cell(1).shouldHaveText("Opened Name 2");
    }

    @Test
    void testFiltered() {
        setResourcePath("net/n2oapp/framework/autotest/datasources/cached_datasource/filtered");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/datasources/cached_datasource/filtered/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/cached_datasource/filtered/test.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/cached_datasource/filtered/page1.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/cached_datasource/filtered/page2.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/cached_datasource/filtered/page3.page.xml")
        );
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        checkValues(page, "Фильтр-константа");
        checkValues(page, "Фильтр из параметра");
        checkValues(page, "Фильтр из модели");
    }

    @Test
    void testInvalidateCache() {
        setResourcePath("net/n2oapp/framework/autotest/datasources/cached_datasource/invalidate_cache");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/datasources/cached_datasource/invalidate_cache/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/cached_datasource/invalidate_cache/test.query.xml")
        );
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        RadioGroup radioGroup = page.regions().region(0, SimpleRegion.class).content().widget(0, FormWidget.class)
                .fields().field("type").control(RadioGroup.class);

        ButtonField button = page.regions().region(0, SimpleRegion.class).content().widget(0, FormWidget.class).
                fields().field("Найти", ButtonField.class);

        InputText input = page.regions().region(0, SimpleRegion.class).content().widget(1, FormWidget.class)
                .fields().field("Тип документа").control(InputText.class);
        input.shouldBeEmpty();

        radioGroup.check("2");
        button.click();
        input.shouldHaveValue("Паспорт");

        radioGroup.check("1");
        button.click();
        input.shouldHaveValue("Свидетельство о рождении");

        radioGroup.check("2");
        button.click();
        input.shouldHaveValue("Паспорт");
    }

    private void checkOpenPage(String name) {
        StandardPage open = N2oSelenide.page(StandardPage.class);
        open.shouldExists();

        InputText input = open.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class)
                .fields().field("Имя").control(InputText.class);
        input.shouldExists();
        input.shouldHaveValue(name);
        input.click();
        input.setValue("new value");

        Selenide.sleep(500);
        Selenide.refresh();
        input.shouldHaveValue("new value");

        open.breadcrumb().crumb(0).click();
    }

    private void checkValues(SimplePage page, String buttonName) {
        page.toolbar().topLeft().button(buttonName).click();
        Modal modal = N2oSelenide.modal(Modal.class);
        modal.shouldExists();

        RegionItems region = modal.content(StandardPage.class).regions().region(0, SimpleRegion.class).content();

        FormWidget firstForm = region.widget(0, FormWidget.class);
        InputText firstValue = firstForm.fields().field("name (datasource)").control(InputText.class);
        firstValue.shouldHaveValue("test2");

        FormWidget secondForm = region.widget(1, FormWidget.class);
        InputText secondValue = secondForm.fields().field("name (cached-datasource)").control(InputText.class);
        secondValue.shouldHaveValue("test2");

        modal.close();
    }

    /**
     * Тестирование copy зависимости
     */
    @Test
    void testSimpleCopyDepend() {
        setResourcePath("net/n2oapp/framework/autotest/datasources/cached_datasource/copy_depend");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/datasources/cached_datasource/copy_depend/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/cached_datasource/copy_depend/test.query.xml"));

        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        InputText source = page.regions().region(0, SimpleRegion.class).content()
                .widget(0, FormWidget.class).fields().field("text1").control(InputText.class);
        OutputText copy = page.regions().region(0, SimpleRegion.class).content()
                .widget(1, FormWidget.class).fields().field("text2").control(OutputText.class);
        source.shouldExists();
        copy.shouldExists();
        source.click();
        source.setValue("test");
        copy.shouldHaveValue("test");
        source.click();
        source.clear();
        copy.shouldBeEmpty();
    }

    @Test
    void testFetchDependencies() {
        setResourcePath("net/n2oapp/framework/autotest/datasources/cached_datasource/fetch_depend");
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/datasources/cached_datasource/fetch_depend/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/cached_datasource/fetch_depend/test.query.xml"));

        StandardPage page = open(StandardPage.class);
        page.shouldExists();
        FormWidget form1 = page.regions().region(0, SimpleRegion.class).content().widget(0, FormWidget.class);
        FormWidget form2 = page.regions().region(0, SimpleRegion.class).content().widget(1, FormWidget.class);

        InputText input2 = form2.fields().field("Тип документа").control(InputText.class);
        input2.shouldExists();
        input2.shouldHaveValue("Свидетельство о рождении");
        input2.setValue("test");

        InputText input1 = form1.fields().field("name").control(InputText.class);
        input1.shouldExists();
        input1.setValue("a");

        input2.shouldHaveValue("Свидетельство о рождении");
    }

    @Test
    void testSaveFilters() {
        setResourcePath("net/n2oapp/framework/autotest/datasources/cached_datasource/save_filters");
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/datasources/cached_datasource/save_filters/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/cached_datasource/save_filters/test.query.xml"));

        StandardPage page = open(StandardPage.class);
        page.shouldExists();
        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(TableWidget.class);
        InputText input = table.filters().fields().field("name").control(InputText.class);
        input.shouldBeEmpty();
        table.columns().rows().shouldHaveSize(4);
        input.setValue("1");
        table.filters().toolbar().button("Найти").click();
        table.columns().rows().shouldHaveSize(1);
        page.shouldHaveUrlMatches(getBaseUrl() + "/#/\\?w1_name=1");
        Selenide.refresh();
        input.shouldHaveValue("1");
        table.columns().rows().shouldHaveSize(1);
        page.shouldHaveUrlMatches(getBaseUrl() + "/#/\\?w1_name=1");
    }

    /**
     * Тестирование вызова submit
     */
    @Test
    void testSubmit() {
        setResourcePath("net/n2oapp/framework/autotest/datasources/cached_datasource/submit");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/datasources/cached_datasource/submit/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/cached_datasource/submit/test.object.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/cached_datasource/submit/test.query.xml")
        );
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        FormWidget form = page.regions().region(0, SimpleRegion.class).content().widget(0, FormWidget.class);

        // проверяем начальные значения в полях формы и меняем на новые
        InputText input = form.fields().field("Имя").control(InputText.class);
        input.shouldHaveValue("test");
        input.setValue("new value");

        InputSelect select = form.fields().field("Пол").control(InputSelect.class);
        select.openPopup();
        DropDown dropdown = select.dropdown();
        dropdown.item(0).shouldBeSelected();
        dropdown.selectItem(1);

        DateInterval dates = form.fields().field("Даты отпуска").control(DateInterval.class);
        dates.beginShouldHaveValue("01.02.2025");
        dates.endShouldHaveValue("10.02.2025");
        dates.setValueInBegin("01.03.2025");
        dates.setValueInEnd("10.03.2025");

        // нажимаем кнопку submit и проверяем сохраненные значения в полях
        form.toolbar().topLeft().button("submit").click();

        input.shouldHaveValue("new value");

        select.openPopup();
        dropdown.item(1).shouldBeSelected();

        dates.beginShouldHaveValue("01.03.2025");
        dates.endShouldHaveValue("10.03.2025");
    }

    /**
     * Тестирование сохранения значения в storage
     */
    @Test
    void testStorage() {
        setResourcePath("net/n2oapp/framework/autotest/datasources/cached_datasource/storage");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/datasources/cached_datasource/storage/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/cached_datasource/storage/modal.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/cached_datasource/storage/test.query.xml")
        );
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(TableWidget.class);
        table.columns().rows().shouldHaveSize(2);
        table.toolbar().bottomLeft().button("Создать").click();

        Modal modal = N2oSelenide.modal(Modal.class);
        modal.shouldExists();
        FormWidget form = modal.content(StandardPage.class).regions().region(0, SimpleRegion.class).content().widget(0, FormWidget.class);
        form.fields().field("name").control(InputText.class).setValue("test3");
        modal.toolbar().bottomRight().button("Сохранить").click();
        modal.toolbar().bottomRight().button("Закрыть").click();

        table.columns().rows().shouldHaveSize(3);
        table.toolbar().bottomLeft().button("Обновить").click();
        table.columns().rows().shouldHaveSize(3);
        table.columns().rows().row(2).cell(0).shouldHaveText("3");
        table.columns().rows().row(2).cell(1).shouldHaveText("test3");

        Selenide.sessionStorage().clear();
    }
}
