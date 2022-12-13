package net.n2oapp.framework.api.metadata.control;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.VisualAttribute;
import net.n2oapp.framework.api.metadata.VisualComponent;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeType;
import net.n2oapp.framework.api.metadata.meta.control.ImageUpload;

/**
 * Компонент загрузки изображений
 */
@Getter
@Setter
@VisualComponent
public class N2oImageUpload extends N2oFileUpload {
    @VisualAttribute
    private ImageUpload.ListType listType;
    @VisualAttribute
    private Boolean canLightbox;
    @VisualAttribute
    private Boolean canDelete;
    @VisualAttribute
    private Integer width;
    @VisualAttribute
    private Integer height;
    @VisualAttribute
    private String icon;
    @VisualAttribute
    private Integer iconSize;
    @VisualAttribute
    private Boolean showTooltip;
    @VisualAttribute
    private ShapeType shape;
    @VisualAttribute
    private Boolean showName;
}
