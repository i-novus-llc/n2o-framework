package net.n2oapp.framework.api.util;


import net.n2oapp.criteria.dataset.DataSet;

import java.util.Map;

/**
 * User: operhod
 * Date: 18.01.14
 * Time: 13:03
 */
public interface RestClient {


    default DataSet GET(String path, Map<String, Object> params, Map<String, String> headers) throws RestException {
        return GET(path, params, headers, null, null);
    }

    default DataSet GET(String path, Map<String, Object> params) throws RestException {
        return GET(path, params, null, null, null);
    }

    default DataSet GET(String path) throws RestException {
        return GET(path, null, null, null, null);
    }


    DataSet GET(String path, Map<String, Object> params, Map<String, String> headers, String host,
                Integer port) throws RestException;

    DataSet POST(String query, Map<String, Object> args, Map<String, String> headers, String proxyHost, Integer proxyPort) throws RestException;

    DataSet PUT(String query, Map<String, Object> args, Map<String, String> headers, String proxyHost, Integer proxyPort) throws RestException;

    DataSet DELETE(String query, Map<String, Object> args, Map<String, String> headers, String proxyHost, Integer proxyPort) throws RestException;

    DataSet HEAD(String query, Map<String, Object> args, Map<String, String> headers, String proxyHost, Integer proxyPort) throws RestException;


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
