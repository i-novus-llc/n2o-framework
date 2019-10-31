package net.n2oapp.framework.api.metadata.meta.toolbar;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oAbstractCell;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Button;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Group;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ToolbarCell extends N2oAbstractCell {

    @JsonProperty
    private List<Group> toolbar;

    @JsonProperty
    private Map<String, Action> actions;
}
