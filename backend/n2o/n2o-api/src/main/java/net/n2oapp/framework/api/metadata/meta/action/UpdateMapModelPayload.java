package net.n2oapp.framework.api.metadata.meta.action;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

/**
 * Действие обновления поля со списком в модели виджета
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMapModelPayload extends PerformActionPayload {
    @JsonProperty
    private String prefix;
    @JsonProperty
    private String key;
    @JsonProperty
    private String field;
    @JsonProperty
    private Object value;
    @JsonProperty
    private String map;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpdateMapModelPayload payload = (UpdateMapModelPayload) o;
        return prefix.equals(payload.prefix) &&
                key.equals(payload.key) &&
                field.equals(payload.field) &&
                Objects.equals(value, payload.value) &&
                Objects.equals(map, payload.map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(prefix, key, field, value, map);
    }
}
