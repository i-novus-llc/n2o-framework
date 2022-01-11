package net.n2oapp.framework.config.metadata.compile.toolbar;

import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.compile.ButtonGenerator;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.compile.building.Placeholders;
import net.n2oapp.framework.api.metadata.event.action.N2oInvokeAction;
import net.n2oapp.framework.api.metadata.event.action.N2oShowModal;
import net.n2oapp.framework.api.metadata.global.dao.N2oPathParam;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.global.view.page.DefaultValuesMode;
import net.n2oapp.framework.api.metadata.global.view.page.N2oDatasource;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oButton;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.ToolbarItem;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.config.metadata.compile.action.DefaultActions;
import net.n2oapp.framework.config.metadata.compile.widget.WidgetScope;

import java.util.Collections;
import java.util.List;

import static net.n2oapp.framework.config.register.route.RouteUtil.normalize;

public abstract class AbstractButtonGenerator implements ButtonGenerator {

    protected List<ToolbarItem> build(DefaultActions action, CompileProcessor p) {
        N2oButton button = new N2oButton();
        button.setId(action.name());
        button.setLabel(p.getMessage(action.getLabel()));
        button.setIcon(action.getIcon());
        if (action.isContext()) {
            button.setModel(ReduxModel.resolve);
        } else {
            button.setModel(ReduxModel.filter);
        }
        switch (action) {
            case delete: {
                N2oInvokeAction invokeAction = new N2oInvokeAction();
                invokeAction.setOperationId(action.name());
                WidgetScope widgetScope = p.getScope(WidgetScope.class);
                String widgetId = (widgetScope != null && widgetScope.getWidgetId() != null) ? widgetScope.getWidgetId() : "";
                invokeAction.setRoute(normalize("/" + widgetId + "/delete"));
                button.setConfirm(true);
                button.setAction(invokeAction);
            }
            break;
            case create: {
                N2oShowModal modal = new N2oShowModal();
                CompiledObject object = p.getScope(CompiledObject.class);
                modal.setPageId(object.getId());
                modal.setObjectId(object.getId());
                modal.setPageName(p.getMessage(action.getPageName(), object.getName()));
                modal.setSubmitOperationId(action.name());
                N2oDatasource datasource = new N2oDatasource();
                datasource.setDefaultValuesMode(DefaultValuesMode.defaults);
                modal.setDatasources(new N2oDatasource[] { datasource });
                modal.setCloseAfterSubmit(true);
                WidgetScope widgetScope = p.getScope(WidgetScope.class);
                String widgetId = (widgetScope != null && widgetScope.getWidgetId() != null) ? widgetScope.getWidgetId() : "";
                modal.setRoute(normalize("/" + widgetId + "/create"));
                button.setAction(modal);
            }
            break;
            case update: {
                N2oShowModal modal = new N2oShowModal();
                CompiledObject object = p.getScope(CompiledObject.class);
                modal.setPageId(object.getId());
                modal.setObjectId(object.getId());
                modal.setPageName(p.getMessage(action.getPageName(), object.getName()));
                WidgetScope widgetScope = p.getScope(WidgetScope.class);
                String widgetId = (widgetScope != null && widgetScope.getWidgetId() != null) ? widgetScope.getWidgetId() : "";
                String paramName = widgetId + "_" + N2oQuery.Field.PK;
                modal.setRoute(normalize("/" + widgetId + "/:" + paramName + "/update"));
                N2oPathParam pathParam = new N2oPathParam();
                pathParam.setName(paramName);
                pathParam.setValue(Placeholders.ref(N2oQuery.Field.PK));
                modal.addPathParams(new N2oPathParam[]{pathParam});
                N2oDatasource datasource = new N2oDatasource();
                datasource.setDefaultValuesMode(DefaultValuesMode.query);
                N2oPreFilter masterDetailFilter = new N2oPreFilter();
                masterDetailFilter.setType(FilterType.eq);
                masterDetailFilter.setFieldId(N2oQuery.Field.PK);
                masterDetailFilter.setParam(paramName);
                datasource.setFilters(new N2oPreFilter[]{masterDetailFilter});
                modal.setDatasources(new N2oDatasource[]{datasource});
                modal.setSubmitOperationId(action.name());
                modal.setCloseAfterSubmit(true);
                button.setAction(modal);
            }
            break;
            default:
                throw new UnsupportedOperationException();
        }
        return Collections.singletonList(button);
    }
}
