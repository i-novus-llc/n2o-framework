package net.n2oapp.framework.api.metadata.meta.widget;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oAbstractCell;

import java.util.Map;

@Getter
@Setter
public class ListWidget extends Widget {
    @JsonProperty
    private Map<String, N2oAbstractCell> list;
}