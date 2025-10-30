package net.n2oapp.framework.sandbox.service;

import lombok.SneakyThrows;
import net.n2oapp.framework.api.rest.ExportRequest;
import net.n2oapp.framework.sandbox.engine.SandboxTestDataProviderEngine;
import net.n2oapp.framework.sandbox.file_storage.FileStorage;
import net.n2oapp.framework.sandbox.file_storage.model.FileModel;
import net.n2oapp.framework.sandbox.resource.XsdSchemaParser;
import net.n2oapp.framework.sandbox.templates.ProjectTemplateHolder;
import net.n2oapp.framework.sandbox.view.SandboxApplicationBuilderConfigurer;
import net.n2oapp.framework.sandbox.view.SandboxPropertyResolver;
import net.n2oapp.framework.sandbox.view.ViewController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doReturn;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {SandboxTestApplication.class, ViewController.class, SandboxPropertyResolver.class,
                ProjectTemplateHolder.class, SandboxTestDataProviderEngine.class, XsdSchemaParser.class, SandboxApplicationBuilderConfigurer.class},
        properties = {"n2o.access.deny_objects=false"})
@PropertySource("classpath:sandbox.properties")
@EnableAutoConfiguration
class SandboxExportTest {

    @Value("${n2o.sandbox.api.host}")
    private String host;

    @Value("${n2o.sandbox.api.port}")
    private Integer port;

    @Autowired
    private ViewController viewController;

    @MockBean
    private FileStorage fileStorage;

    @SneakyThrows
    @Test
    void export() {
        String expectedBody = """
                "id";"name"
                1;"test1"
                2;"test2"
                3;"test3"
                4;"test4"
                """;

        ExportRequest request = new ExportRequest();
        request.setFormat("csv");
        request.setCharset("UTF-8");
        request.setUrl("/n2o/data/_w1?main_minPrice=5000&page=1&size=10&sorting.name=DESC");
        Map<String, String> requestHeaders = new LinkedHashMap<>();
        requestHeaders.put("id", "id");
        requestHeaders.put("name", "name");
        request.setFields(requestHeaders);

        mockFileStorage();

        ResponseEntity<byte[]> response = viewController.export("myProjectId", request);
        assertThat(response.getStatusCode().value(), is(200));
        assertThat(response.getBody(), is(expectedBody.getBytes(StandardCharsets.UTF_8)));
        HttpHeaders headers = response.getHeaders();
        assertThat(headers.getContentDisposition().toString().matches("attachment; filename=\"export_data_\\d{13}\\.csv\""), is(true));

        Optional<MediaType> contentType = Optional.of(headers.getContentType());
        assertThat(contentType.get().toString(), is("text/csv"));

        Optional<List<String>> contentEncoding = Optional.of(headers.get("Content-Encoding"));
        assertThat(contentEncoding.get().toString(), is("[UTF-8]"));

        assertThat(headers.getContentLength(), is(Integer.toUnsignedLong(expectedBody.getBytes(StandardCharsets.UTF_8).length)));
    }

    private void mockFileStorage() {
        List<FileModel> fileModels = new ArrayList<>();

        FileModel fileModel = new FileModel();
        fileModel.setFile("index.page.xml");
        fileModel.setSource("""
                <?xml version='1.0' encoding='UTF-8'?>
                <simple-page xmlns="http://n2oapp.net/framework/config/schema/page-3.0"
                             name="CRUD Операции">
                    <table query-id="test" auto-focus="true">
                        <columns>
                            <column text-field-id="id"/>
                            <column text-field-id="name"/>
                        </columns>
                        <toolbar generate="crud"/>
                    </table>
                </simple-page>
                """);
        fileModels.add(fileModel);

        FileModel testJson = new FileModel();
        testJson.setFile("test.json");
        testJson.setSource("""
                [
                  {
                    "id": 1,
                    "name": "test1"
                  },
                  {
                    "id": 2,
                    "name": "test2"
                  },
                  {
                    "id": 3,
                    "name": "test3"
                  },
                  {
                    "id": 4,
                    "name": "test4"
                  }
                ]
                """);
        fileModels.add(testJson);

        FileModel testObject = new FileModel();
        testObject.setFile("test.object.xml");
        testObject.setSource("""
                <?xml version='1.0' encoding='UTF-8'?>
                <object xmlns="http://n2oapp.net/framework/config/schema/object-4.0">
                    <operations>
                        <operation id="create">
                            <invocation>
                                <test file="test.json" operation="create"/>
                            </invocation>
                            <in>
                                <field id="name"/>
                            </in>
                            <out>
                                <field id="id"/>
                            </out>
                        </operation>

                        <operation id="update">
                            <invocation>
                                <test file="test.json" operation="update"/>
                            </invocation>
                            <in>
                                <field id="id"/>
                                <field id="name"/>
                            </in>
                        </operation>

                        <operation id="delete">
                            <invocation>
                                <test file="test.json" operation="delete"/>
                            </invocation>
                            <in>
                                <field id="id"/>
                            </in>
                        </operation>
                    </operations>
                </object>
                """);
        fileModels.add(testObject);

        FileModel testPage = new FileModel();
        testPage.setFile("test.page.xml");
        testPage.setSource("""
                <?xml version='1.0' encoding='UTF-8'?>
                <simple-page xmlns="http://n2oapp.net/framework/config/schema/page-3.0">
                    <form query-id="test">
                        <fields>
                            <input-text id="name"/>
                        </fields>
                    </form>
                </simple-page>
                """);
        fileModels.add(testPage);

        FileModel testQuery = new FileModel();
        testQuery.setFile("test.query.xml");
        testQuery.setSource("""
                <?xml version='1.0' encoding='UTF-8'?>
                <query xmlns="http://n2oapp.net/framework/config/schema/query-4.0"
                       object-id="test">
                    <list>
                        <test file="test.json" operation="findAll"/>
                    </list>

                    <fields>
                        <field id="id" domain="integer">
                            <select/>
                            <filters>
                                <eq filter-id="id"/>
                            </filters>
                        </field>
                        <field id="name">
                            <select/>
                        </field>
                    </fields>
                </query>
                """);
        fileModels.add(testQuery);

        String myProjectId = "myProjectId";
        doReturn(fileModels).when(fileStorage).getProjectFiles(myProjectId);
        doReturn("").when(fileStorage).getFileContent(myProjectId, "application.properties");
        doReturn("").when(fileStorage).getFileContent(myProjectId, "user.properties");
        doReturn("""
                [
                  {
                    "id": 1,
                    "name": "test1"
                  },
                  {
                    "id": 2,
                    "name": "test2"
                  },
                  {
                    "id": 3,
                    "name": "test3"
                  },
                  {
                    "id": 4,
                    "name": "test4"
                  }
                ]
                """).when(fileStorage).getFileContent(myProjectId, "test.json");
    }
}