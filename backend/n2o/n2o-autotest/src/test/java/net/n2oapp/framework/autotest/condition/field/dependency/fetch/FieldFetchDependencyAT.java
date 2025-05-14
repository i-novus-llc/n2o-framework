package net.n2oapp.framework.autotest.condition.field.dependency.fetch;

import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.control.CheckboxGroup;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.control.RadioGroup;
import net.n2oapp.framework.autotest.api.component.fieldset.SimpleFieldSet;
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
 * Автотест проверяющий fetch-зависимость у полей
 */
class FieldFetchDependencyAT extends AutoTestBase {

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
        builder.packs(
                new N2oAllPagesPack(),
                new N2oAllDataPack(),
                new N2oApplicationPack()
        );
    }

    @Test
    void fetchWithArray() {
        setResourcePath("net/n2oapp/framework/autotest/condition/field/dependency/fetch");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/condition/field/dependency/fetch/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/condition/field/dependency/fetch/test.query.xml")
        );

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        Fields fields = page.widget(FormWidget.class).fieldsets().fieldset(0, SimpleFieldSet.class).fields();

        InputText inputText = fields.field("values[0].type").control(InputText.class);
        RadioGroup radioGroup = fields.field("Фильтр по радио кнопкам").control(RadioGroup.class);
        CheckboxGroup checkboxGroup = fields.field("Фильтр по чекбоксам").control(CheckboxGroup.class);

        inputText.clickPlusStepButton();
        inputText.shouldHaveValue("1");
        radioGroup.shouldHaveOptions(new String[]{"test1", "test2"});
        checkboxGroup.shouldHaveOptions(new String[]{"test1", "test2"});

        inputText.clickPlusStepButton();
        inputText.shouldHaveValue("2");
        radioGroup.shouldHaveOptions(new String[]{"test3", "test4"});
        checkboxGroup.shouldHaveOptions(new String[]{"test3", "test4"});

        inputText.clickMinusStepButton();
        inputText.shouldHaveValue("1");
        radioGroup.shouldHaveOptions(new String[]{"test1", "test2"});
        checkboxGroup.shouldHaveOptions(new String[]{"test1", "test2"});

        inputText.clickMinusStepButton();
        inputText.shouldHaveValue("0");
        radioGroup.shouldNotHaveOptions();
        checkboxGroup.shouldNotHaveOptions();
    }
}
