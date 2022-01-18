package net.n2oapp.framework.config.metadata.validation.standard.datasource;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.global.view.page.N2oDatasource;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import org.springframework.stereotype.Component;

/**
 * Валидатор исходного источника данных
 */
@Component
public class DatasourceValidator implements SourceValidator<N2oDatasource>, SourceClassAware {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oDatasource.class;
    }

    @Override
    public void validate(N2oDatasource datasource, SourceProcessor p) {
        if (datasource.getObjectId() != null)
            checkForExistsObject(datasource.getId(), datasource.getObjectId(), p);

        if (datasource.getQueryId() != null)
            checkForExistsQuery(datasource.getId(), datasource.getQueryId(), p);
    }

    /**
     * Проверка существования Объекта
     *
     * @param datasourceId Идентификатор источника данных
     * @param objectId     Идентификатор объекта
     * @param p            Процессор исходных метаданных
     */
    private void checkForExistsObject(String datasourceId, String objectId, SourceProcessor p) {
        p.checkForExists(objectId, N2oObject.class,
                String.format("Источник данных '%s' ссылается на несуществующий объект %s", datasourceId, objectId));
    }

    /**
     * Проверка существования выборки
     * @param datasourceId Идентификатор источника данных
     * @param queryId      Идентификатор выборки
     * @param p            Процессор исходных метаданных
     */
    private void checkForExistsQuery(String datasourceId, String queryId, SourceProcessor p) {
        p.checkForExists(queryId, N2oQuery.class,
                String.format("Источник данных '%s' ссылается на несуществующую выборку %s", datasourceId, queryId));
    }
}
