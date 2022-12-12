package net.n2oapp.framework.config.io.action.v2.ifelse;

import net.n2oapp.framework.api.metadata.action.ifelse.N2oElseBranchAction;
import org.springframework.stereotype.Component;

/**
 * Чтение/запись элемента else оператора if-else
 */
@Component
public class ElseBranchActionElementIOV2 extends ConditionBranchElementIOV2<N2oElseBranchAction> {
    @Override
    public Class<N2oElseBranchAction> getElementClass() {
        return N2oElseBranchAction.class;
    }

    @Override
    public String getElementName() {
        return "else";
    }
}
