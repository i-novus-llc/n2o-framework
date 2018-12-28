package net.n2oapp.framework.api.metadata.control.list;

import lombok.Getter;
import lombok.Setter;

/**
 * Исходная модель компонента radio-group
 */
@Getter
@Setter
public class N2oRadioGroup extends N2oSingleListFieldAbstract implements Inlineable {
    private Boolean inline;
    private String type;
}
