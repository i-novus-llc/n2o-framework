package net.n2oapp.framework.api.metadata.control.list;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.aware.IdAware;

/**
 * Компонент радио кнопок
 */
@Getter
@Setter
public class N2oRadioGroup extends N2oSingleListFieldAbstract implements Inlineable {
    private Boolean inline;
    private RadioGroupType type;

    public enum RadioGroupType implements IdAware {
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
        },
        tabs("tabs") {
            @Override
            public String getId() {
                return "tabs";
            }
        };

        private final String value;

        RadioGroupType(String value) {
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
