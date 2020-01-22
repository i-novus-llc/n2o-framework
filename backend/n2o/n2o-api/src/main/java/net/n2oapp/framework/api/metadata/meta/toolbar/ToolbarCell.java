package net.n2oapp.framework.api.metadata.meta.toolbar;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oAbstractCell;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.Group;

import java.util.List;

@Getter
@Setter
public class ToolbarCell extends N2oAbstractCell {

    @JsonProperty
    private List<Group> toolbar;
}
