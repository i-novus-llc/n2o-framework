package net.n2oapp.framework.access.metadata.transform;

import net.n2oapp.framework.access.metadata.Security;
import net.n2oapp.framework.access.metadata.SecurityObject;
import net.n2oapp.framework.access.metadata.pack.AccessSchemaPack;
import net.n2oapp.framework.access.metadata.pack.AccessTransformersPack;
import net.n2oapp.framework.api.metadata.meta.control.Field;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
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

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

class ButtonFieldAccessTransformerTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.sources(new CompileInfo("net/n2oapp/framework/access/metadata/transform/testQuery.query.xml"),
                new CompileInfo("net/n2oapp/framework/access/metadata/transform/testObjectAccessTransformer.object.xml"));
        builder.packs(new AccessSchemaPack(), new N2oAllPagesPack(), new N2oAllDataPack(), new AccessTransformersPack());
    }

    @Test
    void buttonFieldAccessTransformerTest() {
        ((SimplePropertyResolver) builder.getEnvironment().getSystemProperties()).setProperty("n2o.access.schema.id", "testRegion");

        ReadCompileTerminalPipeline pipeline = compile("net/n2oapp/framework/access/metadata/schema/testRegion.access.xml",
                "net/n2oapp/framework/access/metadata/transform/testButtonFieldAccessTransformer.page.xml");


        StandardPage page = (StandardPage) ((ReadCompileTerminalPipeline) pipeline.transform())
                .get(new PageContext("testButtonFieldAccessTransformer"));

        List<Field> fields = ((Form) page.getRegions().get("single").get(0).getContent().get(0)).getComponent().getFieldsets().get(0)
                .getRows().stream().map(r -> r.getCols().get(0).getFields().get(0)).toList();

        Field field = fields.get(0);
        assertThat(field.getId(), is("btn01"));
        SecurityObject secObj = getSecurityObject(field, "object");
        assertSecurityObject(secObj,
                null, null, null, null,
                1, "role",
                1, "permission",
                1, "user");

        field = fields.get(1);
        assertThat(field.getId(), is("btn02"));
        secObj = getSecurityObject(field, "custom");
        assertSecurityObject(secObj,
                null, null, null, null,
                1, "role1",
                0, null,
                0, null);

        field = fields.get(2);
        assertThat(field.getId(), is("btn10"));
        secObj = getSecurityObject(field, "custom");
        assertSecurityObject(secObj,
                null, null, null, true,
                0, null,
                0, null,
                0, null);

        field = fields.get(3);
        assertThat(field.getId(), is("btn11"));
        assertThat(field.getProperties(), nullValue());

        field = fields.get(4);
        assertThat(field.getId(), is("btn12"));
        secObj = getSecurityObject(field, "object");
        assertSecurityObject(secObj,
                null, null, null, null,
                1, "role",
                1, "permission",
                1, "user");
    }

    private void assertSecurityObject(SecurityObject secObj, Boolean denied, Boolean permitAll, Boolean anonymous, Boolean authenticated,
                                      int roleSize, String role, int permissionsSize, String permissions, int usernamesSize, String usernames) {
        assertThat(secObj.getDenied(), denied != null ? is(denied) : nullValue());
        assertThat(secObj.getPermitAll(), permitAll != null ? is(permitAll) : nullValue());
        assertThat(secObj.getAnonymous(), anonymous != null ? is(anonymous) : nullValue());
        assertThat(secObj.getAuthenticated(), authenticated != null ? is(authenticated) : nullValue());

        if (role != null) {
            assertThat(secObj.getRoles().iterator().next(), is(role));
            assertThat(secObj.getRoles().size(), is(roleSize));
        }
        if (permissions != null) {
            assertThat(secObj.getPermissions().iterator().next(), is(permissions));
            assertThat(secObj.getPermissions().size(), is(permissionsSize));
        }
        if (usernames != null) {
            assertThat(secObj.getUsernames().iterator().next(), is(usernames));
            assertThat(secObj.getUsernames().size(), is(usernamesSize));
        }
    }

    private SecurityObject getSecurityObject(Field field, String key) {
        if (field.getProperties() != null) {
            Object sec = field.getProperties().get("security");
            if (sec != null) {
                Security secMap = (Security) sec;
                if (secMap.get(0).get(key) != null) {
                    return secMap.get(0).get(key);
                }
            }
        }
        return null;
    }
}
