package net.n2oapp.framework.access.metadata.transform;

import net.n2oapp.framework.access.integration.metadata.transform.ApplicationAccessTransformer;
import net.n2oapp.framework.access.metadata.Security;
import net.n2oapp.framework.access.metadata.pack.AccessSchemaPack;
import net.n2oapp.framework.api.metadata.application.Application;
import net.n2oapp.framework.api.metadata.application.Sidebar;
import net.n2oapp.framework.api.metadata.header.Header;
import net.n2oapp.framework.api.metadata.header.HeaderItem;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileTerminalPipeline;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.ApplicationContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SimplePropertyResolver;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static net.n2oapp.framework.access.metadata.Security.SECURITY_PROP_NAME;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

/**
 * Тестирование трансформера прав доступа
 */
public class ApplicationAccessTransformerTest extends SourceCompileTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
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
    public void testHeaderTransform() {
        ((SimplePropertyResolver) builder.getEnvironment().getSystemProperties()).setProperty("n2o.access.schema.id", "testHeader");

        ReadCompileTerminalPipeline pipeline = compile("net/n2oapp/framework/access/metadata/schema/testHeader.access.xml",
                "net/n2oapp/framework/access/metadata/transform/testApplicationAccessTransformer.application.xml");

        Application application = (Application) ((ReadCompileTerminalPipeline) pipeline.transform())
                .get(new ApplicationContext("testApplicationAccessTransformer"));

        checkMenuItem(application.getHeader().getMenu().getItems().get(0));
        checkMenuItem(application.getHeader().getExtraMenu().getItems().get(0));
        checkMenuItem(application.getSidebar().getMenu().getItems().get(0));
        checkMenuItem(application.getSidebar().getExtraMenu().getItems().get(0));

        assertThat(((Security) application.getHeader().getMenu().getItems().get(2).getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("url").getPermissions().size(), is(1));
        assertThat(((Security) application.getSidebar().getMenu().getItems().get(2).getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("url").getPermissions().size(), is(1));
        checkAnchor(application.getHeader().getExtraMenu().getItems().get(2));
        checkAnchor(application.getSidebar().getExtraMenu().getItems().get(2));
    }

    private void checkAnchor(HeaderItem headerItem) {
        assertThat(((Security) headerItem.getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("url").getPermissions(), nullValue());
        assertThat(((Security) headerItem.getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("url").getRoles(), nullValue());
        assertThat(((Security) headerItem.getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("url").getUsernames().contains("user"), is(true));
    }

    private void checkMenuItem(HeaderItem item) {
        assertThat(((Security) item.getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("page").getUsernames().size(), is(1));
        assertThat(((Security) item.getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("page").getUsernames().contains("user"), is(true));
        assertThat(((Security) item.getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("page").getRoles().size(), is(2));
        assertThat(((Security) item.getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("page").getRoles().contains("role"), is(true));
        assertThat(((Security) item.getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("page").getRoles().contains("admin"), is(true));
        assertThat(((Security) item.getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("page").getPermissions(), nullValue());

        assertThat(((Security) item.getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("object").getPermissions(), nullValue());
        assertThat(((Security) item.getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("object").getRoles(), nullValue());
        assertThat(((Security) item.getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("object").getUsernames().size(), is(1));
        assertThat(((Security) item.getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("object").getUsernames().contains("user"), is(true));
    }

    @Test
    public void testHeaderTransformV2() {
        ((SimplePropertyResolver) builder.getEnvironment().getSystemProperties()).setProperty("n2o.access.schema.id", "testHeaderV2");

        ReadCompileTerminalPipeline pipeline = compile("net/n2oapp/framework/access/metadata/schema/testHeaderV2.access.xml",
                "net/n2oapp/framework/access/metadata/transform/testApplicationAccessTransformer.application.xml");

        Application application = (Application) ((ReadCompileTerminalPipeline) pipeline.transform())
                .get(new ApplicationContext("testApplicationAccessTransformer"));
        Header header = application.getHeader();
        assertAccess(((Security) header.getMenu().getItems().get(0).getProperties().get(SECURITY_PROP_NAME)).getSecurityMap());
        assertAccess(((Security) header.getMenu().getItems().get(1).getSubItems().get(0).getProperties().get(SECURITY_PROP_NAME)).getSecurityMap());
        assertThat(((Security) header.getMenu().getItems().get(2).getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("url").getRoles().size(), is(1));
        assertAccess(((Security) header.getExtraMenu().getItems().get(0).getProperties().get(SECURITY_PROP_NAME)).getSecurityMap());
        assertAccess(((Security) header.getExtraMenu().getItems().get(1).getSubItems().get(0).getProperties().get(SECURITY_PROP_NAME)).getSecurityMap());
        Sidebar sidebar = application.getSidebar();
        assertAccess(((Security) sidebar.getMenu().getItems().get(0).getProperties().get(SECURITY_PROP_NAME)).getSecurityMap());
        assertAccess(((Security) sidebar.getMenu().getItems().get(1).getSubItems().get(0).getProperties().get(SECURITY_PROP_NAME)).getSecurityMap());
        assertThat(((Security) sidebar.getMenu().getItems().get(2).getProperties().get(SECURITY_PROP_NAME)).getSecurityMap().get("url").getRoles().size(), is(1));
        assertAccess(((Security) sidebar.getExtraMenu().getItems().get(0).getProperties().get(SECURITY_PROP_NAME)).getSecurityMap());
        assertAccess(((Security) sidebar.getExtraMenu().getItems().get(1).getSubItems().get(0).getProperties().get(SECURITY_PROP_NAME)).getSecurityMap());
    }

    @Test
    public void testHeaderTransformV2permitAll() {
        ((SimplePropertyResolver) builder.getEnvironment().getSystemProperties()).setProperty("n2o.access.schema.id", "testHeaderV2permitAll");
        ReadCompileTerminalPipeline pipeline = compile("net/n2oapp/framework/access/metadata/schema/testHeaderV2permitAll.access.xml",
                "net/n2oapp/framework/access/metadata/transform/testApplicationAccessTransformer.application.xml");
        Application application = (Application) ((ReadCompileTerminalPipeline) pipeline.transform())
                .get(new ApplicationContext("testApplicationAccessTransformer"));
        checkPermitAll(((Security) application.getHeader().getMenu().getItems().get(0).getProperties().get(SECURITY_PROP_NAME)).getSecurityMap());
        checkPermitAll(((Security) application.getSidebar().getMenu().getItems().get(0).getProperties().get(SECURITY_PROP_NAME)).getSecurityMap());
    }

    private void checkPermitAll(Map<String, Security.SecurityObject> securityMap) {
        assertThat(securityMap.get("page").getPermitAll(), is(true));
        assertThat(securityMap.get("page").getAnonymous(), nullValue());
        assertThat(securityMap.get("page").getAuthenticated(), nullValue());
    }

    @Test
    public void testHeaderTransformV2Anonym() {
        ((SimplePropertyResolver) builder.getEnvironment().getSystemProperties()).setProperty("n2o.access.schema.id", "testHeaderV2anonym");
        ReadCompileTerminalPipeline pipeline = compile("net/n2oapp/framework/access/metadata/schema/testHeaderV2anonym.access.xml",
                "net/n2oapp/framework/access/metadata/transform/testApplicationAccessTransformer.application.xml");
        Application application = (Application) ((ReadCompileTerminalPipeline) pipeline.transform())
                .get(new ApplicationContext("testApplicationAccessTransformer"));
        checkAnonym(((Security) application.getHeader().getMenu().getItems().get(0).getProperties().get(SECURITY_PROP_NAME)).getSecurityMap());
        checkAnonym(((Security) application.getSidebar().getMenu().getItems().get(0).getProperties().get(SECURITY_PROP_NAME)).getSecurityMap());
    }

    private void checkAnonym(Map<String, Security.SecurityObject> securityMap) {
        assertThat(securityMap.get("page").getPermitAll(), nullValue());
        assertThat(securityMap.get("page").getAnonymous(), is(true));
        assertThat(securityMap.get("page").getAuthenticated(), nullValue());
    }

    @Test
    public void testHeaderTransformV2Auth() {
        ((SimplePropertyResolver) builder.getEnvironment().getSystemProperties()).setProperty("n2o.access.schema.id", "testHeaderV2auth");
        ReadCompileTerminalPipeline pipeline = compile("net/n2oapp/framework/access/metadata/schema/testHeaderV2auth.access.xml",
                "net/n2oapp/framework/access/metadata/transform/testApplicationAccessTransformer.application.xml");
        Application application = (Application) ((ReadCompileTerminalPipeline) pipeline.transform())
                .get(new ApplicationContext("testApplicationAccessTransformer"));
        checkAuth(((Security) application.getHeader().getMenu().getItems().get(0).getProperties().get(SECURITY_PROP_NAME)).getSecurityMap());
        checkAuth(((Security) application.getSidebar().getMenu().getItems().get(0).getProperties().get(SECURITY_PROP_NAME)).getSecurityMap());
    }

    private void checkAuth(Map<String, Security.SecurityObject> securityMap) {
        assertThat(securityMap.get("page").getPermitAll(), nullValue());
        assertThat(securityMap.get("page").getAnonymous(), nullValue());
        assertThat(securityMap.get("page").getAuthenticated(), is(true));
    }

    private void assertAccess(Map<String, Security.SecurityObject> secMap) {
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