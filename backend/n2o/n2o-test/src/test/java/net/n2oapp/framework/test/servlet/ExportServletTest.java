package net.n2oapp.framework.test.servlet;

import lombok.SneakyThrows;
import net.n2oapp.framework.api.rest.ExportResponse;
import net.n2oapp.framework.api.rest.GetDataResponse;
import net.n2oapp.framework.ui.controller.ExportController;
import net.n2oapp.framework.ui.servlet.table.ExportServlet;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doReturn;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ExportServletTest {

    @Autowired
    private ServletRegistrationBean exportServlet;

    @MockBean
    private ExportController exportController;

    @Mock
    private HttpServletRequest request;

    @Mock
    ExportResponse exportResponse;

    @MockBean
    private GetDataResponse getDataResponse;

    @SneakyThrows
    @Test
    public void export() {
        MockHttpServletResponse response = new MockHttpServletResponse();

        URL resource = ExportServletTest.class.getClassLoader().getResource("META-INF/conf/test/data/export/testExport.csv");
        byte[] bytes = Files.readAllBytes(Paths.get(resource.toURI()));

        doReturn("csv").when(request).getParameter("format");
        doReturn("charset").when(request).getParameter("UTF-8");
        doReturn("/n2o/data/_main?main_minPrice=5000&page=1&size=10&sorting.name=DESC").when(request).getParameter("url");

        doReturn(getDataResponse).when(exportController).getData(any(), anyMap(), any());
        doReturn(exportResponse).when(exportController).export(any(), any(), any());

        doReturn(200).when(exportResponse).getStatus();
        doReturn("csv").when(exportResponse).getFormat();
        doReturn("UTF-8").when(exportResponse).getCharset();
        doReturn("testExport.csv").when(exportResponse).getFileName();
        doReturn(bytes).when(exportResponse).getFile();

        ((ExportServlet) exportServlet.getServlet()).safeDoGet(request, response);

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo("csv;charset=UTF-8");
        assertThat(response.getCharacterEncoding()).isEqualTo("UTF-8");
        assertThat(response.getHeader("Content-Disposition")).isEqualTo("attachment;filename=testExport.csv");
        String exp = new String(bytes, StandardCharsets.UTF_8);
        String act = new String(response.getContentAsByteArray(), StandardCharsets.UTF_8);
        assertThat(act).isEqualTo(exp);
    }
}
