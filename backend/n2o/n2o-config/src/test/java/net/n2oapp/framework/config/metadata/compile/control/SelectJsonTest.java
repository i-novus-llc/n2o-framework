package net.n2oapp.framework.config.metadata.compile.control;

import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.JsonMetadataTestBase;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class SelectJsonTest extends JsonMetadataTestBase {
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
        builder.compilers(new SelectCompiler());
    }

    @Test
    @Ignore //todo
    public void testSelect() {
        check("net/n2oapp/framework/config/metadata/compile/field/testSelect.widget.xml",
                "components/controls/N2OSelect/N2OSelect.meta.json")
                .exclude("loading", "searchByTap", "filter", "resetOnBlur", "closePopupOnSelect")
                .cutXml("form.fieldsets[0].rows[0].cols[0].fields[0].control")
                .assertEquals();
    }
}