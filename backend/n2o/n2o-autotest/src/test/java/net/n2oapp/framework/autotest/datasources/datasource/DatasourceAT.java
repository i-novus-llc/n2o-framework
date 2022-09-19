package net.n2oapp.framework.autotest.datasources.datasource;

import net.n2oapp.framework.autotest.api.component.button.Button;
import net.n2oapp.framework.autotest.api.component.control.DateInput;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.control.OutputText;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.region.TabsRegion;
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
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

/**
 * Тестирование Datasource
 */
public class DatasourceAT extends AutoTestBase {

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
     * Тестирование формы как фильтра
     */
    @Test
    public void testFormAsFilter() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/datasources/datasource/form_as_filter/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/datasource/form_as_filter/test.query.xml"));
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
        filterId.val("3");
        searchButton.click();
        table.columns().rows().shouldHaveSize(1);
        table.columns().rows().row(0).cell(0).textShouldHave("3");
        table.columns().rows().row(0).cell(1).textShouldHave("test3");
        filterName.val("test1");
        searchButton.click();
        table.columns().rows().shouldHaveSize(0);
        clearButton.click();
        table.columns().rows().shouldHaveSize(6);

        filterId.clear();
        filterName.val("test4");
        searchButton.click();
        table.columns().rows().shouldHaveSize(1);
        table.columns().rows().row(0).cell(0).textShouldHave("4");
        table.columns().rows().row(0).cell(1).textShouldHave("test4");
        clearButton.click();

        filterId.val("1");
        filterName.val("test1");
        searchButton.click();
        table.columns().rows().shouldHaveSize(1);
        table.columns().rows().row(0).cell(0).textShouldHave("1");
        table.columns().rows().row(0).cell(1).textShouldHave("test1");
        clearButton.click();

        filterId.val("1");
        filterName.val("test2");
        searchButton.click();
        table.columns().rows().shouldHaveSize(0);
        clearButton.click();

        filterId.clear();
        filterName.val("repeatTest");
        searchButton.click();
        table.columns().rows().shouldHaveSize(2);
        table.columns().rows().columnShouldHaveTexts(1, Arrays.asList("repeatTest", "repeatTest"));
        clearButton.click();

        filterId.val("6");
        filterName.val("repeatTest");
        searchButton.click();
        table.columns().rows().shouldHaveSize(1);
    }

    /**
     * Тестирование одного datasource на несколько виджетов
     */
    @Test
    public void testOneDSManyWidgets() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/datasources/datasource/test_one_ds_many_widgets/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/test.query.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();
        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(0, TableWidget.class);
        TilesWidget tiles = page.regions().region(0, SimpleRegion.class).content().widget(1, TilesWidget.class);
        CardsWidget cards = page.regions().region(0, SimpleRegion.class).content().widget(2, CardsWidget.class);

        table.columns().rows().shouldHaveSize(4);
        tiles.paging().totalElementsShouldBe(4);
        cards.paging().totalElementsShouldBe(4);
    }

    /**
     * Тестирование сохранения нескольких форм одной кнопкой
     */
    @Test
    public void testSaveManyFormOneButton() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/datasources/datasource/save_many_form_one_button/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/datasource/save_many_form_one_button/saveForm.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/datasource/save_many_form_one_button/test.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/datasource/save_many_form_one_button/test.object.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(TableWidget.class);

        page.regions().region(0, SimpleRegion.class).content().widget(TableWidget.class)
                .toolbar().topLeft().button("Создать").click();
        page.breadcrumb().titleShouldHaveText("Создание записи");

        InputText nameInput = page.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class)
                .fields().field("Имя").control(InputText.class);
        InputText surnameInput = page.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class)
                .fields().field("Фамилия").control(InputText.class);
        Button saveButton = page.toolbar().bottomRight().button("Сохранить");

        TabsRegion tabs = page.regions().region(1, TabsRegion.class);
        InputText addressInput = tabs.tab(0).content().widget(FormWidget.class)
                .fields().field("Адрес").control(InputText.class);
        InputText organizationInput = tabs.tab(1).content().widget(FormWidget.class)
                .fields().field("Название организации").control(InputText.class);

        nameInput.val("Сергей");
        surnameInput.val("Катеев");
        addressInput.val("ул. Есенина 54");
        tabs.tab(1).click();
        tabs.tab(0).click();
        saveButton.click();
        tabs.tab(1).shouldBeActive();

        tabs.tab(1).click();
        organizationInput.shouldBeEnabled();
        organizationInput.val("Сбербанк");
        saveButton.click();
        page.breadcrumb().titleShouldHaveText("Сохранение нескольких форм одной кнопкой");

        table.columns().rows().row(0).cell(0).textShouldHave("Сергей");
        table.columns().rows().row(0).click();

        page.regions().region(0, SimpleRegion.class).content().widget(TableWidget.class)
                .toolbar().topLeft().button("Изменить").click();
        page.breadcrumb().titleShouldHaveText("Создание записи");

        nameInput.shouldHaveValue("Сергей");
        tabs.tab(1).click();
        organizationInput.shouldHaveValue("Сбербанк");
    }

    /**
     * Тестирование валидации на несколько форм
     */
    @Test
    public void testValidationManyForm() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/datasources/datasource/validation_many_form/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/datasource/validation_many_form/test.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/datasource/validation_many_form/test.object.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(0, TableWidget.class);
        InputText nameInput = page.regions().region(0, SimpleRegion.class).content().widget(1, FormWidget.class)
                .fields().field("Наименование").control(InputText.class);
        DateInput birthdayInput = page.regions().region(0, SimpleRegion.class).content().widget(2, FormWidget.class)
                .fields().field("Дата рождения").control(DateInput.class);
        Button createButton = page.toolbar().topLeft().button("Создать");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        nameInput.val("Сергей");
        birthdayInput.val(formatter.format(LocalDate.now().plusDays(1)));
        createButton.click();
        page.alerts().alert(0).shouldHaveText("Дата рождения не может быть в будущем");
        table.columns().rows().shouldNotHaveRows();

        nameInput.val("Сергей");
        birthdayInput.val(formatter.format(LocalDate.now().minusDays(1)));
        createButton.click();
        page.alerts().alert(0).shouldHaveText("Данные сохранены");
        table.columns().rows().shouldHaveSize(1);

        nameInput.val("Сергей");
        birthdayInput.val(formatter.format(LocalDate.now().minusDays(1)));
        createButton.click();
        page.alerts().alert(0).shouldHaveText("Имя Сергей уже существует");
        table.columns().rows().shouldHaveSize(1);

        nameInput.val("Артем");
        birthdayInput.val(formatter.format(LocalDate.now().minusDays(1)));
        createButton.click();
        page.alerts().alert(0).shouldHaveText("Данные сохранены");
        table.columns().rows().shouldHaveSize(2);
    }

    /**
     * Тестирование copy зависимости
     */
    @Test
    public void testSimpleCopyDepend() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/datasources/datasource/copy_depend/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/test.query.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        InputText source = page.regions().region(0, SimpleRegion.class).content()
                .widget(0, FormWidget.class).fields().field("text1").control(InputText.class);
        OutputText copy = page.regions().region(0, SimpleRegion.class).content()
                .widget(1, FormWidget.class).fields().field("text2").control(OutputText.class);

        source.val("test");
        copy.shouldHaveValue("test");
        source.clear();
        copy.shouldBeEmpty();
    }

    /**
     * Тестирование copy зависимости.
     * Форма берет данные выбранной записи в таблице
     */
    @Test
    public void testCopyDepend() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/datasources/datasource/copy_depend/resolve/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/test.query.xml"));
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
    }

    /**
     * Тестирование действия Submit
     */
    @Test
    public void testSubmit() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/datasources/datasource/submit/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/datasource/submit/test.object.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/datasources/datasource/submit/test.query.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(0, TableWidget.class);
        InputText name = page.regions().region(0, SimpleRegion.class).content()
                .widget(1, FormWidget.class).fields().field("name").control(InputText.class);
        Button button = page.regions().region(0, SimpleRegion.class).content()
                .widget(1, FormWidget.class).toolbar().bottomLeft().button("Создать");

        name.val("submit-test");
        button.click();
        table.columns().rows().row(0).cell(1).textShouldHave("submit-test");
    }
}
