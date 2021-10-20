package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.Compiled;

import java.util.Map;
import java.util.Objects;

/**
 * Значение по умолчанию для полей
 */
public class DefaultValues implements Compiled {
    private DataSet values;

    public DefaultValues() {
        this.values = new DataSet();
    }

    public DefaultValues(Map<String, Object> values) {
        this.values = new DataSet(values);
    }

    public DefaultValues add(String key, Object value) {
        values.put(key, value);
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getJsonValues() {
        return values;
    }

    public Map<String, Object> getValues() {
        return values;
    }

    public void setValues(Map<String, Object> values) {
        this.values = new DataSet(values);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultValues that = (DefaultValues) o;
        return Objects.equals(values, that.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(values);
    }
}
