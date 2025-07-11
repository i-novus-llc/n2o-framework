package net.n2oapp.framework.config.metadata.compile.datasource;

import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.datasource.AbstractDatasource;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oDatasource;
import net.n2oapp.framework.api.metadata.meta.CopyDependency;
import net.n2oapp.framework.api.metadata.meta.Dependency;
import net.n2oapp.framework.api.metadata.meta.DependencyTypeEnum;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.rest.Paging;
import net.n2oapp.framework.config.metadata.compile.ValidationScope;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.api.metadata.local.util.CompileUtil.castDefault;
import static net.n2oapp.framework.config.util.DatasourceUtil.getClientDatasourceId;

/**
 * Базовая компиляция источника данных
 */
public abstract class BaseDatasourceCompiler<S extends N2oDatasource, D extends AbstractDatasource> extends AbstractDatasourceCompiler<S, D> {

    public void compileDatasource(S source, D compiled, CompileProcessor p) {
        initDatasource(source, compiled, p);
        compiled.setPaging(new Paging(castDefault(source.getSize(),
                () -> p.resolve(property("n2o.api.datasource.size"), Integer.class))));
        compiled.setDependencies(initDependencies(source, p));
        compiled.setValidations(initValidations(source, p, ReduxModelEnum.RESOLVE));
        compiled.setFilterValidations(initValidations(source, p, ReduxModelEnum.FILTER));
        compiled.setSorting(source.getSorting());
    }

    protected Map<String, List<Validation>> initValidations(S source, CompileProcessor p, ReduxModelEnum model) {
        ValidationScope validationScope = p.getScope(ValidationScope.class);
        if (validationScope != null) {
            return validationScope.get(source.getId(), model).stream()
                    .filter(v -> v.getSide() == null || v.getSide().contains("client"))
                    .collect(Collectors.groupingBy(Validation::getFieldId));
        } else
            return Collections.emptyMap();
    }

    protected List<Dependency> initDependencies(N2oDatasource source, CompileProcessor p) {
        List<Dependency> dependencies = new ArrayList<>();
        if (source.getDependencies() != null) {
            for (N2oDatasource.Dependency d : source.getDependencies()) {
                if (d instanceof N2oDatasource.FetchDependency dependency) {
                    Dependency fetchDependency = new Dependency();
                    ModelLink link = new ModelLink(castDefault(dependency.getModel(), ReduxModelEnum.RESOLVE),
                            getClientDatasourceId(dependency.getOn(), p));
                    fetchDependency.setOn(link.getLink());
                    fetchDependency.setType(DependencyTypeEnum.FETCH);
                    dependencies.add(fetchDependency);
                } else if (d instanceof N2oDatasource.CopyDependency dependency) {
                    CopyDependency copyDependency = new CopyDependency();
                    ModelLink link = new ModelLink(castDefault(dependency.getSourceModel(), ReduxModelEnum.RESOLVE),
                            getClientDatasourceId(dependency.getOn(), p), dependency.getSourceFieldId());
                    copyDependency.setOn(link.getLink());
                    copyDependency.setModel(castDefault(dependency.getTargetModel(), ReduxModelEnum.RESOLVE));
                    copyDependency.setField(dependency.getTargetFieldId());
                    copyDependency.setType(DependencyTypeEnum.COPY);
                    copyDependency.setSubmit(castDefault(dependency.getSubmit(),
                            () -> p.resolve(property("n2o.api.datasource.dependency.copy.submit"), Boolean.class)));
                    copyDependency.setApplyOnInit(castDefault(dependency.getApplyOnInit(),
                            () -> p.resolve(property("n2o.api.datasource.dependency.copy.apply_on_init"), Boolean.class)));
                    dependencies.add(copyDependency);
                }
            }
        }
        return dependencies;
    }
}
