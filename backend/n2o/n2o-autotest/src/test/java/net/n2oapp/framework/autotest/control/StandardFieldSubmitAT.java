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
public class StandardFieldSubmitAT extends AutoTestBase {

    private Fields fields;

    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();

        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/control/submit/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/submit/test.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/submit/test.object.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/blank.header.xml"));

        SimplePage page = open(SimplePage.class);
        page.shouldExists();
        fields = page.widget(FormWidget.class).fields();
        fields.shouldHaveSize(3);
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oHeaderPack(), new N2oWidgetsPack(),
                new N2oFieldSetsPack(), new N2oControlsPack(), new N2oAllDataPack());
    }

    @Test
    public void testSubmit() {
        InputText inputText = fields.field("Имя").control(InputText.class);
        inputText.shouldExists();
        inputText.shouldHaveValue("Joe");

        Select select = fields.field("Пол").control(Select.class);
        select.shouldHaveValue("Мужской");

        DateInterval dateInterval = fields.field("Даты отпуска").control(DateInterval.class);
        dateInterval.shouldBeCollapsed();
        dateInterval.beginShouldHaveValue("15.01.2020");
        dateInterval.endShouldHaveValue("30.01.2020");

        // изменяем обычное текстовое поле
        inputText.val("Ann");
        // обновляем страницу и проверяем значения всех полей
        // ожидание отправки поля
        Selenide.sleep(500);
        inputText.shouldHaveValue("Ann");
        Selenide.refresh();
        inputText.shouldHaveValue("Ann");
        select.shouldHaveValue("Мужской");
        dateInterval.shouldBeCollapsed();
        dateInterval.beginShouldHaveValue("15.01.2020");
        dateInterval.endShouldHaveValue("30.01.2020");

        // изменяем списковое поле
        select.select(1);
        Selenide.sleep(500);
        select.shouldHaveValue("Женский");
        Selenide.refresh();
        inputText.shouldHaveValue("Ann");
        select.shouldHaveValue("Женский");
        dateInterval.shouldBeCollapsed();
        dateInterval.beginShouldHaveValue("15.01.2020");
        dateInterval.endShouldHaveValue("30.01.2020");

        // изменяем интервальное поле
        dateInterval.beginVal("18.01.2020");
        Selenide.sleep(500);
        dateInterval.beginShouldHaveValue("18.01.2020");
        Selenide.refresh();
        inputText.shouldHaveValue("Ann");
        select.shouldHaveValue("Женский");
        dateInterval.shouldBeCollapsed();
        dateInterval.beginShouldHaveValue("18.01.2020");
        dateInterval.endShouldHaveValue("30.01.2020");

        dateInterval.endVal("24.01.2020");
        Selenide.sleep(500);
        dateInterval.endShouldHaveValue("24.01.2020");
        Selenide.refresh();
        inputText.shouldHaveValue("Ann");
        select.shouldHaveValue("Женский");
        dateInterval.shouldBeCollapsed();
        dateInterval.beginShouldHaveValue("18.01.2020");
        dateInterval.endShouldHaveValue("24.01.2020");
    }
}
