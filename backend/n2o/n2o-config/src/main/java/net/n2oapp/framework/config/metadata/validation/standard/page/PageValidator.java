package net.n2oapp.framework.config.metadata.validation.standard.page;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.global.N2oMetadata;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.global.view.page.N2oStandardPage;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validate.ValidateProcessor;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    public void validate(N2oStandardPage page, ValidateProcessor p) {
        if (page.getObjectId() != null) {
            p.checkForExists(page.getObjectId(), N2oObject.class,
                    "Страница '" + page.getId() + "' ссылается не несуществующий объект {0}");
        }
        p.checkIdsUnique(page.getN2oRegions(), "Виджет {0} встречается более чем один раз на странице " + page.getId());
        Set<String> widgetIds = p.safeStreamOf(page.getContainers()).map(N2oMetadata::getId).collect(Collectors.toSet());
        p.safeStreamOf(page.getContainers())
                .filter(w -> w.getDependsOn() != null)
                .forEach(w -> {
                    if (!widgetIds.contains(w.getDependsOn()))
                        throw new N2oMetadataValidationException("depends-on link to a non-existent widget " + w.getDependsOn());
                });
    }
}
