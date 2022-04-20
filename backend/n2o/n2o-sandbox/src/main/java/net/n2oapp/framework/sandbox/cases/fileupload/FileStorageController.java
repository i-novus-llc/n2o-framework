package net.n2oapp.framework.sandbox.cases.fileupload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;

@Component
public class FileStorageController implements StorageController {

    @Value("${n2o.sandbox.url}/file")
    private String requestUrl;
    private RestTemplate restTemplate;

    public FileStorageController() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public ListResponse getList() {
        return restTemplate.getForObject(requestUrl + LIST_PREFIX, FileListResponse.class);
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    static class FileListResponse implements ListResponse {
        @JsonProperty
        private Collection<FileModel> files;
    }
}
