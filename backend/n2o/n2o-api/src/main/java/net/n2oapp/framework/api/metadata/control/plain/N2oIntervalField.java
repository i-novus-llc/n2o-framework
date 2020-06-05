package net.n2oapp.framework.api.metadata.control.plain;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.control.N2oField;
import net.n2oapp.framework.api.metadata.control.N2oStandardField;

/**
 * Компонент ввода интервала
 */
@Getter
@Setter
public class N2oIntervalField extends N2oStandardField {
    private N2oField begin;
    private N2oField end;
}
