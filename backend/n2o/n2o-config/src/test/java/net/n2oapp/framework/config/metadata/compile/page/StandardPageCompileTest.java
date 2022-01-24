package net.n2oapp.framework.config.metadata.compile.page;

import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.ClientDataProvider;
import net.n2oapp.framework.api.metadata.meta.Filter;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.action.invoke.InvokeAction;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.ModalPageContext;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import net.n2oapp.framework.config.metadata.pack.N2oAllDataPack;
import net.n2oapp.framework.config.metadata.pack.N2oAllPagesPack;
import net.n2oapp.framework.config.reader.ReferentialIntegrityViolationException;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Тестирование компиляции стандартной страницы
 */
public class StandardPageCompileTest extends SourceCompileTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oAllPagesPack(), new N2oAllDataPack())
                .sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/page/utObjectField.page.xml"),
                        new CompileInfo("net/n2oapp/framework/config/metadata/compile/object/utAction.object.xml"));
    }

    @Test
    public void routes() {
        PageContext context = new PageContext("testRoutes", "/page");
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/page/testStandardPageDependency.query.xml",
                "net/n2oapp/framework/config/metadata/compile/page/testRoutes.page.xml")
                .get(context);
        assertThat(page.getId(), is("page"));
        assertThat(page.getRoutes().getList().size(), is(1));
        assertThat(page.getRoutes().getList().get(0).getPath(), is("/page"));
        assertThat(((PageContext) route("/page", Page.class)).getClientPageId(), is(context.getClientPageId()));
    }

    @Test
    public void masterDetails() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/page/testStandardPageDependency.query.xml",
                "net/n2oapp/framework/config/metadata/compile/page/testStandardPageDependency.object.xml",
                "net/n2oapp/framework/config/metadata/compile/page/testStandardPageDependency.page.xml")
                .get(new PageContext("testStandardPageDependency"));
        Table master = (Table) page.getRegions().get("left").get(0).getContent().get(0);
        Table detail = (Table) page.getRegions().get("right").get(0).getContent().get(0);
        Table panel = (Table) page.getRegions().get("right").get(0).getContent().get(1);

        assertThat(master, notNullValue());
        assertThat(detail, notNullValue());
        assertThat(panel, notNullValue());
        ClientDataProvider masterProvider = page.getDatasources().get(master.getDatasource()).getProvider();
        assertThat(masterProvider.getUrl(), is("n2o/data/testStandardPageDependency/master"));
        ClientDataProvider detailProvider = page.getDatasources().get(detail.getDatasource()).getProvider();
        assertThat(detailProvider.getUrl(), is("n2o/data/testStandardPageDependency/detail"));
        Map<String, ModelLink> detailQueryMapping = page.getDatasources().get(detail.getDatasource()).getProvider().getQueryMapping();
        assertThat(detailQueryMapping.get("detail_parent_id").getParam(), is("detail_parent_id"));
        assertThat(detailQueryMapping.get("detail_parent_id").getBindLink(), is("models.resolve['testStandardPageDependency_master']"));
        assertThat(detailQueryMapping.get("detail_parent_id").getValue(), is("`id`"));
        ClientDataProvider panelProvider = page.getDatasources().get(panel.getDatasource()).getProvider();
        assertThat(panelProvider.getUrl(), is("n2o/data/testStandardPageDependency/panel1"));
        Map<String, ModelLink> panelQueryMapping = page.getDatasources().get(panel.getDatasource()).getProvider().getQueryMapping();
        assertThat(panelQueryMapping.get("panel1_parent_id").getParam(), is("panel1_parent_id"));
        assertThat(panelQueryMapping.get("panel1_parent_id").getBindLink(), is("models.resolve['testStandardPageDependency_detail']"));
        assertThat(panelQueryMapping.get("panel1_parent_id").getValue(), is("`parent.id`"));

        assertThat(((QueryContext) route("/testStandardPageDependency/detail", CompiledQuery.class))
                .getFilters().size(), is(1));
        assertThat(((QueryContext) route("/testStandardPageDependency/panel1", CompiledQuery.class))
                .getFilters().size(), is(1));

        //Условия видимости виджетов
        assertThat(detail.getDependency().getVisible().get(0).getOn(), is("models.resolve['testStandardPageDependency_master']"));
        assertThat(detail.getDependency().getVisible().get(0).getCondition(), is("parent.id == 1"));

       /* fixme //проверим что у кнопки delete родительский pathmapping скопировался
        assertThat(((InvokeAction) panel.getToolbar().getButton("delete").getAction()).getPayload().getDataProvider().getPathMapping()
                        .get("testStandardPageDependency_master_id").getBindLink(),
                is("models.resolve['testStandardPageDependency_master'].id"));*/
    }

    @Test
    public void preFilters() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/page/testStandardPageDependency.query.xml",
                "net/n2oapp/framework/config/metadata/compile/page/testWidgetPrefilters.page.xml")
                .get(new PageContext("testWidgetPrefilters"));

        ClientDataProvider detail1Ds = page.getDatasources().get("testWidgetPrefilters_detail1").getProvider();
        assertThat(detail1Ds.getQueryMapping().get("detail1_parent_id").getBindLink(), is("models.resolve['testWidgetPrefilters_master1']"));
        assertThat(detail1Ds.getQueryMapping().get("detail1_parent_id").getValue(), is("`id`"));
        assertThat(detail1Ds.getQueryMapping().get("detail1_genders_id").getValue(), is(Arrays.asList(1, 2)));
        assertThat(detail1Ds.getQueryMapping().get("nameParam").getBindLink(), nullValue());
        assertThat(detail1Ds.getQueryMapping().get("nameParam").getValue(), is("test"));

        QueryContext detail1QueryCtx = (QueryContext) route("/testWidgetPrefilters/detail1", CompiledQuery.class);
        assertThat(detail1QueryCtx.getFilters().size(), is(3));
        assertThat(detail1QueryCtx.getFilters().stream().map(Filter::getParam).collect(Collectors.toList()), hasItems("nameParam", "detail1_parent_id", "detail1_genders_id"));

        ClientDataProvider detail2Ds = page.getDatasources().get("testWidgetPrefilters_detail2").getProvider();
        assertThat(detail2Ds.getQueryMapping().get("detail2_name").getBindLink(), is("models.filter['testWidgetPrefilters_master2']"));
        assertThat(detail2Ds.getQueryMapping().get("detail2_name").getValue(), is("`name`"));
        assertThat(detail2Ds.getQueryMapping().get("detail2_genders_id").getBindLink(), is("models.filter['testWidgetPrefilters_master2']"));
        assertThat(detail2Ds.getQueryMapping().get("detail2_genders_id").getValue(), is("`genders.map(function(t){return t.id})`"));

        QueryContext detail2QueryCtx = (QueryContext) route("/testWidgetPrefilters/detail2", CompiledQuery.class);
        assertThat(detail2QueryCtx.getFilters().size(), is(2));
        assertThat(detail2QueryCtx.getFilters().stream().map(Filter::getParam).collect(Collectors.toList()), hasItems("detail2_name", "detail2_genders_id"));
    }


    /**
     * Если в роуте виджета есть параметры от предыдущих виджетов,
     * то этот виджет должен иметь ссылки на эти параметры
     */
    @Test
    public void testChainFetching() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/page/testStandardPageDependency.query.xml",
                "net/n2oapp/framework/config/metadata/compile/widgets/testChainWidgetFetching.page.xml")
                .get(new PageContext("testChainWidgetFetching"));

        assertThat(page.getDatasources().get("form").getProvider().getPathMapping().size(), is(1));
        assertThat(page.getDatasources().get("form").getProvider().getPathMapping().get("param1").normalizeLink(), is("models.resolve['table'].id"));

//        deprecated
//        assertThat(page.getDatasources().get("form2").getProvider().getPathMapping().size(), is(2));
//        assertThat(page.getDatasources().get("form2").getProvider().getPathMapping().get("param1").normalizeLink(), is("models.resolve['table'].id"));
//        assertThat(page.getDatasources().get("form2").getProvider().getPathMapping().get("param2").normalizeLink(), is("models.resolve['form'].id"));
//
//        assertThat(page.getDatasources().get("form3").getProvider().getPathMapping().size(), is(3));
//        assertThat(page.getDatasources().get("form3").getProvider().getPathMapping().get("param1").normalizeLink(), is("models.resolve['table'].id"));
//        assertThat(page.getDatasources().get("form3").getProvider().getPathMapping().get("param2").normalizeLink(), is("models.resolve['form'].id"));
//        assertThat(page.getDatasources().get("form3").getProvider().getPathMapping().get("param3").normalizeLink(), is("models.resolve['form2'].id"));
    }

    @Test(expected = ReferentialIntegrityViolationException.class)
    public void validateObjectIdForMainWidget() {
        PageContext validateObjectIdForMainWidget = new PageContext("testStandardPageObject");
        validateObjectIdForMainWidget.setSubmitOperationId("test");
        compile("net/n2oapp/framework/config/metadata/compile/page/testStandardPageObject.page.xml")
                .get(validateObjectIdForMainWidget);

    }

    @Test
    public void testPageTitle() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/page/testStandardPageTitle.page.xml")
                .get(new PageContext("testStandardPageTitle"));
        assertThat(page.getPageProperty().getTitle(), is("title"));
        assertThat(page.getPageProperty().getHtmlTitle(), is("tab title"));
    }

    @Test
    public void testPageTitleInModal() {
        StandardPage page = (StandardPage) compile("net/n2oapp/framework/config/metadata/compile/page/testStandardPageModalTitle.page.xml")
                .get(new ModalPageContext("testStandardPageModalTitle", "/modal"));
        assertThat(page.getPageProperty().getTitle(), nullValue());
        assertThat(page.getPageProperty().getModalHeaderTitle(), is("testPage"));
    }
}