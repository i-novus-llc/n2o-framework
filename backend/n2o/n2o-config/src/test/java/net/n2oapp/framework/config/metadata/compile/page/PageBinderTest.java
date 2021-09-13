package net.n2oapp.framework.config.metadata.compile.page;

import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.api.Criteria;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.local.view.widget.util.SubModelQuery;
import net.n2oapp.framework.api.metadata.meta.Breadcrumb;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.page.Page;
import net.n2oapp.framework.api.metadata.meta.control.DefaultValues;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import net.n2oapp.framework.config.util.N2oSubModelsProcessor;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class PageBinderTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.getEnvironment().getContextProcessor().set("test", "Test");
        builder.packs(new N2oAllDataPack(), new N2oFieldSetsPack(), new N2oControlsPack(), new N2oPagesPack(),
                new N2oWidgetsPack(), new N2oRegionsPack(), new N2oCellsPack());
    }

    @Test
    public void fieldsResolve() {
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
    public void pageNameResolve() {
        PageContext context = new PageContext("testPageBinders", "/page/:name_param/view");
        context.setParentModelLink(new ModelLink(ReduxModel.RESOLVE, "page_master"));
        context.setParentClientWidgetId("page_master");
        context.setParentRoute("/page");
        ModelLink modelLink = new ModelLink(ReduxModel.RESOLVE, "page_master");
        modelLink.setValue("`name`");
        context.setPathRouteMapping(Collections.singletonMap("name_param", modelLink));
        Page page = bind("net/n2oapp/framework/config/metadata/compile/page/testPageBinders.page.xml")
                .get(context, new DataSet().add("name_param", "Joe").add("param_type","22"));
        assertThat(page.getPageProperty().getTitle(), is("Hello, Joe"));
        assertThat(page.getModels().get("resolve['testPageBinders_main'].type") == null, is(true));
    }

    /**
     * Разрешение имени страницы через процессор вложенных моделей в параметре в path
     */
    @Test
    public void pageNameResolveSubModels() {
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
        }).when(subModelsProcessor).executeSubModels(anyListOf(SubModelQuery.class), anyObject());

        PageContext context = new PageContext("testPageBinders", "/page/:id_param/view");
        context.setParentModelLink(new ModelLink(ReduxModel.RESOLVE, "page_master"));
        context.setParentClientWidgetId("page_master");
        context.setParentRoute("/page");
        ModelLink modelLink = new ModelLink(ReduxModel.RESOLVE, "page_master", "id");
        modelLink.setSubModelQuery(new SubModelQuery("query1"));
        context.setPathRouteMapping(Collections.singletonMap("id_param", modelLink));
        context.setParentModelLink(modelLink);
        Page page = bind("net/n2oapp/framework/config/metadata/compile/page/testPageBinders.page.xml")
                .get(context, new DataSet().add("id_param", 123), subModelsProcessor);
        assertThat(page.getPageProperty().getTitle(), is("Hello, Joe"));
    }

    @Test
    public void pageBreadcrumbResolve() {
        PageContext context = new PageContext("testPageBinders", "/page/:name_param/view");
        context.setParentModelLink(new ModelLink(ReduxModel.RESOLVE, "page_master"));
        context.setParentClientWidgetId("page_master");
        context.setParentRoute("/page");
        ModelLink modelLink = new ModelLink(ReduxModel.RESOLVE, "page_master");
        modelLink.setValue("`name`");
        context.setPathRouteMapping(Collections.singletonMap("name_param", modelLink));
        context.setBreadcrumbs(singletonList(new Breadcrumb("prev", "/page")));
        Page page = bind("net/n2oapp/framework/config/metadata/compile/page/testPageBinders.page.xml")
                .get(context, new DataSet().add("name_param", "Joe"));
        assertThat(page.getBreadcrumb().get(1).getLabel(), is("Hello, Joe"));
    }

    /**
     * Разрешение вложенных моделей в фильтрах по url параметру
     */
    @Test
    public void resolveTableFilterSubModels() {
        N2oSubModelsProcessor subModelsProcessor = mock(N2oSubModelsProcessor.class);

        //мок subModelProcessor. Докидывает name в данные
        doAnswer(invocation -> {
            List<SubModelQuery> subModelQueries = invocation.getArgument(0);
            DataSet data = invocation.getArgument(1);
            if (subModelQueries.get(0).getMulti()) {
                List<DataSet> list = (List<DataSet>) data.get(subModelQueries.get(0).getSubModel());
                for (DataSet item : list) {
                    item.put("name", "test" + item.get("id"));
                }
            } else {
                data.put(subModelQueries.get(0).getSubModel() + ".name", "test" + data.get(subModelQueries.get(0).getSubModel() + ".id"));
            }
            return null;
        }).when(subModelsProcessor).executeSubModels(anyListOf(SubModelQuery.class), anyObject());

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
    public void refModelBinderTest() {
        DataSet data = new DataSet("id", 2);
        PageContext context = new PageContext("testRefModel", "/table/:id/modal");
        context.setParentWidgetId("table");
        context.setClientPageId("table_modal");
        context.setParentClientPageId("_");
        context.setParentClientWidgetId("table");
        context.setParentRoute("/table/:id");
        context.setParentModelLink(new ModelLink(ReduxModel.RESOLVE, "table", "id"));
        context.setPathRouteMapping(Map.of("id", new ModelLink(ReduxModel.RESOLVE, "table", "id")));
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
    public void defaultValuesQueryTest() {
        N2oSubModelsProcessor subModelsProcessor = mock(N2oSubModelsProcessor.class);
        PageContext context = new PageContext("testDefValQuery", "table");
        doAnswer(invocation -> new CollectionPage<>(1, singletonList(new DataSet("name", "test1")), new Criteria())).when(subModelsProcessor).getQueryResult(anyString(), any());
        Page page = bind("net/n2oapp/framework/config/metadata/compile/page/defaultValuesQuery/testDefValQuery.page.xml",
                "net/n2oapp/framework/config/metadata/compile/page/defaultValuesQuery/testDefValQuery.query.xml",
                "net/n2oapp/framework/config/metadata/compile/page/defaultValuesQuery/default.query.xml")
                .get(context, new DataSet(), subModelsProcessor);
        //Разрешится значение из выборки
        ModelLink name = page.getModels().get("filter['table_main'].name");
        assertThat(name.getValue(), is("test1"));

        doAnswer(invocation -> new CollectionPage<>(1, singletonList(new DataSet()), new Criteria())).when(subModelsProcessor).getQueryResult(anyString(), any());
        page = bind("net/n2oapp/framework/config/metadata/compile/page/defaultValuesQuery/testDefValQuery.page.xml",
                "net/n2oapp/framework/config/metadata/compile/page/defaultValuesQuery/testDefValQuery.query.xml",
                "net/n2oapp/framework/config/metadata/compile/page/defaultValuesQuery/default.query.xml")
                .get(context, new DataSet(), subModelsProcessor);

        //Разрешится значение по умолчанию на поле, т.к. в выборке не пришло значение
        name = page.getModels().get("filter['table_main'].name");
        assertThat(name.getValue(), is("test2"));

        doAnswer(invocation -> new CollectionPage<>(1, singletonList(new DataSet("name", "test1")), new Criteria())).when(subModelsProcessor).getQueryResult(anyString(), any());
        page = bind("net/n2oapp/framework/config/metadata/compile/page/defaultValuesQuery/testDefValQuery.page.xml",
                "net/n2oapp/framework/config/metadata/compile/page/defaultValuesQuery/testDefValQuery.query.xml",
                "net/n2oapp/framework/config/metadata/compile/page/defaultValuesQuery/default.query.xml")
                .get(context, new DataSet("name", "test3"), subModelsProcessor);

        //Разрешится значение из запроса, т.к. оно самое приоритетное
        name = page.getModels().get("filter['table_main'].name");
        assertThat(name.getValue(), is("test3"));
    }
}
