package net.n2oapp.framework.autotest.action;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.control.MaskedInput;
import net.n2oapp.framework.autotest.api.component.control.OutputText;
import net.n2oapp.framework.autotest.api.component.control.Select;
import net.n2oapp.framework.autotest.api.component.field.ButtonField;
import net.n2oapp.framework.autotest.api.component.field.StandardField;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
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
 * Автотест действия установки значения в модель
 */
class SetValueAT extends AutoTestBase {

    @BeforeAll
    static void beforeClass() {
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
        setResourcePath("net/n2oapp/framework/autotest/action/set_value");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/action/set_value/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/action/set_value/test.query.xml"));
    }

    @Test
    void testSetValue() {
        StandardPage page = open(StandardPage.class);
        page.breadcrumb().crumb(0).shouldHaveLabel("Действие set-value");
        page.shouldExists();

        // вычисление значения
        Fields fields = page.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class).fields();
        fields.field("calcResult").control(OutputText.class).shouldBeEmpty();
        fields.field("calc 55+66+77", ButtonField.class).click();
        fields.field("calcResult").control(OutputText.class).shouldHaveValue("198");

        // текущая дата
        fields.field("clockResult").control(OutputText.class).shouldBeEmpty();
        fields.field("getTime", ButtonField.class).click();
        fields.field("clockResult").control(OutputText.class).element().shouldBe(Condition.matchText("^([0-1]\\d|2[0-3])(:[0-5]\\d){2}$"));

        // копирование из select в output
        StandardField socialField = fields.field("social");
        Select social = socialField.control(Select.class);
        StandardButton copyUrlBtn = socialField.toolbar().button("copyUrl");
        OutputText siteUrl = fields.field("siteUrl").control(OutputText.class);
        siteUrl.shouldBeEmpty();

        social.openPopup();
        social.dropdown().selectItem(0);
        copyUrlBtn.click();
        siteUrl.shouldHaveValue("https://fb.com");
        social.openPopup();
        social.dropdown().selectItem(2);
        copyUrlBtn.click();
        siteUrl.shouldHaveValue("https://youtube.com");

        // копирование из masked-input в masked-input
        StandardField phone1Field = fields.field("phone1");
        MaskedInput phone1 = phone1Field.control(MaskedInput.class);
        StandardButton copyPhoneBtn = phone1Field.toolbar().button("copyPhone");
        MaskedInput phone2 = fields.field("phone2").control(MaskedInput.class);

        phone1.shouldBeEmpty();
        phone2.shouldBeEmpty();
        phone1.setValue("112233");
        phone1.shouldHaveValue("11-22-33");
        copyPhoneBtn.click();
        phone2.shouldHaveValue("11-22-33");
        // повторное копирование
        phone1.setValue("998877");
        phone1.shouldHaveValue("99-88-77");
        copyPhoneBtn.click();
        phone2.shouldHaveValue("99-88-77");

        // сброс значения
        StandardField phoneField = fields.field("phone3");
        MaskedInput phone = phoneField.control(MaskedInput.class);
        StandardButton resetPhoneBtn = phoneField.toolbar().button("reset");

        phone.shouldHaveValue("12-34-56");
        resetPhoneBtn.click();
        phone.shouldBeEmpty();
        // повторный сброс
        phone.setValue("111111");
        phone.shouldHaveValue("11-11-11");
        resetPhoneBtn.click();
        phone.shouldBeEmpty();

        fields.field("source").control(InputText.class).shouldHaveValue("test");
        fields.field("results").control(InputText.class).shouldHaveValue("test");
    }
}
