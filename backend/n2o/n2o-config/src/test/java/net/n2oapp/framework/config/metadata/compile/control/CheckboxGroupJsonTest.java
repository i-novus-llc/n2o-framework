package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.JsonMetadataTestBase;
import org.junit.Before;
import org.junit.Test;

public class CheckboxGroupJsonTest extends JsonMetadataTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oActionsPack(),
                new N2oFieldSetsPack(), new N2oAllDataPack(), new N2oControlsV2IOPack());
        builder.compilers(new CheckboxGroupCompiler());
    }

    @Test
    public void testCheckboxGroup() {
        check("net/n2oapp/framework/config/metadata/compile/field/testCheckboxGroupJson.widget.xml",
                "components/controls/CheckboxGroup/CheckboxGroup.meta.json")
                .exclude("src", "id", "data", "hasSearch", "disabled", "checked", "style", "value", "type",
                        "closePopupOnSelect", "visible", "sortFieldId")
                .cutXml("form.fieldsets[0].rows[0].cols[0].fields[0].control")
                .assertEquals();

    }

}

