package net.n2oapp.framework.api.metadata.meta.widget.table;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.MoveModeEnum;

import java.util.List;


/**
 * Клиентская модель drag-n-drop столбца таблицы
 */
@Getter
@Setter
public class DndColumn extends AbstractColumn {
    @JsonProperty
    private MoveModeEnum moveMode;
    @JsonProperty
    private List<SimpleColumn> children;
}
