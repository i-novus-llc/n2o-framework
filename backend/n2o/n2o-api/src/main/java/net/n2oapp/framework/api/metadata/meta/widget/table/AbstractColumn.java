package net.n2oapp.framework.api.metadata.meta.widget.table;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.aware.IdAware;


/**
 * Клиентская модель абстрактного столбца таблицы
 */
@Getter
@Setter
public class AbstractColumn implements IdAware, Compiled {
    @JsonProperty
    private String id;
    @JsonProperty
    private String src;
}
