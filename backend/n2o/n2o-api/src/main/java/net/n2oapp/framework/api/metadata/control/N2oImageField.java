package net.n2oapp.framework.api.metadata.control;

import lombok.Getter;
import lombok.Setter;

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
    private String textPosition;

}
