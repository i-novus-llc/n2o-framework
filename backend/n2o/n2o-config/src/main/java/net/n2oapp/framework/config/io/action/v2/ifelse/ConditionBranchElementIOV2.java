package net.n2oapp.framework.config.io.action.v2.ifelse;

import net.n2oapp.framework.api.metadata.event.action.ifelse.N2oConditionBranch;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.action.v2.AbstractActionElementIOV2;
import net.n2oapp.framework.config.io.common.ActionsAwareIO;
import org.jdom2.Element;

/**
 * Чтение/запись базовых свойств веток оператора if-else
 */
public abstract class ConditionBranchElementIOV2<T extends N2oConditionBranch> extends AbstractActionElementIOV2<T>
    implements ActionsAwareIO<T> {

    @Override
    public void io(Element e, T a, IOProcessor p) {
        super.io(e, a, p);
        action(e, a, p);
    }
}
