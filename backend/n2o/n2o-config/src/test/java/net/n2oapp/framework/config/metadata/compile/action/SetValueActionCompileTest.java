package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.event.action.MergeMode;
import net.n2oapp.framework.api.metadata.meta.action.set_value.SetValueAction;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.action.SetValueElementIOV1;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Проверка копиляции действия set-value
 */
public class SetValueActionCompileTest extends SourceCompileTestBase {

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
        builder.ios(new SetValueElementIOV1());
        builder.compilers(new SetValueActionCompiler());
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testActionContext.query.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testActionContext.object.xml"));
    }

    @Test
    public void simple() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/action/testSetValueAction.page.xml")
                .get(new PageContext("testSetValueAction", "/w"));

        Table table = (Table) page.getRegions().get("single").get(0).getContent().get(0);
        SetValueAction testAction = (SetValueAction) table.getActions().get("test");
        assertThat(testAction.getType(), is("n2o/models/COPY"));
        assertThat(testAction.getPayload().getSource().getKey(), is("w_list"));
        assertThat(testAction.getPayload().getSource().getPrefix(), is("edit"));
        assertThat(testAction.getPayload().getTarget().getKey(), is("w_form2"));
        assertThat(testAction.getPayload().getTarget().getPrefix(), is("filter"));
        assertThat(testAction.getPayload().getTarget().getField(), is("filedId"));
        assertThat(testAction.getPayload().getMode(), is(MergeMode.add));
        assertThat(testAction.getPayload().getSourceMapper(), is("`return false;`"));

        SetValueAction menuItem0action = (SetValueAction) table.getActions().get("menuItem0");
        assertThat(menuItem0action.getType(), is("n2o/models/COPY"));
        assertThat(menuItem0action.getPayload().getSource().getKey(), is("w_ds1"));
        assertThat(menuItem0action.getPayload().getSource().getPrefix(), is("resolve"));
        assertThat(menuItem0action.getPayload().getTarget().getKey(), is("w_ds1"));
        assertThat(menuItem0action.getPayload().getTarget().getPrefix(), is("resolve"));
        assertThat(menuItem0action.getPayload().getMode(), is(MergeMode.replace));
    }
}