package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeTypeEnum;

/**
 * Клиентская модель компонента загрузки изображений
 */
@Getter
@Setter
public class ImageUpload extends FileUpload {
    @JsonProperty
    private ListTypeEnum listType;
    @JsonProperty("lightbox")
    private Boolean canLightbox;
    @JsonProperty
    private Boolean canDelete;
    @JsonProperty
    private String width;
    @JsonProperty
    private String height;
    @JsonProperty
    private String icon;
    @JsonProperty
    private String iconSize;
    @JsonProperty
    private Boolean showTooltip;
    @JsonProperty
    private ShapeTypeEnum shape;
    @JsonProperty
    private Boolean showName;

    public enum ListTypeEnum {
        image,
        card
    }
}
