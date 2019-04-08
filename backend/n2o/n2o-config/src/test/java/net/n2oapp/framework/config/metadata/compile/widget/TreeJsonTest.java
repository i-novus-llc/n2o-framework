package net.n2oapp.framework.config.metadata.compile.widget;

import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oControlsPack;
import net.n2oapp.framework.config.metadata.pack.N2oFieldSetsPack;
import net.n2oapp.framework.config.metadata.pack.N2oWidgetsPack;
import net.n2oapp.framework.config.test.JsonMetadataTestBase;
import org.junit.Before;
import org.junit.Test;

public class TreeJsonTest extends JsonMetadataTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllDataPack(), new N2oWidgetsPack(), new N2oFieldSetsPack(), new N2oControlsPack());
    }

    @Test
    public void tree() {
        check("net/n2oapp/framework/config/metadata/compile/widgets/testJsonTree.widget.xml",
                "components/widgets/Tree/TreeWidget.meta.json")
                .cutJson("Page_Tree")
                .exclude("dataProvider", "disabled", "parentIcon",
                        "childIcon", "showLine", "bulkData", "icon", "hasFocus", "filter", "expandBtn")
                .assertEquals();

    }
}
