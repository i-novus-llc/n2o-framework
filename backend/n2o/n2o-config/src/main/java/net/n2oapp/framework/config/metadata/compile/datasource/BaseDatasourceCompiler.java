package net.n2oapp.framework.config.metadata.compile.datasource;

import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.datasource.AbstractDatasource;
import net.n2oapp.framework.config.metadata.compile.BaseSourceCompiler;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;

/**
 * Базовая компиляция источника данных
 */
public abstract class BaseDatasourceCompiler<S extends N2oAbstractDatasource, D extends AbstractDatasource> implements
        BaseSourceCompiler<D, S, CompileContext<?, ?>> {

    protected void initDatasource(AbstractDatasource datasource, N2oAbstractDatasource source, CompileContext<?, ?> context,
                                  CompileProcessor p) {
        PageScope pageScope = p.getScope(PageScope.class);
        datasource.setId(pageScope.getGlobalDatasourceId(source.getId()));
    }
}
