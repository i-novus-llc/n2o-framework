package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.action.link.LinkActionImpl;
import net.n2oapp.framework.api.metadata.meta.action.open_drawer.OpenDrawer;
import net.n2oapp.framework.api.metadata.meta.action.show_modal.ShowModal;
import net.n2oapp.framework.api.metadata.meta.control.ButtonField;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;
import net.n2oapp.framework.api.metadata.meta.widget.form.Form;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * Тест формирования маршрутов при открытии страницы
 */
public class OpenPageRouteCompileTest extends SourceCompileTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack(), new N2oAllDataPack());
        builder.sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/route/testOpenPageRoute.query.xml"),
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/route/testOpenPageRoutePage.page.xml"));
    }

    /**
     * Тест фильтрации master/detail при открытии страницы при наличии параметра в пути маршрута.
     * Фильтр должен попасть в pathMapping
     */
    @Test
    public void masterDetailWithPathParam() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/action/route/testOpenPageRouteMasterDetail.page.xml")
                .get(new PageContext("testOpenPageRouteMasterDetail", "/test"));

        LinkActionImpl action = (LinkActionImpl) ((Widget) page.getRegions().get("single").get(0).getContent().get(1))
                .getActions().get("withParam");
        assertThat(action.getUrl(), is("/test/master/:masterId/detail/:detailId/open1"));
        assertThat(action.getPathMapping().get("detailId"), notNullValue());
        assertThat(action.getQueryMapping().isEmpty(), is(true));

    }

    /**
     * Тест фильтрации master/detail при открытии страницы при отсутствии параметра в пути маршрута.
     * Фильтр должен попасть в queryMapping
     */
    @Test
    public void masterDetailWithoutPathParam() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/action/route/testOpenPageRouteMasterDetail.page.xml")
                .get(new PageContext("testOpenPageRouteMasterDetail", "/test"));

        LinkActionImpl action = (LinkActionImpl) ((Widget) page.getRegions().get("single").get(0).getContent().get(1))
                .getActions().get("withoutParam");
        assertThat(action.getUrl(), is("/test/master/:masterId/detail/open2"));
        assertThat(action.getQueryMapping().isEmpty(), is(true));
    }

    /**
     * Тест открытия страницы при наличии параметра в пути маршрута и отсутствии master/detail фильтрации.
     * Параметр должен быть, фильтра не должно быть.
     */
    @Test
    public void masterDetailWithPathParamWithoutMasterDetail() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/action/route/testOpenPageRouteMasterDetail.page.xml")
                .get(new PageContext("testOpenPageRouteMasterDetail", "/test"));

        LinkActionImpl action = (LinkActionImpl) ((Widget) page.getRegions().get("single").get(0).getContent().get(1))
                .getActions().get("withParamWithoutMasterDetail");
        assertThat(action.getUrl(), is("/test/master/:masterId/detail/:detailId/open3"));
        assertThat(action.getPathMapping().get("detailId"), notNullValue());
        assertThat(action.getQueryMapping().isEmpty(), is(true));
        routeAndGet("/test/master/1/detail/2/open3", Page.class);
        QueryContext queryContext = (QueryContext) route("/test/master/1/detail/2/open3/main", CompiledQuery.class);
        assertThat(queryContext.getFilters().isEmpty(), is(true));
        assertThat(queryContext.getSourceId(null), is("testOpenPageRoute?file=test2"));
    }

    /**
     * Тест открытия страницы с query и path параметрами из мастер виджета.
     */
    @Test
    public void masterWidgetWithPathAndQueryParam() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/action/route/testMasterWidgetWithParams.page.xml")
                .get(new PageContext("testMasterWidgetWithParams", "/test"));
        OpenDrawer action = (OpenDrawer) ((ButtonField) ((Form) page.getRegions().get("single").get(0).getContent().get(0))
                .getComponent().getFieldsets().get(0).getRows().get(0).getCols().get(0).getFields().get(0)).getAction();

        Map<String, ModelLink> queryMapping = action.getPayload().getQueryMapping();
        assertThat(queryMapping.size(), is(1));
        ModelLink queryMappingModelLink = queryMapping.get("number");
        assertThat(queryMappingModelLink.getBindLink(), is("models.resolve['test_main']"));
        assertThat(queryMappingModelLink.getValue(), is("`number`"));

        Map<String, ModelLink> pathMapping = action.getPayload().getPathMapping();
        assertThat(pathMapping.size(), is(1));
        assertThat(pathMapping.get("version").getBindLink(), is("models.resolve['test_main'].version"));
    }

    /**
     * Тест открытия страницы с query и path параметрами из поля с кнопкой зависимого виджета.
     */
    @Test
    public void dependentWidgetWithPathAndQueryParam() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/action/route/testDependentWidgetWithParams.page.xml")
                .get(new PageContext("testDependentWidgetWithParams", "/test"));

        OpenDrawer action = (OpenDrawer) ((ButtonField) ((Form) page.getRegions().get("single").get(0).getContent().get(1))
                .getComponent().getFieldsets().get(0).getRows().get(0).getCols().get(0).getFields().get(0)).getAction();

        Map<String, ModelLink> queryMapping = action.getPayload().getQueryMapping();
        assertThat(queryMapping.size(), is(1));
        ModelLink queryMappingModelLink = queryMapping.get("number");
        assertThat(queryMappingModelLink.getBindLink(), is("models.resolve['test_main']"));
        assertThat(queryMappingModelLink.getValue(), is("`number`"));

        Map<String, ModelLink> pathMapping = action.getPayload().getPathMapping();
        assertThat(pathMapping.size(), is(1));
        assertThat(pathMapping.get("version").getBindLink(), is("models.resolve['test_main'].version"));
    }

    /**
     * Тест открытия страницы с query и path параметрами из тулбара зависимого виджета.
     */
    @Test
    public void dependentWidgetsToolbarWithPathAndQueryParam() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/action/route/testDependentWidgetsToolbarWithParams.page.xml")
                .get(new PageContext("testDependentWidgetsToolbarWithParams", "/test"));

        OpenDrawer action = (OpenDrawer) ((Form) page.getRegions().get("single").get(0).getContent().get(1))
                .getToolbar().get("topLeft").get(0).getButtons().get(0).getAction();

        Map<String, ModelLink> queryMapping = action.getPayload().getQueryMapping();
        assertThat(queryMapping.size(), is(1));
        ModelLink queryMappingModelLink = queryMapping.get("number");
        assertThat(queryMappingModelLink.getBindLink(), is("models.resolve['test_main']"));
        assertThat(queryMappingModelLink.getValue(), is("`number`"));

        Map<String, ModelLink> pathMapping = action.getPayload().getPathMapping();
        assertThat(pathMapping.size(), is(2));
        assertThat(pathMapping.get("test_main_id").getBindLink(), is("models.resolve['test_main'].id"));
        assertThat(pathMapping.get("version").getBindLink(), is("models.resolve['test_main'].version"));
    }

    /**
     * Тест открытия страницы с query и path параметрами из тулбара страницы.
     */
    @Test
    public void pageToolbarWithPathAndQueryParam() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/action/route/testPageToolbarWithParams.page.xml")
                .get(new PageContext("testPageToolbarWithParams", "/test"));

        OpenDrawer action = (OpenDrawer) page.getActions().get("menuItem0");

        Map<String, ModelLink> queryMapping = action.getPayload().getQueryMapping();
        assertThat(queryMapping.size(), is(1));
        ModelLink queryMappingModelLink = queryMapping.get("number");
        assertThat(queryMappingModelLink.getBindLink(), is("models.resolve['test_main']"));
        assertThat(queryMappingModelLink.getValue(), is("`number`"));

        Map<String, ModelLink> pathMapping = action.getPayload().getPathMapping();
        assertThat(pathMapping.size(), is(1));
        assertThat(pathMapping.get("version").getBindLink(), is("models.resolve['test_main'].version"));
    }

    /**
     * Тест открытия страницы с path параметрами и проверка дефолтных и заданных значений мастер параметра.
     * Если не заданы, то виджет и модель берутся из кнопки
     */
    @Test
    public void testMasterParamWithDefaultAndDefinedAttributes() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/action/route/testMasterParam.page.xml")
                .get(new PageContext("testMasterParam", "/test"));

        // with itself model and widget-id
        ShowModal action = (ShowModal) ((Form) page.getRegions().get("single").get(0).getContent().get(1))
                .getToolbar().get("topLeft").get(0).getButtons().get(0).getAction();
        Map<String, ModelLink> pathMapping = action.getPayload().getPathMapping();
        assertThat(pathMapping.size(), is(1));
        assertThat(pathMapping.get("id").getBindLink(), is("models.resolve['master'].clientId"));

        // with default (from button) model and widget-id
        action = (ShowModal) ((Form) page.getRegions().get("single").get(0).getContent().get(2))
                .getToolbar().get("topLeft").get(0).getButtons().get(0).getAction();
        pathMapping = action.getPayload().getPathMapping();
        assertThat(pathMapping.size(), is(1));
        assertThat(pathMapping.get("id").getBindLink(), is("models.filter['test_dependent2'].clientId"));
    }


    /**
     * Тест формирования url с route
     */
    @Test
    public void testRouteWithoutParams() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/action/route/testOpenPageRoute.page.xml")
                .get(new PageContext("testOpenPageRoute", "/test"));

        LinkActionImpl routeWithResolveModel = (LinkActionImpl) page.getActions().get("btn1");
        assertThat(routeWithResolveModel.getUrl(), is("/test/update"));
        assertThat(routeWithResolveModel.getPathMapping().isEmpty(), is(true));
        assertThat(routeWithResolveModel.getQueryMapping().isEmpty(), is(true));

        LinkActionImpl routeWithFilterModel = (LinkActionImpl) page.getActions().get("btn2");
        assertThat(routeWithFilterModel.getUrl(), is("/test/update"));
        assertThat(routeWithFilterModel.getPathMapping().isEmpty(), is(true));
        assertThat(routeWithFilterModel.getQueryMapping().isEmpty(), is(true));

        LinkActionImpl routeWithMasterParam = (LinkActionImpl) page.getActions().get("btn3");
        assertThat(routeWithMasterParam.getUrl(), is("/test/update"));
        assertThat(routeWithMasterParam.getPathMapping().isEmpty(), is(true));
        assertThat(routeWithMasterParam.getQueryMapping().isEmpty(), is(true));

        LinkActionImpl routeWithDetailFieldId = (LinkActionImpl) page.getActions().get("btn4");
        assertThat(routeWithDetailFieldId.getUrl(), is("/test/update"));
        assertThat(routeWithDetailFieldId.getPathMapping().isEmpty(), is(true));
        assertThat(routeWithDetailFieldId.getQueryMapping().isEmpty(), is(true));
    }

    /**
     * Тест формирования url без route, c master-param
     */
    @Test
    public void testWithoutRouteWithMasterParam() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/action/route/testOpenPageRoute.page.xml")
                .get(new PageContext("testOpenPageRoute", "/test"));

        LinkActionImpl actionWithResolveModel = (LinkActionImpl) page.getActions().get("btn5");
        assertThat(actionWithResolveModel.getUrl(), is("/test/btn5"));
        assertThat(actionWithResolveModel.getPathMapping().isEmpty(), is(true));
        assertThat(actionWithResolveModel.getQueryMapping().containsKey("id"), is(true));

        LinkActionImpl actionWithFilterModel = (LinkActionImpl) page.getActions().get("btn6");
        assertThat(actionWithFilterModel.getUrl(), is("/test/btn6"));
        assertThat(actionWithFilterModel.getPathMapping().isEmpty(), is(true));
        assertThat(actionWithFilterModel.getQueryMapping().isEmpty(), is(true));
    }

    /**
     * Тест формирования url без route и master-param, но с detail-field-id
     */
    @Test
    public void testWithoutRouteWithDetailFieldId() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/action/route/testOpenPageRoute.page.xml")
                .get(new PageContext("testOpenPageRoute", "/test"));

        LinkActionImpl actionWithResolveModel = (LinkActionImpl) page.getActions().get("btn7");
        assertThat(actionWithResolveModel.getUrl(), is("/test/btn7"));
        assertThat(actionWithResolveModel.getPathMapping().isEmpty(), is(true));
        assertThat(actionWithResolveModel.getQueryMapping().containsKey("id"), is(true));

        LinkActionImpl actionWithFilterModel = (LinkActionImpl) page.getActions().get("btn8");
        assertThat(actionWithFilterModel.getUrl(), is("/test/btn8"));
        assertThat(actionWithFilterModel.getPathMapping().isEmpty(), is(true));
        assertThat(actionWithFilterModel.getQueryMapping().isEmpty(), is(true));
    }

    /**
     * Тест формирования url без route, но с master-param и detail-field-id
     */
    @Test
    public void testWithoutRouteWithMasterParamAndDetailFieldId() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/action/route/testOpenPageRoute.page.xml")
                .get(new PageContext("testOpenPageRoute", "/test"));

        LinkActionImpl actionWithResolveModel = (LinkActionImpl) page.getActions().get("btn9");
        assertThat(actionWithResolveModel.getUrl(), is("/test/btn9"));
        assertThat(actionWithResolveModel.getPathMapping().isEmpty(), is(true));
        assertThat(actionWithResolveModel.getQueryMapping().containsKey("id"), is(true));

        LinkActionImpl actionWithFilterModel = (LinkActionImpl) page.getActions().get("btn10");
        assertThat(actionWithFilterModel.getUrl(), is("/test/btn10"));
        assertThat(actionWithFilterModel.getPathMapping().isEmpty(), is(true));
        assertThat(actionWithFilterModel.getQueryMapping().isEmpty(), is(true));
    }

    /**
     * Тест формирования url без route, master-param, и detail-field-id
     */
    @Test
    public void testWithoutRouteAndAllParams() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/action/route/testOpenPageRoute.page.xml")
                .get(new PageContext("testOpenPageRoute", "/test"));

        LinkActionImpl actionWithResolveModel = (LinkActionImpl) page.getActions().get("btn11");
        assertThat(actionWithResolveModel.getUrl(), is("/test/btn11"));
        assertThat(actionWithResolveModel.getPathMapping().isEmpty(), is(true));
        assertThat(actionWithResolveModel.getQueryMapping().isEmpty(), is(true));

        LinkActionImpl actionWithFilterModel = (LinkActionImpl) page.getActions().get("btn12");
        assertThat(actionWithFilterModel.getUrl(), is("/test/btn12"));
        assertThat(actionWithFilterModel.getPathMapping().isEmpty(), is(true));
        assertThat(actionWithFilterModel.getQueryMapping().isEmpty(), is(true));
    }
}
