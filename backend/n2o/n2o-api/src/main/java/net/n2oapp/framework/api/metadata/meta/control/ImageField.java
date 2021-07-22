package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ImageShape;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.ImageStatusElement;

/**
 * Клиентская модель компонента вывода изображения
 */
@Getter
@Setter
public class ImageField extends Field {
    @JsonProperty
    private String url;
    @JsonProperty
    private String data;
    @JsonProperty
    private String title;
    @JsonProperty
    private String description;
    @JsonProperty
    private TextPosition textPosition;
    @JsonProperty
    private ImageShape shape;
    @JsonProperty
    private String width;
    @JsonProperty
    private ImageStatusElement[] statuses;
}
