package net.n2oapp.framework.api.metadata.control.plain;


import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.N2oAttribute;
import net.n2oapp.framework.api.metadata.N2oComponent;

/**
 * Компонент ввода многострочного текста
 */
@Getter
@Setter
@N2oComponent
public class N2oTextArea extends N2oPlainText {
    @N2oAttribute("Минимальная высота поля в строках")
    private Integer minRows;
    @N2oAttribute("Максимальная высота поля в строках")
    private Integer maxRows;
}
