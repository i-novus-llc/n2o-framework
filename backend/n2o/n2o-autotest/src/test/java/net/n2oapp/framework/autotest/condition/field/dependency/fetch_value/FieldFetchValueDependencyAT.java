package net.n2oapp.framework.autotest.condition.field.dependency.fetch_value;

import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.control.InputText;
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
 * Автотест, проверяющий fetch-value зависимость у полей
 */
class FieldFetchValueDependencyAT extends AutoTestBase {

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
        builder.packs(
                new N2oAllPagesPack(),
                new N2oAllDataPack(),
                new N2oApplicationPack()
        );
    }

    @Test
    void test() {
        setResourcePath("net/n2oapp/framework/autotest/condition/field/dependency/fetch_value");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/condition/field/dependency/fetch_value/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/condition/field/dependency/fetch_value/test.query.xml")
        );

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        Fields fields = page.widget(FormWidget.class).fieldsets().fieldset(0, SimpleFieldSet.class).fields();

        InputText inputText1 = fields.field("Введите id").control(InputText.class);
        InputText inputText2 = fields.field("Должно появиться имя").control(InputText.class);

        inputText1.clickPlusStepButton();
        inputText1.shouldHaveValue("1");
        inputText2.shouldHaveValue("test1");

        inputText1.clickPlusStepButton();
        inputText1.shouldHaveValue("2");
        inputText2.shouldHaveValue("test2");

        inputText1.clickPlusStepButton();
        inputText1.shouldHaveValue("3");
        inputText2.shouldBeEmpty();
    }
}
