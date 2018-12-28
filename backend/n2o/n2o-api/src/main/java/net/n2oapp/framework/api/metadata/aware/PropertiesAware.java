package net.n2oapp.framework.api.metadata.aware;

import com.fasterxml.jackson.annotation.JsonAnyGetter;

import java.util.Map;

/**
 * Знание о дополнительных свойствах
 */
public interface PropertiesAware {
    Map<String, Object> getProperties();
    void setProperties(Map<String, Object> properties);
    @JsonAnyGetter
    default Map<String, Object> getJsonProperties() {
        return getProperties();
    }
}
