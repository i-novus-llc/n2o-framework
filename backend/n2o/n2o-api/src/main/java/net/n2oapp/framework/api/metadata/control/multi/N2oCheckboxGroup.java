package net.n2oapp.framework.api.metadata.control.multi;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.control.list.Inlineable;

/**
 * Компонент группы чекбоксов
 */
@Getter
@Setter
public class N2oCheckboxGroup extends N2oMultiListFieldAbstract implements Inlineable {
    private Boolean inline;
    private String type;
}
