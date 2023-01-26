package net.n2oapp.framework.autotest.control;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import net.n2oapp.framework.autotest.api.component.button.Button;
import net.n2oapp.framework.autotest.api.component.control.*;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
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
 * Автотест проверяющий отправку запроса данных на сохранение
 */
public class RequestDataAT extends AutoTestBase {
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

        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/control/request_data/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/request_data/select.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/request_data/test.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/request_data/test.object.xml"));
    }

    /**
     * Тест проверяет сохранение изменений, после сохранения и обновления страницы
     */
    @Test
    public void testDataRequest() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        Select select = page.widget(FormWidget.class).fields().field("select")
                .control(Select.class);
        InputSelect inputSelect = page.widget(FormWidget.class).fields().field("input_select")
                .control(InputSelect.class);
        AutoComplete autoComplete = page.widget(FormWidget.class).fields().field("auto")
                .control(AutoComplete.class);
        InputSelect inputSelectMulti = page.widget(FormWidget.class).fields().field("input_select_multi")
                .control(InputSelect.class);
        AutoComplete autoCompleteMulti = page.widget(FormWidget.class).fields().field("auto_multi")
                .control(AutoComplete.class);
        Button save = page.widget(FormWidget.class).toolbar().topLeft().button("Сохранить", Button.class);

        select.shouldSelected("test4");
        inputSelect.shouldSelected("test4");
        autoComplete.shouldBeEmpty();
        inputSelectMulti.shouldBeEmpty();
        autoCompleteMulti.shouldBeEmpty();

        select.click();
        select.shouldBeOpened();
        select.select(Condition.text("test5"));
        select.shouldSelected("test5");
        select.closePopup();

        inputSelect.click();
        inputSelect.shouldBeOpened();
        inputSelect.clear();
        inputSelect.shouldBeEmpty();
        inputSelect.select(Condition.text("test6"));
        inputSelect.shouldHaveValue("test6");

        autoComplete.val("test5");
        autoComplete.shouldHaveValue("test5");

        inputSelectMulti.selectMulti(2, 4);
        inputSelectMulti.closePopup();
        inputSelectMulti.shouldSelectedMulti("test3", "test5");

        autoCompleteMulti.addTag("test1");
        autoCompleteMulti.addTag("test6");
        autoCompleteMulti.shouldHaveTags("test1", "test6");

        save.click();
        Selenide.refresh();

        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("Страница для автотеста проверяющего отправку запроса данных на сохранение");

        select.shouldSelected("test5");
        inputSelect.shouldHaveValue("test6");
        autoComplete.shouldHaveValue("test5");
        inputSelectMulti.shouldSelectedMulti("test3", "test5");
        autoCompleteMulti.shouldHaveTags("test1", "test6");

        select.openPopup();
        select.clear();
        select.closePopup();
        select.shouldBeEmpty();

        inputSelect.click();
        inputSelect.shouldBeOpened();
        inputSelect.clear();
        inputSelect.shouldBeEmpty();

        autoComplete.click();
        autoComplete.clear();
        autoComplete.shouldBeEmpty();

        select.click();

        inputSelectMulti.clearItems("test3", "test5");
        inputSelectMulti.shouldBeEmpty();

        autoCompleteMulti.removeTag("test1");
        autoCompleteMulti.removeTag("test6");
        autoCompleteMulti.shouldBeEmpty();

        save.click();
        Selenide.refresh();

        page.shouldExists();
        page.breadcrumb().titleShouldHaveText("Страница для автотеста проверяющего отправку запроса данных на сохранение");

        select.shouldBeEmpty();
        inputSelect.shouldBeEmpty();
        autoComplete.shouldBeEmpty();
        inputSelectMulti.shouldBeEmpty();
        autoCompleteMulti.shouldBeEmpty();
    }
}
