package net.n2oapp.framework.test;

import com.github.tomakehurst.wiremock.WireMockServer;
import net.n2oapp.framework.api.criteria.N2oPreparedCriteria;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.rest.GetDataResponse;
import net.n2oapp.framework.api.ui.QueryRequestInfo;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.compile.pipeline.N2oEnvironment;
import net.n2oapp.framework.config.compile.pipeline.operation.BindOperation;
import net.n2oapp.framework.config.compile.pipeline.operation.CompileOperation;
import net.n2oapp.framework.config.compile.pipeline.operation.ReadOperation;
import net.n2oapp.framework.config.metadata.compile.context.QueryContext;
import net.n2oapp.framework.config.metadata.pack.N2oDataProvidersIOPack;
import net.n2oapp.framework.config.metadata.pack.N2oQueriesPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.selective.SelectiveMetadataLoader;
import net.n2oapp.framework.config.selective.persister.PersisterFactoryByMap;
import net.n2oapp.framework.config.test.SimplePropertyResolver;
import net.n2oapp.framework.ui.controller.query.QueryController;
import net.n2oapp.properties.reader.PropertiesReader;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Тестирование кейсов с ошибками в GraphQl, SQL и REST запросах
 */
@SpringBootTest(properties = {"n2o.ui.message.dev-mode=true"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class QueryControllerExceptionTest {

    private static WireMockServer mockServer = new WireMockServer();
    @Autowired
    private QueryController queryController;
    private N2oApplicationBuilder builder;

    @BeforeAll
    static void beforeAll() {
        mockServer.start();
        stubFor(post(urlMatching("/graphql")).willReturn(aResponse().withHeader("Content-Type", "application/json")
                .withBody("{ \"errors\":[ {\"message\": \"couldn't rewrite queryCar\"}] }")));
        stubFor(get(urlMatching("/data/car/search/findAll")).willReturn(aResponse().withStatus(404)));
    }

    @AfterAll
    static void afterAll() {
        mockServer.stop();
    }

    @BeforeEach
    public void setUp() {
        N2oEnvironment environment = new N2oEnvironment();
        environment.setReadPipelineFunction(p -> p.read());
        environment.setCompilePipelineFunction(p -> p.compile());
        environment.setReadCompilePipelineFunction(p -> p.read().compile());
        environment.setNamespacePersisterFactory(new PersisterFactoryByMap());
        SimplePropertyResolver propertyResolver =
                new SimplePropertyResolver(PropertiesReader.getPropertiesFromClasspath("application.properties"));
        environment.setSystemProperties(propertyResolver);
        builder = new N2oApplicationBuilder(environment)
                .packs(new N2oDataProvidersIOPack(), new N2oQueriesPack())
                .operations(new ReadOperation(), new CompileOperation(), new BindOperation())
                .loaders(new SelectiveMetadataLoader(environment.getNamespaceReaderFactory()))
                .sources(new CompileInfo("META-INF/conf/test/data/dev_mode/testGraphQlException.query.xml"),
                        new CompileInfo("META-INF/conf/test/data/dev_mode/testSqlException.query.xml"),
                        new CompileInfo("META-INF/conf/test/data/dev_mode/testRestException.query.xml"));
    }


    @Test
    public void testGraphQlException() {
        CompiledQuery query = builder.read().compile().get(new QueryContext("testGraphQlException"));
        QueryRequestInfo requestInfo = new QueryRequestInfo();
        requestInfo.setQuery(query);
        requestInfo.setCriteria(new N2oPreparedCriteria());
        GetDataResponse response = queryController.execute(requestInfo, null);

        assertThat(response.getMeta().getAlert().getMessages().size(), is(1));
        assertThat(response.getMeta().getAlert().getMessages().get(0).getText(), is("couldn't rewrite queryCar"));
        assertThat(response.getMeta().getAlert().getMessages().get(0).getPayload().size(), is(1));
        assertThat(response.getMeta().getAlert().getMessages().get(0).getPayload().get(0), is("Executed query: query { queryCar { id } }"));
    }

    @Test
    public void testSqlException() {
        CompiledQuery query = builder.read().compile().get(new QueryContext("testSqlException"));
        QueryRequestInfo requestInfo = new QueryRequestInfo();
        requestInfo.setQuery(query);
        requestInfo.setCriteria(new N2oPreparedCriteria());
        GetDataResponse response = queryController.execute(requestInfo, null);

        assertThat(response.getMeta().getAlert().getMessages().size(), is(1));
        assertThat(response.getMeta().getAlert().getMessages().get(0).getText(),
                is("Bad SQL grammar: Syntax error in SQL statement \"SELCT[*] * FROM TABLE\"; expected \"SET, SAVEPOINT, SCRIPT, SHUTDOWN, SHOW\""));
        assertThat(response.getMeta().getAlert().getMessages().get(0).getPayload().size(), is(1));
        assertThat(response.getMeta().getAlert().getMessages().get(0).getPayload().get(0), is("Executed query: SELCT * FROM table"));
    }

    @Test
    public void testRestException() {
        CompiledQuery query = builder.read().compile().get(new QueryContext("testRestException"));
        QueryRequestInfo requestInfo = new QueryRequestInfo();
        requestInfo.setQuery(query);
        requestInfo.setCriteria(new N2oPreparedCriteria());
        GetDataResponse response = queryController.execute(requestInfo, null);

        assertThat(response.getMeta().getAlert().getMessages().size(), is(1));
        assertThat(response.getMeta().getAlert().getMessages().get(0).getText(), is("404 Not Found: [no body]"));
        assertThat(response.getMeta().getAlert().getMessages().get(0).getPayload().size(), is(1));
        assertThat(response.getMeta().getAlert().getMessages().get(0).getPayload().get(0),
                is("Executed query: http://localhost:8080/data/car/search/findAll"));
    }
}
