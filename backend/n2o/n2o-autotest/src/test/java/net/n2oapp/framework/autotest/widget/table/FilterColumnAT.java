package net.n2oapp.framework.autotest.widget.table;

import net.n2oapp.framework.autotest.api.collection.TableHeaders;
import net.n2oapp.framework.autotest.api.component.control.DateInterval;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.control.Select;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.table.TableFilterHeader;
import net.n2oapp.framework.autotest.api.component.widget.table.TableSimpleHeader;
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
 * Автотест для фильтруемого столбца таблицы
 */
public class FilterColumnAT extends AutoTestBase {
    private SimplePage page;

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
        builder.packs(
                new N2oApplicationPack(),
                new N2oAllPagesPack(),
                new N2oAllDataPack()
        );
        setResourcePath("net/n2oapp/framework/autotest/widget/table/filter_column/simple");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/filter_column/simple/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/table/filter_column/simple/test.query.xml")
        );
    }

    @Test
    void testFilterColumn() {
        page = open(SimplePage.class);
        page.shouldExists();

        TableWidget table = page.widget(TableWidget.class);
        table.shouldExists();

        TableWidget.Rows rows = table.columns().rows();
        TableHeaders headers = table.columns().headers();
        headers.shouldHaveSize(4);

        TableSimpleHeader header1 = headers.header(0);
        TableFilterHeader header2 = headers.header(1, TableFilterHeader.class);
        TableFilterHeader header3 = headers.header(2, TableFilterHeader.class);
        TableFilterHeader header4 = headers.header(3, TableFilterHeader.class);
        header1.shouldHaveTitle("id");
        header2.shouldHaveTitle("name");
        header2.shouldHaveStyle("color: red");
        header3.shouldHaveTitle("type");
        header3.shouldHaveCssClass("font-italic");
        header4.shouldHaveTitle("birthday");

        rows.shouldHaveSize(4);

        InputText header2Input = header2.filterControl(InputText.class);
        Select header3Input = header3.filterControl(Select.class);
        DateInterval header4Input = header4.filterControl(DateInterval.class);

        // проверка фильтрации через текстовое поле с нажатием на кнопку "Искать" или с нажатием Enter
        testInputTextFilter(rows, header2, header2Input, header2::clickSearchButton);
        testInputTextFilter(rows, header2, header2Input, header2Input::pressEnter);

        // проверка фильтрации через списковое поле с нажатием на кнопку "Искать" или с нажатием Enter
        testSelectFilter(rows, header3, header3Input, header3::clickSearchButton);
        testSelectFilter(rows, header3, header3Input, header3Input::pressEnter);

        // проверка фильтрации через интервальное поле с нажатием на кнопку "Искать" или с нажатием Enter
        testDateIntervalFilter(rows, header4, header4Input, header4::clickSearchButton);
        testDateIntervalFilter(rows, header4, header4Input, header4Input::pressEnter);

        // два фильтра одновременно
        header2.openFilterDropdown();
        header2Input.click();
        header2Input.setValue("1");
        header2.clickSearchButton();
        header3.openFilterDropdown();
        header3Input.openPopup();
        header3Input.dropdown().selectItem(0);
        header3.clickSearchButton();
        rows.shouldHaveSize(1);
        rows.row(0).cell(0).shouldHaveText("1");
        // должен сбрасываться только один фильтр
        header2.openFilterDropdown();
        header2.clickResetButton();
        rows.shouldHaveSize(2);
        rows.row(0).cell(0).shouldHaveText("1");
        rows.row(1).cell(0).shouldHaveText("4");
    }

    private static void testInputTextFilter(TableWidget.Rows rows, TableFilterHeader header, InputText headerInput, Runnable runnable) {
        header.openFilterDropdown();
        headerInput.click();
        headerInput.setValue("1");
        runnable.run();
        rows.shouldHaveSize(2);
        rows.row(0).cell(0).shouldHaveText("1");
        rows.row(1).cell(0).shouldHaveText("2");
        header.openFilterDropdown();
        header.clickResetButton();
        rows.shouldHaveSize(4);
    }

    private static void testSelectFilter(TableWidget.Rows rows, TableFilterHeader header, Select headerInput, Runnable runnable) {
        header.openFilterDropdown();
        headerInput.openPopup();
        headerInput.dropdown().selectItem(0);
        runnable.run();
        rows.shouldHaveSize(2);
        rows.row(0).cell(0).shouldHaveText("1");
        rows.row(1).cell(0).shouldHaveText("4");
        header.openFilterDropdown();
        header.clickResetButton();
        rows.shouldHaveSize(4);
    }

    private static void testDateIntervalFilter(TableWidget.Rows rows, TableFilterHeader header, DateInterval headerInput, Runnable runnable) {
        header.openFilterDropdown();
        headerInput.shouldBeClosed();
        headerInput.setValueInBegin("01.01.2019");
        headerInput.setValueInEnd("01.01.2021");
        headerInput.shouldBeOpened();
        runnable.run();
        headerInput.shouldBeClosed();
        rows.shouldHaveSize(2);
        rows.row(0).cell(0).shouldHaveText("2");
        rows.row(1).cell(0).shouldHaveText("3");
        header.openFilterDropdown();
        header.clickResetButton();
        rows.shouldHaveSize(4);
    }

    @Test
    void testIndicator() {
        page = open(SimplePage.class);
        page.shouldExists();

        TableWidget table = page.widget(TableWidget.class);
        table.shouldExists();

        TableHeaders headers = table.columns().headers();
        headers.shouldHaveSize(4);

        TableFilterHeader otherHeader = headers.header(2, TableFilterHeader.class);

        TableFilterHeader header = headers.header(1, TableFilterHeader.class);
        InputText headerInput = header.filterControl(InputText.class);

        //1. ввод значения  -> закрытие dropdown.
        //В этом случае должен появиться hollow индикатор
        //Данные присутствуют при повторном открытии
        header.openFilterDropdown();
        headerInput.click();
        headerInput.setValue("1");
        otherHeader.click();
        header.filterBadgeShouldExists();
        header.filterBadgeIsHollow();
        header.openFilterDropdown();
        headerInput.shouldHaveValue("1");

        //2. ручная очистка значения
        //Индикатор исчезает
        headerInput.clear();
        header.filterBadgeShouldNotExists();

        //3. ввод значения -> нажатие Найти.
        // Появляется заполненный индикатор
        //Данные присутствуют при повторном открытии
        headerInput.setValue("1");
        header.clickSearchButton();
        header.filterBadgeShouldExists();
        header.filterBadgeIsNotHollow();
        header.openFilterDropdown();
        headerInput.shouldHaveValue("1");

        //4. ввод значения после поиска -> закрытие dropdown
        //Заполненный индикатор
        //В фильтре значение, которое было при поиске
        headerInput.click();
        headerInput.setValue("2");
        otherHeader.click();
        header.filterBadgeShouldExists();
        header.filterBadgeIsNotHollow();
        header.openFilterDropdown();
        headerInput.shouldHaveValue("1");

        //5. нажатие Сбросить
        //Нет индикатора
        //Данные пусты
        header.clickResetButton();
        header.filterBadgeShouldNotExists();
        header.openFilterDropdown();
        headerInput.shouldBeEmpty();
    }
}