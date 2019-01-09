package net.n2oapp.framework.engine.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.util.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author operehod
 * @since 22.05.2015
 */
public class TestRestClient implements RestClient {
    private List<Query> queryList = new ArrayList<>();
    private DataSet response;
    private RestException exception;

    public TestRestClient(DataSet response) {
        this.response = response;
    }

    public TestRestClient(RestException exception) {
        this.exception = exception;
    }

    public void clear() {
        queryList.clear();
    }

    public Query getQuery() {
        assert queryList.size() == 1;
        return ((Query) (queryList.get(0)));
    }

    @Override
    public DataSet GET(String path, Map<String, Object> params, Map<String, String> headers, String host, Integer port) throws RestException {
        Query query = new Query();
        query.setPath(path);
        query.setParams(params);
        query.setHeaders(headers);
        query.setHost(host);
        query.setPort(port);
        queryList.add(query);
        if (exception != null) throw exception;
        return response;
    }

    @Override
    public DataSet POST(String query, Map<String, Object> args, Map<String, String> headers, String proxyHost, Integer proxyPort) throws RestException {
        if (exception != null) throw exception;
        Query query1 = new Query();
        query1.setPath(query);
        query1.setParams(args);
        query1.setHeaders(headers);
        query1.setHost(proxyHost);
        query1.setPort(proxyPort);
        queryList.add(query1);
        return response;
    }

    @Override
    public DataSet PUT(String query, Map<String, Object> args, Map<String, String> headers, String proxyHost, Integer proxyPort) throws RestException {
        if (exception != null) throw exception;
        Query query1 = new Query();
        query1.setPath(query);
        query1.setParams(args);
        query1.setParams(args);
        query1.setHeaders(headers);
        query1.setHost(proxyHost);
        query1.setPort(proxyPort);
        queryList.add(query1);
        return response;
    }

    @Override
    public DataSet DELETE(String query, Map<String, Object> args, Map<String, String> headers, String proxyHost, Integer proxyPort) throws RestException {
        if (exception != null) throw exception;
        Query query1 = new Query();
        query1.setPath(query);
        query1.setParams(args);
        query1.setHeaders(headers);
        query1.setHost(proxyHost);
        query1.setPort(proxyPort);
        queryList.add(query1);
        return response;
    }

    @Override
    public DataSet HEAD(String query, Map<String, Object> args, Map<String, String> headers, String proxyHost, Integer proxyPort) throws RestException {
        if (exception != null) throw exception;
        Query query1 = new Query();
        query1.setPath(query);
        query1.setParams(args);
        query1.setHeaders(headers);
        query1.setHost(proxyHost);
        query1.setPort(proxyPort);
        queryList.add(query1);
        return response;
    }

    @Override
    public ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }

    public List<Query> getQueryList() {
        return queryList;
    }

    public void setQueryList(List<Query> queryList) {
        this.queryList = queryList;
    }

    public DataSet getResponse() {
        return response;
    }

    public void setResponse(DataSet response) {
        this.response = response;
    }

    public RestException getException() {
        return exception;
    }

    public void setException(RestException exception) {
        this.exception = exception;
    }

    public static class Query {
        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public Map<String, Object> getParams() {
            return params;
        }

        public void setParams(Map<String, Object> params) {
            this.params = params;
        }

        public Map<String, String> getHeaders() {
            return headers;
        }

        public void setHeaders(Map<String, String> headers) {
            this.headers = headers;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public Integer getPort() {
            return port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }

        private String path;
        private Map<String, Object> params;
        private Map<String, String> headers;
        private String host;
        private Integer port;
    }
}
