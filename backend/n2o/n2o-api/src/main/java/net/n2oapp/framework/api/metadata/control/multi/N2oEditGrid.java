package net.n2oapp.framework.api.metadata.control.multi;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.control.N2oStandardField;

/**
 * Компонент ввода edit-grid
 */
@Getter
@Setter
public class N2oEditGrid extends N2oStandardField {

    private N2oCheckboxGrid.Column[] columns;
    private String objectId;

}
