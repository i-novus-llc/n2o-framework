package net.n2oapp.framework.api.exception;

import com.fasterxml.jackson.annotation.JsonValue;
import net.n2oapp.framework.api.metadata.aware.IdAware;

/**
 * User: operhod
 * Date: 18.11.13
 * Time: 14:49
 */
public enum SeverityTypeEnum implements IdAware {
    DANGER("danger"),
    WARNING("warning"),
    INFO("info"),
    SUCCESS("success");

    private String name;

    SeverityTypeEnum(String name) {
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
