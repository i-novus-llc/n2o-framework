package net.n2oapp.framework.sandbox.service;

import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {SandboxTestApplication.class, ViewController.class, SandboxPropertyResolver.class,
                ProjectTemplateHolder.class, SandboxTestDataProviderEngine.class, XsdSchemaParser.class, SandboxApplicationBuilderConfigurer.class},
        properties = {"n2o.access.deny_objects=false"})
@PropertySource("classpath:sandbox.properties")
@EnableAutoConfiguration
class SandboxExportTest {

    private static final String SAFE_JSON = """
            [
              {"id": 1, "name": "test1"},
              {"id": 2, "name": "test2"},
              {"id": 3, "name": "test3"},
              {"id": 4, "name": "test4"}
            ]
            """;

    private static final String INJECTION_JSON = """
            [
              {"id": 1, "name": "=cmd|'/c calc'!A1"},
              {"id": 2, "name": "+1+1"},
              {"id": 3, "name": "-1+1"},
              {"id": 4, "name": "@SUM(1,1)"},
              {"id": 5, "name": "\\t=1+1"},
              {"id": 6, "name": "\\r=1+1"},
              {"id": 7, "name": "safe"}
            ]
            """;

    @Value("${n2o.sandbox.api.host}")
    private String host;

    @Value("${n2o.sandbox.api.port}")
    private Integer port;

    @Autowired
    private ViewController viewController;

    @MockitoBean
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

        ExportRequest request = buildRequest("export_data", "id", "name");

        mockFileStorage(SAFE_JSON);

        ResponseEntity<byte[]> response = viewController.export("myProjectId", request, mock(HttpServletRequest.class));
        assertThat(response.getStatusCode().value(), is(200));
        assertThat(response.getBody(), is(expectedBody.getBytes(StandardCharsets.UTF_8)));
        HttpHeaders headers = response.getHeaders();
        assertThat(headers.getContentDisposition().toString()
                        .equals("attachment; filename=\"=?UTF-8?Q?export=5Fdata.csv?=\"; filename*=UTF-8''export_data.csv"),
                is(true));

        Optional<MediaType> contentType = Optional.ofNullable(headers.getContentType());
        assertThat(contentType.get().toString(), is("text/csv"));

        Optional<List<String>> contentEncoding = Optional.ofNullable(headers.get("Content-Encoding"));
        assertThat(contentEncoding.get().toString(), is("[UTF-8]"));

        assertThat(headers.getContentLength(), is(Integer.toUnsignedLong(expectedBody.getBytes(StandardCharsets.UTF_8).length)));
    }

    /**
     * Сквозной тест: данные с опасными префиксами, пройдя через ViewController.export,
     * должны получить апостроф и не попасть в CSV в виде, пригодном для исполнения
     * Excel/LibreOffice.
     */
    @SneakyThrows
    @Test
    void export_sanitizesFormulaInjection() {
        ExportRequest request = buildRequest("injection_data", "id", "name");

        mockFileStorage(INJECTION_JSON);

        ResponseEntity<byte[]> response = viewController.export("myProjectId", request, mock(HttpServletRequest.class));
        assertThat(response.getStatusCode().value(), is(200));

        String actual = new String(response.getBody(), StandardCharsets.UTF_8);

        // Опасные значения — с апострофом
        assertTrue(actual.contains("\"'=cmd|'/c calc'!A1\""), actual);
        assertTrue(actual.contains("\"'+1+1\""), actual);
        assertTrue(actual.contains("\"'-1+1\""), actual);
        assertTrue(actual.contains("\"'@SUM(1,1)\""), actual);

        // Безопасное значение осталось как есть
        assertTrue(actual.contains("\"safe\""), actual);

        // Ни одно опасное значение не попало без апострофа
        assertFalse(actual.contains("\"=cmd|'/c calc'!A1\""), actual);
        assertFalse(actual.contains("\"+1+1\""), actual);
        assertFalse(actual.contains("\"-1+1\""), actual);
        assertFalse(actual.contains("\"@SUM(1,1)\""), actual);
    }

    private ExportRequest buildRequest(String filename, String... fieldIds) {
        ExportRequest request = new ExportRequest();
        request.setFormat("csv");
        request.setCharset("UTF-8");
        request.setUrl("/n2o/data/_w1/?main_minPrice=5000&page=1&size=10&sorting.name=DESC");

        List<ExportRequest.ExportField> requestHeaders = new ArrayList<>();
        for (String fieldId : fieldIds) {
            ExportRequest.ExportField field = new ExportRequest.ExportField();
            field.setId(fieldId);
            field.setTitle(fieldId);
            requestHeaders.add(field);
        }
        request.setFields(requestHeaders);
        request.setFilename(filename);
        return request;
    }

    private void mockFileStorage(String jsonData) {
        List<FileModel> fileModels = new ArrayList<>();

        FileModel fileModel = getFileModel();
        fileModels.add(fileModel);

        FileModel testJson = new FileModel();
        testJson.setFile("test.json");
        testJson.setSource(jsonData);
        fileModels.add(testJson);

        FileModel testQuery = getTestQuery();
        fileModels.add(testQuery);

        String myProjectId = "myProjectId";
        doReturn(fileModels).when(fileStorage).getProjectFiles(myProjectId);
        doReturn("").when(fileStorage).getFileContent(myProjectId, "application.properties");
        doReturn("").when(fileStorage).getFileContent(myProjectId, "user.properties");
        doReturn(jsonData).when(fileStorage).getFileContent(myProjectId, "test.json");
    }

    private static FileModel getTestQuery() {
        FileModel testQuery = new FileModel();
        testQuery.setFile("test.query.xml");

        testQuery.setSource("""
                <?xml version='1.0' encoding='UTF-8'?>
                <query xmlns="http://n2oapp.net/framework/config/schema/query-5.0"
                       object-id="test">
                    <list>
                        <test file="test.json" operation="findAll"/>
                    </list>
                    <filters>
                        <eq field-id="id" filter-id="id"/>
                    </filters>
                    <fields>
                        <field id="id" domain="integer"/>
                        <field id="name"/>
                    </fields>
                </query>
                """);
        return testQuery;
    }

    private static FileModel getFileModel() {
        FileModel fileModel = new FileModel();
        fileModel.setFile("index.page.xml");
        fileModel.setSource("""
                <?xml version='1.0' encoding='UTF-8'?>
                <simple-page xmlns="http://n2oapp.net/framework/config/schema/page-4.0"
                             name="CRUD Операции">
                    <table auto-focus="true">
                        <datasource query-id="test"/>
                        <columns>
                            <column text-field-id="id"/>
                            <column text-field-id="name"/>
                        </columns>
                        <toolbar generate="crud"/>
                    </table>
                </simple-page>
                """);
        return fileModel;
    }
}