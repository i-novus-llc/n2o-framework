package net.n2oapp.framework.access.metadata.compile.widget;

import net.n2oapp.framework.access.metadata.Security;
import net.n2oapp.framework.access.metadata.SecurityObject;
import net.n2oapp.framework.access.metadata.compile.SecurityExtensionAttributeMapper;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TableSecurityTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack()).extensions(new SecurityExtensionAttributeMapper());
    }

    @Test
    void tableColumnSecurity() {
        Table table = (Table) compile("net/n2oapp/framework/access/metadata/tableColumnSecurity.widget.xml")
                .get(new WidgetContext("tableColumnSecurity"));

        assertThat(table.getComponent().getHeader().getCells().size(), is(3));

        SecurityObject securityObject = ((Security) table.getComponent().getHeader().getCells().get(0).getProperties().get("security"))
                .get(0).get("custom");
        assertThat(securityObject.getRoles().size(), is(1));
        assertTrue(securityObject.getRoles().contains("user"));
        assertThat(securityObject.getAnonymous(), is(false));
        assertThat(securityObject.getPermissions().size(), is(1));
        assertTrue(securityObject.getPermissions().contains("userName"));

        securityObject = ((Security) table.getComponent().getHeader().getCells().get(1).getProperties().get("security"))
                .get(0).get("custom");
        assertThat(securityObject.getUsernames().size(), is(1));
        assertTrue(securityObject.getUsernames().contains("userName"));
        assertThat(securityObject.getAuthenticated(), is(true));

        securityObject = ((Security) table.getComponent().getHeader().getCells().get(2).getProperties().get("security"))
                .get(0).get("custom");
        assertThat(securityObject.getDenied(), is(false));
        assertThat(securityObject.getPermitAll(), is(true));
    }
}
