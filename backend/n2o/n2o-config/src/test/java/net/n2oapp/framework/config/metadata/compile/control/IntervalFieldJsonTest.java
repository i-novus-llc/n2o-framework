package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.JsonMetadataTestBase;
import org.junit.Before;
import org.junit.Test;

public class IntervalFieldJsonTest extends JsonMetadataTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(),
                new N2oActionsPack(), new N2oAllDataPack(), new N2oControlsPack());
    }

    @Test
    public void testIntervalField() {
        check("net/n2oapp/framework/config/mapping/testIntervalField.widget.xml",
                "components/widgets/Form/fields/RangeField/RangeField.meta.json")
                .cutXml("form.fieldsets[0].rows[0].cols[0].fields[0]").
                exclude("controlClass", "labelStyle", "disabled", "controlClass", "measure", "validationClass",
                        "autoFocus",
                        "loading", "controlStyle", "className", "required", "enabled", "description", "style",
                        "fieldActions",
                        "message", "help", "beginControl.min", "beginControl.max", "endControl.min", "endControl.max"
                        , "beginControl.step", "endControl.step")
                .assertEquals();
    }
}
