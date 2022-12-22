package net.n2oapp.framework.api.metadata.meta.action;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

/**
 * Действие выделения записи в виджете
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SelectedWidgetPayload extends PerformActionPayload {
    @JsonProperty
    private String widgetId;
    @JsonProperty
    private Object value;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SelectedWidgetPayload payload = (SelectedWidgetPayload) o;
        return widgetId.equals(payload.widgetId) &&
                value.equals(payload.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(widgetId, value);
    }
}
