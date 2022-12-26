package net.n2oapp.framework.api.metadata.global.view.fieldset;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.N2oAttribute;
import net.n2oapp.framework.api.metadata.N2oComponent;
import net.n2oapp.framework.api.metadata.SourceComponent;

/**
 * Исходная модель столбца филдсета
 */
@Getter
@Setter
@N2oComponent
public class N2oFieldsetColumn extends net.n2oapp.framework.api.metadata.control.N2oComponent {
    @N2oAttribute("Ширина столбца")
    private Integer size;
    @N2oAttribute("Условие видимости")
    private String visible;
    @N2oAttribute("Элементы столбца филдсета")
    private SourceComponent[] items;
}
