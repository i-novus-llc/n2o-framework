package net.n2oapp.framework.api.metadata.global.view.fieldset;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.aware.FieldsetItem;
import net.n2oapp.framework.api.metadata.control.N2oComponent;

/**
 * Исходная модель столбца филдсета
 */
@Getter
@Setter
public class N2oFieldsetCol extends N2oComponent implements FieldsetItem {
    private Integer size;
    private String visible;
    private FieldsetItem[] items;
}
