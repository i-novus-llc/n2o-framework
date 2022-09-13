package net.n2oapp.framework.engine.processor;

import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.context.ContextProcessor;
import net.n2oapp.framework.api.criteria.N2oPreparedCriteria;
import net.n2oapp.framework.api.metadata.global.dao.query.N2oQuery;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.register.MetaType;
import net.n2oapp.framework.api.test.TestContextEngine;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.compile.pipeline.N2oEnvironment;
import net.n2oapp.framework.config.compile.pipeline.operation.BindOperation;
import net.n2oapp.framework.config.compile.pipeline.operation.CompileOperation;
import net.n2oapp.framework.config.compile.pipeline.operation.ReadOperation;
import net.n2oapp.framework.config.compile.pipeline.operation.SourceTransformOperation;
import net.n2oapp.framework.config.io.dataprovider.TestDataProviderIOv1;
import net.n2oapp.framework.config.io.query.QueryElementIOv5;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import net.n2oapp.framework.config.metadata.compile.object.N2oObjectCompiler;
import net.n2oapp.framework.config.metadata.compile.query.N2oQueryCompiler;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.selective.SelectiveMetadataLoader;
import net.n2oapp.framework.config.test.SimplePropertyResolver;
import net.n2oapp.framework.engine.data.N2oInvocationFactory;
import net.n2oapp.framework.engine.data.N2oQueryExceptionHandler;
import net.n2oapp.framework.engine.data.N2oQueryProcessor;
import net.n2oapp.framework.engine.data.json.TestDataProviderEngine;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Тестирование функций нормализации n2o
 */
public class NormalizeFunctionsTest {

    private N2oQueryProcessor queryProcessor;
    private N2oApplicationBuilder builder;
    private N2oInvocationFactory factory;

    @Before
    public void setUp() throws Exception {
        ContextProcessor contextProcessor = new ContextProcessor(new TestContextEngine());
        factory = mock(N2oInvocationFactory.class);
        queryProcessor = new N2oQueryProcessor(factory, new N2oQueryExceptionHandler());
        N2oEnvironment environment = new N2oEnvironment();
        environment.setContextProcessor(contextProcessor);
        environment.setSystemProperties(new SimplePropertyResolver(new Properties()));
        environment.setReadPipelineFunction(p -> p.read());
        environment.setReadCompilePipelineFunction(p -> p.read().compile());
        environment.setCompilePipelineFunction(p -> p.compile());
        queryProcessor.setEnvironment(environment);
        builder = new N2oApplicationBuilder(environment)
                .types(new MetaType("query", N2oQuery.class))
                .loaders(new SelectiveMetadataLoader()
                        .add(new QueryElementIOv5())
                        .add(new TestDataProviderIOv1()))
                .operations(new ReadOperation(), new CompileOperation(), new BindOperation(), new SourceTransformOperation())
                .compilers(new N2oQueryCompiler(), new N2oObjectCompiler())
                .properties("n2o.api.query.field.is_selected=true", "n2o.api.query.field.is_sorted=false");
    }

    @Test
    public void testJsonToMap() {
        when(factory.produce(any())).thenReturn(new TestDataProviderEngine());
        builder.sources(new CompileInfo("net/n2oapp/framework/engine/processor/normalize/testJsonToMap.query.xml"));
        CompiledQuery query = builder.read().compile().get(new QueryContext("testJsonToMap"));

        N2oPreparedCriteria criteria = new N2oPreparedCriteria();
        criteria.setSize(1);
        CollectionPage<DataSet> result = queryProcessor.execute(query, criteria);
        assertThat(result.getCount(), is(1));

        DataSet first = result.getCollection().iterator().next();
        assertThat(first.getInteger("id"), is(1));
        assertThat(first.getString("name"), is("TEST1"));

        DataSet organization = first.getDataSet("organization");
        assertThat(organization.getInteger("code"), is(2));
        assertThat(organization.getString("title"), is("org2"));

        List<DataSet> organizationEmployees = (List<DataSet>) organization.getList("employees");
        assertThat(organizationEmployees.size(), is(2));
        assertThat(organizationEmployees.get(0).getInteger("id"), is(73));
        assertThat(organizationEmployees.get(0).getString("name"), is("employee73"));
        assertThat(organizationEmployees.get(1).getInteger("id"), is(75));
        assertThat(organizationEmployees.get(1).getString("name"), is("employee75"));
    }

    @Test
    public void testMapToJson() {
        when(factory.produce(any())).thenReturn(new TestDataProviderEngine());
        builder.sources(new CompileInfo("net/n2oapp/framework/engine/processor/normalize/testMapToJson.query.xml"));
        CompiledQuery query = builder.read().compile().get(new QueryContext("testMapToJson"));

        N2oPreparedCriteria criteria = new N2oPreparedCriteria();
        criteria.setSize(1);
        CollectionPage<DataSet> result = queryProcessor.execute(query, criteria);
        assertThat(result.getCount(), is(1));

        DataSet first = result.getCollection().iterator().next();
        assertThat(first.getLong("id"), is(1L));
        assertThat(first.getString("organization"), is("{\"code\":2,\"title\":\"test\"}"));
    }

    @Test
    public void testBase64EncodeDecode() {
        when(factory.produce(any())).thenReturn(new TestDataProviderEngine());
        builder.sources(new CompileInfo("net/n2oapp/framework/engine/processor/normalize/testBase64EncodeDecode.query.xml"));
        CompiledQuery query = builder.read().compile().get(new QueryContext("testBase64EncodeDecode"));

        N2oPreparedCriteria criteria = new N2oPreparedCriteria();
        criteria.setSize(1);
        CollectionPage<DataSet> result = queryProcessor.execute(query, criteria);
        assertThat(result.getCount(), is(1));

        DataSet first = result.getCollection().iterator().next();
        assertThat(first.getLong("id"), is(1L));
        assertThat(first.getString("encoded"), is("dGVzdA=="));
        assertThat(first.getString("decoded"), is("test"));
    }

    @Test
    public void testFormatByMask() {
        when(factory.produce(any())).thenReturn(new TestDataProviderEngine());
        builder.sources(new CompileInfo("net/n2oapp/framework/engine/processor/normalize/testFormatByMask.query.xml"));
        CompiledQuery query = builder.read().compile().get(new QueryContext("testFormatByMask"));

        N2oPreparedCriteria criteria = new N2oPreparedCriteria();
        criteria.setSize(1);
        CollectionPage<DataSet> result = queryProcessor.execute(query, criteria);
        assertThat(result.getCount(), is(1));

        String phone = result.getCollection().iterator().next().getString("phone");
        assertThat(phone, is("8(322) 412-83-75"));
    }

    @Test
    public void testNamesFormat() {
        when(factory.produce(any())).thenReturn(new TestDataProviderEngine());
        builder.sources(new CompileInfo("net/n2oapp/framework/engine/processor/normalize/testNamesFormat.query.xml"));
        CompiledQuery query = builder.read().compile().get(new QueryContext("testNamesFormat"));

        N2oPreparedCriteria criteria = new N2oPreparedCriteria();
        CollectionPage<DataSet> result = queryProcessor.execute(query, criteria);
        assertThat(result.getCount(), is(2));

        List<DataSet> persons = (List<DataSet>) result.getCollection();
        assertThat(persons.get(0).getString("fullName"), is("Толстой Лев Николаевич"));
        assertThat(persons.get(0).getString("shortName"), is("Толстой Л.Н."));
        assertThat(persons.get(1).getString("fullName"), is("Маркс Карл "));
        assertThat(persons.get(1).getString("shortName"), is("Маркс К."));
    }

    @Test
    public void testNormalizerWithAlias() {
        when(factory.produce(any())).thenReturn(new TestDataProviderEngine());
        builder.sources(new CompileInfo("net/n2oapp/framework/engine/processor/normalize/testNormalizerWithAlias.query.xml"));
        CompiledQuery query = builder.read().compile().get(new QueryContext("testNormalizerWithAlias"));

        N2oPreparedCriteria criteria = new N2oPreparedCriteria();
        criteria.setSize(1);
        CollectionPage<DataSet> result = queryProcessor.execute(query, criteria);
        assertThat(result.getCount(), is(1));

        String phone = result.getCollection().iterator().next().getString("key");
        assertThat(phone, is("VALUE"));
    }
}
