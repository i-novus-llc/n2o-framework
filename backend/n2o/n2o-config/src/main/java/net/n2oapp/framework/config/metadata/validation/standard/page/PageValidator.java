package net.n2oapp.framework.config.metadata.validation.standard.page;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.global.view.page.N2oPage;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.validation.ValidationUtil;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * Валидатор страницы
 */
@Component
public class PageValidator implements SourceValidator<N2oPage>, SourceClassAware {
    @Override
    public void validate(N2oPage n2oPage) throws N2oMetadataValidationException {
        ValidationUtil.checkIdsUnique(ValidationUtil.retrieveContainers(n2oPage), "Виджет '%s' встречается более чем один раз в пейдже \'" + n2oPage.getId() + "\'!");
        if (n2oPage.getObjectId() != null)
            ValidationUtil.checkForExists(n2oPage.getObjectId(), N2oObject.class,
                    "Страница '" + n2oPage.getId() + "' ссылается не несуществующий объект '"  + n2oPage.getObjectId() + "'");
        final List<N2oWidget> containers = ValidationUtil.retrieveContainers(n2oPage);
        containers.stream().filter(it -> it.getDependsOn() != null).forEach(it -> {
            findContainer(n2oPage, containers, it.getDependsOn());
        });
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oPage.class;
    }

    private static N2oWidget findContainer(final N2oPage page, List<N2oWidget> containers, final String containerId) {
        N2oWidget container = containers.stream().filter(it -> containerId.equals(it.getId())).findFirst().orElse(null);
        if (container != null)
            return container;
        throw new N2oMetadataValidationException("Не найден виджет \'" + containerId + "\' на странице \'" + page.getId() + "\'");
    }
}
