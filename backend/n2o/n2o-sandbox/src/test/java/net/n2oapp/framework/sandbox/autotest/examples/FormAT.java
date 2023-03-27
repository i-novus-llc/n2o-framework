package net.n2oapp.framework.sandbox.autotest.examples;

import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.control.DateInput;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.control.Select;
import net.n2oapp.framework.autotest.api.component.control.TextArea;
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

public class FormAT extends AutoTestBase {

    private SimplePage page;

    @BeforeAll
    public static void beforeClass() {
        configureSelenide();
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();

        page = open(SimplePage.class);
        page.shouldExists();
        page.header().shouldHaveBrandName("N2O");
        page.breadcrumb().crumb(0).shouldHaveLabel("Форма");
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack(), new N2oApplicationPack(), new N2oAllDataPack());
        builder.sources(new CompileInfo("/examples/form/index.page.xml"));
    }

    @Test
    public void testForm() {
        Fields fields = page.widget(FormWidget.class).fields();
        fields.shouldHaveSize(5);
        fields.field("Имя").shouldExists();
        fields.field("Фамилия").shouldExists();
        fields.field("Пол").shouldExists();
        fields.field("Дата рождения").shouldExists();
        fields.field("Адрес").shouldExists();
    }

    @Test
    public void testNameAndLastNameInputFields() {
        InputText name = page.widget(FormWidget.class).fields().field("Имя").control(InputText.class);
        name.shouldExists();
        name.shouldBeEmpty();
        name.click();
        name.setValue("test-name");
        name.shouldHaveValue("test-name");

        InputText lastName = page.widget(FormWidget.class).fields().field("Фамилия")
                .control(InputText.class);
        lastName.shouldExists();
        lastName.shouldBeEmpty();
        lastName.click();
        lastName.setValue("test-lastName");
        lastName.shouldHaveValue("test-lastName");
    }

    @Test
    public void testGenderSelectField() {
        Select gender = page.widget(FormWidget.class).fields().field("Пол").control(Select.class);
        gender.shouldExists();
        gender.shouldBeEmpty();
        gender.openPopup();
        gender.dropdown().selectItem(0);
        gender.shouldSelected("Мужской");
        gender.openPopup();
        gender.dropdown().selectItem(1);
        gender.shouldSelected("Женский");
        gender.clear();
        gender.shouldBeEmpty();
    }

    @Test
    public void testDataField() {
        DateInput date = page.widget(FormWidget.class).fields().field("Дата рождения")
                .control(DateInput.class);
        date.shouldExists();

        date.shouldBeEmpty();
        date.setValue("15.02.2020");
        date.shouldHaveValue("15.02.2020");
        date.clickCalendarButton();
        date.shouldBeActiveDay("15");
        date.clickDay("12");
        date.shouldHaveValue("12.02.2020");
        // проверка месяцев и переход к предыдущему/следующему месяцу
        date.clickCalendarButton();
        date.shouldHaveCurrentMonth("Февраль");
        date.shouldHaveCurrentYear("2020");
        date.clickPreviousMonthButton();
        date.shouldHaveCurrentMonth("Январь");
        date.clickPreviousMonthButton();
        date.shouldHaveCurrentMonth("Декабрь");
        date.shouldHaveCurrentYear("2019");
        date.clickNextMonthButton();
        date.shouldHaveCurrentMonth("Январь");
    }

    @Test
    public void testAddressField() {
        TextArea textArea = page.widget(FormWidget.class).fields().field("Адрес").control(TextArea.class);
        textArea.shouldBeEnabled();
        textArea.shouldHaveValue("");
        textArea.setValue("1\n2\n3\n4\na\nb\nC");
        textArea.shouldHaveValue("1\n2\n3\n4\na\nb\nC");
    }

}
