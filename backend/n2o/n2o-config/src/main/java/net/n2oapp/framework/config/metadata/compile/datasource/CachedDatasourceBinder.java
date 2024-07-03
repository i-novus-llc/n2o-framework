package net.n2oapp.framework.config.metadata.compile.datasource;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.BindProcessor;
import net.n2oapp.framework.api.metadata.datasource.CachedDatasource;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import net.n2oapp.framework.config.util.BindUtil;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Связывание с данными кэширующего источника данных
 */
@Component
public class CachedDatasourceBinder implements BaseMetadataBinder<CachedDatasource> {
    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return CachedDatasource.class;
    }

    @Override
    public CachedDatasource bind(CachedDatasource datasource, BindProcessor p) {
        if (datasource.getProvider() != null) {
            Map<String, ModelLink> pathMapping = datasource.getProvider().getPathMapping();
            Map<String, ModelLink> queryMapping = datasource.getProvider().getQueryMapping();
            datasource.getProvider().setUrl(p.resolveUrl(datasource.getProvider().getUrl(), pathMapping, queryMapping));
            if (pathMapping != null) {
                pathMapping.forEach((k, v) -> pathMapping.put(k, (ModelLink) p.resolveLink(v)));
            }
            if (queryMapping != null) {
                queryMapping.forEach((k, v) -> queryMapping.put(k, (ModelLink) p.resolveLink(v)));
            }
        }

        if (datasource.getSubmit() != null) {
            BindUtil.bindDataProvider(datasource.getSubmit(), p);
        }
        return datasource;
    }
}
