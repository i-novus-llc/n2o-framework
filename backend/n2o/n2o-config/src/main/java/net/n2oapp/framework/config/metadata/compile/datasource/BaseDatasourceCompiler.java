package net.n2oapp.framework.config.metadata.compile.datasource;

import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.datasource.AbstractDatasource;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oDatasource;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oStandardDatasource;
import net.n2oapp.framework.api.metadata.meta.CopyDependency;
import net.n2oapp.framework.api.metadata.meta.Dependency;
import net.n2oapp.framework.api.metadata.meta.DependencyType;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.config.metadata.compile.ValidationScope;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.config.util.DatasourceUtil.getClientDatasourceId;

/**
 * Базовая компиляция источника данных
 */
public abstract class BaseDatasourceCompiler<S extends N2oDatasource, D extends AbstractDatasource> extends AbstractDatasourceCompiler<S, D> {

    public void compileDatasource(S source, D compiled, CompileContext<?, ?> context, CompileProcessor p) {
        initDatasource(source, compiled, p);
        compiled.setSize(p.cast(source.getSize(), p.resolve(property("n2o.api.datasource.size"), Integer.class)));
        compiled.setDependencies(initDependencies(source, context, p));
        compiled.setValidations(initValidations(source, p, ReduxModel.resolve));
        compiled.setFilterValidations(initValidations(source, p, ReduxModel.filter));
    }

    protected Map<String, List<Validation>> initValidations(S source, CompileProcessor p, ReduxModel model) {
        ValidationScope validationScope = p.getScope(ValidationScope.class);
        if (validationScope != null) {
            return validationScope.get(source.getId(), model).stream()
                    .filter(v -> v.getSide() == null || v.getSide().contains("client"))
                    .collect(Collectors.groupingBy(Validation::getFieldId));
        } else
            return Collections.emptyMap();
    }

    protected List<Dependency> initDependencies(N2oDatasource source, CompileContext<?, ?> context, CompileProcessor p) {
        List<Dependency> dependencies = new ArrayList<>();
        if (source.getDependencies() != null) {
            for (N2oStandardDatasource.Dependency d : source.getDependencies()) {
                if (d instanceof N2oStandardDatasource.FetchDependency) {
                    N2oStandardDatasource.FetchDependency dependency = (N2oStandardDatasource.FetchDependency) d;
                    Dependency fetchDependency = new Dependency();
                    ModelLink link = new ModelLink(p.cast(dependency.getModel(), ReduxModel.resolve),
                            getClientDatasourceId(dependency.getOn(), context, p));
                    fetchDependency.setOn(link.getBindLink());
                    fetchDependency.setType(DependencyType.fetch);
                    dependencies.add(fetchDependency);
                } else if (d instanceof N2oStandardDatasource.CopyDependency) {
                    N2oStandardDatasource.CopyDependency dependency = (N2oStandardDatasource.CopyDependency) d;
                    CopyDependency copyDependency = new CopyDependency();
                    ModelLink link = new ModelLink(p.cast(dependency.getSourceModel(), ReduxModel.resolve),
                            getClientDatasourceId(dependency.getOn(), context, p), dependency.getSourceFieldId());
                    copyDependency.setOn(link.getBindLink());
                    copyDependency.setModel(p.cast(dependency.getTargetModel(), ReduxModel.resolve));
                    copyDependency.setField(dependency.getTargetFieldId());
                    copyDependency.setType(DependencyType.copy);
                    copyDependency.setSubmit(p.cast(dependency.getSubmit(), false));
                    copyDependency.setApplyOnInit(p.cast(dependency.getApplyOnInit(), false));
                    dependencies.add(copyDependency);
                }
            }
        }
        return dependencies;
    }
}
