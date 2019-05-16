package net.n2oapp.framework.api.metadata.aware;

import com.fasterxml.jackson.annotation.JsonAnyGetter;

import java.util.Map;

/**
 * Знание о дополнительных свойствах и выгрузка их в json
 */
public interface JsonPropertiesAware extends PropertiesAware {
    @JsonAnyGetter
    default Map<String, Object> getJsonProperties() {
        return getProperties();
    }
}
