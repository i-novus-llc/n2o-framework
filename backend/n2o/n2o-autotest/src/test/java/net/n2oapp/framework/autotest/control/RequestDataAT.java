package net.n2oapp.framework.autotest.control;

import com.codeborne.selenide.Condition;
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
        builder.packs(new N2oApplicationPack(), new N2oAllPagesPack(), new N2oAllDataPack());

        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/control/request_data/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/request_data/select.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/request_data/test.query.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/control/request_data/test.object.xml"));
    }

    @Test
    public void testDataRequest() {
        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        InputText inputText = page.widget(FormWidget.class).fields().field("input")
                .control(InputText.class);
        MaskedInput maskedInput = page.widget(FormWidget.class).fields().field("masked")
                .control(MaskedInput.class);
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
        DateInterval dateInterval = page.widget(FormWidget.class).fields().field("Интервал")
                .control(DateInterval.class);
        DateInterval dateIntervalNoTime = page.widget(FormWidget.class).fields().field("Интервал без проставленного времени")
                .control(DateInterval.class);
        DateInput date = page.widget(FormWidget.class).fields().field("Дата")
                .control(DateInput.class);
        DateInput dateNoTime = page.widget(FormWidget.class).fields().field("Дата без проставленного времени")
                .control(DateInput.class);
        TimePicker timePicker = page.widget(FormWidget.class).fields().field("time")
                .control(TimePicker.class);
        Button save = page.widget(FormWidget.class).toolbar().topLeft().button("Сохранить", Button.class);

        inputText.shouldHaveValue("test-input");
        maskedInput.shouldHaveValue("+7 (212) 312-31-22");
        select.shouldSelected("test2");
        inputSelect.shouldSelected("test1");
        autoComplete.shouldHaveValue("test3");
        inputSelectMulti.shouldSelectedMulti("test2", "test3", "test5");
        autoCompleteMulti.shouldHaveTags("test2", "test3", "test5");
        dateInterval.shouldHaveValue("14.08.2022 — 28.08.2022");
        dateIntervalNoTime.shouldHaveValue("03.08.2022 — 18.08.2022");
        date.shouldHaveValue("18.08.2022");
        dateNoTime.shouldHaveValue("13.08.2022");
        timePicker.shouldHaveValue("12:30");

        inputText.val("test");
        maskedInput.val("2283221337");
        select.click();
        select.select(Condition.text("test5"));
        select.shouldSelected("test5");
        inputSelect.click();
        inputSelect.clear();
        inputSelect.click();
        inputSelect.select(Condition.text("test5"));
        autoComplete.val("test5");
        inputSelectMulti.click();
        inputSelectMulti.clearItems("test5");
        page.breadcrumb().element().click();
        autoCompleteMulti.click();
        autoCompleteMulti.chooseDropdownOption("test1");
        page.breadcrumb().element().click();
        dateInterval.beginVal("10.08.2022");
        dateInterval.endVal("18.08.2022");
        dateIntervalNoTime.beginVal("10.08.2022");
        dateIntervalNoTime.endVal("18.08.2022");
        date.val("10.08.2022");
        dateNoTime.val("10.08.2022");
        timePicker.selectHours("10");
        timePicker.selectMinutes("30");

//        inputSelect.expand();
//        inputSelect.clear();
//        inputSelect.collapse();
//
//        save.click();
//        inputSelect.shouldExists();
//        Selenide.refresh();

        page.breadcrumb().titleShouldHaveText("Автоматическое сохранение формы");
        inputText.shouldHaveValue("test");
        maskedInput.shouldHaveValue("+7 (228) 322-13-37");
        select.shouldSelected("test5");
        inputSelect.shouldSelected("");
        autoComplete.shouldHaveValue("test5");
        inputSelectMulti.shouldSelectedMulti("test2", "test3");
        autoCompleteMulti.shouldHaveTags("test2", "test3", "test5", "test1");
        dateInterval.shouldHaveValue("10.08.2022 — 18.08.2022");
        dateIntervalNoTime.shouldHaveValue("10.08.2022 — 18.08.2022");
        date.shouldHaveValue("10.08.2022");
        dateNoTime.shouldHaveValue("10.08.2022");
        timePicker.shouldHaveValue("10:30");
    }
}
