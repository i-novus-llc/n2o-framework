package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.meta.action.editlist.EditListAction;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import org.springframework.stereotype.Component;

/**
 * Связывание действия edit-list
 */
@Component
public class EditListActionBinder implements BaseMetadataBinder<EditListAction> {
    @Override
    public EditListAction bind(EditListAction action, BindProcessor p) {
        if (action.getPayload().getList().getField() != null)
            action.getPayload().getList().setField(p.resolveTextByParams(action.getPayload().getList().getField()));
        return action;
    }

    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return EditListAction.class;
    }
}
