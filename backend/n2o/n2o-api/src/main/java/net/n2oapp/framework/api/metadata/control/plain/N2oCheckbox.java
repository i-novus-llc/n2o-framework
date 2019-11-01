package net.n2oapp.framework.api.metadata.control.plain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class N2oCheckbox extends N2oPlainField {
    private CheckboxDefaultValueEnum unchecked;
}
