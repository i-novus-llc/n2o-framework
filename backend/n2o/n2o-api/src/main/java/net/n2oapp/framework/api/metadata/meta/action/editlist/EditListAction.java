package net.n2oapp.framework.api.metadata.meta.action.editlist;

import net.n2oapp.framework.api.metadata.meta.action.AbstractAction;
import net.n2oapp.framework.api.metadata.meta.saga.MetaSaga;

/**
 * Клиентская модель действия редактирования записи списка
 */
public class EditListAction extends AbstractAction<EditListActionPayload, MetaSaga> {
    public EditListAction() {
        super(new EditListActionPayload(), null);
    }
}
