package net.n2oapp.framework.config.metadata.compile;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.local.view.widget.util.SubModelQuery;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.compile.pipeline.N2oEnvironment;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.test.N2oTestBase;
import net.n2oapp.framework.config.util.N2oSubModelsProcessor;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

public class N2oCompileProcessorTest extends N2oTestBase {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
    }

    @Test
    public void testResolveLink() {
        PageContext context = new PageContext("test");
        Map<String, ModelLink> routeInfos = new HashMap<>();
        ModelLink value = new ModelLink(ReduxModel.RESOLVE, "widgetId");
        value.setValue("`testField`");
        routeInfos.put("paramName", value);
        context.setQueryRouteMapping(routeInfos);
        DataSet data = new DataSet();
        data.put("paramName", "testValue");
        N2oCompileProcessor processor = new N2oCompileProcessor(builder.getEnvironment(), context, data);
        ModelLink testML = new ModelLink(ReduxModel.RESOLVE, "widgetId");
        testML.setValue("`testField`");
        //проверяем, что заменился линк
        processor.resolveLink(testML);
//        assertThat(testML.getBindLink(), nullValue());todo может можно оставить bindLink с конктантой?
        assertThat(testML.getValue(), is("testValue"));
        testML = new ModelLink(ReduxModel.RESOLVE, "widgetId");
        processor.resolveLink(testML);
        //проверяем, что замена не произошла
        assertThat(testML.getBindLink(), is("models.resolve['widgetId']"));
    }

    @Test
    public void testResolveText() {
        PageContext context = new PageContext("test");
        Map<String, ModelLink> queryMapping = new HashMap<>();
        ModelLink nameLink = new ModelLink(ReduxModel.RESOLVE, "widgetId");
        nameLink.setFieldValue("name");
        queryMapping.put("param", nameLink);
        context.setQueryRouteMapping(queryMapping);
        DataSet data = new DataSet();
        data.put("param", "Joe");
        N2oCompileProcessor processor = new N2oCompileProcessor(builder.getEnvironment(), context, data);

        // совпадают модель и виджет
        String resultText = processor.resolveText("Hello, {name}", new ModelLink(ReduxModel.RESOLVE, "widgetId"));
        assertThat(resultText, is("Hello, Joe"));

        // совпадают модель и виджет и поле
        resultText = processor.resolveText("Hello, {name}", new ModelLink(ReduxModel.RESOLVE, "widgetId", "name"));
        assertThat(resultText, is("Hello, Joe"));

        // не совпадает поле (главное чтобы совпал виджет и модель)
        resultText = processor.resolveText("Hello, {name}", new ModelLink(ReduxModel.RESOLVE, "widgetId", "name2"));
        assertThat(resultText, is("Hello, Joe"));

        // не совпадает виджет
        resultText = processor.resolveText("Hello, {name}", new ModelLink(ReduxModel.RESOLVE, "otherWidgetId"));
        assertThat(resultText, is("Hello, {name}"));

        // не совпадает модель
        resultText = processor.resolveText("Hello, {name}", new ModelLink(ReduxModel.FILTER, "widgetId"));
        assertThat(resultText, is("Hello, {name}"));

        // нет данных (на null или пустую строку не заменяется)
        processor = new N2oCompileProcessor(builder.getEnvironment(), context, new DataSet());
        resultText = processor.resolveText("Hello, {name}", new ModelLink(ReduxModel.RESOLVE, "widgetId", "name"));
        assertThat(resultText, is("Hello, {name}"));
    }

    @Test
    public void testResolveTextSubModels() {
        N2oSubModelsProcessor subModelsProcessor = mock(N2oSubModelsProcessor.class);
        ((N2oEnvironment) builder.getEnvironment()).setSubModelsProcessor(subModelsProcessor);

        doAnswer(invocation -> {
            DataSet data = invocation.getArgument(1);
            data.put("name", "Joe");
            return null;
        }).when(subModelsProcessor).executeSubModels(anyList(), any());


        PageContext context = new PageContext("test");
        Map<String, ModelLink> queryMapping = new HashMap<>();
        ModelLink linkId = new ModelLink(ReduxModel.RESOLVE, "widget1", "id");
        linkId.setSubModelQuery(new SubModelQuery("query1"));
        queryMapping.put("param_id", linkId);
        context.setQueryRouteMapping(queryMapping);
        DataSet data = new DataSet();
        data.put("paramId", 123);
        N2oCompileProcessor processor = new N2oCompileProcessor(builder.getEnvironment(), context, data);

        // совпадают модель и виджет
        String resultText = processor.resolveText("Hello, {name}", linkId);
        assertThat(resultText, is("Hello, Joe"));
    }

    @Test
    public void testResolveUrl() {
        PageContext context = new PageContext("test");
        DataSet data = new DataSet();
        data.put("path_param", "test1");
        data.put("query_param", "test2");
        N2oCompileProcessor processor = new N2oCompileProcessor(builder.getEnvironment(), context, data);
        String url = processor.resolveUrl("/a/:path_param/b?query_param=:query_param");
        assertThat(url, is("/a/test1/b?query_param=test2"));
    }

    @Test
    public void testResolveUrlWithMapping() {
        //Константы в mapping
        PageContext context = new PageContext("test");
        DataSet data = new DataSet();
        data.put("param1", "testValue");
        Map<String, ModelLink> pathParams = new HashMap<>();
        pathParams.put("param1", new ModelLink("param1value"));
        Map<String, ModelLink> queryParams = new HashMap<>();
        queryParams.put("paramQ1", new ModelLink("param2Value"));
        N2oCompileProcessor processor = new N2oCompileProcessor(builder.getEnvironment(), context, data);

        String url = processor.resolveUrl("/p/w/:param1/a?paramQ1=:paramQ1", pathParams, queryParams);
        assertThat(url, is("/p/w/param1value/a?paramQ1=param2Value"));
        assertThat(pathParams.size(), is(0));
        assertThat(queryParams.size(), is(0));

        //Данные в контексте path mapping
        data = new DataSet();
        data.put("param1", "testValue");
        pathParams = new HashMap<>();
        pathParams.put("param1", new ModelLink(ReduxModel.RESOLVE, "wgt", "field1"));
        queryParams = new HashMap<>();
        queryParams.put("param2", new ModelLink(ReduxModel.RESOLVE, "wgt", "field2"));
        context = new PageContext("test");
        context.setQueryRouteMapping(queryParams);
        context.setPathRouteMapping(pathParams);
        processor = new N2oCompileProcessor(builder.getEnvironment(), context, data);

        url = processor.resolveUrl("/p/w/:param1/a?param2=:param2", pathParams, queryParams);
        assertThat(url, is("/p/w/testValue/a?param2=:param2"));
        assertThat(pathParams.size(), is(0));
        assertThat(queryParams.size(), is(1));


        //Данные в контексте query mapping
        data = new DataSet();
        data.put("param2", "testValue2");
        pathParams = new HashMap<>();
        pathParams.put("param1", new ModelLink(ReduxModel.RESOLVE, "wgt", "field1"));
        queryParams = new HashMap<>();
        queryParams.put("param2", new ModelLink(ReduxModel.RESOLVE, "wgt", "field2"));
        context = new PageContext("test");
        context.setQueryRouteMapping(queryParams);
        context.setPathRouteMapping(pathParams);
        processor = new N2oCompileProcessor(builder.getEnvironment(), context, data);
        url = processor.resolveUrl("/p/w/:param1/a?param2=:param2", pathParams, queryParams);
        assertThat(url, is("/p/w/:param1/a?param2=testValue2"));
        assertThat(pathParams.size(), is(1));
        assertThat(queryParams.size(), is(0));
    }

    @Test
    public void testResolveUrlWithLink() {
        PageContext context = new PageContext("test");

        Map<String, ModelLink> routeInfos = new HashMap<>();
        ModelLink value = new ModelLink(ReduxModel.RESOLVE, "widgetId");
        value.setValue("`versionId`");
        routeInfos.put("versionId", value);
        context.setQueryRouteMapping(routeInfos);
        Map<String, ModelLink> pathRouteInfos = new HashMap<>();
        ModelLink pathValue = new ModelLink(ReduxModel.RESOLVE, "widgetId", "id");
        pathRouteInfos.put("w_id", pathValue);
        context.setPathRouteMapping(pathRouteInfos);

        DataSet data = new DataSet();
        data.put("versionId", "2");
        data.put("w_id", "1");

        N2oCompileProcessor processor = new N2oCompileProcessor(builder.getEnvironment(), context, data);
        // все необходимые данные есть и плейсхолдер заменился заменился
        ModelLink testML = new ModelLink(ReduxModel.RESOLVE, "widgetId");
        String resultText = processor.resolveUrl("http://page/widget/:w_id/action?versionId=:versionId", testML);
        assertThat(resultText, is("http://page/widget/1/action?versionId=2"));
        // нет подходящего по widgetId
        resultText = processor.resolveUrl("http://page/widget/:w_id/action?versionId=:versionId", new ModelLink(ReduxModel.RESOLVE, "otherWidgetId"));
        assertThat(resultText, is("http://page/widget/:w_id/action?versionId=:versionId"));
        // нет подходящего по model
        resultText = processor.resolveText("http://page/widget/:w_id/action?versionId=:versionId", new ModelLink(ReduxModel.FILTER, "widgetId"));
        assertThat(resultText, is("http://page/widget/:w_id/action?versionId=:versionId"));
        // нет подходящего значения в data
        processor = new N2oCompileProcessor(builder.getEnvironment(), context, new DataSet());
        resultText = processor.resolveText("http://page/widget/:w_id/action?versionId=:versionId", testML);
        assertThat(resultText, is("http://page/widget/:w_id/action?versionId=:versionId"));
    }

    @Test
    public void resolveJS() {
        N2oCompileProcessor processor = new N2oCompileProcessor(builder.getEnvironment());
        assertThat(processor.resolveJS("Hello {name}", String.class), is("`'Hello '+name`"));
        assertThat(processor.resolveJS("{name}", String.class), is("`name`"));
        assertThat(processor.resolveJS("true", Boolean.class), is(true));
        assertThat(processor.resolveJS("false", Boolean.class), is(false));
    }
}
