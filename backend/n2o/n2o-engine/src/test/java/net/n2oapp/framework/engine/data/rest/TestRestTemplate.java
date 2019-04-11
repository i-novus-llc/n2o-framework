package net.n2oapp.framework.engine.data.rest;

import lombok.Getter;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Getter
public class TestRestTemplate extends RestTemplate {

    private String query;
    private HttpEntity<?> requestEntity;
    private Class<?> responseType;


    @Override
    public <T> ResponseEntity<T> exchange(String url, HttpMethod method,
                                          HttpEntity<?> requestEntity, Class<T> responseType, Object... uriVariables) throws RestClientException {

        this.query = url;
        this.requestEntity = requestEntity;
        this.responseType = responseType;
        return new ResponseEntity<>(requestEntity.getHeaders(), HttpStatus.OK);
    }

}
