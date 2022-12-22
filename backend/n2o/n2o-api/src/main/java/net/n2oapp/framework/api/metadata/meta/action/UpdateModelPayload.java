package net.n2oapp.framework.api.metadata.meta.action;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.action.custom.CustomActionPayload;

import java.util.Objects;

/**
 * Действие обновления поля в модели виджета
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateModelPayload extends CustomActionPayload {
    @JsonProperty
    private String prefix;
    @JsonProperty
    private String key;
    @JsonProperty
    private String field;
    @JsonProperty
    private Object value;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpdateModelPayload payload = (UpdateModelPayload) o;
        return prefix.equals(payload.prefix) &&
                key.equals(payload.key) &&
                field.equals(payload.field) &&
                Objects.equals(value, payload.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(prefix, key, field, value);
    }
}
