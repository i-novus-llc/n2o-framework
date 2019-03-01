package net.n2oapp.framework.config.metadata.validation.standard.page;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.global.view.page.N2oPage;
import net.n2oapp.framework.api.metadata.global.view.page.N2oStandardPage;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validate.ValidateProcessor;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * Валидатор страницы
 */
@Component
public class PageValidator implements SourceValidator<N2oStandardPage>, SourceClassAware {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oStandardPage.class;
    }

    @Override
    public void validate(N2oStandardPage n2oPage, ValidateProcessor p) {
        if (n2oPage.getObjectId() != null) {
            p.checkForExists(n2oPage.getObjectId(), N2oObject.class,
                    "Страница '" + n2oPage.getId() + "' ссылается не несуществующий объект {0}");
        }
        p.checkIdsUnique(n2oPage.getN2oRegions(), "Виджет {0} встречается более чем один раз на странице " + n2oPage.getId());
    }
}
