package net.n2oapp.framework.autotest.control;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.AutoComplete;
import net.n2oapp.framework.autotest.api.component.control.InputSelect;
import net.n2oapp.framework.autotest.api.component.control.Select;
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

        setJsonPath("net/n2oapp/framework/autotest/control/request_data");
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
        StandardButton save = page.widget(FormWidget.class).toolbar().topLeft().button("Сохранить");

        select.shouldSelected("test4");
        inputSelect.shouldHaveValue("test4");
        autoComplete.shouldBeEmpty();
        inputSelectMulti.shouldBeEmpty();
        autoCompleteMulti.shouldBeEmpty();

        select.click();
        select.shouldBeOpened();
        select.openPopup();
        select.dropdown().selectItemBy(Condition.text("test5"));
        select.shouldSelected("test5");
        select.closePopup();

        inputSelect.click();
        inputSelect.shouldBeOpened();
        inputSelect.clearUsingIcon();
        inputSelect.shouldBeEmpty();
        inputSelect.openPopup();
        inputSelect.dropdown().selectItemBy(Condition.text("test6"));
        inputSelect.shouldHaveValue("test6");

        autoComplete.click();
        autoComplete.setValue("test5");
        autoComplete.shouldHaveValue("test5");

        inputSelectMulti.openPopup();
        inputSelectMulti.dropdown().selectMulti(2, 4);
        inputSelectMulti.closePopup();
        inputSelectMulti.shouldSelectedMulti(new String[]{"test3", "test5"});

        autoCompleteMulti.click();
        autoCompleteMulti.setValue("test1");
        autoCompleteMulti.enter();

        autoCompleteMulti.click();
        autoCompleteMulti.setValue("test6");
        autoCompleteMulti.enter();

        autoCompleteMulti.shouldHaveTags(new String[]{"test1", "test6"});

        save.click();
        // ожидание валидации и сохранения
        Selenide.sleep(500);
        Selenide.refresh();

        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Страница для автотеста проверяющего отправку запроса данных на сохранение");

        select.shouldSelected("test5");
        inputSelect.shouldHaveValue("test6");
        autoComplete.shouldHaveValue("test5");
        inputSelectMulti.shouldSelectedMulti(new String[]{"test3", "test5"});
        autoCompleteMulti.shouldHaveTags(new String[]{"test1", "test6"});

        select.openPopup();
        select.clear();
        select.closePopup();
        select.shouldBeEmpty();

        inputSelect.click();
        inputSelect.shouldBeOpened();
        inputSelect.clearUsingIcon();
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
        // ожидание валидации и сохранения
        Selenide.sleep(500);
        Selenide.refresh();

        page.shouldExists();
        page.breadcrumb().crumb(0).shouldHaveLabel("Страница для автотеста проверяющего отправку запроса данных на сохранение");

        select.shouldBeEmpty();
        inputSelect.shouldBeEmpty();
        autoComplete.shouldBeEmpty();
        inputSelectMulti.shouldBeEmpty();
        autoCompleteMulti.shouldBeEmpty();
    }
}
