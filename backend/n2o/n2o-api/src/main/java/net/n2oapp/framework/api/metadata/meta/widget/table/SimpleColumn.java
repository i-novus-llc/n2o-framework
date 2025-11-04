package net.n2oapp.framework.api.metadata.meta.widget.table;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;

/**
 * Клиентская модель простого столбца таблицы
 */
@Getter
@Setter
public class SimpleColumn extends BaseColumn {
    @JsonProperty
    private Boolean filterable;
    @JsonProperty
    private StandardField<?> filterField;
}
