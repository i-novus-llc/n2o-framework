package net.n2oapp.framework.api.metadata.meta.cell;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * Клиентская модель переключателя ячеек таблицы
 */
@Getter
@Setter
public class SwitchCell extends AbstractCell {
    @JsonProperty
    private String switchFieldId;
    @JsonProperty
    private Map<String, AbstractCell> switchList = new HashMap<>();
    @JsonProperty
    private AbstractCell switchDefault;
}
