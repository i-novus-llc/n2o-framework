package net.n2oapp.framework.config.metadata.validation.standard.page;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.global.view.page.N2OStandardPage;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validate.ValidateProcessor;
import org.springframework.stereotype.Component;

/**
 * Валидатор стандартной страницы
 */
@Component
public class StandardPageValidator implements SourceValidator<N2OStandardPage>, SourceClassAware {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2OStandardPage.class;
    }

    @Override
    public void validate(N2OStandardPage page, ValidateProcessor p) {
        p.checkIdsUnique(page.getRegions(), "Виджет {0} встречается более чем один раз на странице " + page.getId());
        p.safeStreamOf(page.getToolbars())
                .forEach(n2oToolbar -> p.safeStreamOf(n2oToolbar.getAllActions()).forEach(p::validate));
    }
}
