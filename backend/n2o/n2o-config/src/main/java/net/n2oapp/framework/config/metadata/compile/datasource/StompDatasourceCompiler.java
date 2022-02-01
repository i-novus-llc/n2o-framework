package net.n2oapp.framework.config.metadata.compile.datasource;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.application.N2oStompDatasource;
import net.n2oapp.framework.api.metadata.application.StompDatasource;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        compiled.setProvider(initProvider(source));
        compiled.setValues(initValues(source));
        return compiled;
    }

    private StompDatasource.Provider initProvider(N2oStompDatasource source) {
        StompDatasource.Provider provider = new StompDatasource.Provider();
        provider.setDestination(source.getDestination());
        provider.setType("stomp");
        return provider;
    }

    private List<Map<String, Object>> initValues(N2oStompDatasource source) {
        if (source.getValues() == null)
            return null;
        List<Map<String, Object>> values = new ArrayList<>();
        for (Map.Entry<String, Object> entry : source.getValues().entrySet()) {
            Map<String, Object> value = new HashMap<>();
            value.put(entry.getKey(), entry.getValue());
            values.add(value);
        }
        return values;
    }
}
