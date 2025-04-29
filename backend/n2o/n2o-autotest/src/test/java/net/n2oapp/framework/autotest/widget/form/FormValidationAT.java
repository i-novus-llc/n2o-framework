package net.n2oapp.framework.autotest.widget.form;

import com.codeborne.selenide.Condition;
import net.n2oapp.framework.autotest.api.component.button.StandardButton;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.field.StandardField;
import net.n2oapp.framework.autotest.api.component.page.StandardPage;
import net.n2oapp.framework.autotest.api.component.region.SimpleRegion;
import net.n2oapp.framework.autotest.api.component.region.TabsRegion;
import net.n2oapp.framework.autotest.api.component.snippet.Alert;
import net.n2oapp.framework.autotest.api.component.widget.FormWidget;
import net.n2oapp.framework.autotest.run.AutoTestBase;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Тестирование валидации форм
 */
class FormValidationAT extends AutoTestBase {
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
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oApplicationPack(), new N2oWidgetsPack(),
                new N2oFieldSetsPack(), new N2oActionsPack(), new N2oControlsPack(), new N2oAllDataPack());

        builder.sources(new CompileInfo("net/n2oapp/framework/autotest/widget/form/validation/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/widget/form/validation/test.object.xml"));
    }

    @Test
    void formValidationTest() {
        StandardPage page = open(StandardPage.class);
        page.shouldExists();

        FormWidget form = page.regions().region(0, SimpleRegion.class).content().widget(FormWidget.class);
        form.shouldExists();
        StandardButton validateFormBtn = form.toolbar().topLeft().button("Валидировать форму");
        StandardButton validatePageBtn = form.toolbar().topLeft().button("Валидировать страницу");

        TabsRegion tabs = page.regions().region(1, TabsRegion.class);
        tabs.shouldHaveSize(3);
        tabs.tab(0).shouldBeActive();

        // validate one form
        StandardField form1Field = tabs.tab(0).content().widget(FormWidget.class).fields().field("form1.name");
        InputText form1Control = form1Field.control(InputText.class);
        form1Control.shouldBeEmpty();
        validateFormBtn.click();
        form1Field.shouldHaveValidationMessage(Condition.text("Поле обязательно для заполнения"));
        page.alerts(Alert.PlacementEnum.top).shouldBeEmpty();

        form1Control.click();
        form1Control.setValue("value1");
        validateFormBtn.click();
        form1Field.shouldHaveValidationMessage(Condition.empty);
        page.alerts(Alert.PlacementEnum.top).shouldHaveSize(1);

        // validate all forms
        tabs.tab(2).click();
        StandardField form3Field = tabs.tab(1).content().widget(FormWidget.class).fields().field("form3.name");
        InputText form3Control = form3Field.control(InputText.class);
        form3Control.click();
        form3Control.setValue("value3");

        validatePageBtn.click();
        page.alerts(Alert.PlacementEnum.top).shouldBeEmpty();

        tabs.tab(1).click();
        StandardField form2Field = tabs.tab(1).content().widget(FormWidget.class).fields().field("form2.name");
        InputText form2Control = form2Field.control(InputText.class);
        form2Control.click();
        form2Control.setValue("value2");

        validatePageBtn.click();
        page.alerts(Alert.PlacementEnum.top).shouldHaveSize(1);

    }
}
