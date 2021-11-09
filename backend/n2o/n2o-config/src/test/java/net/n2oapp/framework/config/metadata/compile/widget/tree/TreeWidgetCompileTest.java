package net.n2oapp.framework.config.metadata.compile.widget.tree;

import net.n2oapp.framework.api.metadata.meta.widget.Tree;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.widget.v4.TreeElementIOv4;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.metadata.compile.widget.TreeCompiler;
import net.n2oapp.framework.config.metadata.pack.*;
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
        builder.packs(new N2oAllDataPack(), new N2oFieldSetsPack(), new N2oControlsPack(), new N2oCellsPack(), new N2oActionsPack())
                .ios(new TreeElementIOv4())
                .compilers(new TreeCompiler())
                .sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/stub/utBlank.query.xml"),
                        new CompileInfo("net/n2oapp/framework/config/metadata/compile/stub/utBlank.object.xml"));
    }

    @Test
    public void testTree() {
        Tree tree = (Tree) compile("net/n2oapp/framework/config/metadata/compile/widgets/testTreeCompile.widget.xml")
                .get(new WidgetContext("testTreeCompile"));
        assertThat(tree.getId(), is("$testTreeCompile"));
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
        assertThat(tree.getDataProvider().getUrl(), is("n2o/data/testTreeCompile"));
    }
}
