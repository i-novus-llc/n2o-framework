package net.n2oapp.framework.api.metadata.meta;

import com.fasterxml.jackson.annotation.JsonProperty;
import net.n2oapp.framework.api.metadata.meta.control.Field;

import java.io.Serializable;

/**
 * Клиентская модель ячейки записей
 */
@Deprecated
public class Column implements Serializable {
    @JsonProperty
    private String id;
    @JsonProperty
    private String name;
    @JsonProperty
    private Field field;
    private String textFieldId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
