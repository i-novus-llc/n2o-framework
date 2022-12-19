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

import java.util.HashMap;

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
            MetaActions pageMetaActions = p.getScope(MetaActions.class);
            MetaActions widgetActions = getWidgetActions(p);
            if ((pageMetaActions == null || pageMetaActions.isEmpty()) && widgetActions.isEmpty())
                throw new N2oMetadataValidationException(String.format("Для компонента с actions=\"%s\" не найдены действия <actions>",
                        String.join(",", source.getActionIds())));
            for (String actionId : source.getActionIds()) {
                if (!(pageMetaActions.containsKey(actionId) || widgetActions.containsKey(actionId)))
                    throw new N2oMetadataValidationException(
                            String.format("Компонент с actions \"%s\" ссылается на несуществующее действие", actionId));
            }
        }
    }

    private MetaActions getWidgetActions(SourceProcessor p) {
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        if (widgetScope == null || widgetScope.getActions() == null || widgetScope.getActions().isEmpty())
            return new MetaActions();
        return widgetScope.getActions();
    }
}
