package net.n2oapp.framework.api.metadata.meta.cell;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Component;

/**
 * Клиентская модель редактируемой ячейки таблицы
 */
@Getter
@Setter
public class EditCell extends ActionCell {
    @JsonProperty
    private Component control;
    @JsonProperty
    private String format;
    @JsonProperty("editable")
    private Object enabled;
}
