package net.n2oapp.framework.config.metadata.validation.standard.page;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.global.N2oMetadata;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.global.view.page.N2oPage;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

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
        if (page.getObjectId() != null) {
            checkForExistsObject(page.getId(), page.getObjectId(), p);
        }

        PageScope scope = new PageScope();
        scope.setWidgetIds(p.safeStreamOf(page.getWidgets()).map(N2oMetadata::getId).collect(Collectors.toSet()));
        checkForExistsDependsOnWidget(page, scope, p);
    }

    /**
     * Проверка существования Объекта
     *
     * @param pageId   Идентификатор страницы
     * @param objectId Идентификатор объекта
     * @param p        Процессор исходных метаданных
     */
    private void checkForExistsObject(String pageId, String objectId, SourceProcessor p) {
        p.checkForExists(objectId, N2oObject.class,
                String.format("Страница %s ссылается на несуществующий объект %s", pageId, objectId));
    }

    /**
     * Проверка существования depends-on виджетов
     *
     * @param page  Страница
     * @param scope Информация о странице
     * @param p     Процессор исходных метаданных
     */
    private void checkForExistsDependsOnWidget(N2oPage page, PageScope scope, SourceProcessor p) {
        p.safeStreamOf(page.getWidgets())
                .filter(w -> w.getDependsOn() != null)
                .forEach(w -> {
                    if (!scope.getWidgetIds().contains(w.getDependsOn()))
                        throw new N2oMetadataValidationException(
                                "Атрибут depends-on ссылается на несуществующий виджет " + w.getDependsOn());
                });
    }

}
