package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.action.MergeMode;
import net.n2oapp.framework.api.metadata.meta.action.set_value.SetValueAction;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.action.v2.SetValueElementIOV2;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Проверка копиляции действия set-value
 */
class SetValueActionCompileTest extends SourceCompileTestBase {

    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oPagesPack(), new N2oRegionsPack(), new N2oWidgetsPack(), new N2oControlsPack(),
                new N2oAllDataPack(), new N2oFieldSetsPack());
        builder.ios(new SetValueElementIOV2());
        builder.compilers(new SetValueActionCompiler());
        builder.sources(
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testActionContext.query.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testActionContext.object.xml"));
    }

    @Test
    void simple() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/action/testSetValueAction.page.xml")
                .get(new PageContext("testSetValueAction", "/w"));

        Form table = (Form) page.getRegions().get("single").get(0).getContent().get(0);
        SetValueAction testAction = (SetValueAction) table.getToolbar().getButton("test").getAction();
        assertThat(testAction.getType(), is("n2o/models/COPY"));
        assertThat(testAction.getValidate(), is(true));
        assertThat(testAction.getPayload().getSource().getKey(), is("w_list"));
        assertThat(testAction.getPayload().getSource().getPrefix(), is("edit"));
        assertThat(testAction.getPayload().getTarget().getKey(), is("w_form2"));
        assertThat(testAction.getPayload().getTarget().getPrefix(), is("filter"));
        assertThat(testAction.getPayload().getTarget().getField(), is("filedId"));
        assertThat(testAction.getPayload().getMode(), is(MergeMode.add));
        assertThat(testAction.getPayload().getSourceMapper(), is("(function(){return false;}).call(this)"));

        SetValueAction menuItem0action = (SetValueAction) table.getToolbar().getButton("test2").getAction();
        assertThat(menuItem0action.getType(), is("n2o/models/COPY"));
        assertThat(menuItem0action.getValidate(), is(false));
        assertThat(menuItem0action.getPayload().getSource().getKey(), is("w_table"));
        assertThat(menuItem0action.getPayload().getSource().getPrefix(), is("resolve"));
        assertThat(menuItem0action.getPayload().getTarget().getKey(), is("w_table"));
        assertThat(menuItem0action.getPayload().getTarget().getPrefix(), is("resolve"));
        assertThat(menuItem0action.getPayload().getMode(), is(MergeMode.replace));
    }
}
