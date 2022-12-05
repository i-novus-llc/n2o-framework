package net.n2oapp.framework.config.metadata.merge;

import net.n2oapp.framework.api.metadata.application.Application;
import net.n2oapp.framework.api.metadata.application.Sidebar;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.datasource.StandardDatasourceIO;
import net.n2oapp.framework.config.metadata.compile.application.sidebar.SidebarValidator;
import net.n2oapp.framework.config.metadata.compile.context.ApplicationContext;
import net.n2oapp.framework.config.metadata.compile.datasource.StandardDatasourceCompiler;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class MergeOperationTest extends SourceCompileTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oApplicationPack());
        builder.ios(new StandardDatasourceIO());
        builder.compilers(new StandardDatasourceCompiler());
        builder.validators(new SidebarValidator());
    }

    @Test
    public void testMerge() {
        Application application = compile("net/n2oapp/framework/config/metadata/merge/operation/app.application.xml",
                "net/n2oapp/framework/config/metadata/merge/operation/childSidebar.sidebar.xml")
                .get(new ApplicationContext("app"));

        assertThat(application.getSidebars().size(), is(1));
        Sidebar sidebar = application.getSidebars().get(0);
        assertThat(sidebar.getDatasource(), is("ds"));
        assertThat(sidebar.getMenu().getItems().size(), is(1));
    }
}
