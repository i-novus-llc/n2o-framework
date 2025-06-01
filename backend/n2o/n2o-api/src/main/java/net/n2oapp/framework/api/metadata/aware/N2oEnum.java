package net.n2oapp.framework.api.metadata.aware;

import com.fasterxml.jackson.annotation.JsonValue;

public interface N2oEnum {
    @JsonValue
    String getId();
}