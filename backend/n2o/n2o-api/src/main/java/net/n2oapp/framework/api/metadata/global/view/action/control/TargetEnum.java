package net.n2oapp.framework.api.metadata.global.view.action.control;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.IdAware;

/**
 * Тип сценария открытия ссылки
 */
@RequiredArgsConstructor
@Getter
public enum TargetEnum implements IdAware {
    SELF("self", "_self"),
    NEW_WINDOW("newWindow", "_blank"),
    APPLICATION("application", "application");

    private final String id;
    private final String value;

    @JsonValue
    public String getValue() {
        return this.value;
    }

    @Override
    public void setId(String id) {
        // no implementation
    }
}