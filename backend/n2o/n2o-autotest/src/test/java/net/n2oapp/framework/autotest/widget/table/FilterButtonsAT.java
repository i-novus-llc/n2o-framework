package net.n2oapp.framework.autotest.widget.table;

import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.field.ButtonField;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.RegionItems;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.widget.table.TableWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.autotest.run.N2oController;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.exceptions.verification.TooManyActualInvocations;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Автотест для проверки кнопок в фильтрах таблицы
 */
class FilterButtonsAT extends AutoTestBase {

    @MockitoSpyBean
    private N2oController controller;

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

    @Test
    void testSeparateButtons() {
        setResourcePath("net/n2oapp/framework/autotest/widget/table/filters/buttons/separate_buttons");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/filters/buttons/separate_buttons/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/filters/buttons/separate_buttons/test.query.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        TableWidget table = page.widget(TableWidget.class);
        table.shouldExists();

        TableWidget.Filters filters = table.filters();
        InputText name = filters.fields().field("name").control(InputText.class);
        InputText type = filters.fields().field("type").control(InputText.class);
        ButtonField search = filters.toolbar().button("search", ButtonField.class);
        ButtonField clear = filters.toolbar().button("clear", ButtonField.class);

        table.columns().rows().shouldHaveSize(4);
        type.setValue("1");
        table.columns().rows().shouldHaveSize(4);
        search.click();
        table.columns().rows().shouldHaveSize(2);
        type.shouldHaveValue("1");
        name.setValue("test1");
        table.columns().rows().shouldHaveSize(2);
        search.click();
        table.columns().rows().shouldHaveSize(1);
        type.shouldHaveValue("1");
        name.shouldHaveValue("test1");


        clear.click();
        table.columns().rows().shouldHaveSize(2);
        type.shouldHaveValue("1");
        name.shouldBeEmpty();
    }

    @Test
    void testSearchButtons() {
        setResourcePath("net/n2oapp/framework/autotest/widget/table/filters/buttons/search_buttons");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/filters/buttons/search_buttons/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/filters/buttons/search_buttons/test.query.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        TableWidget table = page.widget(TableWidget.class);
        table.shouldExists();

        TableWidget.Filters filters = table.filters();
        InputText name = filters.fields().field("name").control(InputText.class);
        InputText type = filters.fields().field("type").control(InputText.class);
        ButtonField search = filters.toolbar().button("SearchB", ButtonField.class);
        ButtonField clear = filters.toolbar().button("ClearB", ButtonField.class);

        table.columns().rows().shouldHaveSize(4);
        type.setValue("1");
        table.columns().rows().shouldHaveSize(4);
        search.click();
        table.columns().rows().shouldHaveSize(2);
        type.shouldHaveValue("1");
        name.setValue("test1");
        table.columns().rows().shouldHaveSize(2);
        search.click();
        table.columns().rows().shouldHaveSize(1);
        type.shouldHaveValue("1");
        name.shouldHaveValue("test1");

        clear.click();
        table.columns().rows().shouldHaveSize(2);
        type.shouldHaveValue("1");
        name.shouldBeEmpty();
    }

    @Test
    void testFetchOnClearWithClearButton() {
        setResourcePath("net/n2oapp/framework/autotest/widget/table/filters/buttons/clear_button_fetch_on_clear");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/filters/buttons/clear_button_fetch_on_clear/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/filters/buttons/clear_button_fetch_on_clear/test.query.xml")
        );

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        TableWidget table = page.widget(TableWidget.class);
        InputText input = table.filters().fields().field("name").control(InputText.class);
        table.columns().rows().shouldHaveSize(4);
        input.click();
        input.setValue("test1");
        table.columns().rows().shouldHaveSize(1);

        table.filters().toolbar().button("clear").click();
        table.columns().rows().shouldHaveSize(0);
        input.shouldBeEmpty();
    }

    @Test
    void testFetchOnClearWithSearchButtons() {
        setResourcePath("net/n2oapp/framework/autotest/widget/table/filters/buttons/search_buttons_fetch_on_clear");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/filters/buttons/search_buttons_fetch_on_clear/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/filters/buttons/search_buttons_fetch_on_clear/test.query.xml")
        );

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        TableWidget table = page.widget(TableWidget.class);
        InputText input = table.filters().fields().field("name").control(InputText.class);
        input.click();
        input.setValue("test");
        table.filters().toolbar().button("Найти").click();
        table.columns().rows().shouldHaveSize(4);

        table.filters().toolbar().button("Сбросить").click();
        table.columns().rows().shouldHaveSize(0);
        verifyNeverGetDataInvocation(2, "Запрос за данными таблицы при fetch-on-clear=false");
        input.shouldBeEmpty();
        table.paging().shouldNotHaveTotalElements();
    }

    @Test
    void testFetchOnEnter() {
        setResourcePath("net/n2oapp/framework/autotest/widget/table/filters/buttons/fetch_on_enter");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/filters/buttons/fetch_on_enter/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/filters/buttons/fetch_on_enter/test.query.xml")
        );

        StandardPage page = open(StandardPage.class);
        page.shouldExists();
        RegionItems content = page.regions().region(0, SimpleRegion.class).content();

        // fetch-on-enter="false"
        TableWidget table = content.widget(0, TableWidget.class);
        TableWidget.Filters filters = table.filters();
        StandardButton searchButton = filters.toolbar().button("Найти");
        InputText minPriceInput = filters.fields().field("Минимальная цена").control(InputText.class);
        InputText maxPriceInput = filters.fields().field("Максимальная цена").control(InputText.class);

        table.columns().rows().shouldHaveSize(4);
        minPriceInput.setValue("111");
        minPriceInput.pressEnter();
        maxPriceInput.setValue("131");
        maxPriceInput.pressEnter();
        verifyNeverGetDataInvocation(2, "Запрос за данными таблицы при fetch-on-enter=false");
        table.columns().rows().shouldHaveSize(4);

        searchButton.click();
        table.columns().rows().shouldHaveSize(2);

        // fetch-on-enter="true"
        table = content.widget(1, TableWidget.class);
        filters = table.filters();
        searchButton = filters.toolbar().button("Найти");
        minPriceInput = filters.fields().field("Минимальная цена").control(InputText.class);
        maxPriceInput = filters.fields().field("Максимальная цена").control(InputText.class);

        table.columns().rows().shouldHaveSize(4);
        minPriceInput.setValue("111");
        minPriceInput.pressEnter();
        table.columns().rows().shouldHaveSize(3);
        maxPriceInput.setValue("131");
        maxPriceInput.pressEnter();
        table.columns().rows().shouldHaveSize(2);

        searchButton.click();
        table.columns().rows().shouldHaveSize(2);
    }

    private void verifyNeverGetDataInvocation(int times, String errorMessage) {
        try {
            verify(controller, times(times)).getData(any());
        } catch (TooManyActualInvocations e) {
            throw new AssertionError(errorMessage);
        }
    }
}
