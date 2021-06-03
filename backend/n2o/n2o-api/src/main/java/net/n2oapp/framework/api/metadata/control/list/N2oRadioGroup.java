package net.n2oapp.framework.api.metadata.control.list;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.Setter;

/**
 * Компонент радио кнопок
 */
@Getter
@Setter
public class N2oRadioGroup extends N2oSingleListFieldAbstract implements Inlineable {
    private Boolean inline;
    private RadioGroupType type;

    public enum RadioGroupType {
        defaultType("default"),
        n2o("n2o"),
        btn("btn"),
        tabs("tabs");

        private final String value;

        RadioGroupType(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return this.value;
        }
    }
}
