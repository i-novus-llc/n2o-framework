package net.n2oapp.framework.autotest.action;

import com.codeborne.selenide.Selenide;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.button.Button;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.InputSelect;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.modal.Modal;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.impl.component.region.N2oSimpleRegion;
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
 * Автотест для действия открытия модального окна
 */
public class ShowModalAT extends AutoTestBase {

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
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.application.xml"));
    }

    @Test
    public void scrollableModalTest() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/action/modal/scrollable/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/modal/scrollable/test.page.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("Модальное окно с фиксированной высотой");

        Button openScrollableModal = page.widget(FormWidget.class).toolbar().topLeft().button("Открыть со скроллом");
        openScrollableModal.shouldExists();
        Button openSimpleModal = page.widget(FormWidget.class).toolbar().topLeft().button("Открыть без скролла");
        openSimpleModal.shouldExists();

        openScrollableModal.click();
        Modal modalPage = N2oSelenide.modal();
        modalPage.shouldExists();
        modalPage.shouldHaveTitle("Модальное окно");
        modalPage.shouldBeScrollable();
        Fields fields = modalPage.content(SimplePage.class).widget(FormWidget.class).fields();
        fields.shouldHaveSize(11);
        fields.field("id").control(InputText.class).shouldExists();
        modalPage.scrollDown();
        fields.field("value9").control(InputText.class).shouldExists();
        modalPage.close();


        openSimpleModal.click();
        modalPage = N2oSelenide.modal();
        modalPage.shouldExists();
        modalPage.shouldHaveTitle("Модальное окно");
        modalPage.shouldNotBeScrollable();
        fields = modalPage.content(SimplePage.class).widget(FormWidget.class).fields();
        fields.shouldHaveSize(11);
        fields.field("id").control(InputText.class).shouldExists();
        fields.field("value9").control(InputText.class).shouldExists();
        modalPage.close();
        page.shouldExists();
    }

    @Test
    public void customizeModalTest() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/action/modal/customize/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/modal/customize/test.page.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("Настраиваемое модальное окно");

        Button openModalWithHeader = page.widget(FormWidget.class).toolbar().topLeft().button("Открыть с хедером");
        openModalWithHeader.shouldExists();
        Button openModalWithoutHeader = page.widget(FormWidget.class).toolbar().topLeft().button("Открыть без хедера");
        openModalWithoutHeader.shouldExists();

        openModalWithHeader.click();
        Modal modalPage = N2oSelenide.modal();
        modalPage.shouldExists();
        modalPage.shouldHaveTitle("Модальное окно");
        modalPage.content(SimplePage.class).widget(FormWidget.class).fields().shouldHaveSize(1);
        modalPage.clickBackdrop();
        modalPage.shouldExists();
        modalPage.close();
        modalPage.shouldNotExists();

        openModalWithoutHeader.click();
        modalPage = N2oSelenide.modal();
        modalPage.shouldExists();
        modalPage.shouldNotHaveHeader();
        modalPage.content(SimplePage.class).widget(FormWidget.class).fields().shouldHaveSize(1);
        modalPage.clickBackdrop();
        modalPage.shouldNotExists();
    }

    @Test
    public void valueFromParent() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/action/modal/valueFromParent/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/test.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/modal/valueFromParent/test.page.xml"));

        StandardPage page = open(StandardPage.class);
        page.shouldExists();
        N2oSimpleRegion region = page.regions().region(0, N2oSimpleRegion.class);
        FormWidget formWidget = region.content().widget(0, FormWidget.class);
        Fields fields = formWidget.fields();
        InputText name = fields.field("Простое поле").control(InputText.class);
        name.val("testValue");
        InputSelect address = fields.field("Простой список").control(InputSelect.class);
        address.select(2);
        InputSelect addresses = fields.field("Список с множественным выбором").control(InputSelect.class);
        addresses.selectMulti(1, 2);
        Fields fields2 = region.content().widget(1, FormWidget.class).fields();
        InputText id = fields2.field("id").control(InputText.class);
        id.val("44");
        InputText name2 = fields2.field("name").control(InputText.class);
        name2.val("test400");

        Button open = formWidget.toolbar().topLeft().button("Открыть");
        open.shouldExists();
        open.click();
        Modal modalPage = N2oSelenide.modal();
        modalPage.shouldExists();
        modalPage.shouldHaveTitle("Модальное окно");
        Fields modalFields = modalPage.content(SimplePage.class).widget(FormWidget.class).fields();
        modalFields.shouldHaveSize(5);
        InputText modalName = modalFields.field("Получение значения для простого поля по ссылке").control(InputText.class);
        modalName.shouldExists();
        modalName.shouldHaveValue("testValue");
        InputText modalName2 = modalFields.field("Получение значения для простого поля по default-value").control(InputText.class);
        modalName2.shouldExists();
        modalName2.shouldHaveValue("testValue");
        InputSelect modalAddress = modalFields.field("Получение значения для спиского поля по default-value").control(InputSelect.class);
        modalAddress.shouldSelected("test300");
        InputSelect modalAddresses = modalFields.field("Получение значения для спиского поля с множественным выбором").control(InputSelect.class);
        modalAddresses.shouldSelectedMulti("test200","test300");
        InputSelect addressByForm = modalFields.field("Получение значения для спиского поля из модели всей формы").control(InputSelect.class);
        addressByForm.shouldSelected("test400");
    }
    @Test
    public void buttonsEnablingInModalTest() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/action/modal/customize/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/modal/customize/test.page.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("Настраиваемое модальное окно");
        StandardButton openModalButton = page.widget(FormWidget.class).toolbar().topLeft().button("Открыть с хедером");
        openModalButton.shouldExists();
        openModalButton.shouldBeEnabled();
        openModalButton.click();

        Modal modalPage = N2oSelenide.modal();
        modalPage.shouldExists();
        modalPage.shouldHaveTitle("Модальное окно");
        modalPage.content(SimplePage.class).widget(FormWidget.class).fields().shouldHaveSize(1);
        StandardButton saveButton = modalPage.content(SimplePage.class).widget(FormWidget.class).toolbar()
                .bottomLeft().button("Сохранить");
        saveButton.shouldExists();
        saveButton.shouldBeDisabled();
        StandardButton closeButton = modalPage.content(SimplePage.class).widget(FormWidget.class).toolbar()
                .bottomLeft().button("Закрыть");
        closeButton.shouldExists();
        closeButton.shouldBeDisabled();

        InputText inputText = modalPage.content(SimplePage.class).widget(FormWidget.class)
                .fields().field("Input").control(InputText.class);
        inputText.shouldExists();
        inputText.shouldBeEnabled();
        inputText.val("test");
        inputText.shouldHaveValue("test");
        saveButton.shouldBeEnabled();
        modalPage.close();
        Selenide.confirm();
        modalPage.shouldNotExists();

        // при повторном открытии модальной страницы кнопки в тулбаре должны быть заблокированы
        openModalButton.click();
        modalPage.shouldExists();
        modalPage.shouldHaveTitle("Модальное окно");
        saveButton.shouldExists();
        saveButton.shouldBeDisabled();
        closeButton.shouldExists();
        closeButton.shouldBeDisabled();
    }
}