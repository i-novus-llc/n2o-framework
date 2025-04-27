package net.n2oapp.framework.api.metadata.meta.widget.table;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


/**
 * Клиентская модель многоуровневого столбца таблицы
 */
@Getter
@Setter
public class MultiColumn extends BaseColumn {
    @JsonProperty
    private Boolean multiHeader;
    @JsonProperty
    private List<BaseColumn> children;
}
