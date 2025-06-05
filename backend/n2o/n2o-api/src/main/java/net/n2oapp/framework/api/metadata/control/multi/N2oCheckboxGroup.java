package net.n2oapp.framework.api.metadata.control.multi;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;
import net.n2oapp.framework.api.metadata.control.list.Inlineable;

/**
 * Компонент группы чекбоксов
 */
@Getter
@Setter
public class N2oCheckboxGroup extends N2oMultiListFieldAbstract implements Inlineable {
    private Boolean inline;
    private CheckboxGroupTypeEnum type;

    @RequiredArgsConstructor
    @Getter
    public enum CheckboxGroupTypeEnum implements N2oEnum {
        DEFAULT("default"),
        BTN("btn");

        private final String id;
    }
}