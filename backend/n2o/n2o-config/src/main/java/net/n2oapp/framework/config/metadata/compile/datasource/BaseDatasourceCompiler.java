package net.n2oapp.framework.config.metadata.compile.datasource;

import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.datasource.AbstractDatasource;
import net.n2oapp.framework.api.metadata.global.view.page.N2oDatasource;
import net.n2oapp.framework.api.metadata.global.view.page.N2oStandardDatasource;
import net.n2oapp.framework.api.metadata.meta.CopyDependency;
import net.n2oapp.framework.api.metadata.meta.Dependency;
import net.n2oapp.framework.api.metadata.meta.DependencyConditionType;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.config.metadata.compile.ValidationList;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import net.n2oapp.framework.config.util.CompileUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Базовая компиляция источника данных
 */
public abstract class BaseDatasourceCompiler<S extends N2oDatasource, D extends AbstractDatasource> extends AbstractDatasourceCompiler<S, D> {

    protected Map<String, List<Validation>> initValidations(N2oDatasource source, CompileProcessor p) {
        ValidationList validationList = p.getScope(ValidationList.class);
        if (validationList != null) {
            //todo why RESOLVE ?
            return validationList.get(source.getId(), ReduxModel.resolve).stream()
                    .filter(v -> v.getSide() == null || v.getSide().contains("client"))
                    .collect(Collectors.groupingBy(Validation::getFieldId));
        } else
            return Collections.emptyMap();
    }

    protected List<Dependency> initDependencies(N2oDatasource source, CompileProcessor p) {
        PageScope pageScope = p.getScope(PageScope.class);
        if (pageScope == null)
            return null;
        List<Dependency> dependencies = new ArrayList<>();
        String pageId = pageScope.getPageId();
        if (source.getDependencies() != null) {
            for (N2oStandardDatasource.Dependency d : source.getDependencies()) {
                if (d instanceof N2oStandardDatasource.FetchDependency) {
                    N2oStandardDatasource.FetchDependency dependency = (N2oStandardDatasource.FetchDependency) d;
                    ModelLink bindLink = new ModelLink(p.cast(dependency.getModel(), ReduxModel.resolve),
                            CompileUtil.generateWidgetId(pageId, dependency.getOn()));
                    Dependency fetchDependency = new Dependency();
                    fetchDependency.setOn(bindLink.getBindLink());
                    fetchDependency.setType(DependencyConditionType.fetch);
                    dependencies.add(fetchDependency);
                } else if (d instanceof N2oStandardDatasource.CopyDependency) {
                    N2oStandardDatasource.CopyDependency dependency = (N2oStandardDatasource.CopyDependency) d;
                    CopyDependency copyDependency = new CopyDependency();
                    ModelLink modelLink = new ModelLink(p.cast(dependency.getSourceModel(), ReduxModel.resolve),
                            dependency.getOn(), dependency.getSourceFieldId());
                    copyDependency.setModel(p.cast(dependency.getTargetModel(), ReduxModel.resolve));
                    copyDependency.setType(DependencyConditionType.copy);
                    copyDependency.setField(dependency.getTargetFieldId());
                    copyDependency.setOn(modelLink.getBindLink());
                    dependencies.add(copyDependency);
                }
            }
        }
        return dependencies;
    }
}
