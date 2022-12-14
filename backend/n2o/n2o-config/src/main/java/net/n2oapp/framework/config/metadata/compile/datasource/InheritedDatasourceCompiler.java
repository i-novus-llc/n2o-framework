package net.n2oapp.framework.config.metadata.compile.datasource;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.datasource.InheritedDatasource;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oInheritedDatasource;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.config.metadata.compile.ParentRouteScope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
        compiled.setProvider(initProvider(source, p));
        compiled.setSubmit(initSubmit(source, p));
        compiled.getProvider().setFilters(initFilters(source, compiled, p));
        return compiled;
    }

    private InheritedDatasource.Submit initSubmit(N2oInheritedDatasource source, CompileProcessor p) {
        if (source.getSubmit() == null) return null;

        InheritedDatasource.Submit submit = new InheritedDatasource.Submit();
        N2oInheritedDatasource.Submit sourceSubmit = source.getSubmit();
        submit.setAuto(p.cast(sourceSubmit.getAuto(), true));
        submit.setModel(p.cast(sourceSubmit.getModel(), ReduxModel.resolve));
        submit.setTargetDs(getClientDatasourceId(p.cast(sourceSubmit.getTargetDatasource(), source.getSourceDatasource()), p));
        submit.setTargetModel(p.cast(sourceSubmit.getTargetModel(), source.getSourceModel(), ReduxModel.resolve));
        submit.setTargetField(p.cast(sourceSubmit.getTargetFieldId(), source.getSourceFieldId()));
        return submit;
    }

    private InheritedDatasource.Provider initProvider(N2oInheritedDatasource source, CompileProcessor p) {
        InheritedDatasource.Provider provider = new InheritedDatasource.Provider();
        provider.setSourceDs(getClientDatasourceId(source.getSourceDatasource(), p));
        provider.setSourceModel(p.cast(source.getSourceModel(), ReduxModel.resolve));
        provider.setSourceField(source.getSourceFieldId());
        return provider;
    }

    private List<InheritedDatasource.Filter> initFilters(N2oInheritedDatasource source,
                                                         InheritedDatasource compiled,
                                                         CompileProcessor p) {
        if (source.getFilters() == null) return null;
        List<InheritedDatasource.Filter> filters = new ArrayList<>();
        for (N2oPreFilter sourceFilter : source.getFilters()) {
            InheritedDatasource.Filter filter = new InheritedDatasource.Filter();
            filter.setType(sourceFilter.getType());
            filter.setFieldId(sourceFilter.getFieldId());
            boolean required = p.cast(sourceFilter.getRequired(), false);
            filter.setRequired(required);
            Object value = getPrefilterValue(sourceFilter);
            ParentRouteScope routeScope = p.getScope(ParentRouteScope.class);
            if (routeScope != null && routeScope.getQueryMapping() != null
                    && routeScope.getQueryMapping().containsKey(sourceFilter.getParam())) {
                //фильтр из родительского маршрута
                filter.setModelLink(routeScope.getQueryMapping().get(sourceFilter.getParam()));
            } else if (StringUtils.isJs(value)) {
                String clientDatasourceId = sourceFilter.getRefPageId() != null ?
                        getClientDatasourceId(sourceFilter.getDatasourceId(), sourceFilter.getRefPageId(), p) :
                        getClientDatasourceId(sourceFilter.getDatasourceId(), p);
                ReduxModel model = p.cast(sourceFilter.getModel(), ReduxModel.resolve);
                ModelLink link = new ModelLink(model, clientDatasourceId);
                link.setValue(value);
                link.setParam(sourceFilter.getParam());
                link.setRequired(required);
                filter.setModelLink(link);
            } else {
                //фильтр с константным значением или значением из параметра в url
                ModelLink link = new ModelLink(value);
                link.setParam(sourceFilter.getParam());
                link.setRequired(required);
                filter.setModelLink(link);
            }
            filters.add(filter);
        }
        return filters;
    }

    private Object getPrefilterValue(N2oPreFilter n2oPreFilter) {
        if (n2oPreFilter.getValues() == null) {
            return ScriptProcessor.resolveExpression(n2oPreFilter.getValue());
        } else {
            return ScriptProcessor.resolveArrayExpression(n2oPreFilter.getValues());
        }
    }
}
