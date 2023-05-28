package net.n2oapp.framework.autotest.condition.field.dependency.enabling;

import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.control.InputText;
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

public class FieldEnablingAT extends AutoTestBase {

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
    public void simpleEnabling() {
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/condition/field/dependency/enabling/index.page.xml")
        );

        SimplePage page = open(SimplePage.class);
        page.shouldExists();

        Fields fields = page.widget(FormWidget.class).fields();

        fields.field("dependencies[apply-on-init].enabling=true").control(InputText.class).shouldBeEnabled();

        fields.field("dependencies[apply-on-init].enabling=false").control(InputText.class).shouldBeDisabled();

        fields.field("dependencies[apply-on-init].enabling=undefined").control(InputText.class).shouldBeDisabled();

        fields.field("dependencies[apply-on-init].enabling=null").control(InputText.class).shouldBeDisabled();
    }
}
