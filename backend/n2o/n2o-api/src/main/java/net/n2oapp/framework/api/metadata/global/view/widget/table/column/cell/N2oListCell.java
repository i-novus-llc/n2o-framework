package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;

import lombok.Getter;
import lombok.Setter;

/**
 * Ячейка со списком
 */
@Getter
@Setter
public class N2oListCell extends N2oAbstractCell {
    private String labelFieldId;
    private String color;
    private Integer size;
    private Boolean inline;
    private String separator;
    private N2oCell cell;
}
