package net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ImageShape;

/**
 * Ячейка с изображением
 */
@Getter
@Setter
public class N2oImageCell extends N2oActionCell {
    @JsonProperty
    private Integer width;
    private String url;
    @JsonProperty
    private ImageShape shape;
    @JsonProperty
    private String title;
    @JsonProperty
    private String data;
    @JsonProperty
    private String description;
    @JsonProperty
    private Position textPosition;
    private ImageStatusElement[] statuses;

    public enum Position {
        top, left, right, bottom
    }
}