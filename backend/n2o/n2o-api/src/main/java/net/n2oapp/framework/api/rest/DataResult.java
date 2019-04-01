package net.n2oapp.framework.api.rest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DataResult<T> {
    private ResponseErrorInfo errorInfo;
    private T data;

    public DataResult(T data) {
        this.data = data;
    }

    public DataResult(String alertKey, Exception e) {
        this.errorInfo = new ResponseErrorInfo(alertKey, e);
    }

    public boolean isError() {
        return errorInfo != null;
    }
}
