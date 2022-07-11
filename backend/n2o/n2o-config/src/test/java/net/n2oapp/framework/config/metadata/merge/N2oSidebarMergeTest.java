package net.n2oapp.framework.config.metadata.merge;

import net.n2oapp.framework.api.metadata.application.N2oSidebar;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.test.SourceMergerTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class N2oSidebarMergeTest extends SourceMergerTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oApplicationPack());
    }

    @Test
    public void mergeSidebar() {
        N2oSidebar sidebar = merge("net/n2oapp/framework/config/metadata/merge/parentSidebar.sidebar.xml",
                "net/n2oapp/framework/config/metadata/merge/childSidebar.sidebar.xml")
                .get("parentSidebar", N2oSidebar.class);
        assertThat(sidebar.getTitle(), is("N2o"));
        assertThat(sidebar.getSubtitle(), is("Simple subtitle"));
        assertThat(sidebar.getPath(), is("/hello"));
        assertThat(sidebar.getHomePageUrl(), is("/home"));
        assertThat(sidebar.getMenu().getMenuItems()[0].getName(), is("firstName"));
        assertThat(sidebar.getMenu().getMenuItems()[1].getName(), is("lastName"));
        assertThat(sidebar.getLogoClass(), is("top-logo"));
    }

}
