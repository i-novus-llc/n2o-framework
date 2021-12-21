package net.n2oapp.framework.config.metadata.compile.widget.tree;

import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.widget.Tree;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TreeWidgetCompileTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllDataPack(), new N2oAllPagesPack())
                .sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/stub/utBlank.query.xml"),
                        new CompileInfo("net/n2oapp/framework/config/metadata/compile/stub/utBlank.object.xml"));
    }

    @Test
    public void testTree() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/widgets/testTreeCompile.page.xml")
                .get(new PageContext("testTreeCompile"));
        Tree tree = (Tree) page.getWidget();
        assertThat(tree.getId(), is("testTreeCompile_main"));
        assertThat(tree.getParentFieldId(), is("test"));
        assertThat(tree.getChildrenFieldId(), is("test"));
        assertThat(tree.getValueFieldId(), is("test"));
        assertThat(tree.getLabelFieldId(), is("test"));
        assertThat(tree.getIconFieldId(), is("test"));
        assertThat(tree.getImageFieldId(), is("test"));
        assertThat(tree.getBadgeFieldId(), is("test"));
        assertThat(tree.getBadgeColorFieldId(), is("test"));
        assertThat(tree.getAjax(), is(true));
        assertThat(tree.getHasCheckboxes(), is(true));
        assertThat(tree.getMultiselect(), is(false));
        assertThat(page.getDatasources().get(tree.getDatasource()).getProvider().getUrl(), is("n2o/data/testTreeCompile/main"));
    }
}
