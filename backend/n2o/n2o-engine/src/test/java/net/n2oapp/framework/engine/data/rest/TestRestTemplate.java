package net.n2oapp.framework.engine.data.rest;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.mock.http.client.MockClientHttpRequest;
import org.springframework.mock.http.client.MockClientHttpResponse;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

public class TestRestTemplate extends RestTemplate {

    private String query;
    private Object requestBody;
    private ClientHttpResponse mockResponse;

    public TestRestTemplate(ClientHttpResponse mockResponse) {
        this.mockResponse = mockResponse;
    }

    public TestRestTemplate(String mockResponseBody) {
        this.mockResponse = new MockClientHttpResponse(mockResponseBody.getBytes(StandardCharsets.UTF_8), HttpStatus.OK);
    }

    @Override
    public <T> RequestCallback httpEntityCallback(Object requestBody, Type responseType) {
        this.requestBody = requestBody;
        return super.httpEntityCallback(requestBody, responseType);
    }

    public String getQuery() {
        return query;
    }

    public Object getRequestBody() {
        return (requestBody instanceof HttpEntity ? ((HttpEntity) requestBody).getBody() : requestBody);
    }

    public Object getRequestHeader() {
        return (requestBody instanceof HttpEntity ? ((HttpEntity) requestBody).getHeaders() : Collections.EMPTY_MAP);
    }

    @Override
    protected ClientHttpRequest createRequest(URI url, HttpMethod method) throws IOException {
        this.query = url.toString();
        MockClientHttpRequest mockRequest = new MockClientHttpRequest(method, url);
        mockRequest.setResponse(mockResponse);
        return mockRequest;
    }
}
