package net.n2oapp.framework.api.metadata.control.list;

import net.n2oapp.framework.api.metadata.control.N2oListField;

/**
 * Абстрактная реализация списка с выбором одного значения
 */
public abstract class N2oSingleListFieldAbstract extends N2oListField implements N2oSingleListField {

    @Override
    protected boolean isSingle() {
        return true;
    }
}
