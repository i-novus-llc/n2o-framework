package net.n2oapp.framework.config.metadata.compile.datasource;

import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.datasource.BrowserStorageDatasource;
import net.n2oapp.framework.api.metadata.global.view.page.N2oBrowserStorageDatasource;
import net.n2oapp.framework.api.metadata.meta.DependencyCondition;
import net.n2oapp.framework.api.metadata.meta.DependencyConditionType;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.config.metadata.compile.ValidationList;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import net.n2oapp.framework.config.util.CompileUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;

/**
 * Компиляция источника данных BrowserStorage
 */
@Component
public class BrowserStorageDatasourceCompiler extends BaseDatasourceCompiler<N2oBrowserStorageDatasource, BrowserStorageDatasource> {

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oBrowserStorageDatasource.class;
    }

    @Override
    public BrowserStorageDatasource compile(N2oBrowserStorageDatasource source, CompileContext<?, ?> context, CompileProcessor p) {
        BrowserStorageDatasource compiled = new BrowserStorageDatasource();
        initDatasource(compiled, source, context, p);
        compiled.setSize(p.cast(source.getSize(), p.resolve(property("n2o.api.widget.table.size"), Integer.class)));
        compiled.setProvider(initProvider(source, p));
        compiled.setSubmit(initSubmit(source,p));
        compiled.setDependencies(initDependencies(source, p));
        compiled.setValidations(initValidations(source, p));
        return compiled;
    }

    private List<DependencyCondition> initDependencies(N2oBrowserStorageDatasource source, CompileProcessor p) {
        PageScope pageScope = p.getScope(PageScope.class);
        if (pageScope == null)
            return null;
        List<DependencyCondition> fetch = new ArrayList<>();
        String pageId = pageScope.getPageId();
        if (source.getDependencies() != null) {
            for (N2oBrowserStorageDatasource.Dependency d : source.getDependencies())
                if (d instanceof N2oBrowserStorageDatasource.FetchDependency) {
                    ModelLink bindLink = new ModelLink(p.cast(((N2oBrowserStorageDatasource.FetchDependency) d).getModel(), ReduxModel.resolve),
                            CompileUtil.generateWidgetId(pageId, ((N2oBrowserStorageDatasource.FetchDependency) d).getOn()));
                    DependencyCondition condition = new DependencyCondition();
                    condition.setOn(bindLink.getBindLink());
                    condition.setType(DependencyConditionType.fetch);
                    fetch.add(condition);
                }
        }
        return fetch;
    }

    private Map<String, List<Validation>> initValidations(N2oBrowserStorageDatasource source, CompileProcessor p) {
        ValidationList validationList = p.getScope(ValidationList.class);
        if (validationList != null) {
            //todo why RESOLVE ?
            return validationList.get(source.getId(), ReduxModel.resolve).stream()
                    .filter(v -> v.getSide() == null || v.getSide().contains("client"))
                    .collect(Collectors.groupingBy(Validation::getFieldId));
        } else
            return Collections.emptyMap();
    }

    private BrowserStorageDatasource.Submit initSubmit(N2oBrowserStorageDatasource source, CompileProcessor p) {
        BrowserStorageDatasource.Submit submit = new BrowserStorageDatasource.Submit();
        submit.setKey(p.cast(source.getSubmit().getKey(), source.getId()));
        submit.setAuto(source.getSubmit().getAuto());
        submit.setStorage(p.cast(source.getSubmit().getStorageType(), N2oBrowserStorageDatasource.BrowserStorageType.sessionStorage));
        return submit;
    }

    private BrowserStorageDatasource.Provider initProvider(N2oBrowserStorageDatasource source, CompileProcessor p) {
        BrowserStorageDatasource.Provider provider = new BrowserStorageDatasource.Provider();
        provider.setKey(p.cast(source.getKey(), source.getId()));
        provider.setStorage(p.cast(source.getStorageType(), N2oBrowserStorageDatasource.BrowserStorageType.sessionStorage));
        return provider;
    }
}
