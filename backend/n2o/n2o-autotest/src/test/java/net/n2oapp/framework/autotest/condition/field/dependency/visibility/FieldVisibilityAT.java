package net.n2oapp.framework.autotest.condition.field.dependency.visibility;

import net.n2oapp.framework.autotest.api.collection.Fields;
import net.n2oapp.framework.autotest.api.component.control.InputText;
import net.n2oapp.framework.autotest.api.component.control.RadioGroup;
import net.n2oapp.framework.autotest.api.component.page.SimplePage;
import net.n2oapp.framework.autotest.api.component.snippet.Text;
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
 * Тестирование видимость-зависимости полей
 */
class FieldVisibilityAT extends AutoTestBase {

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
                new N2oApplicationPack(),
                new N2oAllDataPack()
        );
        builder.sources(
                new CompileInfo("net/n2oapp/framework/autotest/condition/field/dependency/visibility/index.page.xml")
        );
    }

    @Test
    void defaultVisibility() {
        final SimplePage page = open(SimplePage.class);
        page.shouldExists();

        final Fields fields = page.widget(FormWidget.class).fields();

        fields.field("dependencies[apply-on-init].visibility=true").control(InputText.class).shouldExists();

        fields.field("dependencies[apply-on-init].visibility=false").control(InputText.class).shouldNotExists();

        fields.field("dependencies[apply-on-init].visibility=undefined").control(InputText.class).shouldNotExists();

        fields.field("dependencies[apply-on-init].visibility=null").control(InputText.class).shouldNotExists();
    }

    @Test
    void dynamicVisibility() {
        final SimplePage page = open(SimplePage.class);
        page.shouldExists();

        final Fields fields = page.widget(FormWidget.class).fields();
        final RadioGroup type = fields.field("type").control(RadioGroup.class);
        type.shouldExists();
        type.check("visible");
        
        final InputText inputText = fields.field("visible by condition").control(InputText.class);

        inputText.shouldExists();

        type.check("not visible");

        inputText.shouldNotExists();
    }
}
