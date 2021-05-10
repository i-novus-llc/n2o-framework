package net.n2oapp.framework.api.metadata.global.view.fieldset;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.control.N2oComponent;

/**
 * Исходная модель столбца филдсета
 */
@Getter
@Setter
public class N2oFieldsetColumn extends N2oComponent {
    private Integer size;
    private String visible;
    private SourceComponent[] items;
}
