package net.n2oapp.framework.engine.processor;

import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.api.Direction;
import net.n2oapp.criteria.api.Sorting;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.context.ContextProcessor;
import net.n2oapp.framework.api.criteria.N2oPreparedCriteria;
import net.n2oapp.framework.api.criteria.Restriction;
import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.data.QueryProcessor;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.register.MetaType;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.compile.pipeline.N2oEnvironment;
import net.n2oapp.framework.config.compile.pipeline.operation.BindOperation;
import net.n2oapp.framework.config.compile.pipeline.operation.CompileOperation;
import net.n2oapp.framework.config.compile.pipeline.operation.ReadOperation;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import net.n2oapp.framework.config.metadata.compile.object.N2oObjectCompiler;
import net.n2oapp.framework.config.metadata.compile.query.N2oQueryCompiler;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.selective.reader.SelectiveStandardReader;
import net.n2oapp.framework.engine.data.N2oInvocationFactory;
import net.n2oapp.framework.engine.data.N2oQueryExceptionHandler;
import net.n2oapp.framework.engine.data.N2oQueryProcessor;
import net.n2oapp.framework.engine.data.java.JavaDataProviderEngine;
import net.n2oapp.framework.engine.data.json.TestDataProviderEngine;
import net.n2oapp.framework.engine.exception.N2oFoundMoreThanOneRecordException;
import net.n2oapp.framework.engine.exception.N2oRecordNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static net.n2oapp.framework.api.util.N2oTestUtil.assertOnException;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Тестирование процессора запросов
 */
public class QueryProcessorTest {
    private QueryProcessor queryProcessor;
    private N2oApplicationBuilder builder;
    private N2oInvocationFactory factory;

    @Before
    public void setUp() throws Exception {
        ContextProcessor contextProcessor = mock(ContextProcessor.class);
        factory = mock(N2oInvocationFactory.class);
        when(contextProcessor.resolve(anyString())).then((Answer) invocation -> invocation.getArguments()[0]);
        queryProcessor = new N2oQueryProcessor(factory, contextProcessor, new DomainProcessor(), new N2oQueryExceptionHandler());
        N2oEnvironment environment = new N2oEnvironment();
        environment.setContextProcessor(contextProcessor);
        environment.setReadPipelineFunction(p -> p.read());
        environment.setReadCompilePipelineFunction(p -> p.read().compile());
        environment.setCompilePipelineFunction(p -> p.compile());
        builder = new N2oApplicationBuilder(environment)
                .types(new MetaType("query", N2oQuery.class))
                .loaders(new SelectiveStandardReader().addQueryReader())
                .operations(new ReadOperation(), new CompileOperation(), new BindOperation())
                .compilers(new N2oQueryCompiler(), new N2oObjectCompiler())
                .sources(new CompileInfo("net/n2oapp/framework/engine/processor/testQueryProcessor.query.xml"),
                        new CompileInfo("net/n2oapp/framework/engine/processor/testQueryProcessorV4Java.query.xml"),
                        new CompileInfo("net/n2oapp/framework/engine/processor/testQueryProcessorUnique.query.xml"),
                        new CompileInfo("net/n2oapp/framework/engine/processor/testQueryProcessorNorm.query.xml"));
    }

    @Test
    public void query3Sql() {
        when(factory.produce(any())).thenReturn(new SqlDataProviderEngineMock());
        CompiledQuery query = builder.read().compile().get(new QueryContext("testQueryProcessor"));
        N2oPreparedCriteria criteria = new N2oPreparedCriteria();
        criteria.setCount(10);
        criteria.setPage(2);
        criteria.setSize(5);
        criteria.addRestriction(new Restriction("surname", "Фамилия", FilterType.eq));
        criteria.addRestriction(new Restriction("gender.name", "Женский", FilterType.like));
        criteria.setSorting(new Sorting("name", Direction.DESC));
        CollectionPage<DataSet> collectionPage = queryProcessor.execute(query, criteria);
        assertThat(collectionPage.getCount(), is(11));//был запрос за count
        DataSet dataSet = (DataSet) ((List) collectionPage.getCollection()).get(0);
        assertThat(dataSet.get("id"), is(1));//автоинкремент от 1
        assertThat(dataSet.get("surname"), is("Фамилия"));//есть в display
        assertThat(dataSet.get("patr.name"), is("Без отчества"));//значение по умолчанию в display
        assertThat(dataSet.get("gender.name"), nullValue());//нет display
    }

    @Test
    public void query3SqlGetOneRecordTest1() {
        when(factory.produce(any())).thenReturn(new SqlDPEOneSizeMock(1));
        CompiledQuery query = builder.read().compile().get(new QueryContext("testQueryProcessor"));
        N2oPreparedCriteria criteria = new N2oPreparedCriteria();
        criteria.setSize(1);
        criteria.setSorting(new Sorting("name", Direction.DESC));

        CollectionPage<DataSet> collectionPage = queryProcessor.execute(query, criteria);
        assertThat(collectionPage.getCollection().size(), is(1));
    }

    @Test
    public void query3SqlGetOneRecordTest2() {
        when(factory.produce(any())).thenReturn(new SqlDPEOneSizeMock(2));
        CompiledQuery query = builder.read().compile().get(new QueryContext("testQueryProcessor"));
        N2oPreparedCriteria criteria = new N2oPreparedCriteria();
        criteria.setSize(1);
        criteria.setSorting(new Sorting("name", Direction.DESC));

        assertOnException(() -> queryProcessor.execute(query, criteria), N2oFoundMoreThanOneRecordException.class);
    }

    @Test
    public void query3SqlGetOneRecordTest0() {
        when(factory.produce(any())).thenReturn(new SqlDPEOneSizeMock(0));
        CompiledQuery query = builder.read().compile().get(new QueryContext("testQueryProcessor"));
        N2oPreparedCriteria criteria = new N2oPreparedCriteria();
        criteria.setSize(1);
        criteria.setSorting(new Sorting("name", Direction.DESC));

        assertOnException(() -> queryProcessor.execute(query, criteria), N2oRecordNotFoundException.class);
    }


    @Test
    public void query4Java() {
        when(factory.produce(any())).thenReturn(new JavaDataProviderEngine());
        JavaDataProviderEngine javaDataEngineMock = mock(JavaDataProviderEngine.class);
        List<Object> list = new ArrayList<>();
        ///  when(javaDataEngineMock.invoke(any(), any())).thenReturn(list);
        CompiledQuery query = builder.read().compile().get(new QueryContext("testQueryProcessorV4Java"));

        //case without arguments
        N2oPreparedCriteria criteria = new N2oPreparedCriteria();
        CollectionPage<DataSet> collectionPage = queryProcessor.execute(query, criteria);
        assertThat(collectionPage.getCount(), is(10));
        DataSet dataSet = (DataSet) ((List) collectionPage.getCollection()).get(0);
        assertThat(dataSet.get("id"), is(0));
//        //case with primitive
        criteria = new N2oPreparedCriteria();
        criteria.addRestriction(new Restriction("value", "test"));
        collectionPage = queryProcessor.execute(query, criteria);
        assertThat(collectionPage.getCount(), is(10));
        dataSet = (DataSet) ((List) collectionPage.getCollection()).get(0);
        assertThat(dataSet.get("id"), is(0));
        assertThat(dataSet.get("value"), is("test"));
        //case with criteria (criteria-api)
        criteria = new N2oPreparedCriteria();
        criteria.addRestriction(new Restriction("name", "test"));
        collectionPage = queryProcessor.execute(query, criteria);
        assertThat(collectionPage.getCount(), is(10));
        dataSet = (DataSet) ((List) collectionPage.getCollection()).get(0);
        assertThat(dataSet.get("id"), is(0));
        assertThat(dataSet.get("name"), is("test"));
        //case with page request (spring data) todo
    }

    @Test
    public void query4Unique() {
        TestDataProviderEngine testDataprovider = new TestDataProviderEngine();
        when(factory.produce(any())).thenReturn(testDataprovider);
        CompiledQuery query = builder.read().compile().get(new QueryContext("testQueryProcessorUnique"));

        //case unique selection
        N2oPreparedCriteria criteria = new N2oPreparedCriteria();
        criteria.setSize(1);
        criteria.addRestriction(new Restriction("id", 1));
        CollectionPage<DataSet> result = queryProcessor.execute(query, criteria);
        assertThat(result.getCount(), is(1));
        DataSet first = result.getCollection().iterator().next();
        assertThat(first.get("id"), is(1L));

        //case list filter by code selection
        criteria = new N2oPreparedCriteria();
        criteria.setSize(1);
        criteria.addRestriction(new Restriction("code", "test1"));
        result = queryProcessor.execute(query, criteria);
        assertThat(result.getCount(), is(1));
        first = result.getCollection().iterator().next();
        assertThat(first.get("code"), is("test1"));


        //case list any filters selection
        criteria = new N2oPreparedCriteria();
        criteria.setSize(1);
        criteria.addRestriction(new Restriction("type", 10));
        result = queryProcessor.execute(query, criteria);
        assertThat(result.getCount(), is(1));
        first = result.getCollection().iterator().next();
        assertThat(first.get("type"), is(10));
    }

    @Test
    public void query4Normalize() {
        when(factory.produce(any())).thenReturn(new TestDataProviderEngine());
        CompiledQuery query = builder.read().compile().get(new QueryContext("testQueryProcessorNorm"));

        N2oPreparedCriteria criteria = new N2oPreparedCriteria();
        criteria.setSize(1);
        CollectionPage<DataSet> result = queryProcessor.execute(query, criteria);
        assertThat(result.getCount(), is(1));
        DataSet first = result.getCollection().iterator().next();
        assertThat(first.get("normTest"), is(Integer.MAX_VALUE));
        assertThat(query.getFieldsMap().get("normTest").getSelectDefaultValue(), is("defaultValue"));
    }
}
