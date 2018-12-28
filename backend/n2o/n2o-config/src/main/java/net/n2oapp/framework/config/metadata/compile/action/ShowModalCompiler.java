package net.n2oapp.framework.config.metadata.compile.action;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.ModelAware;
import net.n2oapp.framework.api.metadata.compile.CompileContext;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.event.action.N2oShowModal;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.view.action.control.Target;
import net.n2oapp.framework.api.metadata.local.util.StrictMap;
import net.n2oapp.framework.api.metadata.meta.BindLink;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.action.show_modal.ShowModal;
import net.n2oapp.framework.api.metadata.meta.action.show_modal.ShowModalPayload;
import net.n2oapp.framework.config.metadata.compile.ComponentScope;
import net.n2oapp.framework.config.metadata.compile.ParentRoteScope;
import net.n2oapp.framework.config.metadata.compile.context.ModalPageContext;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;
import net.n2oapp.framework.config.register.route.RouteUtil;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.colon;
import static net.n2oapp.framework.api.metadata.compile.building.Placeholders.property;
import static net.n2oapp.framework.config.register.route.RouteUtil.normalize;
import static net.n2oapp.framework.config.register.route.RouteUtil.parent;

/**
 * Компиляция show-modal
 */
@Component
public class ShowModalCompiler extends AbstractOpenPageCompiler<ShowModal, N2oShowModal> {
    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oShowModal.class;
    }

    @Override
    public ShowModal compile(N2oShowModal source, CompileContext<?, ?> context, CompileProcessor p) {
        ShowModal showModal = new ShowModal();
        showModal.getOptions().setType(p.resolve(property("n2o.api.action.show_modal.type"), String.class));
        showModal.setObjectId(source.getObjectId());
        showModal.setOperationId(source.getOperationId());
        showModal.setPageId(source.getPageId());
        compileAction(showModal, source, p);
        compilePayload(showModal, source, context, p);
        return showModal;
    }

    private void compilePayload(ShowModal showModal, N2oShowModal source, CompileContext<?, ?> context, CompileProcessor p) {
        Map<String, BindLink> pathMapping = new StrictMap<>();
        ShowModalPayload payload = showModal.getOptions().getPayload();
        payload.setSize(p.cast(source.getModalSize(), null));//todo modalPage.getModalSize() ?
        ParentRoteScope routeScope = p.getScope(ParentRoteScope.class);
        String route = p.cast(routeScope != null ? routeScope.getUrl() : null, "");
        Map<String, ModelLink> modelLinkMap = new HashMap<>(routeScope.getPathMapping());
        WidgetScope widgetScope = p.getScope(WidgetScope.class);
        ReduxModel widgetModel = ReduxModel.RESOLVE;
        ComponentScope componentScope = p.getScope(ComponentScope.class);
        if (componentScope != null) {
            ModelAware modelAware = componentScope.unwrap(ModelAware.class);
            if (modelAware != null && modelAware.getModel() != null) {
                widgetModel = modelAware.getModel();
            }
        }
        boolean contextAction = ReduxModel.RESOLVE.equals(widgetModel);
        String masterIdFilter = null;
        if (widgetScope != null && contextAction) {
            masterIdFilter = widgetScope.getClientWidgetId() + "_id";
            route = route + normalize(colon(masterIdFilter));
            ModelLink link = new ModelLink(ReduxModel.RESOLVE, widgetScope.getClientWidgetId(), "id", masterIdFilter);
            pathMapping.put(masterIdFilter, link);
            modelLinkMap.put(masterIdFilter, link);
        }
        String parentRoute = normalize(route);
        route += normalize(p.cast(source.getRoute(), source.getId()));
        route = normalize(route);
        payload.setPageUrl(route);
        ModalPageContext modalPageContext = new ModalPageContext(source.getPageId(), route);
        modalPageContext.setSubmitOperationId(source.getSubmitOperationId());
        modalPageContext.setSubmitLabel(source.getSubmitLabel());
        modalPageContext.setSubmitModel(source.getSubmitModel());
        modalPageContext.setResultWidgetId(source.getResultContainerId());
        modalPageContext.setUpload(source.getUpload());
        modalPageContext.setParentWidgetId(widgetScope != null ? widgetScope.getClientWidgetId() : null);//todo для page actions какой parentWidgetId?
        modalPageContext.setParentRoute(parentRoute);
        modalPageContext.setCloseOnSuccessSubmit(p.cast(source.getCloseAfterSubmit(), true));
        modalPageContext.setRefreshOnSuccessSubmit(p.cast(source.getRefreshAfterSubmit(), true));
        modalPageContext.setRefreshOnClose(p.cast(source.getRefreshOnClose(), false));
        modalPageContext.setPageName(source.getPageName());
        if (routeScope != null
                && source.getSubmitOperationId() != null
                && source.getRedirectUrlAfterSubmit() == null
                && source.getFocusAfterSubmit() != null
                && source.getFocusAfterSubmit()) {
            String redirectToFocusUrl = parent(colon("id"));
            redirectToFocusUrl = contextAction ? parent(redirectToFocusUrl) : redirectToFocusUrl;
            modalPageContext.setRedirectUrlOnSuccessSubmit(redirectToFocusUrl);
            modalPageContext.setRedirectTargetOnSuccessSubmit(Target.application);
        } else if (source.getRedirectUrlAfterSubmit() != null) {
            modalPageContext.setRedirectUrlOnSuccessSubmit(source.getRedirectUrlAfterSubmit());
            modalPageContext.setRedirectTargetOnSuccessSubmit(p.cast(source.getRedirectTargetAfterSubmit(),
                    RouteUtil.isApplicationUrl(source.getRedirectUrlAfterSubmit()) ? Target.application : Target.self));
        }
        String modalPageId = RouteUtil.convertPathToId(route);
        payload.setName(modalPageId);
        payload.setPageId(modalPageId);
        modalPageContext.setClientPageId(modalPageId);
        if (routeScope != null && routeScope.getPathMapping() != null) {
            for (String pathParam : routeScope.getPathMapping().keySet()) {
                BindLink bindLink = routeScope.getPathMapping().get(pathParam);
                pathMapping.put(pathParam, bindLink);
            }
        }
        payload.setPathMapping(pathMapping);
        Set<String> pathParams = pathMapping.keySet();
        List<N2oPreFilter> preFilters = initPreFilters(source, masterIdFilter, widgetScope, p);
        modalPageContext.setQueryRouteInfos(initRouteInfos(preFilters, pathParams, p));
        modalPageContext.setPathRouteInfos(modelLinkMap);
        modalPageContext.setPreFilters(preFilters);
        p.addRoute(route, modalPageContext);
        payload.setCloseButton(true);
        Map<String, BindLink> queryMapping = new StrictMap<>();
        preFilters.stream().filter(n2oPreFilter -> n2oPreFilter.getParam() != null && !pathParams.contains(n2oPreFilter.getParam()))
                .forEach(preFilter -> queryMapping.put(preFilter.getParam(), createModelLink(preFilter, null, preFilter.getParam(), p)));
        payload.setQueryMapping(queryMapping);
    }
}
