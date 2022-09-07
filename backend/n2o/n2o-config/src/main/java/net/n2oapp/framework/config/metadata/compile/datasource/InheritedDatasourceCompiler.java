package net.n2oapp.framework.config.metadata.compile.datasource;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.datasource.InheritedDatasource;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oInheritedDatasource;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.util.DatasourceUtil.getClientDatasourceId;

/**
 * Компиляция источника данных, получающего данные из другого источника данных
 */
@Component
public class InheritedDatasourceCompiler extends BaseDatasourceCompiler<N2oInheritedDatasource, InheritedDatasource> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oInheritedDatasource.class;
    }

    @Override
    public InheritedDatasource compile(N2oInheritedDatasource source, CompileContext<?, ?> context, CompileProcessor p) {
        InheritedDatasource compiled = new InheritedDatasource();
        compileDatasource(source, compiled, context, p);
        compiled.setProvider(initProvider(source, context, p));
        compiled.setSubmit(initSubmit(source, context, p));
        return compiled;
    }

    private InheritedDatasource.Submit initSubmit(N2oInheritedDatasource source, CompileContext<?, ?> context, CompileProcessor p) {
        if (source.getSubmit() == null) return null;

        InheritedDatasource.Submit submit = new InheritedDatasource.Submit();
        N2oInheritedDatasource.Submit sourceSubmit = source.getSubmit();
        submit.setAuto(p.cast(sourceSubmit.getAuto(), true));
        submit.setModel(p.cast(sourceSubmit.getModel(), ReduxModel.resolve));
        submit.setTargetDs(getClientDatasourceId(p.cast(sourceSubmit.getTargetDatasource(), source.getSourceDatasource()), context, p));
        submit.setTargetModel(p.cast(sourceSubmit.getTargetModel(), source.getSourceModel(), ReduxModel.resolve));
        submit.setTargetField(p.cast(sourceSubmit.getTargetFieldId(), source.getSourceFieldId()));
        return submit;
    }

    private InheritedDatasource.Provider initProvider(N2oInheritedDatasource source, CompileContext<?, ?> context, CompileProcessor p) {
        InheritedDatasource.Provider provider = new InheritedDatasource.Provider();
        provider.setSourceDs(getClientDatasourceId(source.getSourceDatasource(), context, p));
        provider.setSourceModel(p.cast(source.getSourceModel(), ReduxModel.resolve));
        provider.setSourceField(source.getSourceFieldId());
        return provider;
    }
}
