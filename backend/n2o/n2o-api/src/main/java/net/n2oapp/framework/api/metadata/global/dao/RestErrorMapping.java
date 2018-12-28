package net.n2oapp.framework.api.metadata.global.dao;

import java.io.Serializable;

/**
* User: operehod
* Date: 29.01.2015
* Time: 13:57
*/
public class RestErrorMapping implements Serializable {
    public RestErrorMapping(String message, String detailedMessage, String stackTrace) {
        this.message = message;
        this.detailedMessage = detailedMessage;
        this.stackTrace = stackTrace;
    }

    public String getMessage() {
        return message;
    }

    public String getDetailedMessage() {
        return detailedMessage;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    private String message;
    private String detailedMessage;
    private String stackTrace;
}
