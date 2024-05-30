package net.n2oapp.framework.config.metadata.compile.page;

import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.api.Criteria;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.datasource.StandardDatasource;
import net.n2oapp.framework.api.metadata.local.view.widget.util.SubModelQuery;
import net.n2oapp.framework.api.metadata.meta.Breadcrumb;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.action.modal.show_modal.ShowModal;
import net.n2oapp.framework.api.metadata.meta.control.DefaultValues;
import net.n2oapp.framework.api.metadata.meta.event.OnChangeEvent;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.metadata.meta.page.SimplePage;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileBindTerminalPipeline;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import net.n2oapp.framework.config.util.N2oSubModelsProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

class PageBinderTest extends SourceCompileTestBase {
    @Override
    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.getEnvironment().getContextProcessor().set("test", "Test");
        builder.packs(new N2oAllDataPack(), new N2oFieldSetsPack(), new N2oControlsPack(), new N2oPagesPack(),
                new N2oWidgetsPack(), new N2oRegionsPack(), new N2oCellsPack(), new N2oActionsPack());
    }

    @Test
    void fieldsResolve() {
        Page page = bind("net/n2oapp/framework/config/metadata/compile/page/testPageBinders.page.xml")
                .get(new PageContext("testPageBinders"), new DataSet());

        assertThat(page.getModels().get("resolve['testPageBinders_main'].name").getValue(), is("Test"));
        assertThat(page.getModels().get("resolve['testPageBinders_main'].gender").getBindLink(), is("models.resolve['testPageBinders_main'].gender"));
        assertThat(((DefaultValues) page.getModels().get("resolve['testPageBinders_main'].gender").getValue()).getValues().get("id"), is("Test"));
        assertThat(page.getModels().get("resolve['testPageBinders_main'].birthday").getBindLink(), is("models.resolve['testPageBinders_main']"));
        assertThat(((DefaultValues) page.getModels().get("resolve['testPageBinders_main'].birthday").getValue()).getValues().get("begin"), is("01.11.2018"));
        assertThat(((DefaultValues) page.getModels().get("resolve['testPageBinders_main'].birthday").getValue()).getValues().get("end"), is("11.11.2018"));
        assertThat(page.getModels().get("resolve['testPageBinders_main'].intervalTest.end").getValue(), is(156));
        assertThat(page.getModels().get("resolve['testPageBinders_main'].intervalTest.begin") == null, is(true));
        assertThat(page.getModels().get("resolve['testPageBinders_main'].type").getParam(), is("param_type"));
    }

    /**
     * Разрешение имени страницы через параметр в path
     */
    @Test
    void pageNameResolve() {
        PageContext context = new PageContext("testPageBinders", "/page/:name_param/view");
        context.setParentModelLinks(Collections.singletonList(new ModelLink(ReduxModel.resolve, "page_master")));
        context.setParentClientWidgetId("page_master");
        context.setParentRoute("/page");
        ModelLink modelLink = new ModelLink(ReduxModel.resolve, "page_master");
        modelLink.setValue("`name`");
        context.setPathRouteMapping(Collections.singletonMap("name_param", modelLink));
        Page page = bind("net/n2oapp/framework/config/metadata/compile/page/testPageBinders.page.xml")
                .get(context, new DataSet().add("name_param", "Joe").add("param_type","22"));
        assertThat(page.getPageProperty().getTitle(), is("`'Hello, Joe '+code`"));
        assertThat(page.getModels().get("resolve['testPageBinders_main'].type") == null, is(true));
    }

    /**
     * Если не удалось разрешить ссылку в биндере, то пробрасываем на клиент в JS виде
     */
    @Test
    void pageNameClientResolvingForwarding() {
        Page page = bind("net/n2oapp/framework/config/metadata/compile/page/testPageNameClientResolvingForwarding.page.xml")
                .get(new PageContext("testPageNameClientResolvingForwarding"), new DataSet());
        assertThat(page.getPageProperty().getTitle(), is("`'title '+main`"));
        assertThat(page.getPageProperty().getHtmlTitle(), is("`'title '+html`"));
    }

    /**
     * Разрешение имени страницы через процессор вложенных моделей в параметре в path
     */
    @Test
    void pageNameResolveSubModels() {
        N2oSubModelsProcessor subModelsProcessor = mock(N2oSubModelsProcessor.class);
        doAnswer(invocation -> {
            List<SubModelQuery> subModelQueries = invocation.getArgument(0);
            DataSet data = invocation.getArgument(1);
            if (!subModelQueries.isEmpty()
                    && "query1".equals(subModelQueries.get(0).getQueryId())
                    && data.get("id").equals(123)) {
                data.put("name", "Joe");
            }
            return null;
        }).when(subModelsProcessor).executeSubModels(anyList(), any());

        PageContext context = new PageContext("testPageBinders", "/page/:id_param/view");
        context.setParentModelLinks(Collections.singletonList(new ModelLink(ReduxModel.resolve, "page_master")));
        context.setParentClientWidgetId("page_master");
        context.setParentRoute("/page");
        ModelLink modelLink = new ModelLink(ReduxModel.resolve, "page_master", "id");
        modelLink.setSubModelQuery(new SubModelQuery("query1"));
        context.setPathRouteMapping(Collections.singletonMap("id_param", modelLink));
        context.setParentModelLinks(Collections.singletonList(modelLink));
        Page page = bind("net/n2oapp/framework/config/metadata/compile/page/testPageBinders.page.xml")
                .get(context, new DataSet().add("id_param", 123), subModelsProcessor);
        assertThat(page.getPageProperty().getTitle(), is("`'Hello, Joe '+code`"));
    }

    @Test
    void pageBreadcrumbResolve() {
        PageContext context = new PageContext("testPageBinders", "/page/:name_param/view");
        context.setParentModelLinks(Collections.singletonList(new ModelLink(ReduxModel.resolve, "page_master")));
        context.setParentClientWidgetId("page_master");
        context.setParentRoute("/page");
        ModelLink modelLink = new ModelLink(ReduxModel.resolve, "page_master");
        modelLink.setValue("`name`");
        context.setPathRouteMapping(Collections.singletonMap("name_param", modelLink));
        context.setBreadcrumbs(singletonList(new Breadcrumb("prev", "/page")));
        Page page = bind("net/n2oapp/framework/config/metadata/compile/page/testPageBinders.page.xml")
                .get(context, new DataSet().add("name_param", "Joe"));
        assertThat(page.getBreadcrumb().get(1).getLabel(), is("`'Hello, Joe '+code`"));
    }

    /**
     * Разрешение вложенных моделей в фильтрах по url параметру
     */
    @Test
    void resolveTableFilterSubModels() {
        N2oSubModelsProcessor subModelsProcessor = mock(N2oSubModelsProcessor.class);

        //мок subModelProcessor. Докидывает name в данные
        doAnswer(invocation -> {
            List<SubModelQuery> subModelQueries = invocation.getArgument(0);
            DataSet data = invocation.getArgument(1);
            if (subModelQueries.get(0).isMulti()) {
                List<DataSet> list = (List<DataSet>) data.get(subModelQueries.get(0).getFullName());
                for (DataSet item : list) {
                    item.put("name", "test" + item.get("id"));
                }
            } else {
                data.put(subModelQueries.get(0).getFullName() + ".name", "test" + data.get(subModelQueries.get(0).getSubModel() + ".id"));
            }
            return null;
        }).when(subModelsProcessor).executeSubModels(anyList(), any());

        DataSet data = new DataSet();
        List<String> ids = new ArrayList<>();
        ids.add("1");
        ids.add("2");
        data.put("multiParam", ids);
        data.put("singleParam", "1");
        data.put("singleOptionParam", "1");
        data.put("multiFormParam", ids);
        data.put("singleFormParam", "1");
        data.put("singleOptionFormParam", "1");
        Page page = bind("net/n2oapp/framework/config/metadata/compile/page/submodels/testSubModels.page.xml",
                "net/n2oapp/framework/config/metadata/compile/page/submodels/testModel.query.xml",
                "net/n2oapp/framework/config/metadata/compile/page/submodels/testSubModel.query.xml")
                .get(new PageContext("testSubModels"), data, subModelsProcessor);
        //single фильтр по умолчанию
        assertThat(((DefaultValues) page.getModels().get("filter['testSubModels_w0'].testSingleDefault").getValue()).getValues().get("name"), is("test1"));
        //multi фильтр по умолчанию
        assertThat(((DefaultValues) ((List) page.getModels().get("filter['testSubModels_w0'].testMultiDefault").getValue()).get(0)).getValues().get("name"), is("test1"));
        //single фильтр c options(значение test1 из Mock)
        assertThat(((DefaultValues) page.getModels().get("filter['testSubModels_w1'].testSingleOptions").getValue()).getValues().get("name"), is("test1"));
        //single фильтр по URL
        assertThat(((DefaultValues) page.getModels().get("filter['testSubModels_w1'].testSingleUrl").getValue()).getValues().get("name"), is("test1"));
        //multi фильтр по URL
        assertThat(((DefaultValues) ((List) page.getModels().get("filter['testSubModels_w1'].testMultiUrl").getValue()).get(0)).getValues().get("name"), is("test1"));
        assertThat(((DefaultValues) ((List) page.getModels().get("filter['testSubModels_w1'].testMultiUrl").getValue()).get(1)).getValues().get("name"), is("test2"));
        //single поле по умолчанию
        assertThat(((DefaultValues) page.getModels().get("resolve['testSubModels_w2'].testSingle").getValue()).getValues().get("name"), is("test1"));
        //multi поле по умолчанию
        assertThat(((DefaultValues) ((List) page.getModels().get("resolve['testSubModels_w2'].testMulti").getValue()).get(0)).getValues().get("name"), is("test1"));
        data.put("w0_testSingleDefault_id", "2");
        data.put("w0_testMultiDefault_id", Arrays.asList("2"));
        //single поле из параметров
        assertThat(((DefaultValues) page.getModels().get("resolve['testSubModels_w3'].testSingleOptionsForm").getValue()).getValues().get("name"), is("test1"));
        //single поле с options из параметров
        assertThat(((DefaultValues) page.getModels().get("resolve['testSubModels_w3'].testSingleUrlForm").getValue()).getValues().get("name"), is("test1"));
        //multi поле из параметров
        assertThat(((DefaultValues) ((List) page.getModels().get("resolve['testSubModels_w3'].testMultiUrlForm").getValue()).get(0)).getValues().get("name"), is("test1"));
        assertThat(((DefaultValues) ((List) page.getModels().get("resolve['testSubModels_w3'].testMultiUrlForm").getValue()).get(1)).getValues().get("name"), is("test2"));


        data.put("w0_testSingleDefault_id", "2");
        data.put("w0_testMultiDefault_id", Arrays.asList("2"));
        page = bind("net/n2oapp/framework/config/metadata/compile/page/submodels/testSubModels.page.xml",
                "net/n2oapp/framework/config/metadata/compile/page/submodels/testModel.query.xml",
                "net/n2oapp/framework/config/metadata/compile/page/submodels/testSubModel.query.xml")
                .get(new PageContext("testSubModels"), data, subModelsProcessor);

        //Фильтры из URL перекрывают дефолтные значения
        assertThat(((DefaultValues) page.getModels().get("filter['testSubModels_w0'].testSingleDefault").getValue()).getValues().get("name"), is("test2"));
        assertThat(((DefaultValues) ((List) page.getModels().get("filter['testSubModels_w0'].testMultiDefault").getValue()).get(0)).getValues().get("name"), is("test2"));
        assertThat(((List) page.getModels().get("filter['testSubModels_w0'].testMultiDefault").getValue()).size(), is(1));
    }

    /**
     * Разрешение моделей ссылающихся на родительский виджет и родительскую страницу
     */
    @Test
    void refModelBinderTest() {
        DataSet data = new DataSet("id", 2);
        PageContext context = new PageContext("testRefModel", "/table/:id/modal");
        context.setClientPageId("table_modal");
        context.setParentClientPageId("_");
        context.setParentClientWidgetId("table");
        context.setParentRoute("/table/:id");
        context.setParentModelLinks(Collections.singletonList(new ModelLink(ReduxModel.resolve, "table", "id")));
        context.setPathRouteMapping(Map.of("id", new ModelLink(ReduxModel.resolve, "_table", "id")));
        Page page = bind("net/n2oapp/framework/config/metadata/compile/page/refModel/testRefModel.page.xml",
                "net/n2oapp/framework/config/metadata/compile/page/refModel/testRefModel.query.xml")
                .get(context, data);
        //ссылка на родительский виджет
        ModelLink mainId = page.getModels().get("resolve['table_modal_dependent'].mainId");
        assertThat(mainId.isObserve(), is(true));
        assertThat(mainId.getBindLink(), is("models.resolve['table_modal_main']"));
        assertThat(mainId.getValue(), is("`id`"));
        //ссылка на родительскую страницу
        ModelLink mainId2 = page.getModels().get("resolve['table_modal_dependent'].mainId2");
        assertThat(mainId2.getValue(), is(2));
    }

    /**
     * Разрешение моделей фильтров через выборку
     */
    @Test
    void defaultValuesQueryTest() {
        N2oSubModelsProcessor subModelsProcessor = mock(N2oSubModelsProcessor.class);
        PageContext context = new PageContext("testDefValQuery", "table");
        doAnswer(invocation -> new CollectionPage<>(1, singletonList(new DataSet("name", "test1")), new Criteria())).when(subModelsProcessor).getQueryResult(anyString(), any());
        Page page = bind("net/n2oapp/framework/config/metadata/compile/page/defaultValuesQuery/testDefValQuery.page.xml",
                "net/n2oapp/framework/config/metadata/compile/page/defaultValuesQuery/testDefValQuery.query.xml",
                "net/n2oapp/framework/config/metadata/compile/page/defaultValuesQuery/default.query.xml")
                .get(context, new DataSet(), subModelsProcessor);
        //Разрешится значение из выборки
        ModelLink name = page.getModels().get("filter['table_w1'].name");
        assertThat(name.getValue(), is("test1"));

        doAnswer(invocation -> new CollectionPage<>(1, singletonList(new DataSet()), new Criteria())).when(subModelsProcessor).getQueryResult(anyString(), any());
        page = bind("net/n2oapp/framework/config/metadata/compile/page/defaultValuesQuery/testDefValQuery.page.xml",
                "net/n2oapp/framework/config/metadata/compile/page/defaultValuesQuery/testDefValQuery.query.xml",
                "net/n2oapp/framework/config/metadata/compile/page/defaultValuesQuery/default.query.xml")
                .get(context, new DataSet(), subModelsProcessor);

        //Разрешится значение по умолчанию на поле, т.к. в выборке не пришло значение
        name = page.getModels().get("filter['table_w1'].name");
        assertThat(name.getValue(), is("test2"));

       /* todo NNO-7523
        doAnswer(invocation -> new CollectionPage<>(1, singletonList(new DataSet("name", "test1")), new Criteria())).when(subModelsProcessor).getQueryResult(anyString(), any());
        page = bind("net/n2oapp/framework/config/metadata/compile/page/defaultValuesQuery/testDefValQuery.page.xml",
                "net/n2oapp/framework/config/metadata/compile/page/defaultValuesQuery/testDefValQuery.query.xml",
                "net/n2oapp/framework/config/metadata/compile/page/defaultValuesQuery/default.query.xml")
                .get(context, new DataSet("name", "test3"), subModelsProcessor);

        //Разрешится значение из запроса, т.к. оно самое приоритетное
        name = page.getModels().get("filter['table_main'].name");
        assertThat(name.getValue(), is("test3"));*/
    }

    /**
     * Проверка резолва ссылок в datasource
     */
    @Test
    void autoSubmit() {
        ReadCompileBindTerminalPipeline pipeline = bind("net/n2oapp/framework/config/metadata/compile/page/testDatasourceRouteBinder.page.xml",
                "net/n2oapp/framework/config/metadata/compile/object/utAction.object.xml",
                "net/n2oapp/framework/config/metadata/compile/stub/utBlank2.query.xml");
        PageContext context = new PageContext("testDatasourceRouteBinder", "/p/w/:param0/form");
        SimplePage page = (SimplePage) pipeline.get(context, new DataSet().add("param0", "1"));
        assertThat(((StandardDatasource) page.getDatasources().get("p_w_form_w1")).getSubmit().getUrl(), containsString("/p/w/1/form"));
        assertThat(((StandardDatasource) page.getDatasources().get("p_w_form_w1")).getProvider().getUrl(), containsString("/p/w/1/form"));
    }

    @Test
    void eventsBinder() {
        ReadCompileBindTerminalPipeline pipeline =bind("net/n2oapp/framework/config/metadata/compile/page/testEventActionBinder.page.xml",
                "net/n2oapp/framework/config/metadata/compile/page/submodels/testSubModel.query.xml");
        PageContext context = new PageContext("testEventActionBinder", "/p/w/:id/view");
        StandardPage page = (StandardPage) pipeline.get(context, new DataSet().add("id", "3"));
        assertThat(((ShowModal) ((OnChangeEvent) page.getEvents().get(0)).getAction()).getPayload().getPageUrl(), is("/p/w/3/view/modal"));
    }
}
