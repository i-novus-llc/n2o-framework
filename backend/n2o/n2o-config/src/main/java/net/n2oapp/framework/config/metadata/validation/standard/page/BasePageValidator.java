package net.n2oapp.framework.config.metadata.validation.standard.page;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.global.view.page.N2oBasePage;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validate.ValidateProcessor;
import org.springframework.stereotype.Component;

/**
 * Валидатор "Исходной" модели страницы
 */
@Component
public class BasePageValidator implements SourceValidator<N2oBasePage>, SourceClassAware {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oBasePage.class;
    }

    @Override
    public void validate(N2oBasePage page, ValidateProcessor p) {
        p.safeStreamOf(page.getToolbars())
                .forEach(n2oToolbar -> p.safeStreamOf(n2oToolbar.getAllActions()).forEach(p::validate));
        p.safeStreamOf(page.getActions()).forEach(actionsBar -> p.validate(actionsBar.getAction()));
    }
}
