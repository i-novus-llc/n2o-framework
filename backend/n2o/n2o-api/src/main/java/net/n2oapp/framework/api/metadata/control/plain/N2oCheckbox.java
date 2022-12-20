package net.n2oapp.framework.api.metadata.control.plain;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.N2oAttribute;
import net.n2oapp.framework.api.metadata.N2oComponent;

@Getter
@Setter
@N2oComponent
public class N2oCheckbox extends N2oPlainField {
    @N2oAttribute("Значение при не выбранном состоянии")
    private CheckboxDefaultValueEnum unchecked;
}
