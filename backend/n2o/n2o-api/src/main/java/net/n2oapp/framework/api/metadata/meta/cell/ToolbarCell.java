package net.n2oapp.framework.api.metadata.meta.cell;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Group;

import java.util.List;

/**
 * Клиентская модель ячейки с кнопками
 */
@Getter
@Setter
public class ToolbarCell extends AbstractCell {
    @JsonProperty
    private List<Group> toolbar;
}
