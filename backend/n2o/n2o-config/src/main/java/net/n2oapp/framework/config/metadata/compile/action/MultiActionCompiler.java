package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.action.N2oMultiAction;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.action.multi.MultiAction;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.config.metadata.compile.action.ActionCompileStaticProcessor.initFailConditionBranchesScope;

/**
 * Сборка multi действия
 */
@Component
public class MultiActionCompiler extends AbstractActionCompiler<MultiAction, N2oMultiAction> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oMultiAction.class;
    }

    @Override
    public MultiAction compile(N2oMultiAction source, CompileContext<?, ?> context, CompileProcessor p) {
        List<Action> actions = Arrays.stream(source.getN2oActions())
                .filter(ActionCompileStaticProcessor::isNotFailConditions)
                .map(n2oAction -> (Action) p.compile(n2oAction, context,
                        initFailConditionBranchesScope(n2oAction, source.getN2oActions())))
                .collect(Collectors.toList());

        MultiAction multiAction = new MultiAction(actions);
        multiAction.setType(p.resolve(property("n2o.api.action.multi.type"), String.class));
        return multiAction;
    }
}
