package net.n2oapp.framework.api.metadata.control;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.control.ImageUpload;

/**
 * Компонент загрузки изображений
 */
@Getter
@Setter
public class N2oImageUpload extends N2oFileUpload {
    private ImageUpload.ListType listType;
    private Boolean lightbox;
    private Boolean showName;
}
