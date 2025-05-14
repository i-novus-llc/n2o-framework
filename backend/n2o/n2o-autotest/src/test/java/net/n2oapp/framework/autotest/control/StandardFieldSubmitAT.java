package net.n2oapp.framework.autotest.control;

import com.codeborne.selenide.Selenide;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.control.DateInterval;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.control.Select;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Автотест автоматического сохранения полей
 */
class StandardFieldSubmitAT extends AutoTestBase {

    private Fields fields;

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
    void testSubmit() {
        setResourcePath("net/n2oapp/framework/autotest/control/submit");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/control/submit/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/submit/test.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/submit/test.object.xml"));
        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        fields = page.widget(FormWidget.class).fields();
        fields.shouldHaveSize(3);
        InputText inputText = fields.field("Имя").control(InputText.class);
        inputText.shouldExists();
        inputText.shouldHaveValue("Joe");

        Select select = fields.field("Пол").control(Select.class);
        select.shouldHaveValue("Мужской");

        DateInterval dateInterval = fields.field("Даты отпуска").control(DateInterval.class);
        dateInterval.shouldBeClosed();
        dateInterval.beginShouldHaveValue("15.01.2020");
        dateInterval.endShouldHaveValue("30.01.2020");

        // изменяем обычное текстовое поле
        inputText.click();
        inputText.setValue("Ann");
        // обновляем страницу и проверяем значения всех полей
        // ожидание отправки поля
        Selenide.sleep(500);
        inputText.shouldHaveValue("Ann");
        Selenide.refresh();
        inputText.shouldHaveValue("Ann");
        select.shouldHaveValue("Мужской");
        dateInterval.shouldBeClosed();
        dateInterval.beginShouldHaveValue("15.01.2020");
        dateInterval.endShouldHaveValue("30.01.2020");

        // изменяем списковое поле
        select.openPopup();
        select.dropdown().selectItem(1);
        Selenide.sleep(500);
        select.shouldHaveValue("Женский");
        Selenide.refresh();
        inputText.shouldHaveValue("Ann");
        select.shouldHaveValue("Женский");
        dateInterval.shouldBeClosed();
        dateInterval.beginShouldHaveValue("15.01.2020");
        dateInterval.endShouldHaveValue("30.01.2020");

        // изменяем интервальное поле
        dateInterval.setValueInBegin("18.01.2020");
        Selenide.sleep(500);
        dateInterval.beginShouldHaveValue("18.01.2020");
        Selenide.refresh();
        inputText.shouldHaveValue("Ann");
        select.shouldHaveValue("Женский");
        dateInterval.shouldBeClosed();
        dateInterval.beginShouldHaveValue("18.01.2020");
        dateInterval.endShouldHaveValue("30.01.2020");

        dateInterval.setValueInEnd("24.01.2020");
        Selenide.sleep(500);
        dateInterval.endShouldHaveValue("24.01.2020");
        Selenide.refresh();
        inputText.shouldHaveValue("Ann");
        select.shouldHaveValue("Женский");
        dateInterval.shouldBeClosed();
        dateInterval.beginShouldHaveValue("18.01.2020");
        dateInterval.endShouldHaveValue("24.01.2020");
    }
}
