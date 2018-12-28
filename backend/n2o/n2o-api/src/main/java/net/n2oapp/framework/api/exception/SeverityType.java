package net.n2oapp.framework.api.exception;

import com.fasterxml.jackson.annotation.JsonValue;
import net.n2oapp.framework.api.metadata.aware.IdAware;

/**
 * User: operhod
 * Date: 18.11.13
 * Time: 14:49
 */
public enum SeverityType implements IdAware {
    danger("danger"),
    warning("warning"),
    info("info"),
    success("success");

    private String name;

    SeverityType(String name) {
        this.name = name;
    }


    @Override
    @JsonValue
    public String getId() {
        return name;
    }

    @Override
    public void setId(String id) {
        throw new UnsupportedOperationException();
    }
}
