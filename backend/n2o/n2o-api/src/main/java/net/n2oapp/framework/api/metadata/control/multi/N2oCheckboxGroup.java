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
    private CheckboxGroupType type;

    public enum CheckboxGroupType implements IdAware {
        defaultType("default") {
            @Override
            public String getId() {
                return "default";
            }
        },
        @Deprecated n2o("n2o") {
            @Override
            public String getId() {
                return "n2o";
            }
        },
        btn("btn") {
            @Override
            public String getId() {
                return "btn";
            }
        };

        private final String value;

        CheckboxGroupType(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return this.value;
        }

        @Override
        public void setId(String id) {
            throw new UnsupportedOperationException();
        }
    }
}
