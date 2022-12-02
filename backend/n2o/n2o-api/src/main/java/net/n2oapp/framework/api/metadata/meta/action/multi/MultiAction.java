package net.n2oapp.framework.api.metadata.meta.action.multi;

import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.meta.action.AbstractAction;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.saga.MetaSaga;

import java.util.List;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Клиентская модель последовательности действий
 */
public class MultiAction extends AbstractAction<MultiActionPayload, MetaSaga> {

    public MultiAction(List<Action> actions, CompileProcessor p) {
        super(new MultiActionPayload(actions), null);
        setType(p.resolve(property("n2o.api.action.multi.type"), String.class));
    }
}
