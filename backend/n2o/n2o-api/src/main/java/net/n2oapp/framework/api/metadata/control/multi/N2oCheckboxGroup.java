package net.n2oapp.framework.api.metadata.control.multi;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.control.list.Inlineable;

/**
 * Компонент группы чекбоксов
 */
@Getter
@Setter
public class N2oCheckboxGroup extends N2oMultiListFieldAbstract implements Inlineable {
    private Boolean inline;
    private CheckboxGroupTypeEnum type;

    public enum CheckboxGroupTypeEnum implements IdAware {
        DEFAULT("default"),
        BTN("btn");

        private final String value;

        CheckboxGroupTypeEnum(String value) {
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
