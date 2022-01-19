package net.n2oapp.framework.config.metadata.validation.standard.page;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.global.N2oMetadata;
import net.n2oapp.framework.api.metadata.global.view.page.N2oBasePage;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

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
    public void validate(N2oBasePage page, SourceProcessor p) {
        PageScope scope = new PageScope();
        scope.setWidgetIds(p.safeStreamOf(page.getWidgets()).map(N2oMetadata::getId).collect(Collectors.toSet()));

        p.safeStreamOf(page.getToolbars())
                .forEach(n2oToolbar -> p.safeStreamOf(n2oToolbar.getAllActions()).forEach(action -> p.validate(action, scope)));
        p.safeStreamOf(page.getActions()).forEach(actionsBar -> p.validate(actionsBar.getAction(), scope));

        p.checkIdsUnique(page.getDatasources(),
                "Источник данных {0} встречается более чем один раз в метаданной страницы " + page.getId());
    }
}
