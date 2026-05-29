package net.n2oapp.framework.access.metadata.transform;

import net.n2oapp.framework.access.integration.metadata.transform.ApplicationAccessTransformer;
import net.n2oapp.framework.access.metadata.Security;
import net.n2oapp.framework.access.metadata.SecurityObject;
import net.n2oapp.framework.access.metadata.pack.AccessSchemaPack;
import net.n2oapp.framework.api.metadata.application.Application;
import net.n2oapp.framework.api.metadata.application.Sidebar;
import net.n2oapp.framework.api.metadata.header.Header;
import net.n2oapp.framework.api.metadata.header.MenuItem;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileTerminalPipeline;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.ApplicationContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SimplePropertyResolver;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static net.n2oapp.framework.access.metadata.Security.SECURITY_PROP_NAME;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

/**
 * Тестирование трансформера прав доступа
 */
class ApplicationAccessTransformerTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        ApplicationAccessTransformer headerAccessTransformer = new ApplicationAccessTransformer();
        builder.packs(new N2oApplicationPack(), new N2oAllPagesPack(), new AccessSchemaPack())
                .sources(new CompileInfo("net/n2oapp/framework/access/metadata/transform/testHeaderAccessTransformer.page.xml"))
                .sources(new CompileInfo("net/n2oapp/framework/access/metadata/transform/testObjectAccessTransformer.object.xml"))
                .transformers(headerAccessTransformer);
    }

    @Test
    void testHeaderSidebarTransform() {
        ((SimplePropertyResolver) builder.getEnvironment().getSystemProperties()).setProperty("n2o.access.schema.id", "testHeader");

        ReadCompileTerminalPipeline pipeline = compile("net/n2oapp/framework/access/metadata/schema/testHeader.access.xml",
                "net/n2oapp/framework/access/metadata/transform/testApplicationAccessTransformer.application.xml");

        Application application = (Application) ((ReadCompileTerminalPipeline) pipeline.transform())
                .get(new ApplicationContext("testApplicationAccessTransformer"));

        checkMenuItem(application.getHeader().getMenu().getItems().getFirst());
        checkMenuItem(application.getHeader().getExtraMenu().getItems().getFirst());
        checkMenuItem(application.getSidebars().getFirst().getMenu().getItems().getFirst());
        checkMenuItem(application.getSidebars().getFirst().getExtraMenu().getItems().getFirst());

        assertThat(((Security) application.getHeader().getMenu().getItems().get(2).getProperties().get(SECURITY_PROP_NAME)).getFirst().get("url").getPermissions().size(), is(1));
        assertThat(((Security) application.getSidebars().getFirst().getMenu().getItems().get(2).getProperties().get(SECURITY_PROP_NAME)).getFirst().get("url").getPermissions().size(), is(1));
        checkAnchor(application.getHeader().getExtraMenu().getItems().get(2));
        checkAnchor(application.getSidebars().getFirst().getExtraMenu().getItems().get(2));
    }

    private void checkAnchor(MenuItem menuItem) {
        assertThat(((Security) menuItem.getProperties().get(SECURITY_PROP_NAME)).getFirst().get("url").getPermissions(), nullValue());
        assertThat(((Security) menuItem.getProperties().get(SECURITY_PROP_NAME)).getFirst().get("url").getRoles(), nullValue());
        assertThat(((Security) menuItem.getProperties().get(SECURITY_PROP_NAME)).getFirst().get("url").getUsernames().contains("user"), is(true));
    }

    private void checkMenuItem(MenuItem item) {
        assertThat(((Security) item.getProperties().get(SECURITY_PROP_NAME)).getFirst().get("page").getUsernames().size(), is(1));
        assertThat(((Security) item.getProperties().get(SECURITY_PROP_NAME)).getFirst().get("page").getUsernames().contains("user"), is(true));
        assertThat(((Security) item.getProperties().get(SECURITY_PROP_NAME)).getFirst().get("page").getRoles().size(), is(2));
        assertThat(((Security) item.getProperties().get(SECURITY_PROP_NAME)).getFirst().get("page").getRoles().contains("role"), is(true));
        assertThat(((Security) item.getProperties().get(SECURITY_PROP_NAME)).getFirst().get("page").getRoles().contains("admin"), is(true));
        assertThat(((Security) item.getProperties().get(SECURITY_PROP_NAME)).getFirst().get("page").getPermissions(), nullValue());

        assertThat(((Security) item.getProperties().get(SECURITY_PROP_NAME)).getFirst().get("object").getPermissions(), nullValue());
        assertThat(((Security) item.getProperties().get(SECURITY_PROP_NAME)).getFirst().get("object").getRoles(), nullValue());
        assertThat(((Security) item.getProperties().get(SECURITY_PROP_NAME)).getFirst().get("object").getUsernames().size(), is(1));
        assertThat(((Security) item.getProperties().get(SECURITY_PROP_NAME)).getFirst().get("object").getUsernames().contains("user"), is(true));
    }

    @Test
    void testHeaderSidebarTransformV2() {
        ((SimplePropertyResolver) builder.getEnvironment().getSystemProperties()).setProperty("n2o.access.schema.id", "testHeaderV2");

        ReadCompileTerminalPipeline pipeline = compile("net/n2oapp/framework/access/metadata/schema/testHeaderV2.access.xml",
                "net/n2oapp/framework/access/metadata/transform/testApplicationAccessTransformer.application.xml");

        Application application = (Application) ((ReadCompileTerminalPipeline) pipeline.transform())
                .get(new ApplicationContext("testApplicationAccessTransformer"));
        Header header = application.getHeader();
        assertAccess(((Security) header.getMenu().getItems().getFirst().getProperties().get(SECURITY_PROP_NAME)).getFirst());
        assertAccess(((Security) header.getMenu().getItems().get(1).getSubItems().getFirst().getProperties().get(SECURITY_PROP_NAME)).getFirst());
        assertThat(((Security) header.getMenu().getItems().get(2).getProperties().get(SECURITY_PROP_NAME)).getFirst().get("url").getRoles().size(), is(1));
        assertAccess(((Security) header.getExtraMenu().getItems().getFirst().getProperties().get(SECURITY_PROP_NAME)).getFirst());
        assertAccess(((Security) header.getExtraMenu().getItems().get(1).getSubItems().getFirst().getProperties().get(SECURITY_PROP_NAME)).getFirst());
        Sidebar sidebar = application.getSidebars().getFirst();
        assertAccess(((Security) sidebar.getMenu().getItems().getFirst().getProperties().get(SECURITY_PROP_NAME)).getFirst());
        assertAccess(((Security) sidebar.getMenu().getItems().get(1).getSubItems().getFirst().getProperties().get(SECURITY_PROP_NAME)).getFirst());
        assertThat(((Security) sidebar.getMenu().getItems().get(2).getProperties().get(SECURITY_PROP_NAME)).getFirst().get("url").getRoles().size(), is(1));
        assertAccess(((Security) sidebar.getExtraMenu().getItems().getFirst().getProperties().get(SECURITY_PROP_NAME)).getFirst());
        assertAccess(((Security) sidebar.getExtraMenu().getItems().get(1).getSubItems().getFirst().getProperties().get(SECURITY_PROP_NAME)).getFirst());
    }

    @Test
    void testHeaderSidebarTransformV2permitAll() {
        ((SimplePropertyResolver) builder.getEnvironment().getSystemProperties()).setProperty("n2o.access.schema.id", "testHeaderV2permitAll");
        ReadCompileTerminalPipeline pipeline = compile("net/n2oapp/framework/access/metadata/schema/testHeaderV2permitAll.access.xml",
                "net/n2oapp/framework/access/metadata/transform/testApplicationAccessTransformer.application.xml");
        Application application = (Application) ((ReadCompileTerminalPipeline) pipeline.transform())
                .get(new ApplicationContext("testApplicationAccessTransformer"));
        assertThat(application.getHeader().getMenu().getItems().getFirst().getProperties() == null ||
                application.getHeader().getMenu().getItems().getFirst().getProperties().get(SECURITY_PROP_NAME) == null, is(true));
        assertThat(application.getSidebars().getFirst().getMenu().getItems().getFirst().getProperties() == null ||
                application.getSidebars().getFirst().getMenu().getItems().getFirst().getProperties().get(SECURITY_PROP_NAME) == null, is(true));
    }

    @Test
    void testHeaderSidebarTransformV2Anonym() {
        ((SimplePropertyResolver) builder.getEnvironment().getSystemProperties()).setProperty("n2o.access.schema.id", "testHeaderV2anonym");
        ReadCompileTerminalPipeline pipeline = compile("net/n2oapp/framework/access/metadata/schema/testHeaderV2anonym.access.xml",
                "net/n2oapp/framework/access/metadata/transform/testApplicationAccessTransformer.application.xml");
        Application application = (Application) ((ReadCompileTerminalPipeline) pipeline.transform())
                .get(new ApplicationContext("testApplicationAccessTransformer"));
        checkAnonym(((Security) application.getHeader().getMenu().getItems().getFirst().getProperties().get(SECURITY_PROP_NAME)).getFirst());
        checkAnonym(((Security) application.getSidebars().getFirst().getMenu().getItems().getFirst().getProperties().get(SECURITY_PROP_NAME)).getFirst());
    }

    private void checkAnonym(Map<String, SecurityObject> securityMap) {
        assertThat(securityMap.get("page").getPermitAll(), nullValue());
        assertThat(securityMap.get("page").getAnonymous(), is(true));
        assertThat(securityMap.get("page").getAuthenticated(), nullValue());
    }

    @Test
    void testHeaderSidebarTransformV2Auth() {
        ((SimplePropertyResolver) builder.getEnvironment().getSystemProperties()).setProperty("n2o.access.schema.id", "testHeaderV2auth");
        ReadCompileTerminalPipeline pipeline = compile("net/n2oapp/framework/access/metadata/schema/testHeaderV2auth.access.xml",
                "net/n2oapp/framework/access/metadata/transform/testApplicationAccessTransformer.application.xml");
        Application application = (Application) ((ReadCompileTerminalPipeline) pipeline.transform())
                .get(new ApplicationContext("testApplicationAccessTransformer"));
        checkAuth(((Security) application.getHeader().getMenu().getItems().getFirst().getProperties().get(SECURITY_PROP_NAME)).getFirst());
        checkAuth(((Security) application.getSidebars().getFirst().getMenu().getItems().getFirst().getProperties().get(SECURITY_PROP_NAME)).getFirst());
    }

    private void checkAuth(Map<String, SecurityObject> securityMap) {
        assertThat(securityMap.get("page").getPermitAll(), nullValue());
        assertThat(securityMap.get("page").getAnonymous(), nullValue());
        assertThat(securityMap.get("page").getAuthenticated(), is(true));
    }

    private void assertAccess(Map<String, SecurityObject> secMap) {
        assertThat(secMap.get("page").getUsernames().size(), is(1));
        assertThat(secMap.get("page").getUsernames().contains("user"), is(true));
        assertThat(secMap.get("page").getRoles().size(), is(2));
        assertThat(secMap.get("page").getRoles().contains("role"), is(true));
        assertThat(secMap.get("page").getRoles().contains("admin"), is(true));
        assertThat(secMap.get("page").getPermissions(), nullValue());
        assertThat(secMap.get("object").getPermissions(), nullValue());
        assertThat(secMap.get("object").getRoles(), nullValue());
        assertThat(secMap.get("object").getUsernames().size(), is(1));
        assertThat(secMap.get("object").getUsernames().contains("user"), is(true));
        assertThat(secMap.get("object").getAnonymous(), is(true));
    }
}
