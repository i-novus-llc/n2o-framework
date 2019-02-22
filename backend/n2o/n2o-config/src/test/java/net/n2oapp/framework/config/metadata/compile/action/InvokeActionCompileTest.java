package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.data.validation.ConditionValidation;
import net.n2oapp.framework.api.data.validation.ConstraintValidation;
import net.n2oapp.framework.api.data.validation.MandatoryValidation;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.Page;
import net.n2oapp.framework.api.metadata.meta.action.invoke.InvokeAction;
import net.n2oapp.framework.api.metadata.meta.widget.RequestMethod;
import net.n2oapp.framework.api.metadata.meta.widget.WidgetDataProvider;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.action.InvokeActionElementIOV1;
import net.n2oapp.framework.config.metadata.compile.context.ActionContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.context.WidgetContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Проверка копиляции invoke-action
 */
public class InvokeActionCompileTest  extends SourceCompileTestBase {

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
        builder.ios(new InvokeActionElementIOV1());
        builder.compilers(new InvokeActionCompiler());
        builder.binders(new InvokeActionBinder(), new ReduxActionBinder());

        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testActionContext.query.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testActionContext.object.xml"));
    }

    @Test
    public void simple() {
        Table table = (Table) compile("net/n2oapp/framework/config/metadata/compile/action/testInvokeAction.widget.xml")
                .get(new WidgetContext("testInvokeAction", "/w"));

        //filter model
        InvokeAction testAction = (InvokeAction) table.getActions().get("test");
        assertThat(testAction.getSrc(), is("perform"));
        assertThat(testAction.getOptions().getType(), is("n2o/actionImpl/START_INVOKE"));
        assertThat(testAction.getOptions().getPayload().getModelLink(), is("models.filter['w']"));
        assertThat(testAction.getOptions().getPayload().getWidgetId(), is("w"));
        assertThat(testAction.getOptions().getPayload().getDataProvider().getMethod(), is(RequestMethod.POST));
        assertThat(testAction.getOptions().getPayload().getDataProvider().getUrl(), is("n2o/data/w/test"));
        assertThat(testAction.getOptions().getPayload().getDataProvider().getQueryMapping(), is(nullValue()));
        assertThat(testAction.getOptions().getMeta().getSuccess().getRefresh(), notNullValue());
        assertThat(testAction.getOptions().getMeta().getSuccess().getRefresh().getOptions().getWidgetId(), is("w"));
        assertThat(testAction.getOptions().getMeta().getSuccess().getCloseLastModal(), nullValue());

        //resolve model
        InvokeAction menuItem0action = (InvokeAction) table.getActions().get("menuItem0");
        assertThat(menuItem0action.getSrc(), is("perform"));
        assertThat(menuItem0action.getOptions().getType(), is("n2o/actionImpl/START_INVOKE"));
        assertThat(menuItem0action.getOptions().getPayload().getModelLink(), is("models.resolve['w']"));
        assertThat(menuItem0action.getOptions().getPayload().getWidgetId(), is("w"));
        assertThat(menuItem0action.getOptions().getMeta().getSuccess().getRefresh().getOptions().getWidgetId(), is("w"));
//        assertThat(menuItem0action.getOptions().getMeta().getSuccess().getCloseLastModal(), is(true));
        WidgetDataProvider dataProvider = menuItem0action.getOptions().getPayload().getDataProvider();
        assertThat(dataProvider.getMethod(), is(RequestMethod.POST));
        assertThat(dataProvider.getUrl(), is("n2o/data/w/:w_id/menuItem0"));
        assertThat(dataProvider.getQueryMapping(), nullValue());
        assertThat(dataProvider.getPathMapping(), notNullValue());
        assertThat(dataProvider.getPathMapping().get("w_id"), notNullValue());
        assertThat(route("/w/:w_id/menuItem0").getContext(CompiledObject.class), notNullValue());
    }

    @Test
    public void validations() {
        compile("net/n2oapp/framework/config/metadata/compile/action/testRegisterActionContext.widget.xml")
                .get(new WidgetContext("testRegisterActionContext"));
        ActionContext context = (ActionContext) route("/:testRegisterActionContext_id/test").getContext(CompiledObject.class);
        assertThat(context, notNullValue());
        assertThat(context.getOperationId(), is("create"));
        assertThat(context.getValidations().size(), is(3));
        assertThat(context.getValidations().get(0), instanceOf(MandatoryValidation.class));
        assertThat(context.getValidations().get(1), instanceOf(ConditionValidation.class));
        assertThat(context.getValidations().get(2), instanceOf(ConstraintValidation.class));
        compile("net/n2oapp/framework/config/metadata/compile/action/testRegisterActionContextForPageAction.page.xml")
                .get(new PageContext("testRegisterActionContextForPageAction", "/route"));
        context = (ActionContext) route("/route/test").getContext(CompiledObject.class);
        assertThat(context, notNullValue());
        assertThat(context.getOperationId(), is("create"));
        assertThat(context.getValidations().size(), is(3));
        assertThat(context.getValidations().get(0), instanceOf(MandatoryValidation.class));
        assertThat(context.getValidations().get(1), instanceOf(ConditionValidation.class));
        assertThat(context.getValidations().get(2), instanceOf(ConstraintValidation.class));
        assertThat(context.getFailAlertWidgetId(), is("route_testActionContext"));
        assertThat(context.getSuccessAlertWidgetId(), is("route_testActionContext"));
    }

    @Test
    public void bindDataProvider() {
        DataSet data = new DataSet().add("parent_id", 123);
        Page page = bind("net/n2oapp/framework/config/metadata/compile/action/testInvokeActionBind.page.xml")
                .get(new PageContext("testInvokeActionBind", "/p/:parent_id/create"), data);
        InvokeAction a1 = (InvokeAction) page.getWidgets().get("p_create_w1").getActions().get("a1");
        assertThat(a1.getOptions().getPayload().getDataProvider().getUrl(), is("n2o/data/p/123/create/w1/a1"));
        InvokeAction a2 = (InvokeAction) page.getWidgets().get("p_create_w2").getActions().get("a2");
        assertThat(a2.getOptions().getPayload().getDataProvider().getUrl(), is("n2o/data/p/123/create/w2/:p_create_w2_id/a2"));
    }

    @Test
    public void bindRedirect() {
        DataSet data = new DataSet().add("parent_id", 123);
        PageContext context = new PageContext("testInvokeActionBind", "/p/:parent_id/create");
        Page page = bind("net/n2oapp/framework/config/metadata/compile/action/testInvokeActionBind.page.xml")
                .get(context, data);
        InvokeAction a1 = (InvokeAction) page.getWidgets().get("p_create_w1").getActions().get("a1");
        assertThat(a1.getOptions().getMeta().getSuccess().getRedirect().getPath(), is("/p/123"));
    }

    @Test
    public void pageAction() {
        Page page = (Page) compile("net/n2oapp/framework/config/metadata/compile/action/testPageInvokeAction.page.xml")
                .get(new PageContext("testPageInvokeAction", "/p"));
        InvokeAction testAction = (InvokeAction) page.getActions().get("test");
        assertThat(testAction.getSrc(), is("perform"));
        assertThat(testAction.getOptions().getType(), is("n2o/actionImpl/START_INVOKE"));
        assertThat(testAction.getOptions().getPayload().getModelLink(), is("models.filter['p_w']"));
        assertThat(testAction.getOptions().getPayload().getWidgetId(), is("p_w"));
        assertThat(testAction.getOptions().getPayload().getPageId(), is("p"));
    }
}