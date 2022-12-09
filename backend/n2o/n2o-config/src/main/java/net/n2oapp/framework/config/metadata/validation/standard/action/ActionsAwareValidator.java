package net.n2oapp.framework.config.metadata.validation.standard.action;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.ActionsAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.action.N2oAction;
import net.n2oapp.framework.api.metadata.action.ifelse.N2oConditionBranch;
import net.n2oapp.framework.api.metadata.validation.TypedMetadataValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.widget.MetaActions;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Collectors;

import static net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils.checkActionExistence;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

/**
 * Валидатор {@link ActionsAware} компонентов
 */
@Component
public class ActionsAwareValidator extends TypedMetadataValidator<ActionsAware> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return ActionsAware.class;
    }

    @Override
    public void validate(ActionsAware source, SourceProcessor p) {
        MetaActions metaActions = p.getScope(MetaActions.class);
        N2oAction[] actions = source.getActions();
        String actionId = source.getActionId();

        if (actionId != null) {
            if (metaActions.isEmpty())
                throw new N2oMetadataValidationException(
                        String.format("Для компонента с action-id=\"%s\" не найдены действия <actions>",
                                actionId)
                );
            checkActionExistence(source.getActionId(), metaActions,
                    String.format("Компонент с action-id=\"%s\" ссылается на несуществующее действие %s", actionId, source.getActionId()));
            if (isNotEmpty(actions))
                throw new N2oMetadataValidationException(
                        String.format("Компонент с action-id=\"%s\" содержит действия и использует ссылку action-id одновременно",
                                actionId)
                );
        }

        if (isNotEmpty(actions)) {
            LinkedList<N2oConditionBranch> ifElseBranches = Arrays.stream(actions)
                    .filter(N2oConditionBranch.class::isInstance)
                    .map(N2oConditionBranch.class::cast)
                    .collect(Collectors.toCollection(LinkedList::new));
            if (!ifElseBranches.isEmpty())
                ValidationUtils.validateIfElse(ifElseBranches, p);

            Arrays.stream(actions)
                    .forEach(action -> p.validate(action, metaActions, new ComponentScope(source, p.getScope(ComponentScope.class))));
        }
    }
}
