package net.n2oapp.framework.api.metadata.control.list;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.N2oComponent;
import net.n2oapp.framework.api.metadata.N2oAttribute;
import net.n2oapp.framework.api.metadata.aware.IdAware;

/**
 * Компонент радио кнопок
 */
@Getter
@Setter
@N2oComponent
public class N2oRadioGroup extends N2oSingleListFieldAbstract implements Inlineable {
    @N2oAttribute
    private Boolean inline;
    @N2oAttribute
    private RadioGroupType type;

    public enum RadioGroupType implements IdAware {
        defaultType("default"),
        btn("btn"),
        tabs("tabs");

        private final String value;

        RadioGroupType(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String getId() {
            return this.value;
        }

        @Override
        public void setId(String id) {
            throw new UnsupportedOperationException();
        }
    }
}
