package net.n2oapp.framework.api.metadata.control;

import net.n2oapp.framework.api.metadata.aware.IdAware;

public enum PageRef implements IdAware {
    THIS {
        @Override
        public String getId() {
            return "this";
        }
    }, PARENT() {
        @Override
        public String getId() {
            return "parent";
        }
    };

    @Override
    public void setId(String id) {
    }
}
