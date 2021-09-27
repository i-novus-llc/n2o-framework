package net.n2oapp.framework.config.metadata.validation.standard.page;

import net.n2oapp.criteria.filters.Pair;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.global.N2oMetadata;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.global.view.page.N2oPage;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validate.ValidateProcessor;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
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
    public void validate(N2oPage page, ValidateProcessor p) {
        if (page.getObjectId() != null) {
            checkForExistsObject(page.getId(), page.getObjectId(), p);
        }

        PageScope scope = new PageScope();
        scope.setWidgetIds(p.safeStreamOf(page.getWidgets()).map(N2oMetadata::getId).collect(Collectors.toSet()));
        p.safeStreamOf(page.getWidgets()).forEach(widget -> p.validate(widget, scope));
        checkForExistsDependsOnWidget(page, scope, p);
        checkWidgetDatasources(page);
    }

    /**
     * Проверка существования Объекта
     *
     * @param pageId   Идентификатор страницы
     * @param objectId Идентификатор объекта
     * @param p        Процессор валидации метаданных
     */
    private void checkForExistsObject(String pageId, String objectId, ValidateProcessor p) {
        p.checkForExists(objectId, N2oObject.class,
                String.format("Страница %s ссылается на несуществующий объект %s", pageId, objectId));
    }

    /**
     * Проверка существования depends-on виджетов
     *
     * @param page  Страница
     * @param scope Информация о странице
     * @param p     Процессор валидации метаданных
     */
    private void checkForExistsDependsOnWidget(N2oPage page, PageScope scope, ValidateProcessor p) {
        p.safeStreamOf(page.getWidgets())
                .filter(w -> w.getDependsOn() != null)
                .forEach(w -> {
                    if (!scope.getWidgetIds().contains(w.getDependsOn()))
                        throw new N2oMetadataValidationException(
                                "Атрибут depends-on ссылается на несуществующий виджет " + w.getDependsOn());
                });
    }

    /**
     * Проверка идентичности query-id и object-id при одинаковом datasource
     *
     * @param page  Страница
     */
    private void checkWidgetDatasources(N2oPage page) {
        if (page.getWidgets() == null || page.getWidgets().isEmpty()) return;
        Map<String, Pair<String>> datasourceMap = new HashMap<>();
        page.getWidgets().forEach(w -> {
            if (w.getDatasource() != null) {
                if (datasourceMap.containsKey(w.getDatasource())){
                    Pair<String> actual = datasourceMap.get(w.getDatasource());
                    if (!actual.getLeft().equals(w.getQueryId()) || !actual.getRight().equals(w.getObjectId())) {
                        throw new N2oMetadataValidationException(
                                String.format("2 виджета с одинаковым datasource %s имеют разные query-id или object-id",
                                        w.getDatasource()));
                    }
                } else {
                    datasourceMap.put(w.getDatasource(), new Pair<>(w.getQueryId(), w.getObjectId()));
                }
            }
        });
    }
}
