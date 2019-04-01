package net.n2oapp.framework.api.rest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseErrorInfo {
    private String alertKey;
    private Exception exception;

    public ResponseErrorInfo(String alertKey, Exception e) {
        this.alertKey = alertKey;
        this.exception = e;
    }
}
