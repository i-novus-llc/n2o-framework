package net.n2oapp.framework.api.metadata.meta.widget.table;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.meta.toolbar.Toolbar;

/**
 * Клиентская модель "Тулбар на строках таблицы"
 */
@Getter
@Setter
public class RowOverlay implements Compiled {
    @JsonProperty
    private String className;
    @JsonProperty
    private Toolbar toolbar;
}
