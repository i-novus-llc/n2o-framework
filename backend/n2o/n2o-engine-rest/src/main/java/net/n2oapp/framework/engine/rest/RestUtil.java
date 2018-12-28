package net.n2oapp.framework.engine.rest;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.exception.N2oUserException;
import net.n2oapp.framework.api.metadata.global.dao.RestErrorMapping;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * User: operehod
 * Date: 29.01.2015
 * Time: 13:54
 */
public class RestUtil {

    private static final RestErrorMapping defaultErrorMapping
            = new RestErrorMapping("message", "detailedMessage", "stackTrace");


    public static N2oException toN2oRestException(ApacheRestClient.RestException e, RestErrorMapping mapping, String finalQuery, String method, Map<String, Object> args) {
        if (mapping == null)
            mapping = defaultErrorMapping;
        DataSet b = e.getBody();
        N2oException res;
        if (b != null) {
            String summaryMessage = retrieveMessage(mapping.getMessage(), b);
            String stacktrace = retrieveMessage(mapping.getStackTrace(), b);
            if (summaryMessage != null) {
                res = new N2oUserException(summaryMessage);
            } else {
                res = new N2oRestException(stacktrace, e.getHttpStatus());
                ((N2oRestException)res).setResponse(e.getResponse());
                res.setHttpStatus(e.getHttpStatus());
                fillBaseFields(finalQuery, method, args, (N2oRestException) res);
            }
            res.setHttpStatus(e.getHttpStatus());
        } else {
            res = new N2oRestException(e);
            ((N2oRestException)res).setResponse(e.getResponse());
            res.setHttpStatus(e.getHttpStatus());
            fillBaseFields(finalQuery, method, args, (N2oRestException) res);
        }
        return res;
    }


    public static N2oException toN2oRestException(Exception e, String finalQuery, String method, Map<String, Object> args) {
        N2oRestException res = new N2oRestException(e);
        fillBaseFields(finalQuery, method, args, res);
        return res;
    }


    private static void fillBaseFields(String finalQuery, String method, Map<String, Object> args, N2oRestException res) {
        res.setUrl(finalQuery);
        res.setMethod(method);
        res.setArgs(args);
    }

    private static String retrieveMessage(String mapping, DataSet dataSet) {
        return dataSet != null && mapping != null && dataSet.get(mapping) != null ? dataSet.get(mapping).toString() : null;
    }


    public static String encode(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return value;
        }
    }


}
