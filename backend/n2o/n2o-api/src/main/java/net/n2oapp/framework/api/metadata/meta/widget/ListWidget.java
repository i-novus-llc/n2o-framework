package net.n2oapp.framework.api.metadata.meta.widget;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.cell.AbstractCell;
import net.n2oapp.framework.api.metadata.meta.widget.table.Pagination;
import net.n2oapp.framework.api.metadata.meta.widget.table.RowClick;

import java.util.Map;

@Getter
@Setter
public class ListWidget extends Widget {
    @JsonProperty
    private Map<String, AbstractCell> list;
    @JsonProperty
    private Pagination paging;
    @JsonProperty
    private RowClick rowClick;
    @JsonProperty
    private Rows rows;
}