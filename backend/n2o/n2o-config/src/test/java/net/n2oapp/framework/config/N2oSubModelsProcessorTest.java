package net.n2oapp.framework.config;

import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.criteria.N2oPreparedCriteria;
import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.data.QueryProcessor;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.local.view.widget.util.SubModelQuery;
import net.n2oapp.framework.api.metadata.pipeline.PipelineFunction;
import net.n2oapp.framework.api.metadata.pipeline.ReadCompileBindTerminalPipeline;
import net.n2oapp.framework.api.util.N2oTestUtil;
import net.n2oapp.framework.config.compile.pipeline.N2oEnvironment;
import net.n2oapp.framework.config.util.N2oSubModelsProcessor;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.function.BiFunction;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

public class N2oSubModelsProcessorTest {

    private N2oSubModelsProcessor processor;

    @Before
    public void setUp() {
        QueryProcessor queryProcessor = mock(QueryProcessor.class);
        when(queryProcessor.executeOneSizeQuery(any(), any()))
                .thenReturn(new CollectionPage<>(1, Arrays.asList(new DataSet("label", "someLabel").add("someField", "someFieldValue").add("id", 1)), null));
        N2oEnvironment environment = mock(N2oEnvironment.class);
        PipelineFunction pipelineFunction = mock(PipelineFunction.class);
        ReadCompileBindTerminalPipeline pipeline = mock(ReadCompileBindTerminalPipeline.class);
        when(environment.getReadCompileBindTerminalPipelineFunction()).thenReturn(pipelineFunction);
        when(environment.getReadCompileBindTerminalPipelineFunction().apply(any())).thenReturn(pipeline);
        when(environment.getReadCompileBindTerminalPipelineFunction().apply(any()).get(any(), any())).thenReturn(new TestCompiledQuery("someQuery"));
        this.processor = new N2oSubModelsProcessor(queryProcessor, new DomainProcessor());
        this.processor.setEnvironment(environment);
    }

    @Test
    public void testSingleListSubModel() {
        //успех
        SubModelQuery subModelQuery = new SubModelQuery("gender", "someQuery", "id", "label", false, null);
        DataSet dataSet = new DataSet("gender.id", 1);
        processor.executeSubModels(Collections.singletonList(subModelQuery), dataSet);
        assert ((Map) dataSet.get("gender")).size() == 3;
        assert dataSet.get("gender.id").equals(1);
        assert dataSet.get("gender.label").equals("someLabel");
        assert dataSet.get("gender.someField").equals("someFieldValue");

        //label уже есть
        subModelQuery = new SubModelQuery("gender", "someQuery", "id", "label", false, null);
        dataSet = new DataSet("gender.id", 1).add("gender.label", "someLabel");
        processor.executeSubModels(Collections.singletonList(subModelQuery), dataSet);
        assert ((Map) dataSet.get("gender")).size() == 2;
        assert dataSet.get("gender.id").equals(1);
        assert dataSet.get("gender.label").equals("someLabel");

        //value == null
        subModelQuery = new SubModelQuery("gender", "someQuery", "id", "label", false, null);
        dataSet = new DataSet("gender.id", null);
        processor.executeSubModels(Collections.singletonList(subModelQuery), dataSet);
        assert ((Map) dataSet.get("gender")).size() == 1;
        assert dataSet.get("gender.id") == null;

    }

    @Test
    public void testSingleListSubModelError() {
        //в query нету поля для value
        SubModelQuery subModelQuery = new SubModelQuery("gender", "someQuery", "wrong", "label", false, null);
        DataSet dataSet = new DataSet("gender.wrong", 1);
        N2oTestUtil.assertOnException(() -> processor.executeSubModels(Collections.singletonList(subModelQuery), dataSet), RuntimeException.class);
    }


    @Test
    public void tesMultiListSubModel() {
        //успех
        SubModelQuery subModelQuery = new SubModelQuery("gender", "someQuery", "id", "label", true, null);
        DataSet dataSet = new DataSet("gender[0].id", 1).add("gender[1].id", 2);
        processor.executeSubModels(Collections.singletonList(subModelQuery), dataSet);
        assert ((List) dataSet.get("gender")).size() == 2;
        assert dataSet.get("gender[0].id").equals(1);
        assert dataSet.get("gender[1].id").equals(1);
        assert dataSet.get("gender[0].label").equals("someLabel");
        assert dataSet.get("gender[1].label").equals("someLabel");
        assert dataSet.get("gender[0].someField").equals("someFieldValue");
        assert dataSet.get("gender[1].someField").equals("someFieldValue");

        //label уже есть
        subModelQuery = new SubModelQuery("gender", "someQuery", "id", "label", true, null);
        dataSet = new DataSet("gender[0].id", 1).add("gender[1].id", 2).add("gender[0].label", "someLabel").add("gender[1].label", "someLabel");
        processor.executeSubModels(Collections.singletonList(subModelQuery), dataSet);
        assert ((List) dataSet.get("gender")).size() == 2;
        assert dataSet.get("gender[0].id").equals(1);
        assert dataSet.get("gender[1].id").equals(2);
        assert dataSet.get("gender[0].label").equals("someLabel");
        assert dataSet.get("gender[1].label").equals("someLabel");
        assert dataSet.get("gender[0].someField") == null;
        assert dataSet.get("gender[1].someField") == null;

        //value == null
        subModelQuery = new SubModelQuery("gender", "someQuery", "id", "label", true, null);
        dataSet = new DataSet("gender[0].id", null).add("gender[1].id", null);
        processor.executeSubModels(Collections.singletonList(subModelQuery), dataSet);
        assertThat(((List) dataSet.get("gender")).isEmpty(), is(true));

        //компонент с <options>
        Map<String, Object> optionsMap = new HashMap<>();
        optionsMap.put("id", 1);
        optionsMap.put("name", "test");
        optionsMap.put("type", "1");
        List<Map<String, Object>> options = Collections.singletonList(optionsMap);
        subModelQuery = new SubModelQuery("gender", null, "id", "name", true, options);
        dataSet = new DataSet("gender[0].id", "1");
        processor.executeSubModels(Collections.singletonList(subModelQuery), dataSet);
        assertThat(((Map) ((List) dataSet.get("gender")).get(0)).get("id"), is(1));
        assertThat(((Map) ((List) dataSet.get("gender")).get(0)).get("name"), is("test"));
        assertThat(((Map) ((List) dataSet.get("gender")).get(0)).get("type"), is("1"));

        optionsMap.put("id", 2);
        dataSet = new DataSet("gender[0].id", "2");
        processor.executeSubModels(Collections.singletonList(subModelQuery), dataSet);
        assert "test".equals(((Map) ((List) dataSet.get("gender")).get(0)).get("name"));
    }


    @Test
    public void testMultiListSubModelError() {
        //в query нету поля для value
        SubModelQuery subModelQuery = new SubModelQuery("gender", "someQuery", "wrong", "label", true, null);
        DataSet dataSet = new DataSet("gender[0].wrong", 1);
        N2oTestUtil.assertOnException(() -> processor.executeSubModels(Collections.singletonList(subModelQuery), dataSet), RuntimeException.class);
    }


    //моки
    private static class TestCompiledQuery extends CompiledQuery {
        public TestCompiledQuery(String queryId) {
            this.id = queryId;
            this.fieldsMap = new HashMap<>();
            N2oQuery.Field idField = new N2oQuery.Field("id");
            idField.setDomain("integer");
            this.fieldsMap.put("id", idField);
            this.displayFields = Arrays.asList(idField, new N2oQuery.Field("label"), new N2oQuery.Field("someField"));
            N2oQuery.Filter filter = new N2oQuery.Filter();
            Map<FilterType, N2oQuery.Filter> filterMap = new HashMap<>();
            filterMap.put(FilterType.eq, filter);
            this.getFiltersMap().put("id", filterMap);
        }
    }

    private static class TestQueryExecutor implements BiFunction<CompiledQuery, N2oPreparedCriteria, CollectionPage<DataSet>> {
        CompiledQuery compiledQuery;
        N2oPreparedCriteria criteria;

        @Override
        public CollectionPage<DataSet> apply(CompiledQuery compiledQuery, N2oPreparedCriteria criteria) {
            this.compiledQuery = compiledQuery;
            this.criteria = criteria;
            return new CollectionPage<>(1,
                    Arrays.asList(new DataSet("label", "someLabel").add("someField", "someFieldValue").add("id", 1)),
                    criteria);
        }
    }
}
