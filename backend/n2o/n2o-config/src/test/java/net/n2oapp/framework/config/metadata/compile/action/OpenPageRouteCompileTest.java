package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.Page;
import net.n2oapp.framework.api.metadata.meta.action.link.LinkAction;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

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
                new CompileInfo("net/n2oapp/framework/config/metadata/compile/action/route/testOpenPageRoute.page.xml"));
    }

    /**
     * Тест фильтрации master/detail при открытии страницы при наличии параметра в пути маршрута.
     * Фильтр должен попасть в pathMapping
     */
    @Test
    public void masterDetailWithPathParam() {
        Page page = compile("net/n2oapp/framework/config/metadata/compile/action/route/testOpenPageRouteMasterDetail.page.xml")
                .get(new PageContext("testOpenPageRouteMasterDetail", "/test"));

        LinkAction action = (LinkAction) page.getWidgets().get("test_detail").getActions().get("withParam");
        assertThat(action.getOptions().getPath(), is("/test/:masterId/detail/:detailId/open1"));
        assertThat(action.getOptions().getPathMapping().get("detailId"), notNullValue());
        assertThat(action.getOptions().getQueryMapping().isEmpty(), is(true));

    }

    /**
     * Тест фильтрации master/detail при открытии страницы при отсутствии параметра в пути маршрута.
     * Фильтр должен попасть в queryMapping
     */
    @Test
    public void masterDetailWithoutPathParam() {
        Page page = compile("net/n2oapp/framework/config/metadata/compile/action/route/testOpenPageRouteMasterDetail.page.xml")
                .get(new PageContext("testOpenPageRouteMasterDetail", "/test"));

        LinkAction action = (LinkAction) page.getWidgets().get("test_detail").getActions().get("withoutParam");
        assertThat(action.getOptions().getPath(), is("/test/:masterId/detail/open2"));
        assertThat(action.getOptions().getQueryMapping().get("detailId"), notNullValue());
    }

    /**
     * Тест открытия страницы при наличии параметра в пути маршрута и отсутствии master/detail фильтрации.
     * Параметр должен быть, фильтра не должно быть.
     */
    @Test
    public void masterDetailWithPathParamWithoutMasterDetail() {
        Page page = compile("net/n2oapp/framework/config/metadata/compile/action/route/testOpenPageRouteMasterDetail.page.xml")
                .get(new PageContext("testOpenPageRouteMasterDetail", "/test"));

        LinkAction action = (LinkAction) page.getWidgets().get("test_detail").getActions().get("withParamWithoutMasterDetail");
        assertThat(action.getOptions().getPath(), is("/test/:masterId/detail/:detailId/open3"));
        assertThat(action.getOptions().getPathMapping().get("detailId"), notNullValue());
        assertThat(action.getOptions().getQueryMapping().isEmpty(), is(true));
        QueryContext queryContext = (QueryContext) route("/test/:masterId/detail/123/open3/main").getContext(CompiledQuery.class);
        assertThat(queryContext.getFilters().isEmpty(), is(true));
    }
}
