package net.n2oapp.framework.api.metadata.control;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.meta.control.ImageField;

/**
 * Исходная модель поля Image с заголовком и подзаголовком
 */
@Getter
@Setter
public class N2oImageField extends N2oField {

    private String url;
    private String data;
    private String title;
    private String description;
    private ImageField.Position textPosition;
    private String width;

}
