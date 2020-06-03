package net.n2oapp.framework.api.config;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.criteria.dataset.DataSet;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Конфигурация клиента N2O приложения
 */
@Getter
@Setter
public class AppConfig {
//    @JsonIgnore
    private Map<String, Object> properties = new LinkedHashMap<>();

    public Object getProperty(String property) {
        return properties.get(property);
    }

    @JsonAnyGetter
    public Map<String, Object> getProperties() {
        return properties;
    }

    @JsonAnySetter
    public void setProperty(String property, Object value) {
        this.properties.put(property, value);
    }
}
