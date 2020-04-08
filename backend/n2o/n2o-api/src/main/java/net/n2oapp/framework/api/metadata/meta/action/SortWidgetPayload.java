package net.n2oapp.framework.api.metadata.meta.action;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

/**
 * Действие сортировки виджета
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SortWidgetPayload extends PerformActionPayload {

    @JsonProperty
    private String widgetId;

    @JsonProperty
    private String field;

    @JsonProperty
    private Object direction;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SortWidgetPayload payload = (SortWidgetPayload) o;
        return widgetId.equals(payload.widgetId) &&
                field.equals(payload.field) &&
                direction.equals(payload.direction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(widgetId, field, direction);
    }
}
