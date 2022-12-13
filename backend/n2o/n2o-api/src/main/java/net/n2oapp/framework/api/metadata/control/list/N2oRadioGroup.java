package net.n2oapp.framework.api.metadata.control.list;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.VisualAttribute;
import net.n2oapp.framework.api.metadata.VisualComponent;
import net.n2oapp.framework.api.metadata.aware.IdAware;

/**
 * Компонент радио кнопок
 */
@Getter
@Setter
@VisualComponent
public class N2oRadioGroup extends N2oSingleListFieldAbstract implements Inlineable {
    @VisualAttribute
    private Boolean inline;
    @VisualAttribute
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
