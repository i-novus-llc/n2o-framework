package net.n2oapp.framework.api.metadata.meta.action;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.action.custom.CustomActionPayload;

import java.util.Objects;

/**
 * Действие сортировки виджета
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SortWidgetPayload extends CustomActionPayload {
    @JsonProperty
    private String widgetId;
    @JsonProperty
    private String fieldKey;
    @JsonProperty
    private Object sortDirection;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SortWidgetPayload payload = (SortWidgetPayload) o;
        return widgetId.equals(payload.widgetId) &&
                fieldKey.equals(payload.fieldKey) &&
                sortDirection.equals(payload.sortDirection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(widgetId, fieldKey, sortDirection);
    }
}
