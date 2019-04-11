package net.n2oapp.framework.engine.rest;


import net.n2oapp.criteria.dataset.DataSet;

import java.util.Map;

/**
 * @deprecated В 7.1 перейдем на RestTemplate
 */
@Deprecated
public interface RestClient {


    default Object GET(String path, Map<String, Object> params, Map<String, String> headers) throws RestException {
        return GET(path, params, headers, null, null);
    }

    default Object GET(String path, Map<String, Object> params) throws RestException {
        return GET(path, params, null, null, null);
    }

    default Object GET(String path) throws RestException {
        return GET(path, null, null, null, null);
    }


    Object GET(String path, Map<String, Object> params, Map<String, String> headers, String host,
               Integer port) throws RestException;

    Object POST(String query, Map<String, Object> args, Map<String, String> headers, String proxyHost, Integer proxyPort) throws RestException;

    Object PUT(String query, Map<String, Object> args, Map<String, String> headers, String proxyHost, Integer proxyPort) throws RestException;

    Object DELETE(String query, Map<String, Object> args, Map<String, String> headers, String proxyHost, Integer proxyPort) throws RestException;

    Object HEAD(String query, Map<String, Object> args, Map<String, String> headers, String proxyHost, Integer proxyPort) throws RestException;


    class RestException extends RuntimeException {
        private int httpStatus;
        private DataSet body;
        private String response;

        public RestException(int httpStatus, DataSet body) {
            super("http response status " + httpStatus);
            this.httpStatus = httpStatus;
            this.body = body;
        }

        public RestException(int httpStatus, String response) {
            super("http response status " + httpStatus);
            this.httpStatus = httpStatus;
            this.response = response;
        }

        public int getHttpStatus() {
            return httpStatus;
        }

        public DataSet getBody() {
            return body;
        }

        public String getResponse() {
            return response;
        }
    }

}
