package net.n2oapp.framework.config.metadata.compile.datasource;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.datasource.InheritedDatasource;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import org.springframework.stereotype.Component;

/**
 * Связывание с данными источника, получающего данные из другого источника данных
 */
@Component
public class InheritedDatasourceBinder implements BaseMetadataBinder<InheritedDatasource> {
    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return InheritedDatasource.class;
    }

    @Override
    public InheritedDatasource bind(InheritedDatasource compiled, BindProcessor p) {
        if (compiled.getFilters() != null) {
            compiled.getFilters().stream().filter(f -> f.getModelLink() != null)
                    .forEach(f -> {
                        ModelLink resolvedLink = (ModelLink) p.resolveLink(f.getModelLink(), true);
                        f.setModelLink(resolvedLink);
                    });
        }
        return compiled;
    }
}
