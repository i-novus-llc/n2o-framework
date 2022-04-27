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
public class PersonStorageController implements StorageController {

    private static final String CREATE_PREFIX = "/create";
    private static final String UPDATE_PREFIX = "/update";
    @Value("${n2o.sandbox.url}/persons")
    private String requestUrl;
    private RestTemplate restTemplate;

    public PersonStorageController() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public ListResponse getList() {
        return restTemplate.getForObject(requestUrl + LIST_PREFIX, PersonListResponse.class);
    }

    public PersonModel getById(String id) {
        return restTemplate.getForObject(requestUrl + "/" + id, PersonModel.class);
    }

    public String createPerson(PersonModel person) {
        return restTemplate.postForObject(requestUrl + CREATE_PREFIX, person, String.class);
    }

    public void updatePerson(PersonModel person) {
        restTemplate.put(requestUrl + UPDATE_PREFIX, person);
    }

    public void deletePerson(String id) {
        restTemplate.delete(requestUrl + "/" + id);
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    static class PersonListResponse implements ListResponse {
        @JsonProperty
        private Collection<PersonModel> persons;
    }
}
