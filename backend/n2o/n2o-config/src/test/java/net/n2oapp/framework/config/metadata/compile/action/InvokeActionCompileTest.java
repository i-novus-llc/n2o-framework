package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.data.validation.ConditionValidation;
import net.n2oapp.framework.api.data.validation.ConstraintValidation;
import net.n2oapp.framework.api.data.validation.MandatoryValidation;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.ClientDataProvider;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.action.invoke.InvokeAction;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.RequestMethod;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.ActionContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static net.n2oapp.properties.test.TestUtil.assertOnException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Проверка компиляции invoke-action
 */
public class InvokeActionCompileTest extends SourceCompileTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack(), new N2oAllDataPack());

        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testActionContext.query.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/testActionContext.object.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/empty.object.xml"));
    }

    @Test
    public void simple() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/action/testInvokeAction.page.xml")
                .get(new PageContext("testInvokeAction", "/w"));
        Table table = (Table) page.getWidget();
        //filter model
        InvokeAction testAction = (InvokeAction) table.getToolbar().getButton("test").getAction();
        assertThat(testAction.getType(), is("n2o/actionImpl/START_INVOKE"));
        assertThat(testAction.getPayload().getModel(), is(ReduxModel.FILTER));
        assertThat(testAction.getPayload().getDatasource(), is("w_main"));
        assertThat(testAction.getPayload().getDataProvider().getMethod(), is(RequestMethod.POST));
        assertThat(testAction.getPayload().getDataProvider().getUrl(), is("n2o/data/w/test"));
        assertThat(testAction.getPayload().getDataProvider().getQueryMapping().size(), is(0));
        assertThat(testAction.getMeta().getSuccess().getRefresh(), notNullValue());
        assertThat(testAction.getMeta().getSuccess().getRefresh().getDatasources(), hasItem("w_testW"));
        assertThat(testAction.getMeta().getSuccess().getModalsToClose(), nullValue());

        //resolve model
        InvokeAction menuItem0action = (InvokeAction) table.getToolbar().getButton("menuItem0").getAction();
        assertThat(menuItem0action.getType(), is("n2o/actionImpl/START_INVOKE"));
        assertThat(menuItem0action.getPayload().getModel(), is(ReduxModel.RESOLVE));
        assertThat(menuItem0action.getPayload().getDatasource(), is("w_main"));
        assertThat(menuItem0action.getMeta().getSuccess().getRefresh().getDatasources(), hasItem("w_main"));
//        assertThat(menuItem0action.getOptions().getMeta().getSuccess().getModalsToClose(), is(1));
        ClientDataProvider dataProvider = menuItem0action.getPayload().getDataProvider();
        assertThat(dataProvider.getMethod(), is(RequestMethod.POST));
        assertThat(dataProvider.getUrl(), is("n2o/data/w/menuItem0"));
        assertThat(dataProvider.getQueryMapping().size(), is(0));
        assertThat(dataProvider.getOptimistic(), is(true));
        assertThat(route("/w/menuItem0", CompiledObject.class), notNullValue());
    }

    @Test
    public void method() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/action/testInvokeActionMethod.page.xml")
                .get(new PageContext("testInvokeActionMethod", "/w"));
        Table table = (Table) page.getWidget();
        InvokeAction testAction;
        testAction = (InvokeAction) table.getToolbar().getButton("testDefault").getAction();
        assertThat(testAction.getType(), is("n2o/actionImpl/START_INVOKE"));
        assertThat(testAction.getPayload().getModel(), is(ReduxModel.FILTER));
        assertThat(testAction.getPayload().getDatasource(), is("w_main"));
        assertThat(testAction.getPayload().getDataProvider().getMethod(), is(RequestMethod.POST));
        assertThat(testAction.getPayload().getDataProvider().getUrl(), is("n2o/data/w/testDefault"));
        assertThat(testAction.getPayload().getDataProvider().getQueryMapping().size(), is(0));
        assertThat(testAction.getPayload().getDataProvider().getPathMapping().size(), is(0));

        testAction = (InvokeAction) table.getToolbar().getButton("testPut").getAction();
        assertThat(testAction.getType(), is("n2o/actionImpl/START_INVOKE"));
        assertThat(testAction.getPayload().getModel(), is(ReduxModel.RESOLVE));
        assertThat(testAction.getPayload().getDatasource(), is("w_main"));
        assertThat(testAction.getPayload().getDataProvider().getMethod(), is(RequestMethod.PUT));
        assertThat(testAction.getPayload().getDataProvider().getUrl(), is("n2o/data/w/:id"));
        assertThat(testAction.getPayload().getDataProvider().getQueryMapping().size(), is(0));
        Map<String, ModelLink> pathMapping = testAction.getPayload().getDataProvider().getPathMapping();
        assertThat(pathMapping.size(), is(1));
        assertThat(pathMapping.get("id").getBindLink(), is("models.resolve['w_main']"));
        assertThat(pathMapping.get("id").getValue(), is("`id`"));

        testAction = (InvokeAction) table.getToolbar().getButton("testDelete").getAction();
        assertThat(testAction.getType(), is("n2o/actionImpl/START_INVOKE"));
        assertThat(testAction.getPayload().getModel(), is(ReduxModel.RESOLVE));
        assertThat(testAction.getPayload().getDatasource(), is("w_main"));
        assertThat(testAction.getPayload().getDataProvider().getMethod(), is(RequestMethod.DELETE));
        assertThat(testAction.getPayload().getDataProvider().getUrl(), is("n2o/data/w/testDelete"));
        assertThat(testAction.getPayload().getDataProvider().getQueryMapping().size(), is(0));
        assertThat(testAction.getPayload().getDataProvider().getPathMapping().size(), is(0));
    }

    @Test
    public void validations() {
        compile("net/n2oapp/framework/config/metadata/compile/action/testRegisterActionContext.page.xml")
                .get(new PageContext("testRegisterActionContext", "/"));
        ActionContext context = (ActionContext) route("/:test", CompiledObject.class);
        assertThat(context, notNullValue());
        assertThat(context.getOperationId(), is("create"));
        assertThat(context.getValidations().size(), is(3));
        assertThat(context.getValidations().get(0), instanceOf(MandatoryValidation.class));
        assertThat(context.getValidations().get(1), instanceOf(ConditionValidation.class));
        assertThat(context.getValidations().get(2), instanceOf(ConstraintValidation.class));
        compile("net/n2oapp/framework/config/metadata/compile/action/testRegisterActionContextForPageAction.page.xml")
                .get(new PageContext("testRegisterActionContextForPageAction", "/route"));
        context = (ActionContext) route("/route/test", CompiledObject.class);
        assertThat(context, notNullValue());
        assertThat(context.getOperationId(), is("create"));
        assertThat(context.getValidations().size(), is(3));
        assertThat(context.getValidations().get(0), instanceOf(MandatoryValidation.class));
        assertThat(context.getValidations().get(1), instanceOf(ConditionValidation.class));
        assertThat(context.getValidations().get(2), instanceOf(ConstraintValidation.class));
        assertThat(context.getFailAlertWidgetId(), is("route_testActionContext"));
        assertThat(context.getSuccessAlertWidgetId(), is("route_testActionContext"));

        compile("net/n2oapp/framework/config/metadata/compile/action/testNotValidateAction.page.xml")
                .get(new PageContext("testNotValidateAction", "/p"));
        context = (ActionContext) route("/p/create", CompiledObject.class);
        assertThat(context, notNullValue());
        assertThat(context.getOperationId(), is("create"));
        assertThat(context.getValidations(), nullValue());
    }

    @Test
    public void bindDataProvider() {
        DataSet data = new DataSet().add("parent_id", 123);
        StandardPage page = (StandardPage) bind("net/n2oapp/framework/config/metadata/compile/action/testInvokeActionBind.page.xml")
                .get(new PageContext("testInvokeActionBind", "/p/:parent_id/create"), data);
        InvokeAction a1 = (InvokeAction) ((Form)page.getRegions().get("left").get(0).getContent().get(0)).getToolbar()
                .get("topLeft").get(0).getButtons().get(0).getAction();
        assertThat(a1.getPayload().getDataProvider().getUrl(), is("n2o/data/p/123/create/a1"));
        InvokeAction a2 = (InvokeAction) ((Widget) page.getRegions().get("right").get(0).getContent().get(0)).getToolbar().getButton("a2").getAction();
        assertThat(a2.getPayload().getDataProvider().getUrl(), is("n2o/data/p/123/create/a2"));
    }

    @Test
    public void bindRedirect() {
        DataSet data = new DataSet().add("parent_id", 123);
        PageContext context = new PageContext("testInvokeActionBind", "/p/:parent_id/create");
        StandardPage page = (StandardPage) bind("net/n2oapp/framework/config/metadata/compile/action/testInvokeActionBind.page.xml")
                .get(context, data);
        InvokeAction a1 = (InvokeAction) ((Form)page.getRegions().get("left").get(0).getContent().get(0)).getToolbar()
                .get("topLeft").get(0).getButtons().get(0).getAction();
        assertThat(a1.getMeta().getSuccess().getRedirect().getPath(), is("/p/123"));
    }

    @Test
    public void pageAction() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/action/testPageInvokeAction.page.xml")
                .get(new PageContext("testPageInvokeAction", "/p"));
        InvokeAction testAction = (InvokeAction) page.getToolbar().getButton("test").getAction();
        assertThat(testAction.getType(), is("n2o/actionImpl/START_INVOKE"));
        assertThat(testAction.getPayload().getModel(), is(ReduxModel.FILTER));
        assertThat(testAction.getPayload().getDatasource(), is("p_w"));
    }

    @Test
    public void dataProviderParams() {
        SimplePage page = (SimplePage) compile("net/n2oapp/framework/config/metadata/compile/action/testInvokeActionParam.page.xml")
                .get(new PageContext("testInvokeActionParam", "/w"));
        Table table = (Table) page.getWidget();
        // operationMapping should not contains form params from corresponding invoke
        // but could contains from other
        ActionContext actionContext1 = (ActionContext) route("/w/123/create", CompiledObject.class);
        assertThat(actionContext1.getOperationMapping().containsKey("fpName1"), is(false));
        ActionContext actionContext2 = (ActionContext) route("/w/123/update", CompiledObject.class);
        assertThat(actionContext2.getOperationMapping().containsKey("fpGender.id"), is(false));

        //filter model
        InvokeAction testAction = (InvokeAction) table.getToolbar().getButton("test").getAction();
        ClientDataProvider provider1 = testAction.getPayload().getDataProvider();
        assertThat(provider1.getSubmitForm(), is(true));
        assertThat(provider1.getFormMapping().size(), is(1));
        assertThat(provider1.getPathMapping().size(), is(1));
        assertThat(provider1.getHeadersMapping().size(), is(1));

        assertThat(provider1.getFormMapping().get("fpName1").getValue(), is("fpValue1"));
        assertThat(provider1.getPathMapping().get("ppName1").getValue(), is("ppValue1"));
        assertThat(provider1.getHeadersMapping().get("hpName1").getValue(), is("hpValue1"));

        //resolve model
        InvokeAction menuItem0action = (InvokeAction) table.getToolbar().getButton("menuItem1").getAction();
        ClientDataProvider provider2 = menuItem0action.getPayload().getDataProvider();
        assertThat(provider2.getSubmitForm(), is(false));
        assertThat(provider2.getFormMapping().size(), is(1));
        assertThat(provider2.getPathMapping().size(), is(1));
        assertThat(provider2.getHeadersMapping().size(), is(1));

        assertThat(provider2.getFormMapping().get("fpGender.id").getValue(), is(1));
        assertThat(provider2.getPathMapping().get("ppName2").getValue(), is("ppValue2"));
        assertThat(provider2.getHeadersMapping().get("hpName2").getValue(), is("hpValue2"));
    }

    @Test
    public void routeAndPathValidationTest() {
        DataSet data = new DataSet().add("parent_id", 123);
        StandardPage page = (StandardPage) bind("net/n2oapp/framework/config/metadata/compile/action/testInvokeActionValidation/routeAndPath.page.xml")
                .get(new PageContext("routeAndPath"), data);
        InvokeAction action = (InvokeAction) ((Widget) page.getRegions().get("right").get(0).getContent().get(0)).getToolbar().getButton("b1").getAction();
        assertThat(action.getPayload().getDataProvider().getUrl(), is("n2o/data/routeAndPath/:main_id"));
        assertThat(action.getType(), is("n2o/actionImpl/START_INVOKE"));
        assertThat(action.getPayload().getModel(), is(ReduxModel.RESOLVE));
        assertThat(action.getPayload().getDatasource(), is("routeAndPath_w2"));

        action = (InvokeAction) ((Widget) page.getRegions().get("right").get(0).getContent().get(0)).getToolbar().getButton("b2").getAction();
        assertThat(action.getPayload().getDataProvider().getUrl(), is("n2o/data/routeAndPath/b2"));
        assertThat(action.getType(), is("n2o/actionImpl/START_INVOKE"));
        assertThat(action.getPayload().getModel(), is(ReduxModel.RESOLVE));
        assertThat(action.getPayload().getDatasource(), is("routeAndPath_w2"));
    }

    @Test
    public void emptyRouteValidationTest() {
        assertOnException(() -> bind("net/n2oapp/framework/config/metadata/compile/action/testInvokeActionValidation/emptyRoute.page.xml")
                        .get(new PageContext("emptyRoute"), null),
                N2oException.class,
                e -> assertThat(e.getMessage(), is("path-param \"main_id\" not used in route")));
    }

    @Test
    public void emptyPathValidationTest() {
        assertOnException(() -> bind("net/n2oapp/framework/config/metadata/compile/action/testInvokeActionValidation/emptyPath.page.xml")
                        .get(new PageContext("emptyPath"), null),
                N2oException.class,
                e -> assertThat(e.getMessage(), is("path-param \"/:main_id\" for route \"main_id\" not set")));
    }

    @Test()
    public void multiplyPathValidationTest() {
        assertOnException(() -> bind("net/n2oapp/framework/config/metadata/compile/action/testInvokeActionValidation/multiplyPath.page.xml")
                        .get(new PageContext("multiplyPath"), null),
                N2oException.class,
                e -> assertThat(e.getMessage(), is("route \"/:main_id\" not contains path-param \"t_id\"")));
    }

    @Test
    public void invokeObjectTest() {
        DataSet data = new DataSet().add("parent_id", 123);
        StandardPage page = (StandardPage) bind("net/n2oapp/framework/config/metadata/compile/action/testInvokeActionObject.page.xml")
                .get(new PageContext("testInvokeActionObject", "/p/:parent_id/create"), data);
        InvokeAction a1 = (InvokeAction) ((Form)page.getRegions().get("left").get(0).getContent().get(0)).getToolbar()
                .get("topLeft").get(0).getButtons().get(0).getAction();
        assertThat(a1.getPayload().getDataProvider().getUrl(), is("n2o/data/p/123/create/a1"));
        InvokeAction a2 = (InvokeAction) ((Widget) page.getRegions().get("right").get(0).getContent().get(0)).getToolbar().getButton("a2").getAction();
        assertThat(a2.getPayload().getDataProvider().getUrl(), is("n2o/data/p/123/create/a2"));
    }

    @Test
    public void datasource() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/action/testInvokeActionDatasource.page.xml")
                .get(new PageContext("testInvokeActionDatasource"));
        Form widget = (Form) page.getRegions().values().iterator().next().get(0).getContent().get(0);
        InvokeAction testAction1 = (InvokeAction) widget.getToolbar().getButton("test1").getAction();
        assertThat(testAction1.getType(), is("n2o/actionImpl/START_INVOKE"));
        assertThat(testAction1.getPayload().getModel(), is(ReduxModel.FILTER));
        assertThat(testAction1.getPayload().getDatasource(), is("testInvokeActionDatasource_main"));
        assertThat(testAction1.getPayload().getDataProvider().getMethod(), is(RequestMethod.POST));
        assertThat(testAction1.getPayload().getDataProvider().getUrl(), is("n2o/data/testInvokeActionDatasource/test1"));

        InvokeAction testAction2 = (InvokeAction) widget.getToolbar().getButton("test2").getAction();
        assertThat(testAction2.getPayload().getModel(), is(ReduxModel.RESOLVE));
        assertThat(testAction2.getPayload().getDatasource(), is("testInvokeActionDatasource_outer"));
        assertThat(testAction2.getPayload().getDataProvider().getUrl(), is("n2o/data/testInvokeActionDatasource/test2"));
    }

}
