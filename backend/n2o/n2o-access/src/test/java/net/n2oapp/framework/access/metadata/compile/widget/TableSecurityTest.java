package net.n2oapp.framework.access.metadata.compile.widget;

import net.n2oapp.framework.access.metadata.Security;
import net.n2oapp.framework.access.metadata.compile.SecurityExtensionAttributeMapper;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.metadata.pack.N2oObjectsPack;
import net.n2oapp.framework.config.metadata.pack.N2oQueriesPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;

public class TableSecurityTest extends SourceCompileTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack()).extensions(new SecurityExtensionAttributeMapper());
    }

    @Test
    public void tableColumnSecurity() {
        Table table = (Table) compile("net/n2oapp/framework/access/metadata/tableColumnSecurity.widget.xml")
                .get(new WidgetContext("tableColumnSecurity"));

        assertThat(table.getComponent().getHeaders().size(), is(3));

        Security.SecurityObject securityObject = ((Security) table.getComponent().getHeaders().get(0).getProperties().get("security"))
                .getSecurityMap().get("custom");
        assertThat(securityObject.getRoles().size(), is(1));
        assertTrue(securityObject.getRoles().contains("user"));
        assertThat(securityObject.getAnonymous(), is(false));
        assertThat(securityObject.getPermissions().size(), is(1));
        assertTrue(securityObject.getPermissions().contains("userName"));

        securityObject = ((Security) table.getComponent().getHeaders().get(1).getProperties().get("security"))
                .getSecurityMap().get("custom");
        assertThat(securityObject.getUsernames().size(), is(1));
        assertTrue(securityObject.getUsernames().contains("userName"));
        assertThat(securityObject.getAuthenticated(), is(true));

        securityObject = ((Security) table.getComponent().getHeaders().get(2).getProperties().get("security"))
                .getSecurityMap().get("custom");
        assertThat(securityObject.getDenied(), is(false));
        assertThat(securityObject.getPermitAll(), is(true));
    }
}
