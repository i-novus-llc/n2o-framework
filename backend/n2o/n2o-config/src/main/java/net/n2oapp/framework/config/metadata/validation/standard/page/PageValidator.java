package net.n2oapp.framework.config.metadata.validation.standard.page;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.N2oMetadata;
import net.n2oapp.framework.api.metadata.global.view.page.N2oPage;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

import static net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils.getIdOrEmptyString;

/**
 * Валидатор страницы
 */
@Component
public class PageValidator implements SourceValidator<N2oPage>, SourceClassAware {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oPage.class;
    }

    @Override
    public void validate(N2oPage page, SourceProcessor p) {
        if (page.getObjectId() != null)
            ValidationUtils.checkForExistsObject(page.getObjectId(),
                    String.format("Страница %s", getIdOrEmptyString(page.getId())), p);

        PageScope scope = new PageScope();
        scope.setWidgetIds(p.safeStreamOf(page.getWidgets()).map(N2oMetadata::getId).collect(Collectors.toSet()));
    }
}