package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;

import java.util.Map;

/**
 * Значение по умолчанию для полей
 */
@Getter
@Setter
@NoArgsConstructor
public class DefaultValues implements Compiled {
    private Map<String, Object> values;

    public DefaultValues(Map<String, Object> values) {
        this.values = values;
    }

    @JsonAnyGetter
    public Map<String, Object> getJsonValues() {
        return values;
    }
}
