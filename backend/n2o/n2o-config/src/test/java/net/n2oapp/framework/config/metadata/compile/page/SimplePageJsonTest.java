package net.n2oapp.framework.config.metadata.compile.page;

import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.test.JsonMetadataTestBase;
import org.junit.Before;
import org.junit.Test;

/**
 * Тестирвоание маппинга java модели в json простой старницы
 */
public class SimplePageJsonTest extends JsonMetadataTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack());
    }

    @Test
    public void simplePage() {
        check("net/n2oapp/framework/config/mapping/testSimplePageJson.page.xml",
                "components/core/Page.meta.json")
                .changeValue("id", "test")
                .exclude("breadcrumb", "routes", "page.modelLink", "models",
                        "widget.actions",
                        "widget.toolbar.topLeft[0].id",
                        "widget.toolbar.topLeft[0].buttons[0].conditions",
                        "widget.toolbar.topLeft[0].buttons[0].confirm",
                        "widget.toolbar.topLeft[0].buttons[0].hint",
                        "widget.toolbar.topLeft[0].buttons[0].src",
                        "widget.toolbar.topLeft[0].buttons[0].action",
                        "layout.regions.single[0].items[0].id",
                        "layout.regions.single[0].items[0].label",
                        "widget.toolbar.topLeft[0].buttons[0].hintPosition")
                .assertEquals();
    }
}
