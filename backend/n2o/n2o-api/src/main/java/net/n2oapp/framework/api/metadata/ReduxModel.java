package net.n2oapp.framework.api.metadata;


import net.n2oapp.framework.api.metadata.aware.IdAware;

/**
 * Модель на клиенте
 */
public enum ReduxModel implements IdAware {
    RESOLVE {
        @Override
        public String getId() {
            return "resolve";
        }
    }, FILTER() {
        @Override
        public String getId() {
            return "filter";
        }
    }, SELECTED() {
        @Override
        public String getId() {
            return "selected";
        }
    }, EDIT() {
        @Override
        public String getId() {
            return "edit";
        }
    }, MULTI() {
        @Override
        public String getId() {
            return "multi";
        }
    }, DATASOURCE() {
        @Override
        public String getId() {
            return "datasource";
        }
    };


    @Override
    public void setId(String id) {
    }
}
