package net.n2oapp.framework.config.metadata.merge;

import net.n2oapp.framework.api.metadata.application.N2oApplication;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.pack.N2oApplicationPack;
import net.n2oapp.framework.config.test.SourceMergerTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class N2oMenuMergerTest extends SourceMergerTestBase {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oApplicationPack());
    }

    @Test
    void testNavMerge() {
        N2oApplication application = merge("net/n2oapp/framework/config/metadata/merge/menu/navParent.application.xml",
                "net/n2oapp/framework/config/metadata/merge/menu/childNav.menu.xml")
                .get("navParent", N2oApplication.class);

        assertThat(application.getSidebars()[0].getMenu().getMenuItems().length, is(3));
        assertThat(application.getSidebars()[0].getMenu().getSrc(), is("testParentSrc"));
        assertThat(application.getSidebars()[0].getMenu().getMenuItems()[0].getId(), is("testChildDropdown"));
        assertThat(application.getSidebars()[0].getMenu().getMenuItems()[1].getName(), is("testChildItem"));
        assertThat(application.getSidebars()[0].getMenu().getMenuItems()[2].getName(), is("testParentItem"));
    }

    @Test
    void testExtraMenuMerge() {
        N2oApplication application = merge("net/n2oapp/framework/config/metadata/merge/menu/extraMenuParent.application.xml",
                "net/n2oapp/framework/config/metadata/merge/menu/childExtraMenu.menu.xml")
                .get("extraMenuParent", N2oApplication.class);

        assertThat(application.getSidebars()[0].getExtraMenu().getSrc(), is("childSrc"));
        assertThat(application.getSidebars()[0].getExtraMenu().getMenuItems().length, is(2));
        assertThat(application.getSidebars()[0].getExtraMenu().getSrc(), is("childSrc"));
        assertThat(application.getSidebars()[0].getExtraMenu().getMenuItems()[0].getName(), is("childMenuItem"));
        assertThat(application.getSidebars()[0].getExtraMenu().getMenuItems()[1].getName(), is("testParentItem"));
    }
}
