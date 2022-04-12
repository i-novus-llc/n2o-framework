package net.n2oapp.framework.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import net.n2oapp.framework.api.criteria.N2oPreparedCriteria;
import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.metadata.dataprovider.N2oGraphQlDataProvider;
import net.n2oapp.framework.api.metadata.dataprovider.N2oRestDataProvider;
import net.n2oapp.framework.api.metadata.dataprovider.N2oSqlDataProvider;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.widget.MessagePlacement;
import net.n2oapp.framework.api.register.MetaType;
import net.n2oapp.framework.api.rest.GetDataResponse;
import net.n2oapp.framework.api.ui.AlertMessageBuilder;
import net.n2oapp.framework.api.ui.QueryRequestInfo;
import net.n2oapp.framework.boot.graphql.GraphQlDataProviderEngine;
import net.n2oapp.framework.boot.sql.SqlDataProviderEngine;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.compile.pipeline.N2oEnvironment;
import net.n2oapp.framework.config.compile.pipeline.operation.BindOperation;
import net.n2oapp.framework.config.compile.pipeline.operation.CompileOperation;
import net.n2oapp.framework.config.compile.pipeline.operation.ReadOperation;
import net.n2oapp.framework.config.io.dataprovider.GraphQlDataProviderIOv1;
import net.n2oapp.framework.config.io.dataprovider.RestDataProviderIOv1;
import net.n2oapp.framework.config.io.dataprovider.SqlDataProviderIOv1;
import net.n2oapp.framework.config.io.query.QueryElementIOv4;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import net.n2oapp.framework.config.metadata.compile.object.N2oObjectCompiler;
import net.n2oapp.framework.config.metadata.compile.query.N2oQueryCompiler;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.selective.SelectiveMetadataLoader;
import net.n2oapp.framework.config.test.SimplePropertyResolver;
import net.n2oapp.framework.config.util.N2oSubModelsProcessor;
import net.n2oapp.framework.engine.data.N2oInvocationFactory;
import net.n2oapp.framework.engine.data.N2oQueryExceptionHandler;
import net.n2oapp.framework.engine.data.N2oQueryProcessor;
import net.n2oapp.framework.engine.data.rest.SpringRestDataProviderEngine;
import net.n2oapp.framework.engine.modules.stack.SpringDataProcessingStack;
import net.n2oapp.framework.ui.controller.query.QueryController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.RestTemplate;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class QueryControllerExceptionTest {

    private static final WireMockServer wireMockServer = new WireMockServer();
    private QueryController queryController;
    private N2oInvocationFactory invocationFactory;
    private N2oApplicationBuilder builder;
    @Autowired
    private SqlDataProviderEngine sqlDataProviderEngine;
    private RestTemplate restTemplate;

    @BeforeEach
    public void setUp() throws Exception {
        N2oEnvironment environment = new N2oEnvironment();
        environment.setReadPipelineFunction(p -> p.read());
        environment.setReadCompilePipelineFunction(p -> p.read().compile());
        environment.setCompilePipelineFunction(p -> p.compile());
        SpringDataProcessingStack dataProcessingStack = mock(SpringDataProcessingStack.class);
        invocationFactory = mock(N2oInvocationFactory.class);
        N2oQueryExceptionHandler exceptionHandler = new N2oQueryExceptionHandler();
        N2oQueryProcessor queryProcessor = new N2oQueryProcessor(invocationFactory, exceptionHandler);
        queryProcessor.setEnvironment(environment);
        DomainProcessor domainProcessor = new DomainProcessor();
        N2oSubModelsProcessor subModelsProcessor = new N2oSubModelsProcessor(queryProcessor, domainProcessor);
        SimplePropertyResolver propertyResolver = mock(SimplePropertyResolver.class);
        when(propertyResolver.getProperty("n2o.api.message.placement", MessagePlacement.class)).thenReturn(MessagePlacement.top);
        when(propertyResolver.getProperty("n2o.ui.message.dev-mode", Boolean.class)).thenReturn(true);
        when(propertyResolver.getProperty("n2o.api.message.danger.timeout")).thenReturn("8000");
        when(propertyResolver.getProperty("n2o.config.rest.filters_separator")).thenReturn("&");
        when(propertyResolver.getProperty("n2o.config.rest.select_separator")).thenReturn("&");
        when(propertyResolver.getProperty("n2o.config.rest.join_separator")).thenReturn("&");
        when(propertyResolver.getProperty("n2o.config.rest.sorting_separator")).thenReturn("&");
        environment.setSystemProperties(propertyResolver);
        MessageSourceAccessor messageSourceAccessor = mock(MessageSourceAccessor.class);
        when(messageSourceAccessor.getMessage("Query execution error", "Query execution error"))
                .thenReturn("Query execution error");
        AlertMessageBuilder messageBuilder = new AlertMessageBuilder(messageSourceAccessor, propertyResolver);
        queryController = new QueryController(dataProcessingStack, queryProcessor, subModelsProcessor, messageBuilder, environment);
        restTemplate = new RestTemplate();
        builder = new N2oApplicationBuilder(environment)
                .types(new MetaType("query", N2oQuery.class))
                .loaders(new SelectiveMetadataLoader()
                        .add(new QueryElementIOv4())
                        .add(new GraphQlDataProviderIOv1())
                        .add(new SqlDataProviderIOv1())
                        .add(new RestDataProviderIOv1()))
                .operations(new ReadOperation(), new CompileOperation(), new BindOperation())
                .compilers(new N2oQueryCompiler(), new N2oObjectCompiler())
                .sources(new CompileInfo("META-INF/conf/test/data/dev_mode/testGraphQlException.query.xml"),
                        new CompileInfo("META-INF/conf/test/data/dev_mode/testSqlException.query.xml"),
                        new CompileInfo("META-INF/conf/test/data/dev_mode/testRestException.query.xml"));
    }


    @Test
    public void testGraphQlException() {
        wireMockServer.start();
        stubFor(post(urlMatching("/graphql")).willReturn(aResponse().withHeader("Content-Type", "application/json")
                .withBody("{ \"errors\":[] }")));
        GraphQlDataProviderEngine providerEngine = new GraphQlDataProviderEngine();
        providerEngine.setRestTemplate(restTemplate);
        when(invocationFactory.produce(N2oGraphQlDataProvider.class)).thenReturn(providerEngine);
        CompiledQuery query = builder.read().compile().get(new QueryContext("testGraphQlException"));

        QueryRequestInfo requestInfo = new QueryRequestInfo();
        requestInfo.setQuery(query);
        requestInfo.setCriteria(new N2oPreparedCriteria());
        GetDataResponse response = queryController.execute(requestInfo, null);

        assertThat(response.getMeta().getAlert().getMessages().size(), is(1));
        assertThat(response.getMeta().getAlert().getMessages().get(0).getText(), is("Query execution error"));
        assertThat(response.getMeta().getAlert().getMessages().get(0).getPayload().size(), is(1));
        assertThat(response.getMeta().getAlert().getMessages().get(0).getPayload().get(0), is("query { queryCar { id } }"));
    }

    @Test
    public void testSqlException() {
        when(invocationFactory.produce(N2oSqlDataProvider.class)).thenReturn(sqlDataProviderEngine);
        CompiledQuery query = builder.read().compile().get(new QueryContext("testSqlException"));

        QueryRequestInfo requestInfo = new QueryRequestInfo();
        requestInfo.setQuery(query);
        requestInfo.setCriteria(new N2oPreparedCriteria());
        GetDataResponse response = queryController.execute(requestInfo, null);

        assertThat(response.getMeta().getAlert().getMessages().size(), is(1));
        assertThat(response.getMeta().getAlert().getMessages().get(0).getText(), is("Query execution error"));
        assertThat(response.getMeta().getAlert().getMessages().get(0).getPayload().size(), is(1));
        assertThat(response.getMeta().getAlert().getMessages().get(0).getPayload().get(0), is("SELECT * FROM"));
    }

    @Test
    public void testRestException() {
        wireMockServer.start();
        stubFor(get(urlMatching("/data/car/search/findAll")).willReturn(aResponse().withStatus(404)));
        SpringRestDataProviderEngine restDataProviderEngine = new SpringRestDataProviderEngine(restTemplate, new ObjectMapper());
        restDataProviderEngine.setBaseRestUrl("");
        when(invocationFactory.produce(N2oRestDataProvider.class)).thenReturn(restDataProviderEngine);
        CompiledQuery query = builder.read().compile().get(new QueryContext("testRestException"));

        QueryRequestInfo requestInfo = new QueryRequestInfo();
        requestInfo.setQuery(query);
        requestInfo.setCriteria(new N2oPreparedCriteria());
        GetDataResponse response = queryController.execute(requestInfo, null);

        assertThat(response.getMeta().getAlert().getMessages().size(), is(1));
        assertThat(response.getMeta().getAlert().getMessages().get(0).getText(), is("Query execution error"));
        assertThat(response.getMeta().getAlert().getMessages().get(0).getPayload().size(), is(1));
        assertThat(response.getMeta().getAlert().getMessages().get(0).getPayload().get(0), is("http://localhost:8080/data/car/search/findAll"));
    }
}
