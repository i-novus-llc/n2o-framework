package net.n2oapp.framework.config.metadata.compile.page;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.compile.CompileProcessor;
import net.n2oapp.framework.api.metadata.local.view.widget.util.SingleListFieldSubModelQuery;
import net.n2oapp.framework.api.metadata.local.view.widget.util.SubModelQuery;
import net.n2oapp.framework.api.metadata.meta.BindLink;
import net.n2oapp.framework.api.metadata.meta.ModelLink;
import net.n2oapp.framework.api.metadata.meta.Page;
import net.n2oapp.framework.api.metadata.meta.PageRoutes;
import net.n2oapp.framework.api.metadata.meta.control.DefaultValues;
import net.n2oapp.framework.api.metadata.meta.control.Field;
import net.n2oapp.framework.api.metadata.meta.control.ListControl;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.api.metadata.meta.fieldset.FieldSet;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;
import net.n2oapp.framework.api.metadata.meta.widget.form.FormWidgetComponent;
import net.n2oapp.framework.api.metadata.meta.widget.table.AbstractTable;
import net.n2oapp.framework.api.metadata.meta.widget.table.Table;
import net.n2oapp.framework.config.metadata.compile.BaseMetadataBinder;
import net.n2oapp.framework.config.metadata.compile.redux.Redux;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Базовое связывание данных на странице
 */
@Component
public class PageBinder implements BaseMetadataBinder<Page> {
    @Override
    public Page bind(Page page, CompileProcessor p) {
        if (page.getWidgets() != null) {
            page.getWidgets().values().forEach(p::bind);
        }
        if (page.getActions() != null)
            page.getActions().values().forEach(p::bind);
        if (page.getRoutes() != null) {
            Map<String, BindLink> pathMappings = new HashMap<>();
            page.getRoutes().getPathMapping().forEach((k, v) -> {
                pathMappings.put(k, Redux.createBindLink(v));
            });
            for (PageRoutes.Route route : page.getRoutes().getList()) {
                route.setPath(p.resolveUrl(route.getPath(), pathMappings, null));
            }
        }
        if (page.getBreadcrumb() != null)
            page.getBreadcrumb().stream().filter(b -> b.getPath() != null)
                    .forEach(b -> b.setPath(p.resolveUrl(b.getPath(), null, null)));
        if (page.getModels() != null) {
            page.getModels().values().forEach(bl -> {
                if (bl.getValue() instanceof String) {
                    bl.setValue(p.resolveText((String) bl.getValue()));
                }
            });
            resolveLinks(page.getModels(), p);
        }
        resolveDefaultValues(page, p);
        return page;
    }

    private void resolveDefaultValues(Page page, CompileProcessor p) {
        if (page.getModels() == null) return;
        for (Map.Entry<String, Widget> entry : page.getWidgets().entrySet()) {
            String widgetId = entry.getKey();
            Widget widget = entry.getValue();
            if (widget instanceof Table) {
                AbstractTable.Filter filter = ((Table) widget).getFilter();
                if (filter != null && filter.getFilterFieldsets() != null)
                    handleFieldSets(ReduxModel.FILTER, page, filter.getFilterFieldsets(), widgetId, p);
            } else if (widget.getComponent() instanceof FormWidgetComponent) {
                FormWidgetComponent component = (FormWidgetComponent) widget.getComponent();
                handleFieldSets(ReduxModel.RESOLVE, page, component.getFieldsets(), widgetId, p);
            }
        }
    }

    private void handleFieldSets(ReduxModel model, Page page, List<FieldSet> fieldSets, String widgetId, CompileProcessor p) {
        if (fieldSets == null) return;
        for (FieldSet fieldSet : fieldSets) {
            if (fieldSet.getRows() == null) continue;
            for (FieldSet.Row row : fieldSet.getRows()) {
                if (row.getCols() == null) continue;
                for (FieldSet.Column col : row.getCols()) {
                    if (col.getFields() == null) continue;
                    for (Field field : col.getFields())
                        handleField(model, field, page, widgetId, p);
                    handleFieldSets(model, page, col.getFieldsets(), widgetId, p);
                }
            }
        }
    }

    private void handleField(ReduxModel model, Field field, Page page, String widgetId, CompileProcessor p) {
        ListControl control = null;
        if (field instanceof StandardField) {
            StandardField standardField = (StandardField) field;
            if (standardField.getControl() == null || !(standardField.getControl() instanceof ListControl))
                return;
            control = (ListControl) standardField.getControl();
        }

        if (page.getModels().get(model, widgetId, field.getId()) != null) {
            DefaultValues defaultValues = (DefaultValues) page.getModels().get(model, widgetId, field.getId()).getValue();
            Map<String, Object> values = defaultValues.getValues();
            String labelFieldId = control.getLabelFieldId();
            if (values.containsKey(control.getValueFieldId()) &&
                    !values.containsKey(labelFieldId)) {

                SubModelQuery subModelQuery = initSubModelQuery(control);
                DataSet dataSet = initDataSet(control, subModelQuery, defaultValues);
                Map<String, Object> result = (Map<String, Object>) p.resolveSubModels(subModelQuery, dataSet).get(control.getId());
                defaultValues.getValues().put(labelFieldId, result.get(labelFieldId));
            }
        }
    }

    private DataSet initDataSet(ListControl control, SubModelQuery subModelQuery, DefaultValues defaultValues) {
        DataSet dataSet = new DataSet();
        String key = subModelQuery.getSubModel() + "." + subModelQuery.getValueFieldId();
        dataSet.put(key, defaultValues.getValues().get(control.getValueFieldId()));
        return dataSet;
    }

    private SubModelQuery initSubModelQuery(ListControl control) {
        SubModelQuery subModelQuery = new SingleListFieldSubModelQuery(
                control.getId(),
                control.getQueryId(),
                control.getValueFieldId(),
                control.getLabelFieldId()
        );
        return subModelQuery;
    }

    private void resolveLinks(Map<String, ModelLink> linkMap, CompileProcessor p) {
        linkMap.keySet().forEach(param ->
                linkMap.put(param, p.resolveLink(linkMap.get(param)))
        );
    }

    @Override
    public Class<? extends Compiled> getCompiledClass() {
        return Page.class;
    }
}
