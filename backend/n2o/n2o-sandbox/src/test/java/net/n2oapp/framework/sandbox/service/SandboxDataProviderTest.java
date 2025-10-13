package net.n2oapp.framework.sandbox.service;

import lombok.SneakyThrows;
import net.n2oapp.framework.api.rest.GetDataResponse;
import net.n2oapp.framework.api.rest.SetDataResponse;
import net.n2oapp.framework.migrate.XmlIOVersionMigrator;
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
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

/**
 * Тест получения и установки значений провайдером данных
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {SandboxTestApplication.class, ViewController.class, SandboxPropertyResolver.class,
                ProjectTemplateHolder.class, SandboxTestDataProviderEngine.class, XsdSchemaParser.class, SandboxApplicationBuilderConfigurer.class},
        properties = {"n2o.access.deny_objects=false"})
@PropertySource("classpath:sandbox.properties")
@EnableAutoConfiguration
class SandboxDataProviderTest {

    private static final MockHttpServletRequest request = new MockHttpServletRequest();

    @Value("${n2o.sandbox.api.host}")
    private String host;

    @Value("${n2o.sandbox.api.port}")
    private Integer port;

    @MockBean
    private XmlIOVersionMigrator migrator;

    @Autowired
    private ViewController viewController;

    @MockBean
    private FileStorage fileStorage;

    @SneakyThrows
    @Test
    void testGetData() {
        request.setRequestURI("/sandbox/view/myProjectId/n2o/data/_w1");
        request.setParameters(new ParameterMap<>(Map.of("page", new String[]{"1"}, "size", new String[]{"10"})));
        mockFileStorage();

        ResponseEntity<GetDataResponse> response = viewController.getData("myProjectId", request);
        assertThat(response.getStatusCode().value(), is(200));
        assertThat(response.getBody().getPaging().getCount(), is(4));
        assertThat(response.getBody().getList().get(0).get("id"), is(1L));
        assertThat(response.getBody().getList().get(1).get("id"), is(2L));
        assertThat(response.getBody().getList().get(2).get("id"), is(3L));
        assertThat(response.getBody().getList().get(3).get("id"), is(4L));
        assertThat(response.getBody().getList().get(0).get("name"), is("test1"));
        assertThat(response.getBody().getList().get(1).get("name"), is("test2"));
        assertThat(response.getBody().getList().get(2).get("name"), is("test3"));
        assertThat(response.getBody().getList().get(3).get("name"), is("test4"));

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

    @SneakyThrows
    @Test
    void testSetData() {
        request.setRequestURI("/sandbox/view/myProjectId/n2o/data/w1/3/update/multi1");
        request.setParameters(new ParameterMap<>(Map.of("page", new String[]{"1"}, "size", new String[]{"10"})));
        mockFileStorage();

        ResponseEntity<SetDataResponse> response = viewController.setData("myProjectId",
                new LinkedHashMap<>(Map.of("name", "name3", "id", 3)), request);
        assertThat(response.getStatusCode().value(), is(200));
        assertThat(response.getBody().getData().get("id"), is(3));
        assertThat(response.getBody().getData().get("name"), is("name3"));
        assertThat(response.getBody().getMeta().getAlert().getMessages().get(0).getText(), is("Данные сохранены"));
    }

    @Test
    void testMigration() {
        String oldXml = "<?xml version='1.0' encoding='UTF-8'?>" +
                "<query xmlns=\"http://n2oapp.net/framework/config/schema/query-4.0\"/>";
        String newXml = "<?xml version='1.0' encoding='UTF-8'?>\r\n" +
                "<query xmlns=\"http://n2oapp.net/framework/config/schema/query-5.0\"/>";
        when(migrator.migrate(oldXml)).thenReturn(newXml);
        String result = viewController.migrate(oldXml);
        assertEquals(newXml, result);
    }

    @SneakyThrows
    @Test
    void testPagingCount() {
        request.setRequestURI("/sandbox/view/myProjectId/n2o/count/_w1");
        mockFileStorage();

        ResponseEntity<Integer> response = viewController.getCount("myProjectId", request);
        assertThat(response.getStatusCode().value(), is(200));
        assertThat(response.getBody(), is(4));
    }
}