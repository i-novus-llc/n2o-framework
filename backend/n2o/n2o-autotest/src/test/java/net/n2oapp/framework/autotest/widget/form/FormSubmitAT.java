package net.n2oapp.framework.autotest.widget.form;

import com.codeborne.selenide.Selenide;
import net.n2oapp.framework.autotest.N2oSelenide;
import net.n2oapp.framework.autotest.api.collection.Cells;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.DateInterval;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.control.Select;
import net.n2oapp.framework.autotest.api.component.modal.Modal;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.api.component.widget.table.TableWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Автотест автоматического сохранения формы
 */
public class FormSubmitAT extends AutoTestBase {

    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();

        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"));
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oHeaderPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(),
                new N2oControlsPack(), new N2oAllDataPack(), new N2oActionsPack(), new N2oCellsPack());
    }

    @Test
    public void testSubmit() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/widget/form/submit/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/form/submit/test.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/form/submit/test.object.xml"));
        final int DELAY = 600;

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        Fields fields = page.widget(FormWidget.class).fields();
        fields.shouldHaveSize(3);

        InputText inputText = fields.field("Имя").control(InputText.class);
        inputText.shouldExists();
        inputText.shouldHaveValue("Joe");

        Select select = fields.field("Пол").control(Select.class);
        select.shouldHaveValue("Мужской");

        DateInterval dateInterval = fields.field("Даты отпуска").control(DateInterval.class);
        dateInterval.beginShouldHaveValue("15.01.2020");
        dateInterval.endShouldHaveValue("30.01.2020");

        // изменяем обычное текстовое поле
        inputText.val("Ann");
        // обновляем страницу и проверяем значения всех полей
        // ожидание отправки поля
        Selenide.sleep(DELAY);
        inputText.shouldHaveValue("Ann");
        Selenide.refresh();
        inputText.shouldHaveValue("Ann");
        select.shouldHaveValue("Мужской");
        dateInterval.beginShouldHaveValue("15.01.2020");
        dateInterval.endShouldHaveValue("30.01.2020");

        // изменяем списковое поле
        select.select(1);
        Selenide.sleep(DELAY);
        select.shouldHaveValue("Женский");
        Selenide.refresh();
        inputText.shouldHaveValue("Ann");
        select.shouldHaveValue("Женский");
        dateInterval.beginShouldHaveValue("15.01.2020");
        dateInterval.endShouldHaveValue("30.01.2020");

        // изменяем интервальное поле
        dateInterval.beginVal("18.01.2020");
        Selenide.sleep(DELAY);
        dateInterval.beginShouldHaveValue("18.01.2020");
        Selenide.refresh();
        inputText.shouldHaveValue("Ann");
        select.shouldHaveValue("Женский");
        dateInterval.beginShouldHaveValue("18.01.2020");
        dateInterval.endShouldHaveValue("30.01.2020");

        dateInterval.endVal("24.01.2020");
        Selenide.sleep(DELAY);
        dateInterval.endShouldHaveValue("24.01.2020");
        Selenide.refresh();
        inputText.shouldHaveValue("Ann");
        select.shouldHaveValue("Женский");
        dateInterval.beginShouldHaveValue("18.01.2020");
        dateInterval.endShouldHaveValue("24.01.2020");
    }

    @Test
    public void testModalSubmit() {
        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/widget/form/submit_modal/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/form/submit_modal/modal.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/form/submit_modal/test.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/form/submit_modal/test.object.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        TableWidget table = page.widget(TableWidget.class);
        table.shouldExists();
        table.columns().rows().shouldHaveSize(3);

        Cells row = table.columns().rows().row(1);
        row.cell(1).textShouldHave("test2");
        row.cell(2).textShouldHave("20");

        // открываем модальную страницу для второй записи
        row.click();
        StandardButton btn = table.toolbar().topLeft().button("Открыть");
        btn.click();
        Modal modal = N2oSelenide.modal();

        FormWidget modalForm = modal.content(SimplePage.class).widget(FormWidget.class);
        InputText name = modalForm.fields().field("Имя").control(InputText.class);
        InputText age = modalForm.fields().field("Возраст").control(InputText.class);
        name.shouldHaveValue("test2");
        age.shouldHaveValue("20");

        // меняем имя и ждем отправки значения
        name.val("test123");
        Selenide.sleep(500);
        modalForm.toolbar().bottomRight().button("Закрыть").click();

        row.cell(1).textShouldHave("test123");
        row.cell(2).textShouldHave("20");

        // открываем модальную страницу для второй записи
        row.click();
        btn.click();

        // меняем возраст и ждем отправки значения
        age.val("99");
        Selenide.sleep(500);
        modalForm.toolbar().bottomRight().button("Закрыть").click();

        row.cell(1).textShouldHave("test123");
        row.cell(2).textShouldHave("99");
    }
}