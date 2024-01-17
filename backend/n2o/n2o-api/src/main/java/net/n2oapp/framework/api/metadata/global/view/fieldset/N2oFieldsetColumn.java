package net.n2oapp.framework.api.metadata.global.view.fieldset;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.aware.FieldsetItem;

/**
 * Исходная модель столбца филдсета
 */
@Getter
@Setter
public class N2oFieldsetColumn extends net.n2oapp.framework.api.metadata.control.N2oComponent implements FieldsetItem {
    private Integer size;
    private String visible;
    private FieldsetItem[] items;
}
