package net.n2oapp.framework.engine.rest;


import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.util.RestClient;
import net.n2oapp.framework.engine.util.json.DateFormatHolder;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ProtocolException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * User: operhod
 * Date: 18.01.14
 * Time: 13:03
 */
public class ApacheRestClient implements RestClient {

    private static Logger logger = LoggerFactory.getLogger(ApacheRestClient.class);
    private ObjectMapper mapper;
    private static final String CONTENT_TYPE = "application/json";
    private Integer timeoutInMillis;

    public ApacheRestClient() {
    }

    public ApacheRestClient(String millis) {
        timeoutInMillis = Integer.parseInt(millis) * 1000;
    }

    @Required
    public void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public ObjectMapper getObjectMapper() {
        return mapper;
    }

    //API
    public Object GET(String path, Map<String, Object> params, Map<String, String> headers, String host,
                      Integer port) throws RestException {
        return doRequestWithPathParameters(path, params, headers, host, port, "GET");
    }

    public Object POST(String query, Map<String, Object> args, Map<String, String> headers, String proxyHost, Integer proxyPort) throws RestException {
        return doRequestWithBody(query, args, headers, proxyHost, proxyPort, "POST");
    }

    public Object PUT(String query, Map<String, Object> args, Map<String, String> headers, String proxyHost, Integer proxyPort) throws RestException {
        return doRequestWithBody(query, args, headers, proxyHost, proxyPort, "PUT");
    }

    public Object DELETE(String query, Map<String, Object> args, Map<String, String> headers, String proxyHost, Integer proxyPort) throws RestException {
        return doRequestWithPathParameters(query, args, headers, proxyHost, proxyPort, "DELETE");
    }

    @Override
    public Object HEAD(String query, Map<String, Object> args, Map<String, String> headers, String proxyHost, Integer proxyPort) throws RestException {
        return doRequestWithPathParameters(query, args, headers, proxyHost, proxyPort, "HEAD");
    }


    //inner methods
    private static String getURL(String host, Integer port, String url) throws IOException {
        if (host == null || port == null)
            return url;
        else
            return "http://" + host + ":" + port + url;
    }

    private static String addParams(String path, Map<String, Object> params) throws UnsupportedEncodingException {
        if (params == null) return path;
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String key : params.keySet()) {
            Object value = params.get(key);
            if (value != null && value instanceof Collection)
                for (Object el : (Collection) value) {
                    first = addValue(sb, first, key, el);
                }
            else
                first = addValue(sb, first, key, value);
        }
        path += sb.toString();
        return path;
    }

    private static boolean addValue(StringBuilder sb, boolean first, String key, Object value) throws UnsupportedEncodingException {
        if (first) sb.append("?");
        else sb.append("&");
        sb.append(URLEncoder.encode(key, "UTF-8"));
        sb.append("=");
        sb.append(URLEncoder.encode(toString(value), "UTF-8"));
        return false;
    }

    private static String toString(Object value) {

        if (value == null)
            return "null";

        if (value instanceof Date) {
            String format = DateFormatHolder.getDateFormat();
            if (format != null) {
                return new SimpleDateFormat(format).format(value);
            }
        }

        return value.toString();
    }

    private static void addHeaders(HttpRequestBase request, Map<String, String> headers) {
        request.addHeader("Content-Type", CONTENT_TYPE);
        if (headers == null) return;
        for (String key : headers.keySet()) {
            request.addHeader(key, headers.get(key));
        }
    }

    private DataSet doRequestWithBody(String query, Map<String, Object> body, Map<String, String> headers,
                                      String proxyHost,
                                      Integer proxyPort, String method) throws RestException {

        HttpResponse response = null;
        String url = null;
        String result = null;
        String sbody = "";
        try (CloseableHttpClient client = getHttpClient()) {
            url = getURL(proxyHost, proxyPort, query.trim());
            HttpEntityEnclosingRequestBase request;
            switch (method) {
                case "POST":
                    request = new HttpPost(url);
                    break;
                case "PUT":
                    request = new HttpPut(url);
                    break;
                default:
                    return null;
            }
            if (logger.isDebugEnabled()) {
                logger.debug("Request to remote http service.\r\n  URL: {}\r\n  Method: {}\r\n  Body: {}\r\n  Headers: {}", query.trim(), method,
                        (body != null && body.size() != 0) ? mapper.writeValueAsString(body) : "",
                        (headers != null && headers.size() != 0) ? headers.toString() : "");
            }
            prepareConnection(headers, request);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            mapper.writeValue(os, body);
            sbody = os.toString("UTF-8");
            StringEntity se = new StringEntity(sbody, "UTF-8");
            se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, CONTENT_TYPE));
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "text/plain; charset=UTF-8"));
            request.setEntity(se);
            response = client.execute(request);
            TypeReference<DataSet> typeRef = new TypeReference<DataSet>() {
            };
            result = getResult(response);
            DataSet resultData;
            try {
                resultData = !result.isEmpty() ? mapper.readValue(result, typeRef) : new DataSet();
            } catch (JsonParseException e) {
                if (!isSuccess(response))
                    //Если в ответе html 400, 404, 500, а не json
                    resultData = null;
                else
                    throw e;
            }
            if (!isSuccess(response)) throw new RestException(response.getStatusLine().getStatusCode(), resultData);
            return resultData;
        } catch (IOException e) {
            throw createRuntimeException(e, url, response, method, sbody, headers, result);
        }
    }


    private Object doRequestWithPathParameters(String query, Map<String, Object> args,
                                               Map<String, String> headers,
                                               String proxyHost,
                                               Integer proxyPort, String method) throws RestException {
        CloseableHttpClient client = null;
        HttpResponse response = null;
        String url = null;
        String result = null;
        String sargs = "";
        try {
            sargs = addParams(query.trim(), args);
            url = getURL(proxyHost, proxyPort, sargs);
            client = getHttpClient();
            HttpRequestBase request = null;
            switch (method) {
                case "GET":
                    request = new HttpGet(url);
                    break;
                case "DELETE":
                    request = new HttpDelete(url);
                    break;
                default:
                    throw new UnsupportedOperationException("Http method is not specified");
            }
            if (logger.isDebugEnabled()) {
                logger.debug("Request to remote http service.\r\n  URL: {}\r\n  Method: {}\r\n  Headers: {}", url.trim(),
                        method,
                        (headers != null && headers.size() != 0) ? headers.toString() : "");
            }
            prepareConnection(headers, request);
            response = client.execute(request);
            Object data = null;
            result = getResult(response);
            if (result != null && !result.isEmpty()) {
                result = result.trim();
                if (result.startsWith("["))
                    data = mapper.<List<DataSet>>readValue(result, mapper.getTypeFactory().constructCollectionType(List.class, DataSet.class));
                else if (result.startsWith("{"))
                    data = mapper.readValue(result, DataSet.class);
                else
                    data = mapper.readValue(result, Object.class);
            }
            if (logger.isDebugEnabled())
                logger.debug("Response from remote rest service: {}", result != null ? result : "");
            if (!isSuccess(response))
                throw new RestException(response.getStatusLine().getStatusCode(), result);
            return data;
        } catch (IOException e) {
            throw createRuntimeException(e, url, response, method, null, headers, result);
        } finally {
            try {
                if (client != null) client.close();
            } catch (IOException e) {
                throw createRuntimeException(e, url, response, null, sargs, headers, result);
            }
        }
    }

    private static String getResult(HttpResponse response) throws IOException {
        String result = response.getEntity() != null && response.getEntity().getContentLength() != 0 ? EntityUtils.toString(response.getEntity(), "UTF-8") : "";
        EntityUtils.consume(response.getEntity());
        return result;
    }

    private static void prepareConnection(Map<String, String> headers, HttpRequestBase request) throws ProtocolException {
        addHeaders(request, headers);
    }

    private static RuntimeException createRuntimeException(IOException e, String url, HttpResponse response, String method, String body, Map<String, String> headers, String res) {
        return new N2oException(buildMessage(url, response, method, body, headers, res), e);
    }

    private static String buildMessage(String url, HttpResponse response, String method, String body, Map<String, String> headers, String res) {
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP request error.");
        sb.append("\r\n REQUEST:");
        sb.append("\r\n   URL: ");
        sb.append(url != null ? url.trim() : "undefined");
        sb.append("\r\n   Method: ");
        sb.append(method.trim());
        sb.append("\r\n   Body: ");
        sb.append(body != null ? body : "undefined");
        sb.append("\r\n   Headers: ");
        sb.append(headers != null && headers.size() != 0 ? headers.toString() : "undefined");
        sb.append("\r\n RESPONSE:");
        sb.append("\r\n   Response-code: ");
        sb.append(getStatus(response));
        sb.append("\r\n   Body: ");
        sb.append(res != null ? res : "undefined");
        return sb.toString();
    }

    private static String getStatus(HttpResponse response) {
        String status = "undefined";
        try {
            if (response.getStatusLine() == null)
                return status;
            status = String.valueOf(response.getStatusLine().getStatusCode());
        } catch (Exception e) {
            logger.debug("cannot get status in http response", e);
        }
        return status;
    }

    private static boolean isSuccess(HttpResponse response) throws IOException {
        return response.getStatusLine().getStatusCode() >= 200 && response.getStatusLine().getStatusCode() < 300;
    }

    private CloseableHttpClient getHttpClient() {
        RequestConfig.Builder requestBuilder = RequestConfig.custom();
        requestBuilder = requestBuilder.setConnectTimeout(timeoutInMillis);
        requestBuilder = requestBuilder.setSocketTimeout(timeoutInMillis);

        HttpClientBuilder builder = HttpClientBuilder.create();
        builder.setDefaultRequestConfig(requestBuilder.build());
        return builder.build();
    }

}
