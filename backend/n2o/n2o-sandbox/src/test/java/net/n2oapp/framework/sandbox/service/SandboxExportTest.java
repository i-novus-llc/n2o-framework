package net.n2oapp.framework.sandbox.service;

import lombok.SneakyThrows;
import net.n2oapp.framework.sandbox.engine.SandboxTestDataProviderEngine;
import net.n2oapp.framework.sandbox.file_storage.FileStorage;
import net.n2oapp.framework.sandbox.file_storage.model.FileModel;
import net.n2oapp.framework.sandbox.resource.XsdSchemaParser;
import net.n2oapp.framework.sandbox.templates.ProjectTemplateHolder;
import net.n2oapp.framework.sandbox.view.SandboxApplicationBuilderConfigurer;
import net.n2oapp.framework.sandbox.view.SandboxPropertyResolver;
import net.n2oapp.framework.sandbox.view.ViewController;
import org.apache.catalina.util.ParameterMap;
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
import org.springframework.mock.web.MockHttpServletRequest;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {SandboxTestApplication.class, ViewController.class, SandboxPropertyResolver.class,
                ProjectTemplateHolder.class, SandboxTestDataProviderEngine.class, XsdSchemaParser.class, SandboxApplicationBuilderConfigurer.class},
        properties = {"n2o.access.deny_objects=false"})
@PropertySource("classpath:sandbox.properties")
@EnableAutoConfiguration
class SandboxExportTest {

    private static final MockHttpServletRequest request = new MockHttpServletRequest();

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
        String expectedBody = "\"id\";\"name\"\n" +
                "1;\"test1\"\n" +
                "2;\"test2\"\n" +
                "3;\"test3\"\n" +
                "4;\"test4\"\n";

        request.setRequestURI("/sandbox/view/myProjectId/n2o/export/_w1");
        request.setParameters(new ParameterMap<>(Map.of(
                "page", new String[]{"1"},
                "size", new String[]{"10"},
                "format", new String[]{"csv"},
                "charset", new String[]{"UTF-8"},
                "url", new String[]{"/n2o/data/_w1?main_minPrice=5000&page=1&size=10&sorting.name=DESC"})));
        mockFileStorage();

        ResponseEntity<byte[]> response = viewController.export("myProjectId", request);
        assertThat(response.getStatusCodeValue(), is(200));
        assertThat(response.getBody(), is(expectedBody.getBytes(StandardCharsets.UTF_8)));
        HttpHeaders headers = response.getHeaders();
        assertThat(headers.getContentDisposition().toString().matches("attachment; filename=\"export_data_\\d{13}\\.csv\""), is(true));

        Optional<MediaType> contentType = Optional.ofNullable(headers.getContentType());
        assertTrue(contentType.isPresent());
        assertThat(contentType.get().toString(), is("text/csv"));

        Optional<List<String>> contentEncoding = Optional.ofNullable(headers.get("Content-Encoding"));
        assertTrue(contentEncoding.isPresent());
        assertThat(contentEncoding.get().toString(), is("[UTF-8]"));

        assertThat(headers.getContentLength(), is(Integer.toUnsignedLong(expectedBody.getBytes(StandardCharsets.UTF_8).length)));
    }

    private void mockFileStorage() {
        List<FileModel> fileModels = new ArrayList<>();
        FileModel fileModel = new FileModel();
        fileModel.setFile("index.page.xml");
        fileModel.setSource("<?xml version='1.0' encoding='UTF-8'?>\r\n<simple-page xmlns=\"http://n2oapp.net/framework/config/schema/page-3.0\"\r\n             name=\"CRUD Операции\">\r\n    <table query-id=\"test\" auto-focus=\"true\">\r\n        <columns>\r\n            <column text-field-id=\"id\"/>\r\n            <column text-field-id=\"name\"/>\r\n        </columns>\r\n        <toolbar generate=\"crud\"/>\r\n    </table>\r\n</simple-page>\r\n");
        fileModels.add(fileModel);
        FileModel testJson = new FileModel();
        testJson.setFile("test.json");
        testJson.setSource("[\r\n  {\r\n    \"id\": 1,\r\n    \"name\": \"test1\"\r\n  },\r\n  {\r\n    \"id\": 2,\r\n    \"name\": \"test2\"\r\n  },\r\n  {\r\n    \"id\": 3,\r\n    \"name\": \"test3\"\r\n  },\r\n  {\r\n    \"id\": 4,\r\n    \"name\": \"test4\"\r\n  }\r\n]\r\n");
        fileModels.add(testJson);
        FileModel testObject = new FileModel();
        testObject.setFile("test.object.xml");
        testObject.setSource("<?xml version='1.0' encoding='UTF-8'?>\r\n<object xmlns=\"http://n2oapp.net/framework/config/schema/object-4.0\">\r\n    <operations>\r\n        <operation id=\"create\">\r\n            <invocation>\r\n                <test file=\"test.json\" operation=\"create\"/>\r\n            </invocation>\r\n            <in>\r\n                <field id=\"name\"/>\r\n            </in>\r\n            <out>\r\n                <field id=\"id\"/>\r\n            </out>\r\n        </operation>\r\n\r\n        <operation id=\"update\">\r\n            <invocation>\r\n                <test file=\"test.json\" operation=\"update\"/>\r\n            </invocation>\r\n            <in>\r\n                <field id=\"id\"/>\r\n                <field id=\"name\"/>\r\n            </in>\r\n        </operation>\r\n\r\n        <operation id=\"delete\">\r\n            <invocation>\r\n                <test file=\"test.json\" operation=\"delete\"/>\r\n            </invocation>\r\n            <in>\r\n                <field id=\"id\"/>\r\n            </in>\r\n        </operation>\r\n    </operations>\r\n</object>\r\n");
        fileModels.add(testObject);
        FileModel testPage = new FileModel();
        testPage.setFile("test.page.xml");
        testPage.setSource("<?xml version='1.0' encoding='UTF-8'?>\r\n<simple-page xmlns=\"http://n2oapp.net/framework/config/schema/page-3.0\">\r\n    <form query-id=\"test\">\r\n        <fields>\r\n            <input-text id=\"name\"/>\r\n        </fields>\r\n    </form>\r\n</simple-page>\r\n");
        fileModels.add(testPage);
        FileModel testQuery = new FileModel();
        testQuery.setFile("test.query.xml");
        testQuery.setSource("<?xml version='1.0' encoding='UTF-8'?>\r\n<query xmlns=\"http://n2oapp.net/framework/config/schema/query-4.0\"\r\n       object-id=\"test\">\r\n    <list>\r\n        <test file=\"test.json\" operation=\"findAll\"/>\r\n    </list>\r\n\r\n    <fields>\r\n        <field id=\"id\" domain=\"integer\">\r\n            <select/>\r\n            <filters>\r\n                <eq filter-id=\"id\"/>\r\n            </filters>\r\n        </field>\r\n        <field id=\"name\">\r\n            <select/>\r\n        </field>\r\n    </fields>\r\n</query>\r\n");
        fileModels.add(testQuery);
        String myProjectId = "myProjectId";
        doReturn(fileModels).when(fileStorage).getProjectFiles(myProjectId);
        doReturn("").when(fileStorage).getFileContent(myProjectId, "application.properties");
        doReturn("").when(fileStorage).getFileContent(myProjectId, "user.properties");
        doReturn("[\r\n  {\r\n    \"id\": 1,\r\n    \"name\": \"test1\"\r\n  },\r\n  {\r\n    \"id\": 2,\r\n    \"name\": \"test2\"\r\n  },\r\n  {\r\n    \"id\": 3,\r\n    \"name\": \"test3\"\r\n  },\r\n  {\r\n    \"id\": 4,\r\n    \"name\": \"test4\"\r\n  }\r\n]\r\n").when(fileStorage).getFileContent(myProjectId, "test.json");
    }
}
