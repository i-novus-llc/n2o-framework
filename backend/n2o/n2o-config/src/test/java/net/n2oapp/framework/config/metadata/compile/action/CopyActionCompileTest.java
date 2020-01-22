package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.meta.action.copy.CopyAction;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.action.CopyActionElementIOV1;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Проверка копиляции действия copy
 */
public class CopyActionCompileTest extends SourceCompileTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oControlsPack(),
                new N2oAllDataPack(), new N2oFieldSetsPack());
        builder.ios(new CopyActionElementIOV1());
        builder.compilers(new CopyActionCompiler());
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testActionContext.query.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testActionContext.object.xml"));
    }

    @Test
    public void simple() {
        Table table = (Table) compile("net/n2oapp/framework/config/metadata/compile/action/testCopyAction.widget.xml")
                .get(new WidgetContext("testCopyAction", "/w"));

        CopyAction testAction = (CopyAction) table.getActions().get("test");
        assertThat(testAction.getType(), is("n2o/models/COPY"));
        assertThat(testAction.getPayload().getSource().getKey(), is("w"));
        assertThat(testAction.getPayload().getSource().getPrefix(), is("edit"));
        assertThat(testAction.getPayload().getTarget().getKey(), is("w"));
        assertThat(testAction.getPayload().getTarget().getPrefix(), is("filter"));

        CopyAction menuItem0action = (CopyAction) table.getActions().get("menuItem0");
        assertThat(menuItem0action.getType(), is("n2o/models/COPY"));
        assertThat(menuItem0action.getPayload().getSource().getKey(), is("w"));
        assertThat(menuItem0action.getPayload().getSource().getPrefix(), is("edit"));
        assertThat(menuItem0action.getPayload().getTarget().getKey(), is("w"));
        assertThat(menuItem0action.getPayload().getTarget().getPrefix(), is("resolve"));
    }
}