package net.n2oapp.framework.autotest.condition.fieldset.enabled;

import net.n2oapp.framework.autotest.api.collection.FieldSets;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.fieldset.MultiFieldSet;
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

public class MultiFieldsetAT extends AutoTestBase {

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
        builder.packs(
                new N2oApplicationPack(),
                new N2oAllPagesPack(),
                new N2oAllDataPack()
        );
    }

    @Test
    void expressionCondition() {
        setJsonPath("net/n2oapp/framework/autotest/condition/fieldset/enabled/multiset_with_expression");
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/condition/fieldset/enabled/multiset_with_expression/index.page.xml"),
                new CompileInfo("net/n2oapp/framework/autotest/condition/fieldset/enabled/multiset_with_expression/test.query.xml")
        );

        final SimplePage page = open(SimplePage.class);
        page.shouldExists();

        final FieldSets fieldsets = page.widget(FormWidget.class).fieldsets();

        fieldsets.fieldset(0, MultiFieldSet.class)
                .item(0)
                .fields()
                .field("value")
                .control(InputText.class)
                .shouldBeEnabled();

        fieldsets.fieldset(1, MultiFieldSet.class)
                .item(0)
                .fields()
                .field("value")
                .control(InputText.class)
                .shouldBeDisabled();

        fieldsets.fieldset(3, MultiFieldSet.class)
                .item(0)
                .fields()
                .field("3")
                .control(InputText.class)
                .shouldBeEnabled();

        fieldsets.fieldset(5, MultiFieldSet.class)
                .item(0)
                .fields()
                .field("4")
                .control(InputText.class)
                .shouldBeDisabled();
    }
}
