package net.n2oapp.framework.config.metadata.validation.standard;

import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.config.metadata.compile.datasource.DatasourceIdsScope;

import java.util.List;

import static java.util.Objects.nonNull;

/**
 * Утилита для проверки метаданных страниц
 */
public class PageValidationUtil {

    public static void fillDatasourceIdsScopeByInlineDatasource(List<N2oWidget> widgets, DatasourceIdsScope datasourceIdsScope, SourceProcessor p) {
        p.safeStreamOf(widgets).filter(widget -> widget.getDatasourceId() == null)
                .forEach(widget -> datasourceIdsScope.add(
                        nonNull(widget.getDatasource()) && nonNull(widget.getDatasource().getId()) ?
                                widget.getDatasource().getId() : widget.getId()));
    }
}
