package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeTypeEnum;
import net.n2oapp.framework.api.metadata.meta.cell.ImageStatusElement;

/**
 * Клиентская модель компонента вывода изображения
 */
@Getter
@Setter
public class ImageField extends ActionField {
    @JsonProperty
    private String data;
    @JsonProperty
    private String title;
    @JsonProperty
    private String description;
    @JsonProperty
    private TextPositionEnum textPosition;
    @JsonProperty
    private ShapeTypeEnum shape;
    @JsonProperty
    private String width;
    @JsonProperty
    private ImageStatusElement[] statuses;
}
