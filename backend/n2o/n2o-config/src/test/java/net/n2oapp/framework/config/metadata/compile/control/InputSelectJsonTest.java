package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.JsonMetadataTestBase;
import org.junit.Before;
import org.junit.Test;

public class InputSelectJsonTest extends JsonMetadataTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(),
                new N2oActionsPack(), new N2oAllDataPack(), new N2oControlsV2IOPack());
        builder.compilers(new InputSelectCompiler());
    }

    @Test
    public void testInputSelect() {
        check("net/n2oapp/framework/config/metadata/compile/field/testInputSelectJson.widget.xml",
                "components/controls/InputSelect/InputSelect.meta.json")
                .exclude("src", "disabled", "hasSearch", "data", "queryId", "loading", "value", "filter", "closePopupOnSelect",
                        "data[0].icon", "data[0].image", "data[0].dob", "data[0].country",
                        "data[1].icon", "data[1].image", "data[1].dob", "data[1].country",
                        "data[2].icon", "data[2].image", "data[2].dob", "data[2].country",
                        "data[3].icon", "data[3].image", "data[3].dob", "data[3].country",
                        "data[4].icon", "data[4].image", "data[4].dob", "data[4].country")
                .cutXml("form.fieldsets[0].rows[0].cols[0].fields[0].control")
                .assertEquals();
    }
}