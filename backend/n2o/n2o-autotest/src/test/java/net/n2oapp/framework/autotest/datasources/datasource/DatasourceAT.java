package net.n2oapp.framework.autotest.datasources.datasource;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.button.Button;
import net.n2oapp.framework.autotest.api.component.control.DateInput;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.control.OutputText;
import net.n2oapp.framework.autotest.api.component.field.ButtonField;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.region.TabsRegion;
import net.n2oapp.framework.autotest.api.component.snippet.Alert;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.api.component.widget.cards.CardsWidget;
import net.n2oapp.framework.autotest.api.component.widget.table.TableWidget;
import net.n2oapp.framework.autotest.api.component.widget.tiles.TilesWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

/**
 * Тестирование Datasource
 */
class DatasourceAT extends AutoTestBase {

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
        builder.packs(
                new N2oApplicationPack(),
                new N2oAllPagesPack(),
                new N2oAllDataPack()
        );
    }

    /**
     * Тестирование формы как фильтра
     */
    @Test
    void testFormAsFilter() {
        setResourcePath("net/n2oapp/framework/autotest/datasources/datasource/form_as_filter");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/datasources/datasource/form_as_filter/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/datasource/form_as_filter/test.query.xml")
        );

        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        InputText filterId = page.regions().region(0, SimpleRegion.class).content()
                .widget(0, FormWidget.class).fields().field("Фильтр по id").control(InputText.class);
        InputText filterName = page.regions().region(0, SimpleRegion.class).content()
                .widget(0, FormWidget.class).fields().field("Фильтр по name").control(InputText.class);
        Button searchButton = page.regions().region(0, SimpleRegion.class).content()
                .widget(0, FormWidget.class).toolbar().topLeft().button("Найти");
        Button clearButton = page.regions().region(0, SimpleRegion.class).content()
                .widget(0, FormWidget.class).toolbar().topLeft().button("Очистить");

        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(1, TableWidget.class);

        table.columns().rows().shouldHaveSize(6);
        filterId.click();
        filterId.setValue("3");
        searchButton.click();
        table.columns().rows().shouldHaveSize(1);
        table.columns().rows().row(0).cell(0).shouldHaveText("3");
        table.columns().rows().row(0).cell(1).shouldHaveText("test3");
        filterName.click();
        filterName.setValue("test1");
        searchButton.click();
        table.columns().rows().shouldHaveSize(0);
        clearButton.click();
        table.columns().rows().shouldHaveSize(6);

        filterId.click();
        filterId.clear();
        filterName.click();
        filterName.setValue("test4");
        searchButton.click();
        table.columns().rows().shouldHaveSize(1);
        table.columns().rows().row(0).cell(0).shouldHaveText("4");
        table.columns().rows().row(0).cell(1).shouldHaveText("test4");
        clearButton.click();

        filterId.click();
        filterId.setValue("1");
        filterName.click();
        filterName.setValue("test1");
        searchButton.click();
        table.columns().rows().shouldHaveSize(1);
        table.columns().rows().row(0).cell(0).shouldHaveText("1");
        table.columns().rows().row(0).cell(1).shouldHaveText("test1");
        clearButton.click();

        filterId.click();
        filterId.setValue("1");
        filterName.click();
        filterName.setValue("test2");
        searchButton.click();
        table.columns().rows().shouldHaveSize(0);
        clearButton.click();

        filterId.click();
        filterId.clear();
        filterName.click();
        filterName.setValue("repeatTest");
        searchButton.click();
        table.columns().rows().shouldHaveSize(2);
        table.columns().rows().columnShouldHaveTexts(1, Arrays.asList("repeatTest", "repeatTest"));
        clearButton.click();

        filterId.click();
        filterId.setValue("6");
        filterName.click();
        filterName.setValue("repeatTest");
        searchButton.click();
        table.columns().rows().shouldHaveSize(1);
    }

    /**
     * Тестирование одного datasource на несколько виджетов
     */
    @Test
    void testOneDSManyWidgets() {
        setResourcePath("net/n2oapp/framework/autotest/datasources/datasource/test_one_ds_many_widgets");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/datasources/datasource/test_one_ds_many_widgets/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/datasource/test_one_ds_many_widgets/test.query.xml")
        );

        StandardPage page = open(StandardPage.class);
        page.shouldExists();
        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(0, TableWidget.class);
        TilesWidget tiles = page.regions().region(0, SimpleRegion.class).content().widget(1, TilesWidget.class);
        CardsWidget cards = page.regions().region(0, SimpleRegion.class).content().widget(2, CardsWidget.class);

        table.columns().rows().shouldHaveSize(4);
        tiles.paging().shouldHaveTotalElements(4);
        cards.paging().shouldHaveTotalElements(4);
    }

    /**
     * Тестирование валидации на несколько форм
     */
    //ToDo создать задачу и исправить
    @Test
    @Disabled
    void testValidationManyForm() {
        setResourcePath("net/n2oapp/framework/autotest/datasources/datasource/validation_many_form");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/datasources/datasource/validation_many_form/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/datasource/validation_many_form/test.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/datasource/validation_many_form/test.object.xml")
        );

        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        //проверка, что имя и валидная дата сохранены
        validationManyForm(page, "Сергей", dateTimeFormatter.format(LocalDate.now().minusDays(1)), "Данные сохранены", 1);

        //проверка, что сохранение не происходит при существующем имени
        validationManyForm(page, "Сергей", dateTimeFormatter.format(LocalDate.now().minusDays(1)), "Имя Сергей уже существует", 1);

        //проверка, что сохранение происходит при новом имени
        validationManyForm(page, "Артем", dateTimeFormatter.format(LocalDate.now().minusDays(1)), "Данные сохранены", 2);

        //проверка, что сохранение не происходит при невалидной дате
        validationManyForm(page, "Иван", dateTimeFormatter.format(LocalDate.now().plusDays(1)), "Дата рождения не может быть в будущем", 2);
    }

    private void validationManyForm(StandardPage page, String name, String date, String alertMessage, Integer rowsSizeExpected) {
        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(0, TableWidget.class);
        InputText nameInput = page.regions().region(0, SimpleRegion.class).content().widget(1, FormWidget.class)
                .fields().field("Наименование").control(InputText.class);
        DateInput birthdayInput = page.regions().region(0, SimpleRegion.class).content().widget(2, FormWidget.class)
                .fields().field("Дата рождения").control(DateInput.class);
        Button createButton = page.toolbar().bottomLeft().button("Создать");
        Alert alert = page.alerts(Alert.PlacementEnum.top).alert(0);

        nameInput.click();
        nameInput.setValue(name);
        birthdayInput.clickCalendarButton();
        birthdayInput.setValue(date);
        createButton.click();
        alert.shouldExists();
        alert.shouldHaveText(alertMessage);
        table.columns().rows().shouldHaveSize(rowsSizeExpected);
    }

    /**
     * Тестирование copy зависимости
     */
    @Test
    void testSimpleCopyDepend() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/datasources/datasource/copy_depend/index.page.xml")
        );

        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        InputText source = page.regions().region(0, SimpleRegion.class).content()
                .widget(0, FormWidget.class).fields().field("text1").control(InputText.class);
        OutputText copy = page.regions().region(0, SimpleRegion.class).content()
                .widget(1, FormWidget.class).fields().field("text2").control(OutputText.class);

        source.click();
        source.setValue("test");
        copy.shouldHaveValue("test");
        source.click();
        source.clear();
        copy.shouldBeEmpty();
    }

    /**
     * Тестирование copy зависимости.
     * Форма берет данные выбранной записи в таблице
     */
    @Test
    void testCopyDepend() {
        setResourcePath("net/n2oapp/framework/autotest/datasources/datasource/copy_depend_resolve");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/datasources/datasource/copy_depend_resolve/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/datasource/copy_depend_resolve/modal.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/datasource/copy_depend_resolve/test.query.xml")
        );

        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(0, TableWidget.class);
        InputText id = page.regions().region(0, SimpleRegion.class).content()
                .widget(1, FormWidget.class).fields().field("id").control(InputText.class);
        InputText name = page.regions().region(0, SimpleRegion.class).content()
                .widget(1, FormWidget.class).fields().field("name").control(InputText.class);

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

        FormWidget form = page.regions().region(0, SimpleRegion.class).content().widget(2, FormWidget.class);
        String uuid = form.fields().field("id").control(InputText.class).getValue();
        form.fields().field("next", ButtonField.class).click();

        StandardPage modal = N2oSelenide.modal().content(StandardPage.class);
        SimpleRegion region = modal.regions().region(0, SimpleRegion.class);
        OutputText profileId = region.content().widget(0, FormWidget.class).fields().field("profileId").control(OutputText.class);
        profileId.shouldHaveValue(uuid);
    }

    /**
     * Тестирование действия Submit
     */
    @Test
    void testSubmit() {
        setResourcePath("net/n2oapp/framework/autotest/datasources/datasource/submit");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/datasources/datasource/submit/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/datasource/submit/test.object.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/datasource/submit/test.query.xml")
        );

        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(0, TableWidget.class);
        InputText name = page.regions().region(0, SimpleRegion.class).content()
                .widget(1, FormWidget.class).fields().field("name").control(InputText.class);
        Button button = page.regions().region(0, SimpleRegion.class).content()
                .widget(1, FormWidget.class).toolbar().bottomLeft().button("Создать");

        name.click();
        name.setValue("submit-test");
        button.click();
        table.columns().rows().row(0).cell(1).shouldHaveText("submit-test");
    }

    /**
     * Тестирование атрибута default-values-mode
     */
    @Test
    void testDefaultValuesMode() {
        setResourcePath("net/n2oapp/framework/autotest/datasources/datasource/default_values_mode");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/datasources/datasource/default_values_mode/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/datasource/default_values_mode/test.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/datasource/default_values_mode/form.widget.xml")
        );

        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        TabsRegion tabs = page.regions().region(0, TabsRegion.class);
        TabsRegion.TabItem tab = tabs.tab(Condition.text("default-values-mode='query'"));
        Fields fields = tab.content().widget(FormWidget.class).fields();
        fields.field("withoutDefault").control(InputText.class).shouldHaveValue("test");
        fields.field("withDefault").control(InputText.class).shouldHaveValue("test");
        fields.field("copiedTrue").control(InputText.class).shouldHaveValue("test");
        fields.field("copiedFalse").control(InputText.class).shouldHaveValue("test");
        fields.field("notExist").control(InputText.class).shouldBeEmpty();

        tab = tabs.tab(Condition.text("default-values-mode='defaults'"));
        tab.click();
        fields = tab.content().widget(FormWidget.class).fields();
        fields.field("withoutDefault").control(InputText.class).shouldBeEmpty();
        fields.field("withDefault").control(InputText.class).shouldHaveValue("default");
        fields.field("copiedTrue").control(InputText.class).shouldHaveValue("default");
        fields.field("copiedFalse").control(InputText.class).shouldHaveValue("default");
        fields.field("notExist").control(InputText.class).shouldHaveValue("default");

        tab = tabs.tab(Condition.text("default-values-mode='merge'"));
        tab.click();
        fields = tab.content().widget(FormWidget.class).fields();
        fields.field("withoutDefault").control(InputText.class).shouldHaveValue("test");
        fields.field("withDefault").control(InputText.class).shouldHaveValue("test");
        fields.field("copiedTrue").control(InputText.class).shouldHaveValue("test");
        fields.field("copiedFalse").control(InputText.class).shouldHaveValue("default");
        fields.field("notExist").control(InputText.class).shouldHaveValue("default");
    }

    @Test
    void testFetchOnInitUnique() {
        setResourcePath("net/n2oapp/framework/autotest/datasources/datasource/fetch_on_init_unique");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/datasources/datasource/fetch_on_init_unique/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/datasource/fetch_on_init_unique/test.query.xml")
        );

        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        InputText test = page.regions().region(0, SimpleRegion.class).content()
                .widget(0, FormWidget.class).fields().field("name").control(InputText.class);
        test.shouldHaveValue("test1");
    }

    @Test
    void testFetchOnInit() {
        setResourcePath("net/n2oapp/framework/autotest/datasources/datasource/fetch_on_init");
        N2oApplicationBuilder sources = builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/datasources/datasource/fetch_on_init/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/datasource/fetch_on_init/test.query.xml")
        );

        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(0, TableWidget.class);
        table.columns().rows().shouldHaveSize(4);
        table.columns().rows().row(0).cell(0).shouldHaveText("test1");
        table.columns().rows().row(0).cell(1).shouldHaveText("1");
        table.columns().rows().row(3).cell(0).shouldHaveText("test4");
        table.columns().rows().row(3).cell(1).shouldHaveText("2");
    }
}
