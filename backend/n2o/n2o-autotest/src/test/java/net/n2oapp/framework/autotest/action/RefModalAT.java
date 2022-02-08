package net.n2oapp.framework.autotest.action;

import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.button.Button;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.drawer.Drawer;
import net.n2oapp.framework.autotest.api.component.field.ButtonField;
import net.n2oapp.framework.autotest.api.component.field.StandardField;
import net.n2oapp.framework.autotest.api.component.modal.Modal;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.LineRegion;
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
 * Автотест прокидывания модели на форму в модалку
 */
public class RefModalAT extends AutoTestBase {

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
                new CompileInfo("net/n2oapp/framework/autotest/model/modal/test.query.xml"));
    }

    @Test
    public void refParentModalTest() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/model/modal/from_form/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/model/modal/from_form/modal.page.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();
        // передача параметров в модельное окно из формы
        Fields formFields = page.regions().region(0, LineRegion.class).content()
                .widget(0, FormWidget.class).fields();
        StandardField addressField = formFields.field("Адрес (собирается из того, что ввели в модальном окне)");
        InputText address = addressField.control(InputText.class);
        address.shouldExists();
        InputText city = formFields.field("Город (значение должно копироваться в модальное окно и обратно)").control(InputText.class);
        city.shouldExists();
        city.shouldHaveValue("Казань");
        InputText street = page.regions().region(0, LineRegion.class).content()
                .widget(1, FormWidget.class).fields().field("Улица (значение должно копироваться в модальное окно, но не обратно)").control(InputText.class);
        street.shouldExists();
        street.shouldHaveValue("Качалова");
        addressField.toolbar().button(0, Button.class).click();
        Modal modalPage = N2oSelenide.modal();
        modalPage.shouldExists();
        Fields fields = modalPage.content(SimplePage.class).widget(FormWidget.class).fields();
        fields.shouldHaveSize(3);
        InputText cityInputModal = fields.field("Значение из поля виджета, в котором кнопку открытия окна").control(InputText.class);
        cityInputModal.shouldHaveValue("Казань");
        cityInputModal.val("Москва");
        cityInputModal.shouldHaveValue("Москва");
        InputText streetInputModal = fields.field("Значение из поля другого виджета").control(InputText.class);
        streetInputModal.shouldHaveValue("Качалова");
        streetInputModal.val("Ленина");
        streetInputModal.shouldHaveValue("Ленина");
        InputText houseInputModal = fields.field("Значение вводимое только в модальном окне").control(InputText.class);
        houseInputModal.val("1");
        houseInputModal.shouldHaveValue("1");
        modalPage.toolbar().bottomRight().button("Сохранить").click();
        address.shouldHaveValue("Москва, Ленина, 1");
        city.shouldHaveValue("Москва");
        street.shouldHaveValue("Качалова");
    }

    @Test
    public void refParentModalFromFiltersTest() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/model/modal/from_filters/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/model/modal/from_filters/filters.page.xml"));
        StandardPage page = open(StandardPage.class);
        page.shouldExists();
        // передача параметров в модельное окно из фильтров таблицы
        TableWidget tableWidget = page.regions().region(0, LineRegion.class).content()
                .widget(0, TableWidget.class);
        tableWidget.shouldExists();
        tableWidget.columns().rows().shouldHaveSize(4);
        TableWidget.Filters filters = tableWidget.filters();
        InputText name = filters.fields().field("name").control(InputText.class);
        name.val("test");
        filters.fields().field("Расширенные фильтры", ButtonField.class).click();
        Drawer drawer = N2oSelenide.drawer();
        drawer.shouldExists();
        Fields fields = drawer.content(SimplePage.class).widget(FormWidget.class).fields();
        fields.shouldHaveSize(2);
        InputText nameInputModal = fields.field("name").control(InputText.class);
        nameInputModal.shouldHaveValue("test");
        nameInputModal.val("test1");
        nameInputModal.shouldHaveValue("test1");
        InputText typeInputModal = fields.field("type").control(InputText.class);
        typeInputModal.val("1");
        typeInputModal.shouldHaveValue("1");
        drawer.toolbar().bottomRight().button("Применить").click();
        filters.search();
        tableWidget.columns().rows().shouldHaveSize(1);
    }
}