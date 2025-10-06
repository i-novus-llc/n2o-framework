package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.action.N2oMultiAction;
import net.n2oapp.framework.api.metadata.action.N2oOnFailAction;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.meta.action.Action;
import net.n2oapp.framework.api.metadata.meta.action.multi.MultiAction;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.config.metadata.compile.action.ActionCompileStaticProcessor.*;

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
                .filter(a -> isNotFailConditions(a) && !(a instanceof N2oOnFailAction))
                .map(n2oAction -> (Action) p.compile(n2oAction, context,
                        initFailConditionBranchesScope(n2oAction, source.getN2oActions())))
                .toList();

        MultiAction multiAction = new MultiAction(actions);
        multiAction.setType(p.resolve(property("n2o.api.action.multi.type"), String.class));
        Optional<N2oOnFailAction> onFailAction = Arrays.stream(source.getN2oActions())
                .filter(N2oOnFailAction.class::isInstance)
                .map(N2oOnFailAction.class::cast).findFirst();
        if (onFailAction.isPresent()) {
            if (onFailAction.get().getActions().length > 1) {
                N2oMultiAction multi = new N2oMultiAction();
                multi.setN2oActions(onFailAction.get().getActions());
                multiAction.getPayload().setFallback(p.compile(multi, context));
            } else {
                multiAction.getPayload().setFallback(p.compile(onFailAction.get().getActions()[0], context));
            }
        }

        return multiAction;
    }
}
