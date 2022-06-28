package net.n2oapp.framework.config.metadata.compile.datasource;

import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.datasource.AbstractDatasource;
import net.n2oapp.framework.api.metadata.global.view.page.N2oDatasource;
import net.n2oapp.framework.api.metadata.global.view.page.N2oStandardDatasource;
import net.n2oapp.framework.api.metadata.meta.DependencyCondition;
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

    protected List<DependencyCondition> initDependencies(N2oDatasource source, CompileProcessor p) {
        PageScope pageScope = p.getScope(PageScope.class);
        if (pageScope == null)
            return null;
        List<DependencyCondition> fetch = new ArrayList<>();
        String pageId = pageScope.getPageId();
        if (source.getDependencies() != null) {
            for (N2oStandardDatasource.Dependency d : source.getDependencies()) {
                if (d instanceof N2oStandardDatasource.FetchDependency) {
                    ModelLink bindLink = new ModelLink(p.cast(((N2oStandardDatasource.FetchDependency) d).getModel(), ReduxModel.resolve),
                            CompileUtil.generateWidgetId(pageId, ((N2oStandardDatasource.FetchDependency) d).getOn()));
                    DependencyCondition condition = new DependencyCondition();
                    condition.setOn(bindLink.getBindLink());
                    condition.setType(DependencyConditionType.fetch);
                    fetch.add(condition);
                }
            }
        }
        return fetch;
    }
}
