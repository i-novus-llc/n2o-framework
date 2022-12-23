package net.n2oapp.framework.config.metadata.validation.standard.control;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.control.N2oMarkdown;
import net.n2oapp.framework.api.metadata.validation.TypedMetadataValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.widget.MetaActions;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * Валидатор {@link N2oMarkdown} компонентов
 */
@Component
public class MarkdownValidator extends TypedMetadataValidator<N2oMarkdown> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oMarkdown.class;
    }

    @Override
    public void validate(N2oMarkdown source, SourceProcessor p) {
        if (!ArrayUtils.isEmpty(source.getActionIds())) {
            MetaActions metaActions = getMetaActions(p);

            if (CollectionUtils.isEmpty(metaActions))
                throw new N2oMetadataValidationException(String.format("Для компонента с actions=\"%s\" не найдены действия <actions>",
                        String.join(",", source.getActionIds())));

            for (String actionId : source.getActionIds())
                if (!metaActions.containsKey(actionId))
                    throw new N2oMetadataValidationException(
                            String.format("Компонент с actions \"%s\" ссылается на несуществующее действие", actionId));
        }
    }

    private MetaActions getMetaActions(SourceProcessor p) {
        MetaActions pageActions = p.getScope(MetaActions.class);
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        MetaActions metaActions = widgetScope == null || widgetScope.getActions() == null ? new MetaActions() : widgetScope.getActions();

        if (!CollectionUtils.isEmpty(pageActions))
            metaActions.putAll(pageActions);
        return metaActions;
    }
}
