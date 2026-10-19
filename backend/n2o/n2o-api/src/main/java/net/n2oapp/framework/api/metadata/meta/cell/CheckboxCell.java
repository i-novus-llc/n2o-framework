package net.n2oapp.framework.api.metadata.meta.cell;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Клиентская модель ячейки с чекбоксом
 */
@Getter
@Setter
public class CheckboxCell extends ActionCell {
    @JsonProperty
    private Serializable disabled;
}
