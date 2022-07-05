package net.n2oapp.framework.config.metadata.compile.datasource;

import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.datasource.ApplicationDatasource;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oApplicationDatasource;
import org.springframework.stereotype.Component;

/**
 * Компиляция источника данных, ссылающегося на источник из application.xml
 */
@Component
public class ApplicationDatasourceCompiler extends BaseDatasourceCompiler<N2oApplicationDatasource, ApplicationDatasource> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oApplicationDatasource.class;
    }

    @Override
    public ApplicationDatasource compile(N2oApplicationDatasource source, CompileContext<?, ?> context, CompileProcessor p) {
        ApplicationDatasource compiled = new ApplicationDatasource();
        compiled.setProvider(new ApplicationDatasource.Provider());
        compiled.getProvider().setDatasource(source.getId());
        initDatasource(compiled, source, context, p);
        compiled.setDependencies(initDependencies(source, p));
        return compiled;
    }
}
