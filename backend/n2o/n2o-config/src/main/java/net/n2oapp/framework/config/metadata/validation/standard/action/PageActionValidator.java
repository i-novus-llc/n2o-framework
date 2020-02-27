package net.n2oapp.framework.config.metadata.validation.standard.action;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.event.action.N2oAbstractPageAction;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.global.view.page.N2oPage;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validate.ValidateProcessor;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import org.springframework.stereotype.Component;

/**
 * Валидатор действия открытия страницы
 */
@Component
public class PageActionValidator implements SourceValidator<N2oAbstractPageAction>, SourceClassAware {
    @Override
    public void validate(N2oAbstractPageAction source, ValidateProcessor p) {
        p.checkForExists(source.getObjectId(), N2oObject.class,
                "Действие открытия страницы: " + source.getId() +
                        " ссылается на несуществующий объект: " + source.getObjectId());

        p.checkForExists(source.getPageId(), N2oPage.class,
                "Действие открытия страницы: " + source.getId() +
                        " ссылается на несуществующую страницу: " + source.getPageId());
        if (source.getSubmitOperationId() != null && source.getObjectId() != null) {
            N2oObject object = p.getOrNull(source.getObjectId(), N2oObject.class);
            p.safeStreamOf(object.getOperations()).
                    filter(operation -> source.getSubmitOperationId().equals(operation.getId())).
                    findFirst().orElseThrow(() -> new N2oMetadataValidationException("Действие открытия страницы: " + source.getId() +
                    " ссылается на несуществующую в объекте: " + source.getObjectId() + " операцию: " + source.getSubmitOperationId()));
        }
        if (source.getRefreshWidgetId() != null) {
            p.checkForExists(source.getRefreshWidgetId(), N2oWidget.class, "Атрибут refresh-widget-id ссылается на несуществующий виджет: " + source.getRefreshWidgetId());
        }
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oAbstractPageAction.class;
    }
}
