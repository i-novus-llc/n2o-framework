package net.n2oapp.framework.autotest.cases;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.control.DateInterval;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.control.MaskedInput;
import net.n2oapp.framework.autotest.api.component.control.Select;
import net.n2oapp.framework.autotest.api.component.drawer.Drawer;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
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

/**
 * Автотест для проверки фильтров в отдельном окне
 */
public class FiltersInDrawerAT extends AutoTestBase {

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
        setResourcePath("net/n2oapp/framework/autotest/action/drawer/filters");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/action/drawer/filters/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/drawer/filters/modal.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/drawer/filters/data.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/drawer/filters/department.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/drawer/filters/region.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/drawer/filters/status.query.xml"));
    }

    @Test
    public void searchOnFiltersChangeTest() {
        StandardPage page = open(StandardPage.class);
        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Фильтры в отдельном окне");

        TableWidget table = page.regions().region(0, SimpleRegion.class).content().widget(TableWidget.class);
        table.shouldExists();
        TableWidget.Rows tableRows = table.columns().rows();
        tableRows.shouldHaveSize(5);
        table.filters().shouldBeVisible();
        Fields filterFields = table.filters().fields();
        filterFields.shouldHaveSize(6);
        table.filters().toolbar().button("Все фильтры").shouldBeEnabled();
        table.filters().toolbar().button("Все фильтры").click();

        Drawer drawer = N2oSelenide.drawer();
        drawer.shouldHaveTitle("Фильтры");
        FormWidget drawerForm = drawer.content(SimplePage.class).widget(FormWidget.class);

        DateInterval dateField = drawerForm.fields().field("Дата подачи заявки").control(DateInterval.class);
        dateField.shouldExists();
        dateField.setValueInBegin("28.02.2021");
        dateField.setValueInEnd("07.03.2021");
        dateField.beginShouldHaveValue("28.02.2021");
        dateField.endShouldHaveValue("07.03.2021");

        InputText fioField = drawerForm.fields().field("Фамилия Имя Отчество").control(InputText.class);
        fioField.shouldExists();
        fioField.click();
        fioField.setValue("Вас");
        fioField.shouldHaveValue("Вас");

        Select regionField = drawerForm.fields().field("Регион").control(Select.class);
        regionField.shouldExists();
        regionField.openPopup();
        regionField.dropdown().selectItem(0);
        regionField.shouldSelected("Республика Татарстан");

        Select departmentField = drawerForm.fields().field("Отделение").control(Select.class);
        departmentField.shouldExists();
        departmentField.openPopup();
        departmentField.dropdown().selectItem(0);
        departmentField.shouldSelected("МФЦ Авиастроительный отдел");

        Select statusField = drawerForm.fields().field("Статус").control(Select.class);
        statusField.shouldExists();
        statusField.openPopup();
        statusField.dropdown().selectItem(0);
        statusField.shouldSelected("Новая");

        MaskedInput snilsField = drawerForm.fields().field("СНИЛС").control(MaskedInput.class);
        snilsField.shouldExists();
        snilsField.setValue("132-451-114 12");
        snilsField.shouldHaveValue("132-451-114 12");

        drawer.toolbar().bottomRight().button("Применить").shouldExists();
        drawer.toolbar().bottomRight().button("Применить").click();
        page.shouldExists();

        //проверка передачи данных в фильтры на главной странице и автообновление таблицы
        filterFields.field("Интервал подачи заявки").control(DateInterval.class).beginShouldHaveValue("28.02.2021");
        filterFields.field("Интервал подачи заявки").control(DateInterval.class).endShouldHaveValue("07.03.2021");
        filterFields.field("Регион").control(Select.class).shouldSelected("Республика Татарстан");
        filterFields.field("Отделение").control(Select.class).shouldSelected("МФЦ Авиастроительный отдел");
        filterFields.field("Статус").control(Select.class).shouldSelected("Новая");

        tableRows.shouldHaveSize(1);
        tableRows.row(0).cell(0).shouldHaveText("1");

        //проверка сохранения параметров фильтрации в drawer
        table.filters().toolbar().button("Все фильтры").click();
        dateField.beginShouldHaveValue("28.02.2021");
        dateField.endShouldHaveValue("07.03.2021");
        regionField.shouldSelected("Республика Татарстан");
        departmentField.shouldSelected("МФЦ Авиастроительный отдел");
        statusField.shouldSelected("Новая");
        fioField.shouldHaveValue("Вас");
        snilsField.shouldHaveValue("132-451-114 12");
        drawer.toolbar().bottomRight().button("Применить").click();

        //проверка сброса фильтра
        table.filters().toolbar().button("Сбросить").shouldExists();
        table.filters().toolbar().button("Сбросить").click();
        filterFields.field("Интервал подачи заявки").control(DateInterval.class).beginShouldBeEmpty();
        filterFields.field("Интервал подачи заявки").control(DateInterval.class).endShouldBeEmpty();
        filterFields.field("Регион").control(Select.class).shouldBeEmpty();
        filterFields.field("Отделение").control(Select.class).shouldBeEmpty();
        filterFields.field("Статус").control(Select.class).shouldBeEmpty();
        tableRows.shouldHaveSize(5);
    }
}

