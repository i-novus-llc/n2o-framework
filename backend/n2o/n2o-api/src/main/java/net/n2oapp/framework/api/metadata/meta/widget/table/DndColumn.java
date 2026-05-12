package net.n2oapp.framework.api.metadata.meta.widget.table;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.aware.JsonPropertiesAware;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.MoveModeEnum;

import java.util.List;
import java.util.Map;


/**
 * Клиентская модель drag-n-drop столбца таблицы
 */
@Getter
@Setter
public class DndColumn extends AbstractColumn implements JsonPropertiesAware {
    @JsonProperty
    private MoveModeEnum moveMode;
    @JsonProperty
    private List<SimpleColumn> children;
    private Map<String, Object> properties;
}
