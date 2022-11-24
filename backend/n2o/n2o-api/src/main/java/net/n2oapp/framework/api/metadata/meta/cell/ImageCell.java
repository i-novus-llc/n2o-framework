package net.n2oapp.framework.api.metadata.meta.cell;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeType;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oImageCell;

/**
 * Клиентская модель ячейки с изображением
 */
@Getter
@Setter
public class ImageCell extends ActionCell {
    @JsonProperty
    private String width;
    @JsonProperty
    private ShapeType shape;
    @JsonProperty
    private String title;
    @JsonProperty
    private String data;
    @JsonProperty
    private String description;
    @JsonProperty
    private N2oImageCell.Position textPosition;
    @JsonProperty
    private ImageStatusElement[] statuses;
}
