package net.n2oapp.framework.api.metadata;


import net.n2oapp.framework.api.metadata.aware.IdAware;

/**
 * Модель на клиенте
 */
public enum ReduxModel implements IdAware {
    resolve {
        @Override
        public String getId() {
            return "resolve";
        }
    }, filter() {
        @Override
        public String getId() {
            return "filter";
        }
    }, selected() {
        @Override
        public String getId() {
            return "selected";
        }
    }, edit() {
        @Override
        public String getId() {
            return "edit";
        }
    }, multi() {
        @Override
        public String getId() {
            return "multi";
        }
    }, datasource() {
        @Override
        public String getId() {
            return "datasource";
        }
    };


    @Override
    public void setId(String id) {
    }
}
