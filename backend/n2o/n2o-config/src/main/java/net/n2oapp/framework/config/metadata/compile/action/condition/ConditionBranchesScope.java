package net.n2oapp.framework.config.metadata.compile.action.condition;

import lombok.Getter;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.event.action.ifelse.N2oConditionBranch;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Скоуп веток условного оператора
 */
@Getter
public class ConditionBranchesScope {

    private final Queue<N2oConditionBranch> scope;
    private String datasourceId;
    private ReduxModel model;

    public ConditionBranchesScope(List<N2oConditionBranch> scope) {
        this.scope = new LinkedList<>(scope);
    }

    public ConditionBranchesScope(ConditionBranchesScope scope, String datasourceId, ReduxModel model) {
        this.datasourceId = datasourceId;
        this.model = model;
        this.scope = scope.getScope();
    }

    public N2oConditionBranch pop() {
        return scope.poll();
    }
}
