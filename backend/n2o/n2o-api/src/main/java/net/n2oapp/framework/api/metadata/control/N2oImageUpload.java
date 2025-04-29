package net.n2oapp.framework.api.metadata.control;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeTypeEnum;
import net.n2oapp.framework.api.metadata.meta.control.ImageUpload;

/**
 * Компонент загрузки изображений
 */
@Getter
@Setter
public class N2oImageUpload extends N2oFileUpload {
    private ImageUpload.ListTypeEnum listType;
    private Boolean canLightbox;
    private Boolean canDelete;
    private String width;
    private String height;
    private String icon;
    private String iconSize;
    private Boolean showTooltip;
    private ShapeTypeEnum shape;
    private Boolean showName;
}
