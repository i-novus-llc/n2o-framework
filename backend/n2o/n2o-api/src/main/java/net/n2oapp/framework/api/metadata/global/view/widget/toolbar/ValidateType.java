package net.n2oapp.framework.api.metadata.global.view.widget.toolbar;

import net.n2oapp.framework.api.metadata.aware.IdAware;

/**
 * Тип валидации виджетов страницы
 */
public enum ValidateType implements IdAware {
    PAGE {
        @Override
        public String getId() {
            return "page";
        }
    },
    WIDGET {
        @Override
        public String getId() {
            return "widget";
        }
    },
    NONE() {
        @Override
        public String getId() {
            return "none";
        }
    },

    // Deprecated
    TRUE() {
        @Override
        public String getId() {
            return "widget";
        }
    },
    FALSE() {
        @Override
        public String getId() {
            return "none";
        }
    };

    @Override
    public void setId(String id) {
    }
}
