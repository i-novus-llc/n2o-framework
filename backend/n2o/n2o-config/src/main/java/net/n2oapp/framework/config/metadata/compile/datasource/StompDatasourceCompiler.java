package net.n2oapp.framework.config.metadata.compile.datasource;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.application.N2oStompDatasource;
import net.n2oapp.framework.api.metadata.application.StompDatasource;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import org.springframework.stereotype.Component;

/**
 * Компиляция  STOMP-источника данных
 */
@Component
public class StompDatasourceCompiler extends BaseDatasourceCompiler<N2oStompDatasource, StompDatasource> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oStompDatasource.class;
    }

    @Override
    public StompDatasource compile(N2oStompDatasource source, CompileContext<?, ?> context, CompileProcessor p) {
        StompDatasource compiled = new StompDatasource();
        initDatasource(compiled, source, context, p);
        initProvider(compiled, source);
        compiled.setValues(source.getValues());
        return compiled;
    }

    private void initProvider(StompDatasource compiled, N2oStompDatasource source) {
        StompDatasource.Provider provider = new StompDatasource.Provider();
        provider.setType("stomp");
        provider.setDestination(source.getDestination());
        compiled.setProvider(provider);
    }
}
