package net.n2oapp.framework.config.metadata.compile.datasource;

import net.n2oapp.framework.api.metadata.N2oAbstractDatasource;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.datasource.AbstractDatasource;
import net.n2oapp.framework.config.metadata.compile.BaseSourceCompiler;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;

import static net.n2oapp.framework.config.util.CompileUtil.getClientDatasourceId;

/**
 * Абстрактная компиляция источника данных
 */
public abstract class AbstractDatasourceCompiler<S extends N2oAbstractDatasource, D extends AbstractDatasource>
        implements BaseSourceCompiler<D, S, CompileContext<?, ?>> {

    protected void initDatasource(N2oAbstractDatasource source, AbstractDatasource datasource, CompileProcessor p) {
        String id = getClientDatasourceId(source.getId(), p.getScope(PageScope.class));
        datasource.setId(id);
    }
}
