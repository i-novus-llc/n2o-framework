package net.n2oapp.framework.config.io.datasource;

import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oDatasource;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;

/**
 * Чтение/запись базового источника данных
 */
public abstract class BaseDatasourceIO<T extends N2oDatasource> extends AbstractDatasourceIO<T> {

    @Override
    public void io(Element e, T ds, IOProcessor p) {
        super.io(e, ds, p);
        p.anyChildren(e, "dependencies", ds::getDependencies, ds::setDependencies,
                p.oneOf(N2oDatasource.Dependency.class)
                        .add("fetch", N2oDatasource.FetchDependency.class, this::fetch)
                        .add("copy", N2oDatasource.CopyDependency.class, this::copy));
    }
}
