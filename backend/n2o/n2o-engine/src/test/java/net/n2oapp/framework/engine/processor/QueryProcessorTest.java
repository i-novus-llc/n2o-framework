package net.n2oapp.framework.engine.processor;

import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.criteria.filters.Filter;
import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.context.ContextProcessor;
import net.n2oapp.framework.api.criteria.N2oPreparedCriteria;
import net.n2oapp.framework.api.criteria.Restriction;
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
import net.n2oapp.framework.config.io.dataprovider.JavaDataProviderIOv1;
import net.n2oapp.framework.config.io.dataprovider.TestDataProviderIOv1;
import net.n2oapp.framework.config.io.query.QueryElementIOv4;
import net.n2oapp.framework.config.io.query.QueryElementIOv5;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import net.n2oapp.framework.config.metadata.compile.object.N2oObjectCompiler;
import net.n2oapp.framework.config.metadata.compile.query.N2oQueryCompiler;
import net.n2oapp.framework.config.metadata.compile.query.TestEngineQueryTransformer;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.selective.SelectiveMetadataLoader;
import net.n2oapp.framework.config.test.SimplePropertyResolver;
import net.n2oapp.framework.engine.data.N2oInvocationFactory;
import net.n2oapp.framework.engine.data.N2oQueryExceptionHandler;
import net.n2oapp.framework.engine.data.N2oQueryProcessor;
import net.n2oapp.framework.engine.data.java.JavaDataProviderEngine;
import net.n2oapp.framework.engine.data.json.TestDataProviderEngine;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Тестирование процессора запросов
 */
public class QueryProcessorTest {
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
                        .add(new QueryElementIOv4())
                        .add(new QueryElementIOv5())
                        .add(new TestDataProviderIOv1())
                        .add(new JavaDataProviderIOv1()))
                .operations(new ReadOperation(), new CompileOperation(), new BindOperation(), new SourceTransformOperation())
                .compilers(new N2oQueryCompiler(), new N2oObjectCompiler())
                .properties("n2o.api.query.field.is_selected=true", "n2o.api.query.field.is_sorted=false")
                .sources(new CompileInfo("net/n2oapp/framework/engine/processor/query/testQueryProcessorV4Java.query.xml"),
                        new CompileInfo("net/n2oapp/framework/engine/processor/query/testQueryProcessorV4JavaMapping.query.xml"),
                        new CompileInfo("net/n2oapp/framework/engine/processor/query/testQueryProcessorUnique.query.xml"),
                        new CompileInfo("net/n2oapp/framework/engine/processor/query/testQueryProcessorNorm.query.xml"),
                        new CompileInfo("net/n2oapp/framework/engine/processor/query/testQueryProcessorRequiredFilter.query.xml"));
    }

    @Test
    public void query4Java() {
        when(factory.produce(any())).thenReturn(new JavaDataProviderEngine());
        CompiledQuery query = builder.read().compile().get(new QueryContext("testQueryProcessorV4Java"));

        //case without arguments
        N2oPreparedCriteria criteria = new N2oPreparedCriteria();
        CollectionPage<DataSet> collectionPage = queryProcessor.execute(query, criteria);
        assertThat(collectionPage.getCount(), is(10));
        DataSet dataSet = (DataSet) ((List) collectionPage.getCollection()).get(0);
        assertThat(dataSet.get("id"), is(0));

        //case with primitive
        criteria = new N2oPreparedCriteria();
        criteria.addRestriction(new Restriction("value", "test"));
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
        assertThat(collectionPage.getCount(), is(1));
        dataSet = (DataSet) ((List) collectionPage.getCollection()).get(0);
        assertThat(dataSet.get("id"), is(0));
        assertThat(dataSet.get("name"), is("test"));
    }

    /**
     * Тестирование маппинга аргументов java провайдера с использованием name аргументов, а не через заданный порядок
     */
    @Test
    public void testNameMappingWithArgumentsInvocationProvider() {
        JavaDataProviderEngine javaDataProviderEngine = new JavaDataProviderEngine();
        when(factory.produce(any())).thenReturn(javaDataProviderEngine);

        CompiledQuery query = builder.read().compile().get(new QueryContext("testQueryProcessorV4JavaMapping"));
        N2oPreparedCriteria criteria = new N2oPreparedCriteria();
        criteria.addRestriction(new Restriction("thirdArg", true));
        criteria.addRestriction(new Restriction("firstArg", 123));
        criteria.addRestriction(new Restriction("nameArg", "test"));

        CollectionPage<DataSet> collectionPage = queryProcessor.execute(query, criteria);
        assertThat(collectionPage.getCount(), is(1));
        // Result
        DataSet result = ((List<DataSet>) collectionPage.getCollection()).get(0);
        assertThat(result.get("firstArg"), is(123));
        assertThat(result.get("nameArg"), is("test"));
        assertThat(result.get("thirdArg"), is(true));
    }

    @Test
    public void testCriteriaRestrictionMerge() {
        when(factory.produce(any())).thenReturn(new JavaDataProviderEngine());
        CompiledQuery query = builder.read().compile().get(new QueryContext("testQueryProcessorV4Java"));
        N2oPreparedCriteria criteria = new N2oPreparedCriteria();
        criteria.addRestriction(new Restriction("id", "1", FilterType.eq));
        criteria.addRestriction(new Restriction("id", "45", FilterType.eq));
        CollectionPage<DataSet> collectionPage = queryProcessor.execute(query, criteria);
        assertThat(collectionPage.getCount(), is(0));

        criteria = new N2oPreparedCriteria();
        criteria.addRestriction(new Restriction("name", "test", FilterType.eq));
        criteria.addRestriction(new Restriction("name", "test", FilterType.eq));
        collectionPage = queryProcessor.execute(query, criteria);
        assertThat(collectionPage.getCount(), is(1));

        criteria = new N2oPreparedCriteria();
        criteria.addRestriction(new Restriction("id", "1", FilterType.more));
        criteria.addRestriction(new Restriction("id", "45", FilterType.less));
        collectionPage = queryProcessor.execute(query, criteria);
        assertThat(collectionPage.getCount(), is(10));

        criteria = new N2oPreparedCriteria();
        criteria.addRestriction(new Restriction("id", "0", FilterType.eq));
        criteria.addRestriction(new Restriction("name", "test", FilterType.eq));
        collectionPage = queryProcessor.execute(query, criteria);
        assertThat(collectionPage.getCount(), is(1));
        DataSet dataSet = (DataSet) ((List) collectionPage.getCollection()).get(0);
        assertThat(dataSet.get("id"), is(0));
        assertThat(dataSet.get("name"), is("test"));
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
        CollectionPage<DataSet> result = queryProcessor.execute(query, criteria);
        DataSet first = result.getCollection().iterator().next();
        assertThat(first.get("normTest"), is(Integer.MAX_VALUE));
        assertThat(query.getSimpleFieldsMap().get("normTest").getDefaultValue(), is("defaultValue"));
    }

    @Test
    public void testNestedFields() {
        when(factory.produce(any())).thenReturn(new TestDataProviderEngine());
        builder.sources(new CompileInfo("net/n2oapp/framework/engine/processor/query/nested_fields/testNestedFields.query.xml"));
        CompiledQuery query = builder.read().compile().get(new QueryContext("testNestedFields"));

        N2oPreparedCriteria criteria = new N2oPreparedCriteria();
        CollectionPage<DataSet> result = queryProcessor.execute(query, criteria);
        assertThat(result.getCount(), is(4));

        DataSet first = result.getCollection().iterator().next();
        assertThat(first.getLong("id"), is(1L));
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


        List<DataSet> departments = (List<DataSet>) first.getList("departments");
        assertThat(departments.size(), is(2));
        assertThat(departments.get(0).getInteger("id"), is(3));
        assertThat(departments.get(0).getString("name"), is("department3"));
        assertThat(departments.get(1).getInteger("id"), is(4));
        assertThat(departments.get(1).getString("name"), is("department4"));

        List<DataSet> departments0Groups = (List<DataSet>) departments.get(0).getList("groups");
        assertThat(departments0Groups.get(0).getInteger("id"), is(9));
        assertThat(departments0Groups.get(0).getString("name"), is("group9"));
        assertThat(departments0Groups.get(1).getInteger("id"), is(10));
        assertThat(departments0Groups.get(1).getString("name"), is("group10"));

        DataSet departments0Manager = departments.get(0).getDataSet("manager");
        assertThat(departments0Manager.getInteger("id"), is(322));
        assertThat(departments0Manager.getString("name"), is("manager322"));
    }

    @Test
    public void testNestedFieldsMapping() {
        when(factory.produce(any())).thenReturn(new TestDataProviderEngine());
        builder.sources(new CompileInfo("net/n2oapp/framework/engine/processor/query/nested_fields/testNestedFieldsMapping.query.xml"));
        CompiledQuery query = builder.read().compile().get(new QueryContext("testNestedFieldsMapping"));

        N2oPreparedCriteria criteria = new N2oPreparedCriteria();
        CollectionPage<DataSet> result = queryProcessor.execute(query, criteria);
        assertThat(result.getCount(), is(4));

        DataSet first = result.getCollection().iterator().next();
        assertThat(first.getLong("myId"), is(1L));
        assertThat(first.getString("myName"), is("TEST1"));

        DataSet organization = first.getDataSet("myOrganization");
        assertThat(organization.size(), is(3));
        assertThat(organization.getInteger("myCode"), is(2));
        assertThat(organization.getString("myTitle"), is("org2"));

        List<DataSet> organizationEmployees = (List<DataSet>) organization.getList("myEmployees");
        assertThat(organizationEmployees.size(), is(2));
        assertThat(organizationEmployees.get(0).getInteger("myId"), is(73));
        assertThat(organizationEmployees.get(0).getString("myName"), is("employee73"));
        assertThat(organizationEmployees.get(1).getInteger("myId"), is(75));
        assertThat(organizationEmployees.get(1).getString("myName"), is("employee75"));


        List<DataSet> departments = (List<DataSet>) first.getList("myDepartments");
        assertThat(departments.size(), is(2));
        assertThat(departments.get(0).getInteger("myId"), is(3));
        assertThat(departments.get(0).getString("myName"), is("department3"));
        assertThat(departments.get(1).getInteger("myId"), is(4));
        assertThat(departments.get(1).getString("myName"), is("department4"));

        List<DataSet> departments0Groups = (List<DataSet>) departments.get(0).getList("myGroups");
        assertThat(departments0Groups.size(), is(2));
        assertThat(departments0Groups.get(0).getInteger("myId"), is(9));
        assertThat(departments0Groups.get(0).getString("myName"), is("group9"));
        assertThat(departments0Groups.get(1).getInteger("myId"), is(10));
        assertThat(departments0Groups.get(1).getString("myName"), is("group10"));

        DataSet departments0Manager = departments.get(0).getDataSet("myManager");
        assertThat(departments0Manager.size(), is(2));
        assertThat(departments0Manager.getInteger("myId"), is(322));
        assertThat(departments0Manager.getString("myName"), is("manager322"));
    }

    @Test
    public void testNestedFieldsDefaultValues() {
        when(factory.produce(any())).thenReturn(new TestDataProviderEngine());
        builder.sources(new CompileInfo("net/n2oapp/framework/engine/processor/query/nested_fields/testNestedFieldsDefaultValues.query.xml"));
        CompiledQuery query = builder.read().compile().get(new QueryContext("testNestedFieldsDefaultValues"));

        N2oPreparedCriteria criteria = new N2oPreparedCriteria();
        CollectionPage<DataSet> result = queryProcessor.execute(query, criteria);
        assertThat(result.getCount(), is(1));
        DataSet first = result.getCollection().iterator().next();

        assertThat(first.getString("myName"), is("defaultName fieldNormalize"));

        DataSet organization = first.getDataSet("myOrganization");
        assertThat(organization.size(), is(2));
        assertThat(organization.getString("myTitle"), is("defaultTitle fieldNormalize"));

        List<DataSet> organizationEmployees = (List<DataSet>) organization.getList("myEmployees");
        assertThat(organizationEmployees.size(), is(2));
        assertThat(organizationEmployees.get(0).getString("myName"), is("defaultName fieldNormalize"));
        assertThat(organizationEmployees.get(1).getString("myName"), is("nameFromReferenceNormalize fieldNormalize"));
    }

    @Test
    public void testListFieldNormalize() {
        when(factory.produce(any())).thenReturn(new TestDataProviderEngine());
        builder.sources(new CompileInfo("net/n2oapp/framework/engine/processor/query/nested_fields/testListFieldNormalize.query.xml"));
        CompiledQuery query = builder.read().compile().get(new QueryContext("testListFieldNormalize"));

        N2oPreparedCriteria criteria = new N2oPreparedCriteria();
        CollectionPage<DataSet> result = queryProcessor.execute(query, criteria);
        assertThat(result.getCount(), is(1));
        DataSet first = result.getCollection().iterator().next();

        List<DataSet> departments = (List<DataSet>) first.getList("myDepartments");
        assertThat(departments.size(), is(2));
        assertThat(departments.get(0).getInteger("myId"), is(103));
        assertThat(departments.get(0).getString("myName"), is("department103"));
        assertThat(departments.get(1).getInteger("myId"), is(104));
        assertThat(departments.get(1).getString("myName"), is("department104"));

        List<DataSet> departments0Groups = (List<DataSet>) departments.get(0).getList("myGroups");
        assertThat(departments0Groups.size(), is(2));
        assertThat(departments0Groups.get(0).getInteger("myId"), is(109));
        assertThat(departments0Groups.get(0).getString("myName"), is("group109"));
        assertThat(departments0Groups.get(1).getInteger("myId"), is(110));
        assertThat(departments0Groups.get(1).getString("myName"), is("group110"));

        DataSet departments0Manager = departments.get(0).getDataSet("myManager");
        assertThat(departments0Manager.size(), is(2));
        assertThat(departments0Manager.getInteger("myId"), is(422));
        assertThat(departments0Manager.getString("myName"), is("manager422"));
    }

    @Test
    public void testNestedNormalize() {
        when(factory.produce(any())).thenReturn(new TestDataProviderEngine());
        builder.sources(new CompileInfo("net/n2oapp/framework/engine/processor/query/nested_fields/testNestedNormalize.query.xml"));
        CompiledQuery query = builder.read().compile().get(new QueryContext("testNestedNormalize"));

        N2oPreparedCriteria criteria = new N2oPreparedCriteria();
        CollectionPage<DataSet> result = queryProcessor.execute(query, criteria);
        assertThat(result.getCount(), is(1));

        DataSet first = result.getCollection().iterator().next();
        assertThat(first.getLong("myId"), is(1001L));
        assertThat(first.getString("myName"), is("TEST1"));

        DataSet organization = first.getDataSet("myOrganization");
        assertThat(organization.size(), is(3));
        assertThat(organization.getString("myCode"), is("102refNormalize"));
        assertThat(organization.getString("myTitle"), is("refNormalize fieldNormalize"));

        List<DataSet> organizationEmployees = (List<DataSet>) organization.getList("myEmployees");
        assertThat(organizationEmployees.size(), is(1));
        assertThat(organizationEmployees.get(0).getString("myId"), is("273employee273"));
        assertThat(organizationEmployees.get(0).getString("myName"), is("employee273 fieldNormalize"));
    }

    @Test
    public void testNestedFieldsFiltering() {
        when(factory.produce(any())).thenReturn(new TestDataProviderEngine());
        builder.transformers(new TestEngineQueryTransformer());
        builder.sources(new CompileInfo("net/n2oapp/framework/engine/processor/query/nested_fields/testNestedFieldsFiltering.query.xml"));
        CompiledQuery query = builder.read().transform().compile().get(new QueryContext("testNestedFieldsFiltering"));

        N2oPreparedCriteria criteria = new N2oPreparedCriteria();
        Filter filter = new Filter(31, FilterType.eq);
        criteria.addRestriction(new Restriction("organization.code", filter));
        CollectionPage<DataSet> result = queryProcessor.execute(query, criteria);
        assertThat(result.getCollection().size(), is(1));
        assertThat(result.getCollection().iterator().next().getDataSet("organization").getInteger("code"), is(31));

        criteria = new N2oPreparedCriteria();
        filter = new Filter(null, FilterType.isNull);
        criteria.addRestriction(new Restriction("organization", filter));
        result = queryProcessor.execute(query, criteria);
        assertThat(result.getCollection().size(), is(1));
        assertThat(result.getCollection().iterator().next().getDataSet("organization"), nullValue());
    }

    @Test
    public void testNestedFieldsMappingFiltering() {
        when(factory.produce(any())).thenReturn(new TestDataProviderEngine());
        builder.transformers(new TestEngineQueryTransformer());
        builder.sources(new CompileInfo("net/n2oapp/framework/engine/processor/query/nested_fields/testNestedFieldsMappingFiltering.query.xml"));
        CompiledQuery query = builder.read().transform().compile().get(new QueryContext("testNestedFieldsMappingFiltering"));

        N2oPreparedCriteria criteria = new N2oPreparedCriteria();
        Filter filter = new Filter("org", FilterType.like);
        criteria.addRestriction(new Restriction("myOrganization.myTitle", filter));
        CollectionPage<DataSet> result = queryProcessor.execute(query, criteria);
        List<DataSet> collection = (List<DataSet>) result.getCollection();
        assertThat(collection.size(), is(2));
        assertThat(collection.get(0).getDataSet("myOrganization").getString("myTitle"), is("org21"));
        assertThat(collection.get(1).getDataSet("myOrganization").getString("myTitle"), is("4orgTitle"));

        criteria = new N2oPreparedCriteria();
        filter = new Filter(null, FilterType.isNotNull);
        criteria.addRestriction(new Restriction("myOrganization", filter));
        result = queryProcessor.execute(query, criteria);
        collection = (List<DataSet>) result.getCollection();
        assertThat(collection.size(), is(3));
        assertThat(collection.get(0).getDataSet("myOrganization"), notNullValue());
        assertThat(collection.get(1).getDataSet("myOrganization"), notNullValue());
        assertThat(collection.get(2).getDataSet("myOrganization"), notNullValue());
    }

    @Test
    public void testHierarchicalSelect() {
        builder.sources(new CompileInfo("net/n2oapp/framework/engine/processor/query/nested_fields/testHierarchicalSelect.query.xml"));
        CompiledQuery query = builder.read().compile().get(new QueryContext("testHierarchicalSelect"));

        N2oPreparedCriteria criteria = new N2oPreparedCriteria();
        Map<String, Object> map = new LinkedHashMap<>();
        N2oQueryProcessor.prepareMapForQuery(map, query, criteria);

        List<String> value = (List<String>) map.get("select");
        assertThat(value.size(), is(3));
        assertThat(value.get(0), is("id"));
        assertThat(value.get(1), is("price"));
        assertThat(value.get(2), is("showrooms { $$showroomsSelect }"));

        value = ((List<String>) map.get("showroomsSelect"));
        assertThat(value.size(), is(3));
        assertThat(value.get(0), is("id"));
        assertThat(value.get(1), is("name"));
        assertThat(value.get(2), is("owner { $$ownerSelect }"));

        value = (List<String>) map.get("ownerSelect");
        assertThat(value.size(), is(2));
        assertThat(value.get(0), is("name"));
        assertThat(value.get(1), is("inn"));
    }

    @Test
    public void testResultNormalize() {
        when(factory.produce(any())).thenReturn(new TestDataProviderEngine());
        builder.sources(new CompileInfo("net/n2oapp/framework/engine/processor/query/nested_fields/testListResultNormalize.query.xml"));
        CompiledQuery query = builder.read().compile().get(new QueryContext("testListResultNormalize"));

        N2oPreparedCriteria criteria = new N2oPreparedCriteria();
        CollectionPage<DataSet> result = queryProcessor.execute(query, criteria);
        assertThat(result.getCount(), is(1));
        DataSet first = result.getCollection().iterator().next();

        List<DataSet> ids = (List<DataSet>) first.getList("ids");
        assertThat(ids.size(), is(2));
        assertThat(ids.get(0).size(), is(1));
        assertThat(ids.get(0).getLong("id"), is(1L));
        assertThat(ids.get(1).size(), is(1));
        assertThat(ids.get(1).getLong("id"), is(2L));

        List<DataSet> names = (List<DataSet>) first.getList("names");
        assertThat(names.size(), is(2));
        assertThat(names.get(0).size(), is(1));
        assertThat(names.get(0).getString("name"), is("test1"));
        assertThat(names.get(1).size(), is(1));
        assertThat(names.get(1).getString("name"), is("test2"));
    }

    @Test
    public void testUniqueResultNormalize() {
        when(factory.produce(any())).thenReturn(new TestDataProviderEngine());
        builder.sources(new CompileInfo("net/n2oapp/framework/engine/processor/query/nested_fields/testUniqueResultNormalize.query.xml"));
        CompiledQuery query = builder.read().compile().get(new QueryContext("testUniqueResultNormalize"));

        N2oPreparedCriteria criteria = new N2oPreparedCriteria();
        criteria.setSize(1);
        CollectionPage<DataSet> result = queryProcessor.execute(query, criteria);
        assertThat(result.getCount(), is(1));

        DataSet unique = result.getCollection().iterator().next();
        assertThat(unique.getLong("id"), is(1L));
        assertThat(unique.getDataSet("info").size(), is(2));
        assertThat(unique.getDataSet("info").getString("name"), is("test1"));
        assertThat(unique.getDataSet("info").getString("type"), is("type11"));
    }
}
