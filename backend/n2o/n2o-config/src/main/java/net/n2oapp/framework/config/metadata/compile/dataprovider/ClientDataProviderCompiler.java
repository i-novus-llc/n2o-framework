package net.n2oapp.framework.config.metadata.compile.dataprovider;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.dataprovider.N2oClientDataProvider;
import net.n2oapp.framework.api.metadata.global.dao.N2oParam;
import net.n2oapp.framework.api.metadata.local.util.StrictMap;
import net.n2oapp.framework.api.metadata.meta.ClientDataProvider;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.widget.RequestMethod;
import net.n2oapp.framework.api.script.ScriptProcessor;
import net.n2oapp.framework.config.metadata.compile.BaseSourceCompiler;
import net.n2oapp.framework.config.metadata.compile.N2oCompileProcessor;
import net.n2oapp.framework.config.metadata.compile.ParentRouteScope;
import net.n2oapp.framework.config.metadata.compile.page.PageScope;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import net.n2oapp.framework.config.util.CompileUtil;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.colon;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.config.register.route.RouteUtil.normalize;

/**
 * Утилита для компиляции провайдера данных клиента
 */
@Component
public class ClientDataProviderCompiler implements BaseSourceCompiler<ClientDataProvider, N2oClientDataProvider, CompileContext<?, ?>> {

    public ClientDataProvider compile(N2oClientDataProvider source, CompileContext<?, ?> context, CompileProcessor p) {
        ClientDataProvider dataProvider = new ClientDataProvider();
        String path = null;

        if (RequestMethod.POST == source.getMethod()) {
            Map<String, ModelLink> pathMapping = new StrictMap<>();
            pathMapping.putAll(compileParams(source.getPathParams(), p, source.getModel(), source.getTargetWidgetId()));
            dataProvider.setFormMapping(compileParams(source.getFormParams(), p, source.getModel(), source.getTargetWidgetId()));
            dataProvider.setHeadersMapping(compileParams(source.getHeaderParams(), p, source.getModel(), source.getTargetWidgetId()));
            ParentRouteScope routeScope = p.getScope(ParentRouteScope.class);
            path = p.cast(routeScope != null ? routeScope.getUrl() : null, context.getRoute((N2oCompileProcessor) p), "");
            WidgetScope widgetScope = p.getScope(WidgetScope.class);
            if (widgetScope != null) {
                String clientWidgetId = widgetScope.getClientWidgetId();
                if (ReduxModel.RESOLVE.equals(source.getModel())) {
                    String widgetSelectedId = clientWidgetId + "_id";
                    //todo не нужно добавлять принудительно параметр в url, нужно только если его задали в route="/:id/action"
                    path = normalize(path + normalize(colon(widgetSelectedId)));
                    pathMapping.put(widgetSelectedId, new ModelLink(source.getModel(), clientWidgetId, "id"));
                }
            }
            path = normalize(path + normalize(p.cast(source.getUrl(), source.getId())));
            dataProvider.setPathMapping(pathMapping);

            dataProvider.setMethod(RequestMethod.POST);
            dataProvider.setOptimistic(source.getOptimistic());
            dataProvider.setSubmitForm(source.getSubmitForm());
        }

        dataProvider.setUrl(p.resolve(property("n2o.config.data.route"), String.class) + p.cast(path, source.getUrl()));
        dataProvider.setQueryMapping(source.getQueryMapping());
        dataProvider.setQuickSearchParam(source.getQuickSearchParam());

        return dataProvider;
    }

    private Map<String, ModelLink> compileParams(N2oParam[] params, CompileProcessor p,
                                                 ReduxModel model, String targetWidgetId) {
        if (params == null)
            return Collections.emptyMap();
        Map<String, ModelLink> result = new StrictMap<>();
        for (N2oParam param : params) {
            ModelLink link;
            Object value = ScriptProcessor.resolveExpression(param.getValue());
            if (param.getValue() == null || StringUtils.isJs(param.getValue())) {
                String widgetId = null;
                if (param.getRefWidgetId() != null) {
                    String pageId = param.getRefPageId();
                    if (param.getRefPageId() == null) {
                        PageScope pageScope = p.getScope(PageScope.class);
                        pageId = pageScope.getPageId();
                    }
                    widgetId = CompileUtil.generateWidgetId(pageId, param.getRefWidgetId());
                }
                link = new ModelLink(p.cast(param.getRefModel(), model), p.cast(widgetId, targetWidgetId));
                link.setValue(value);
            } else {
                link = new ModelLink(value);
            }
            result.put(param.getName(), link);
        }
        return result;
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oClientDataProvider.class;
    }
}
