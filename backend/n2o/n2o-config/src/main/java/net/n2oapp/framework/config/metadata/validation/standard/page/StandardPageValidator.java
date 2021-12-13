package net.n2oapp.framework.config.metadata.validation.standard.page;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.global.view.page.N2oStandardPage;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import org.springframework.stereotype.Component;

/**
 * Валидатор стандартной страницы
 */
@Component
public class StandardPageValidator implements SourceValidator<N2oStandardPage>, SourceClassAware {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oStandardPage.class;
    }

    @Override
    public void validate(N2oStandardPage page, SourceProcessor p) {
        p.checkIdsUnique(page.getWidgets(), "Виджет {0} встречается более чем один раз на странице " + page.getId());
    }
}
