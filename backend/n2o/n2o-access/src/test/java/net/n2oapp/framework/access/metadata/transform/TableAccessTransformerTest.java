package net.n2oapp.framework.access.metadata.transform;

import net.n2oapp.framework.access.metadata.BehaviorEnum;
import net.n2oapp.framework.access.metadata.Security;
import net.n2oapp.framework.access.metadata.SecurityObject;
import net.n2oapp.framework.access.metadata.pack.AccessSchemaPack;
import net.n2oapp.framework.api.metadata.aware.PropertiesAware;
import net.n2oapp.framework.api.metadata.meta.cell.Cell;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.table.AbstractColumn;
import net.n2oapp.framework.api.metadata.meta.widget.table.BaseColumn;
import net.n2oapp.framework.api.metadata.meta.widget.table.DndColumn;
import net.n2oapp.framework.api.metadata.meta.widget.table.MultiColumn;
import net.n2oapp.framework.api.metadata.meta.widget.table.SimpleColumn;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
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

import java.util.Map;

import static net.n2oapp.framework.access.metadata.Security.SECURITY_PROP_NAME;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

class TableAccessTransformerTest extends SourceCompileTestBase {
    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.sources(new CompileInfo("net/n2oapp/framework/access/metadata/transform/testQuery.query.xml"),
                        new CompileInfo("net/n2oapp/framework/access/metadata/transform/testToolbarAccessTransformer.page.xml"),
                        new CompileInfo("net/n2oapp/framework/access/metadata/transform/testObjectAccessTransformer.object.xml"))
                .packs(new AccessSchemaPack(), new N2oAllDataPack(), new N2oAllPagesPack());
    }

    @Test
    void testTableRowsAccess() {
        ((SimplePropertyResolver) builder.getEnvironment().getSystemProperties()).setProperty("n2o.access.schema.id", "testRegion");

        ReadCompileTerminalPipeline pipeline = compile("net/n2oapp/framework/access/metadata/schema/testRegion.access.xml",
                "net/n2oapp/framework/access/metadata/transform/testTableAccessTransformer.page.xml");

        StandardPage page = (StandardPage) ((ReadCompileTerminalPipeline) pipeline.transform())
                .get(new PageContext("testTableAccessTransformer"));

        SecurityObject securityObject = ((Security) (((Table) page.getRegions().get("single").getFirst().getContent().getFirst())
                .getComponent()).getBody().getRow().getProperties()
                .get(SECURITY_PROP_NAME)).getFirst().get("url");

        assertThat(securityObject.getPermissions().size(), is(1));
        assertThat(securityObject.getPermissions().contains("permission"), is(true));
        assertThat(securityObject.getUsernames().contains("user"), is(true));
    }

    @Test
    void testColumnSecurityTransferToCell() {
        ReadCompileTerminalPipeline pipeline = compile("net/n2oapp/framework/access/metadata/default.access.xml",
                "net/n2oapp/framework/access/metadata/transform/testColumnSecurityTransfer.page.xml");

        StandardPage page = (StandardPage) ((ReadCompileTerminalPipeline) pipeline.transform())
                .get(new PageContext("testColumnSecurityTransfer"));

        Table table = (Table) page.getRegions().get("single").getFirst().getContent().getFirst();
        Cell cell = table.getComponent().getBody().getCells().getFirst();

        Security security = (Security) ((PropertiesAware) cell).getProperties().get(SECURITY_PROP_NAME);
        assertThat(security, notNullValue());
        assertThat(security.isEmpty(), is(false));

        SecurityObject securityObject = security.getFirst().get("custom");
        assertThat(securityObject, notNullValue());
        assertThat(securityObject.getRoles().contains("admin"), is(true));
        assertThat(securityObject.getPermissions().contains("edit"), is(true));
    }

    @Test
    void testColumnHiddenWhenAllButtonsRestricted() {
        ReadCompileTerminalPipeline pipeline = compile("net/n2oapp/framework/access/metadata/default.access.xml",
                "net/n2oapp/framework/access/metadata/transform/testColumnAllButtonsRestricted.page.xml");

        StandardPage page = (StandardPage) ((ReadCompileTerminalPipeline) pipeline.transform())
                .get(new PageContext("testColumnAllButtonsRestricted"));

        Table table = (Table) page.getRegions().get("single").getFirst().getContent().getFirst();
        AbstractColumn column = table.getComponent().getHeader().getCells().getFirst();

        Security security = (Security) ((PropertiesAware) column).getProperties().get(SECURITY_PROP_NAME);
        assertThat(security, notNullValue());

        SecurityObject securityObject = security.getFirst().get("custom");
        assertThat(securityObject, notNullValue());
        assertThat(securityObject.getRoles().contains("admin"), is(true));
        assertThat(securityObject.getRoles().contains("user"), is(true));
        assertThat(securityObject.getPermissions().contains("edit"), is(true));
        assertThat(securityObject.getPermissions().contains("view"), is(true));
        assertThat(securityObject.getBehavior(), is(BehaviorEnum.HIDE));
    }

    @Test
    void testColumnNotComputedWhenAnyButtonOpen() {
        ReadCompileTerminalPipeline pipeline = compile("net/n2oapp/framework/access/metadata/default.access.xml",
                "net/n2oapp/framework/access/metadata/transform/testColumnAnyButtonOpen.page.xml");

        StandardPage page = (StandardPage) ((ReadCompileTerminalPipeline) pipeline.transform())
                .get(new PageContext("testColumnAnyButtonOpen"));

        Table table = (Table) page.getRegions().get("single").getFirst().getContent().getFirst();
        AbstractColumn column = table.getComponent().getHeader().getCells().getFirst();

        Map<String, Object> props = ((PropertiesAware) column).getProperties();
        assertThat(props == null || props.get(SECURITY_PROP_NAME) == null, is(true));
    }

    @Test
    void testColumnSecurityNotOverwritten() {
        ReadCompileTerminalPipeline pipeline = compile("net/n2oapp/framework/access/metadata/default.access.xml",
                "net/n2oapp/framework/access/metadata/transform/testColumnSecurityNotOverwritten.page.xml");

        StandardPage page = (StandardPage) ((ReadCompileTerminalPipeline) pipeline.transform())
                .get(new PageContext("testColumnSecurityNotOverwritten"));

        Table table = (Table) page.getRegions().get("single").getFirst().getContent().getFirst();
        AbstractColumn column = table.getComponent().getHeader().getCells().getFirst();

        Security security = (Security) ((PropertiesAware) column).getProperties().get(SECURITY_PROP_NAME);
        SecurityObject securityObject = security.getFirst().get("custom");
        assertThat(securityObject.getRoles().size(), is(1));
        assertThat(securityObject.getRoles().contains("superAdmin"), is(true));
        assertThat(securityObject.getRoles().contains("admin"), is(false));
        assertThat(securityObject.getRoles().contains("user"), is(false));
    }

    @Test
    void testColumnDisabledWhenAnyButtonDisableBehavior() {
        ReadCompileTerminalPipeline pipeline = compile("net/n2oapp/framework/access/metadata/default.access.xml",
                "net/n2oapp/framework/access/metadata/transform/testColumnButtonDisableBehavior.page.xml");

        StandardPage page = (StandardPage) ((ReadCompileTerminalPipeline) pipeline.transform())
                .get(new PageContext("testColumnButtonDisableBehavior"));

        Table table = (Table) page.getRegions().get("single").getFirst().getContent().getFirst();
        AbstractColumn column = table.getComponent().getHeader().getCells().getFirst();

        Security security = (Security) ((PropertiesAware) column).getProperties().get(SECURITY_PROP_NAME);
        SecurityObject securityObject = security.getFirst().get("custom");
        assertThat(securityObject.getBehavior(), is(BehaviorEnum.DISABLE));
    }

    @Test
    void testSubmenuButtonsCounted() {
        ReadCompileTerminalPipeline pipeline = compile("net/n2oapp/framework/access/metadata/default.access.xml",
                "net/n2oapp/framework/access/metadata/transform/testColumnSubmenuOpen.page.xml");

        StandardPage page = (StandardPage) ((ReadCompileTerminalPipeline) pipeline.transform())
                .get(new PageContext("testColumnSubmenuOpen"));

        Table table = (Table) page.getRegions().get("single").getFirst().getContent().getFirst();
        AbstractColumn column = table.getComponent().getHeader().getCells().getFirst();

        Map<String, Object> props = ((PropertiesAware) column).getProperties();
        assertThat(props == null || props.get(SECURITY_PROP_NAME) == null, is(true));
    }

    @Test
    void testMultiColumnChildrenProcessed() {
        ReadCompileTerminalPipeline pipeline = compile("net/n2oapp/framework/access/metadata/default.access.xml",
                "net/n2oapp/framework/access/metadata/transform/testColumnMultiColumnChildren.page.xml");

        StandardPage page = (StandardPage) ((ReadCompileTerminalPipeline) pipeline.transform())
                .get(new PageContext("testColumnMultiColumnChildren"));

        Table table = (Table) page.getRegions().get("single").getFirst().getContent().getFirst();
        MultiColumn multi = (MultiColumn) table.getComponent().getHeader().getCells().getFirst();

        // На самом MultiColumn security не ставится
        Map<String, Object> multiProps = multi.getProperties();
        assertThat(multiProps == null || multiProps.get(SECURITY_PROP_NAME) == null, is(true));

        // На каждом ребёнке — свой security
        BaseColumn childA = multi.getChildren().getFirst();
        Security secA = (Security) childA.getProperties().get(SECURITY_PROP_NAME);
        assertThat(secA, notNullValue());
        SecurityObject soA = secA.getFirst().get("custom");
        assertThat(soA.getRoles().contains("admin"), is(true));
        assertThat(soA.getRoles().contains("user"), is(true));

        BaseColumn childB = multi.getChildren().get(1);
        Security secB = (Security) childB.getProperties().get(SECURITY_PROP_NAME);
        assertThat(secB, notNullValue());
        SecurityObject soB = secB.getFirst().get("custom");
        assertThat(soB.getPermissions().contains("edit"), is(true));
        assertThat(soB.getPermissions().contains("view"), is(true));
    }

    @Test
    void testSubmenuWithOwnSecurityTreatedAsSingleButton() {
        ReadCompileTerminalPipeline pipeline = compile("net/n2oapp/framework/access/metadata/default.access.xml",
                "net/n2oapp/framework/access/metadata/transform/testColumnSubmenuOwnSecurity.page.xml");

        StandardPage page = (StandardPage) ((ReadCompileTerminalPipeline) pipeline.transform())
                .get(new PageContext("testColumnSubmenuOwnSecurity"));

        Table table = (Table) page.getRegions().get("single").getFirst().getContent().getFirst();
        AbstractColumn column = table.getComponent().getHeader().getCells().getFirst();

        Security security = (Security) ((PropertiesAware) column).getProperties().get(SECURITY_PROP_NAME);
        assertThat(security, notNullValue());
        SecurityObject securityObject = security.getFirst().get("custom");
        assertThat(securityObject, notNullValue());
        assertThat(securityObject.getRoles().size(), is(1));
        assertThat(securityObject.getRoles().contains("admin"), is(true));
    }

    @Test
    void testEmptyToolbarLeavesColumnUntouched() {
        ReadCompileTerminalPipeline pipeline = compile("net/n2oapp/framework/access/metadata/default.access.xml",
                "net/n2oapp/framework/access/metadata/transform/testColumnEmptyToolbar.page.xml");

        StandardPage page = (StandardPage) ((ReadCompileTerminalPipeline) pipeline.transform())
                .get(new PageContext("testColumnEmptyToolbar"));

        Table table = (Table) page.getRegions().get("single").getFirst().getContent().getFirst();
        AbstractColumn column = table.getComponent().getHeader().getCells().getFirst();

        Map<String, Object> props = ((PropertiesAware) column).getProperties();
        assertThat(props == null || props.get(SECURITY_PROP_NAME) == null, is(true));
    }

    @Test
    void testDndColumnChildrenProcessed() {
        ReadCompileTerminalPipeline pipeline = compile("net/n2oapp/framework/access/metadata/default.access.xml",
                "net/n2oapp/framework/access/metadata/transform/testColumnDndColumnChildren.page.xml");

        StandardPage page = (StandardPage) ((ReadCompileTerminalPipeline) pipeline.transform())
                .get(new PageContext("testColumnDndColumnChildren"));

        Table table = (Table) page.getRegions().get("single").getFirst().getContent().getFirst();
        DndColumn dnd = (DndColumn) table.getComponent().getHeader().getCells().getFirst();

        SimpleColumn childA = dnd.getChildren().getFirst();
        Security secA = (Security) childA.getProperties().get(SECURITY_PROP_NAME);
        assertThat(secA, notNullValue());
        SecurityObject soA = secA.getFirst().get("custom");
        assertThat(soA.getRoles().contains("admin"), is(true));

        SimpleColumn childB = dnd.getChildren().get(1);
        Security secB = (Security) childB.getProperties().get(SECURITY_PROP_NAME);
        assertThat(secB, notNullValue());
        SecurityObject soB = secB.getFirst().get("custom");
        assertThat(soB.getPermissions().contains("edit"), is(true));
    }
}
