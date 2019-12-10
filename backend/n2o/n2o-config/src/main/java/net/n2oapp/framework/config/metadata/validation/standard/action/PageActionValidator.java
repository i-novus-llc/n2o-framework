package net.n2oapp.framework.config.metadata.validation.standard.action;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.event.action.N2oAbstractPageAction;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validate.ValidateProcessor;
import org.springframework.stereotype.Component;

/**
 * Валидатор действия открытия страницы
 */
@Component
public class PageActionValidator implements SourceValidator<N2oAbstractPageAction>, SourceClassAware {
    @Override
    public void validate(N2oAbstractPageAction source, ValidateProcessor p) {
        if (source != null)
            p.checkForExists(source.getObjectId(), N2oObject.class,
                    "Действие открытия страницы: " + source.getId() + " ссылается на несуществующий объект: " + source.getObjectId());
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oAbstractPageAction.class;
    }
}
