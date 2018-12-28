package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.event.action.N2oAction;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ImageShape;

/**
 * Ячейка с картинкой
 */
@Getter
@Setter
public class N2oImageCell extends N2oActionCell {
    private String width;
    private String url;
    @JsonProperty
    private ImageShape shape;
    private String title;

}