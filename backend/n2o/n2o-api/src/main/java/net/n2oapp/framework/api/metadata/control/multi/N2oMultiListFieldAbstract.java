package net.n2oapp.framework.api.metadata.control.multi;

import net.n2oapp.framework.api.metadata.control.N2oListField;

/**
 * Абстрактная реализация списка с множественным выбором
 */
public abstract class N2oMultiListFieldAbstract extends N2oListField {

    @Override
    public boolean isSingle() {
        return false;
    }
}
