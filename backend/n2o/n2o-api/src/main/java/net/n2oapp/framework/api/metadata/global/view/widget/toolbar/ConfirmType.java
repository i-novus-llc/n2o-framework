package net.n2oapp.framework.api.metadata.global.view.widget.toolbar;

import net.n2oapp.framework.api.metadata.aware.IdAware;

public enum ConfirmType implements IdAware {
    POPOVER {
        @Override
        public String getId() {
            return "popover";
        }
    },
    MODAL {
        @Override
        public String getId() {
            return "modal";
        }
    };


    @Override
    public void setId(String id) {
    }
}
