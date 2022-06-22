package net.n2oapp.framework.config.metadata.compile.datasource;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.datasource.ApplicationDatasource;
import net.n2oapp.framework.api.metadata.global.view.page.N2oApplicationDatasource;
import net.n2oapp.framework.api.metadata.meta.DependencyCondition;
import net.n2oapp.framework.api.metadata.meta.DependencyConditionType;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import net.n2oapp.framework.config.util.CompileUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Компиляция источника данных ссылающегося на источник из application.xml
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

    private List<DependencyCondition> initDependencies(N2oApplicationDatasource source, CompileProcessor p) {
        PageScope pageScope = p.getScope(PageScope.class);
        if (pageScope == null)
            return null;
        List<DependencyCondition> fetch = new ArrayList<>();
        String pageId = pageScope.getPageId();
        if (source.getDependencies() != null) {
            for (N2oApplicationDatasource.Dependency d : source.getDependencies())
                if (d instanceof N2oApplicationDatasource.FetchDependency) {
                    ModelLink bindLink = new ModelLink(p.cast(((N2oApplicationDatasource.FetchDependency) d).getModel(), ReduxModel.resolve),
                            CompileUtil.generateWidgetId(pageId, ((N2oApplicationDatasource.FetchDependency) d).getOn()));
                    DependencyCondition condition = new DependencyCondition();
                    condition.setOn(bindLink.getBindLink());
                    condition.setType(DependencyConditionType.fetch);
                    fetch.add(condition);
                }
        }
        return fetch;
    }
}
