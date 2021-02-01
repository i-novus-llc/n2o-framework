package net.n2oapp.framework.api.metadata.global.view.widget.toolbar;

import net.n2oapp.framework.api.metadata.aware.IdAware;

/**
 * Тип доступности кнопки при пустой модели
 */
public enum DisableOnEmptyModelType implements IdAware {
    AUTO {
        @Override
        public String getId() {
            return "auto";
        }
    },
    TRUE() {
        @Override
        public String getId() {
            return "true";
        }
    },
    FALSE() {
        @Override
        public String getId() {
            return "false";
        }
    };

    @Override
    public void setId(String id) {
    }
}
