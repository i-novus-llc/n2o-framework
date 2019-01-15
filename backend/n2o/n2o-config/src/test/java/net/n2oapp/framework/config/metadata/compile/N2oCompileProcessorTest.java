package net.n2oapp.framework.config.metadata.compile;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.PageContext;
import net.n2oapp.framework.config.test.N2oTestBase;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;

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
        ModelLink link = processor.resolveLink(testML);
        assertThat(link.getBindLink(), nullValue());
        assertThat(link.getValue(), is("testValue"));
        //проверяем, что замена не произошла
        assertThat(processor.resolveLink(new ModelLink(ReduxModel.RESOLVE, "widgetId")).getBindLink(), is("models.resolve['widgetId']"));
    }

    @Test
    public void testResolveUrl() {
        PageContext context = new PageContext("test");
        DataSet data = new DataSet();
        data.put("param1", "testValue");
        Map<String, ModelLink> pathParams = new HashMap<>();
        pathParams.put("param1", new ModelLink("param1value"));
        Map<String, ModelLink> queryParams = new HashMap<>();
        queryParams.put("paramQ1", new ModelLink("paramQ1"));
        N2oCompileProcessor processor = new N2oCompileProcessor(builder.getEnvironment(), context, data);
        String url = processor.resolveUrl("/p/w/:param1/a?paramQ1=:paramQ1", pathParams, queryParams);
        assertThat(url, is("/p/w/:param1/a?paramQ1=:paramQ1"));
        assertThat(pathParams.size(), is(1));
        assertThat(queryParams.size(), is(1));

        context = new PageContext("test");
        context.setQueryRouteMapping(queryParams);
        context.setPathRouteMapping(pathParams);
        processor = new N2oCompileProcessor(builder.getEnvironment(), context, data);
        url = processor.resolveUrl("/p/w/:param1/a?paramQ1=:paramQ1", pathParams, queryParams);
        assertThat(url, is("/p/w/testValue/a?paramQ1=:paramQ1"));
        assertThat(pathParams.size(), is(0));
        assertThat(queryParams.size(), is(1));

        data.put("paramQ1", "testValueQ");
        processor = new N2oCompileProcessor(builder.getEnvironment(), context, data);
        url = processor.resolveUrl("/p/w/:param1/a?paramQ1=:paramQ1", pathParams, queryParams);
        assertThat(url, is("/p/w/testValue/a?paramQ1=testValueQ"));
        assertThat(pathParams.size(), is(0));
        assertThat(queryParams.size(), is(0));
    }

    @Test
    public void testResolveText() {
        PageContext context = new PageContext("test");
        Map<String, ModelLink> routeInfos = new HashMap<>();
        ModelLink value = new ModelLink(ReduxModel.RESOLVE, "widgetId");
        value.setFieldValue("name");
        routeInfos.put("paramName", value);
        context.setQueryRouteMapping(routeInfos);
        DataSet data = new DataSet();
        data.put("paramName", "Joe");
        N2oCompileProcessor processor = new N2oCompileProcessor(builder.getEnvironment(), context, data);
        // все необходимые данные есть и плейсхолдер заменился заменился
        ModelLink testML = new ModelLink(ReduxModel.RESOLVE, "widgetId");
        String resultText = processor.resolveText("Hello, {name}", testML);
        assertThat(resultText, is("Hello, Joe"));
        // нет подходящего по widgetId
        resultText = processor.resolveText("Hello, {name}", new ModelLink(ReduxModel.RESOLVE, "otherWidgetId"));
        assertThat(resultText, is("Hello, {name}"));
        // нет подходящего по model
        resultText = processor.resolveText("Hello, {name}", new ModelLink(ReduxModel.FILTER, "widgetId"));
        assertThat(resultText, is("Hello, {name}"));
        // нет подходящего значения в data
        processor = new N2oCompileProcessor(builder.getEnvironment(), context, new DataSet());
        resultText = processor.resolveText("Hello, {name}", testML);
        assertThat(resultText, is("Hello, {name}"));



    }

}
