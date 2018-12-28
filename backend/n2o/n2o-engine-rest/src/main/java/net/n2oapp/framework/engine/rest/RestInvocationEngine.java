package net.n2oapp.framework.engine.rest;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.data.MapInvocationEngine;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oRestInvocation;
import net.n2oapp.framework.api.util.RestClient;
import net.n2oapp.framework.engine.util.json.DateFormatSerializingTemplate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Сервис вызова rest клиента
 */
@Deprecated
public class RestInvocationEngine implements MapInvocationEngine<N2oRestInvocation> {

    private DateFormatSerializingTemplate<DataSet> dateFormatSerializingTemplate = new DateFormatSerializingTemplate<>();

    private RestClient restClient;
    private RestProcessingEngine processing;


    public RestInvocationEngine(RestClient restClient, RestProcessingEngine processing) {
        this.restClient = restClient;
        this.processing = processing;
    }

    @Override
    public Object invoke(N2oRestInvocation invocation, Map<String, Object> data) {
        String query = invocation.getQuery();
        final String method = invocation.getMethod();
        Map<String, Object> args = new HashMap<> ();
        data.forEach(args::put);
        query = resolvePathPlaceholders(query, args);
        final String finalQuery = query;
        return dateFormatSerializingTemplate.execute(invocation.getDateFormat(), () -> {
            try {
                return executeQuery(method, finalQuery, args, invocation.getProxyHost(), invocation.getProxyPort());
            } catch (RestClient.RestException e) {
                throw RestUtil.toN2oRestException(e, invocation.getErrorMapping(), finalQuery, method, args);
            } catch (Exception e) {
                throw RestUtil.toN2oRestException(e, finalQuery, method, args);
            }
        });
    }

    private DataSet executeQuery(String method, String query, Map<String, Object> args, String proxyHost,
                                 Integer proxyPort) throws ApacheRestClient.RestException {

        Map<String, String> headers = new HashMap<>();
        args = new HashMap<>(args);
        processing.process(method, query, args, headers);

        switch (method) {
            case "GET":
                return restClient.GET(query, args, headers, proxyHost, proxyPort);
            case "POST":
                return restClient.POST(query, args, headers, proxyHost, proxyPort);
            case "PUT":
                return restClient.PUT(query, args, headers, proxyHost, proxyPort);
            case "DELETE":
                return restClient.DELETE(query, args, headers, proxyHost, proxyPort);
        }
        return new DataSet();
    }

    private String resolvePathPlaceholders(String query, Map<String, Object> args) {
        for (String key : new HashSet<>(args.keySet())) {
            String p = "{" + key + "}";
            if (query.contains(p)) {
                String value = args.get(key) == null ? "" : RestUtil.encode(args.get(key).toString());
                query = query.replace("{" + key + "}", value);
                args.remove(key);
            }
        }
        return query;
    }


    @Override
    public Class<N2oRestInvocation> getType() {
        return N2oRestInvocation.class;
    }
}
