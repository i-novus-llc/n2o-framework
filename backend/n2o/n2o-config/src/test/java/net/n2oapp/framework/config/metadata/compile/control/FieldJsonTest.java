package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.JsonMetadataTestBase;
import org.junit.Before;
import org.junit.Test;

/**
 * Тестирование маппинга java модели в json field
 */
public class FieldJsonTest extends JsonMetadataTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsV2IOPack());
        builder.compilers(new InputTextCompiler());
    }

    @Test
    public void testInputText() {
        check("net/n2oapp/framework/config/mapping/testInputText.widget.xml",
                "components/widgets/Form/fields/StandardField/Field.meta.json")
                .cutXml("form.fieldsets[0].rows[0].cols[0].fields[0]")
                .exclude("src", "className", "readOnly", "style", "control")
                .assertEquals();
    }

    @Test
    public void testNoLabelInputText() {
        check("net/n2oapp/framework/config/mapping/testInputText.widget.xml",
                "components/widgets/Form/fields/StandardField/NoLabelField.meta.json")
                .cutXml("form.fieldsets[0].rows[0].cols[1].fields[0]")
                .exclude("src", "readOnly", "labelClass", "style", "control")
                .assertEquals();
    }
}
