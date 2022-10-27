package net.n2oapp.framework.api.metadata.meta.cell;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeType;
import net.n2oapp.framework.api.metadata.meta.badge.Position;

/**
 * Клиентская модель ячейки со значком
 */
@Getter
@Setter
public class BadgeCell extends AbstractCell {
    @JsonProperty("placement")
    private Position position;
    @JsonProperty
    private String text;
    @JsonProperty("format")
    private String textFormat;
    @JsonProperty
    private String color;
    @JsonProperty("badgeFormat")
    private String format;
    @JsonProperty
    private ShapeType shape;
    @JsonProperty
    private String imageFieldId;
    @JsonProperty
    private Position imagePosition;
    @JsonProperty
    private ShapeType imageShape;
}
