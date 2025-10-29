package net.n2oapp.framework.test.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import net.n2oapp.framework.api.rest.ExportRequest;
import net.n2oapp.framework.api.rest.ExportResponse;
import net.n2oapp.framework.api.rest.GetDataResponse;
import net.n2oapp.framework.ui.controller.export.ExportController;
import net.n2oapp.framework.ui.servlet.table.ExportServlet;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class ExportServletTest {

    @Autowired
    private ServletRegistrationBean exportServlet;

    @MockitoBean
    private ExportController exportController;

    @Mock
    private ExportResponse exportResponse;

    @MockitoBean
    private GetDataResponse getDataResponse;

    @SneakyThrows
    @Test
    void export() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        URL resource = ExportServletTest.class.getClassLoader().getResource("META-INF/conf/test/data/export/testExport.csv");
        byte[] bytes = Files.readAllBytes(Paths.get(resource.toURI()));

        ExportRequest exportRequest = new ExportRequest();
        exportRequest.setFormat("csv");
        exportRequest.setCharset("UTF-8");
        exportRequest.setUrl("/n2o/data/_main?main_minPrice=5000&page=1&size=10&sorting.name=DESC");
        exportRequest.setFields(new HashMap<>());

        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(exportRequest);
        request.setContent(requestBody.getBytes(StandardCharsets.UTF_8));
        request.setMethod("POST");
        request.setContentType("application/json");

        doReturn(getDataResponse).when(exportController).getData(eq("/_main"), anyMap(), any());
        doReturn(exportResponse).when(exportController).export(any(), any(), any(), anyMap());

        doReturn(200).when(exportResponse).getStatus();
        doReturn("text/csv").when(exportResponse).getContentType();
        doReturn("UTF-8").when(exportResponse).getCharacterEncoding();
        doReturn("attachment;filename=testExport.csv").when(exportResponse).getContentDisposition();
        doReturn(bytes).when(exportResponse).getFile();

        ((ExportServlet) exportServlet.getServlet()).safeDoPost(request, response);

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo("text/csv;charset=UTF-8");
        assertThat(response.getCharacterEncoding()).isEqualTo("UTF-8");
        assertThat(response.getHeader("Content-Disposition")).isEqualTo("attachment;filename=testExport.csv");
        String exp = new String(bytes, StandardCharsets.UTF_8);
        String act = new String(response.getContentAsByteArray(), StandardCharsets.UTF_8);
        assertThat(act).isEqualTo(exp);
    }
}