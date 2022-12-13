package net.n2oapp.framework.api.metadata.control.plain;

import net.n2oapp.framework.api.metadata.aware.IdAware;

public enum CheckboxDefaultValueEnum implements IdAware {
    NULL {
        @Override
        public String getId() {
            return "null";
        }
    }, FALSE() {
        @Override
        public String getId() {
            return "false";
        }
    };

    @Override
    public void setId(String id) {
    }
}