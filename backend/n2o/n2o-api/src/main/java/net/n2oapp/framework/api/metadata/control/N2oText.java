package net.n2oapp.framework.api.metadata.control;

import lombok.Getter;
import lombok.Setter;

/**
 * Компонент текста
 */
@Getter
@Setter
public class N2oText extends N2oField {
    private String text;
    private String format;
}
