package net.n2oapp.framework.access.metadata.compile;

import net.n2oapp.framework.access.metadata.accesspoint.model.N2oObjectAccessPoint;
import net.n2oapp.framework.access.metadata.accesspoint.model.N2oPageAccessPoint;
import net.n2oapp.framework.access.metadata.accesspoint.model.N2oUrlAccessPoint;
import net.n2oapp.framework.access.metadata.pack.AccessSchemaPack;
import net.n2oapp.framework.access.metadata.schema.AccessContext;
import net.n2oapp.framework.access.metadata.schema.simple.SimpleCompiledAccessSchema;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

public class SimpleAccessSchemaCompileTest  extends SourceCompileTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new AccessSchemaPack());
    }


    @Test
    public void accessSchema() {
        SimpleCompiledAccessSchema accessSchema = (SimpleCompiledAccessSchema) compile("net/n2oapp/framework/access/metadata/simple.access.xml")
                .get(new AccessContext("simple"));
        assertThat(accessSchema.getId(), is("simple"));
        assertThat(accessSchema.getAuthenticatedPoints().size(), is(1));
        assertThat(accessSchema.getAuthenticatedPoints().get(0), instanceOf(N2oUrlAccessPoint.class));
        assertThat(accessSchema.getN2oUserAccesses().size(), is(1));
        assertThat(accessSchema.getN2oUserAccesses().get(0).getAccessPoints()[0], instanceOf(N2oUrlAccessPoint.class));
        assertThat(accessSchema.getPermitAllPoints().size(), is(2));
        assertThat(accessSchema.getPermitAllPoints().get(0), instanceOf(N2oObjectAccessPoint.class));
        assertThat(accessSchema.getPermitAllPoints().get(1), instanceOf(N2oObjectAccessPoint.class));
    }

    @Test
    public void accessSchemaV2() {
        SimpleCompiledAccessSchema accessSchema = (SimpleCompiledAccessSchema) compile("net/n2oapp/framework/access/metadata/simpleV2.access.xml")
                .get(new AccessContext("simpleV2"));

        assertThat(accessSchema.getId(), is("simpleV2"));

        assertThat(accessSchema.getN2oUserAccesses().size(), is(1));
        assertThat(accessSchema.getN2oUserAccesses().get(0).getAccessPoints()[0], instanceOf(N2oUrlAccessPoint.class));

        assertThat(accessSchema.getAuthenticatedPoints().size(), is(1));
        assertThat(accessSchema.getAuthenticatedPoints().get(0), instanceOf(N2oUrlAccessPoint.class));

        assertThat(accessSchema.getAnonymousPoints().size(), is(2));
        assertThat(accessSchema.getAnonymousPoints().get(0), instanceOf(N2oObjectAccessPoint.class));
        assertThat(accessSchema.getAnonymousPoints().get(1), instanceOf(N2oObjectAccessPoint.class));

        assertThat(accessSchema.getPermitAllPoints().size(), is(2));
        assertThat(accessSchema.getPermitAllPoints().get(0), instanceOf(N2oObjectAccessPoint.class));
        assertThat(accessSchema.getPermitAllPoints().get(1), instanceOf(N2oObjectAccessPoint.class));

        assertThat(accessSchema.getN2oRoles().size(), is(1));
        assertThat(accessSchema.getN2oRoles().get(0).getAccessPoints()[0], instanceOf(N2oObjectAccessPoint.class));
        assertThat(accessSchema.getN2oRoles().get(0).getAccessPoints()[1], instanceOf(N2oObjectAccessPoint.class));
        assertThat(accessSchema.getN2oRoles().get(0).getAccessPoints()[2], instanceOf(N2oPageAccessPoint.class));

        assertThat(accessSchema.getN2oPermissions().size(), is(1));
        assertThat(accessSchema.getN2oPermissions().get(0).getAccessPoints()[0], instanceOf(N2oObjectAccessPoint.class));
        assertThat(accessSchema.getN2oPermissions().get(0).getAccessPoints()[1], instanceOf(N2oObjectAccessPoint.class));
    }
}