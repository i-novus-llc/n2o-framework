package net.n2oapp.framework.standard.header.model.global.context;

import java.io.Serializable;

/**
 * User: operhod
 * Date: 19.05.14
 * Time: 15:57
 */
public class UserContextStructure implements Serializable {
    private String src;
    private String queryId;
    private String userFieldId;
    private Position position;
    private Unit[] units;


    public Unit[] getUnits() {
        return units;
    }

    public void setUnits(Unit[] units) {
        this.units = units;
    }

    public String getQueryId() {
        return queryId;
    }

    public void setQueryId(String queryId) {
        this.queryId = queryId;
    }

    public String getUserFieldId() {
        return userFieldId;
    }

    public void setUserFieldId(String userFieldId) {
        this.userFieldId = userFieldId;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public static class Position extends Unit {

        private String hintFieldId;

        public String getHintFieldId() {
            return hintFieldId;
        }

        public void setHintFieldId(String hintFieldId) {
            this.hintFieldId = hintFieldId;
        }

    }

    public static class Unit implements Serializable {
        private String valueFieldId;
        private String labelFieldId;
        private Unit[] units;

        public Unit[] getUnits() {
            return units;
        }

        public void setUnits(Unit[] units) {
            this.units = units;
        }

        public String getValueFieldId() {
            return valueFieldId;
        }

        public void setValueFieldId(String valueFieldId) {
            this.valueFieldId = valueFieldId;
        }

        public String getLabelFieldId() {
            return labelFieldId;
        }

        public void setLabelFieldId(String labelFieldId) {
            this.labelFieldId = labelFieldId;
        }
    }

}
