package net.n2oapp.framework.config.metadata.compile.widget.tree;

import net.n2oapp.framework.api.metadata.datasource.StandardDatasource;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.Tree;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TreeWidgetCompileTest extends SourceCompileTestBase {
    @Override
    @BeforeEach
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
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/widgets/testTreeCompile.page.xml")
                .get(new PageContext("testTreeCompile"));
        Tree tree = (Tree) page.getRegions().get("single").get(0).getContent().get(0);
        assertThat(tree.getId(), is("testTreeCompile_w1"));
        assertThat(tree.getParentFieldId(), is("test"));
        assertThat(tree.getChildrenFieldId(), is("test"));
        assertThat(tree.getValueFieldId(), is("test"));
        assertThat(tree.getLabelFieldId(), is("test"));
        assertThat(tree.getIconFieldId(), is("test"));
        assertThat(tree.getImageFieldId(), is("test"));
        assertThat(tree.getAjax(), is(true));
        assertThat(tree.getHasCheckboxes(), is(true));
        assertThat(tree.getMultiselect(), is(false));
        assertThat(((StandardDatasource) page.getDatasources().get("testTreeCompile_w1")).getProvider().getUrl(), is("n2o/data/testTreeCompile/w1"));
        assertThat(((StandardDatasource) page.getDatasources().get("testTreeCompile_w1")).getPaging().getSize(), is(200));

        //default
        tree = (Tree) page.getRegions().get("single").get(0).getContent().get(1);
        assertThat(tree.getAjax(), is(false));
        assertThat(tree.getMultiselect(), is(false));
        assertThat(tree.getHasCheckboxes(), is(false));
    }
}
