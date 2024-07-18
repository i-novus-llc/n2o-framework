package net.n2oapp.framework.access.metadata.compile;

import net.n2oapp.framework.access.metadata.Security;
import net.n2oapp.framework.access.metadata.SecurityObject;
import net.n2oapp.framework.api.metadata.application.Application;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.ApplicationContext;
import net.n2oapp.framework.config.metadata.compile.context.ObjectContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.metadata.pack.N2oObjectsPack;
import net.n2oapp.framework.config.metadata.pack.N2oQueriesPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static net.n2oapp.framework.access.metadata.Security.SECURITY_PROP_NAME;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Тестирование сборки атрибутов прав доступа
 */
class SecurityExtAttributeMapperTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack(), new N2oApplicationPack(),
                new N2oQueriesPack(), new N2oObjectsPack())
                .sources(new CompileInfo("net/n2oapp/framework/access/metadata/securityExtAttrMapperTest.page.xml"))
                .extensions(new SecurityExtensionAttributeMapper());
    }

    @Test
    void inlineMenu() {
        Application application = compile("net/n2oapp/framework/access/metadata/securityExtAttrMapperTest.application.xml")
                .get(new ApplicationContext("securityExtAttrMapperTest"));
        SecurityObject securityObject = new SecurityObject();
        securityObject.setAnonymous(false);
        securityObject.setAuthenticated(false);
        securityObject.setUsernames(new HashSet<>(Arrays.asList("user")));
        securityObject.setPermissions(new HashSet<>(Arrays.asList("admin", "user")));
        securityObject.setRoles(new HashSet<>(Arrays.asList("admin")));
        Security security = new Security();
        Map<String, SecurityObject> securityObjectMap = new HashMap<>();
        securityObjectMap.put("custom", securityObject);
        security.add(securityObjectMap);
        assertThat(application.getHeader().getMenu().getItems().get(0).getProperties().get(SECURITY_PROP_NAME), is(security));
        assertThat(((Security) application.getHeader().getMenu().getItems().get(0).getProperties().get(SECURITY_PROP_NAME))
                .get(0).get("custom"), is(securityObject));
    }

    @Test
    void inlineToolbarAndRegion() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/access/metadata/securityExtAttrMapperTest.page.xml")
                .get(new PageContext("securityExtAttrMapperTest"));
        SecurityObject securityObject = new SecurityObject();
        securityObject.setAnonymous(false);
        securityObject.setAuthenticated(false);
        securityObject.setUsernames(new HashSet<>(Arrays.asList("user")));
        securityObject.setPermissions(new HashSet<>(Arrays.asList("admin", "user")));
        securityObject.setRoles(new HashSet<>(Arrays.asList("admin")));
        Security security = new Security();
        Map<String, SecurityObject> securityObjectMap = new HashMap<>();
        securityObjectMap.put("custom", securityObject);
        security.add(securityObjectMap);
        assertThat(page.getRegions().get("single").get(0).getProperties().get(SECURITY_PROP_NAME), is(security));
        assertThat(((Security) page.getRegions().get("single").get(0).getProperties().get(SECURITY_PROP_NAME))
                .get(0).get("custom"), is(securityObject));
        assertThat(((Widget) page.getRegions().get("single").get(0).getContent().get(0))
                .getProperties().get(SECURITY_PROP_NAME), is(security));
        assertThat(((Security) ((Widget) page.getRegions().get("single").get(0).getContent().get(0))
                .getProperties().get(SECURITY_PROP_NAME)).get(0).get("custom"), is(securityObject));
        assertThat(page.getToolbar().get("bottomRight").get(0).getButtons().get(0)
                .getProperties().get(SECURITY_PROP_NAME), is(security));
        assertThat(((Security) page.getRegions().get("single").get(0)
                .getProperties().get(SECURITY_PROP_NAME)).get(0).get("custom"), is(securityObject));
    }

    @Test
    void inlineObjectActions() {
        CompiledObject object = compile("net/n2oapp/framework/access/metadata/securityExtAttrMapperTest.object.xml")
                .get(new ObjectContext("securityExtAttrMapperTest"));
        CompiledObject.Operation operation = object.getOperations().get("create");
        SecurityObject securityObject = new SecurityObject();
        securityObject.setPermitAll(true);
        securityObject.setAuthenticated(true);
        securityObject.setDenied(false);
        securityObject.setAnonymous(false);
        securityObject.setRoles(new HashSet<>(Arrays.asList("role1", "role2")));
        securityObject.setUsernames(new HashSet<>(Arrays.asList("user1")));
        securityObject.setPermissions(new HashSet<>(Arrays.asList("p1", "p2", "p3")));
        Security security = new Security();
        Map<String, SecurityObject> securityObjectMap = new HashMap<>();
        securityObjectMap.put("custom", securityObject);
        security.add(securityObjectMap);
        assertThat(operation.getProperties().get(SECURITY_PROP_NAME), is(security));
        assertThat(((Security) operation.getProperties().get(SECURITY_PROP_NAME))
                .get(0).get("custom"), is(securityObject));
    }

    @Test
    void inlineQuery() {
        CompiledQuery query = compile("net/n2oapp/framework/access/metadata/securityExtAttrMapperTest.query.xml")
                .get(new QueryContext("securityExtAttrMapperTest"));
        SecurityObject securityObject = new SecurityObject();
        securityObject.setPermitAll(true);
        securityObject.setAuthenticated(true);
        securityObject.setDenied(false);
        securityObject.setAnonymous(false);
        securityObject.setRoles(new HashSet<>(Arrays.asList("role1", "role2")));
        securityObject.setUsernames(new HashSet<>(Arrays.asList("user1")));
        securityObject.setPermissions(new HashSet<>(Arrays.asList("p1", "p2", "p3")));
        Security security = new Security();
        Map<String, SecurityObject> securityObjectMap = new HashMap<>();
        securityObjectMap.put("custom", securityObject);
        security.add(securityObjectMap);
        assertThat(query.getProperties().get(SECURITY_PROP_NAME), is(security));
        assertThat(((Security) query.getProperties().get(SECURITY_PROP_NAME)).get(0).get("custom"), is(securityObject));
    }
}
