package net.n2oapp.framework.api.metadata.control.plain;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.VisualAttribute;
import net.n2oapp.framework.api.metadata.VisualComponent;
import net.n2oapp.framework.api.metadata.control.N2oField;
import net.n2oapp.framework.api.metadata.control.N2oStandardField;

/**
 * Компонент ввода интервала
 */
@Getter
@Setter
@VisualComponent
public class N2oIntervalField extends N2oStandardField {
    @VisualAttribute
    private N2oField begin;
    @VisualAttribute
    private N2oField end;
}
