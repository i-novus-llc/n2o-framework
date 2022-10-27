package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeType;

/**
 * Клиентская модель компонента загрузки изображений
 */
@Getter
@Setter
public class ImageUpload extends FileUpload {
    @JsonProperty
    private ListType listType;
    @JsonProperty("lightbox")
    private Boolean canLightbox;
    @JsonProperty
    private Boolean canDelete;
    @JsonProperty
    private Integer width;
    @JsonProperty
    private Integer height;
    @JsonProperty
    private String icon;
    @JsonProperty
    private Integer iconSize;
    @JsonProperty
    private Boolean showTooltip;
    @JsonProperty
    private ShapeType shape;
    @JsonProperty
    private Boolean showName;

    public enum ListType {
        image,
        card
    }
}
