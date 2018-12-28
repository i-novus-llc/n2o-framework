package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.JsonMetadataTestBase;
import org.junit.Before;
import org.junit.Test;

/**
 * Тестирвоание маппинга java модели в json input-text
 */
public class OutputTextJsonTest extends JsonMetadataTestBase {
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
        builder.compilers(new OutputTextCompiler());
    }

    @Test
    public void testOutputText() {
        check("net/n2oapp/framework/config/mapping/testOutputText.widget.xml",
                "components/controls/Output/Output.meta.json")
                .cutXml("form.fieldsets[0].rows[0].cols[0].fields[0].control")
                .exclude("style", "src")
                .assertEquals();
    }

}