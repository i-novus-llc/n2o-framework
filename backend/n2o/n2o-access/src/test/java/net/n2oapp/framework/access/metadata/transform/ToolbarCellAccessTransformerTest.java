package net.n2oapp.framework.access.metadata.transform;

import net.n2oapp.framework.access.integration.metadata.transform.ToolbarCellAccessTransformer;
import net.n2oapp.framework.access.integration.metadata.transform.action.InvokeActionAccessTransformer;
import net.n2oapp.framework.access.metadata.Security;
import net.n2oapp.framework.access.metadata.pack.AccessSchemaPack;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.cell.ToolbarCell;
import net.n2oapp.framework.api.metadata.meta.widget.table.TableWidgetComponent;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileTerminalPipeline;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SimplePropertyResolver;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ToolbarCellAccessTransformerTest extends SourceCompileTestBase {
    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack(), new N2oAllDataPack(), new AccessSchemaPack())
                .sources(new CompileInfo("net/n2oapp/framework/access/metadata/transform/testToolbarAccessTransformer.object.xml"))
                .transformers(new ToolbarCellAccessTransformer(), new InvokeActionAccessTransformer());
    }

    @Test
    void toolbarCellAccessTransformer() {
        ((SimplePropertyResolver) builder.getEnvironment().getSystemProperties()).setProperty("n2o.access.schema.id", "testToolbar");

        ReadCompileTerminalPipeline<?> pipeline = compile(
                "net/n2oapp/framework/access/metadata/schema/testToolbar.access.xml",
                "net/n2oapp/framework/access/metadata/transform/testToolbarCellAccessTransformer.page.xml"
        );
        SimplePage page = (SimplePage) pipeline.transform().get(new PageContext("testToolbarCellAccessTransformer"));
        Security.SecurityObject security = ((Security) ((ToolbarCell) ((TableWidgetComponent) page.getWidget()
                .getComponent()).getCells().get(0)).getToolbar().get(0).getButtons().get(0)
                .getProperties().get(Security.SECURITY_PROP_NAME)).getSecurityMap().get("object");

        assertThat(security.getRoles().size(), is(1));
        assertTrue(security.getRoles().contains("admin"));
        assertThat(security.getPermissions().size(), is(1));
        assertTrue(security.getPermissions().contains("permission"));
        assertThat(security.getUsernames().size(), is(1));
        assertTrue(security.getUsernames().contains("user"));

        security = ((Security) ((ToolbarCell) ((TableWidgetComponent) page.getWidget()
                .getComponent()).getCells().get(0)).getToolbar().get(0).getButtons().get(2)
                .getProperties().get(Security.SECURITY_PROP_NAME)).getSecurityMap().get("url");

        assertThat(security.getRoles().size(), is(1));
        assertTrue(security.getRoles().contains("role"));
        assertThat(security.getPermissions(), nullValue());
        assertThat(security.getUsernames(), nullValue());
    }
}