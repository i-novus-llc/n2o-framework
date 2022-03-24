package net.n2oapp.framework.sandbox.fileupload;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

import static net.n2oapp.framework.config.register.route.RouteUtil.normalize;

/**
 * Клиент для отправки запросов на PersonDocRestController примера "Ячейка загрузки файлов"
 */
@Component
public class PersonDocRestClient {

    private static final String FILES_PREFIX = "/files";
    @Value("${n2o.sandbox.case.file_upload}")
    private String baseUrl;
    private RestTemplate restTemplate;

    public PersonDocRestClient() {
        this.restTemplate = new RestTemplate();
    }

    public FileModel uploadFile(String personId, MultipartFile file) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity request = new HttpEntity<>(headers);
        return restTemplate.postForObject(baseUrl + normalize(personId) + FILES_PREFIX + "?file={file}", request, FileModel.class, Map.of("file", file));
    }

    public void deleteFile(String personId, String id) {
        restTemplate.delete(baseUrl + normalize(personId) + FILES_PREFIX + normalize(id));
    }

    public Resource downloadFile(@PathVariable String personId, @PathVariable String fileName) {
        return restTemplate.getForObject(baseUrl + normalize(personId) + FILES_PREFIX + normalize(fileName), Resource.class);
    }
}
