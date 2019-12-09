package net.n2oapp.framework.config.metadata.validation.standard.widget;

import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.SourceClassAware;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oForm;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.*;
import net.n2oapp.framework.api.metadata.validate.SourceValidator;
import net.n2oapp.framework.api.metadata.validate.ValidateProcessor;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Валидатор виджета
 */
@Component
public class WidgetValidator implements SourceValidator<N2oWidget>, SourceClassAware {

    @Override
    public void validate(N2oWidget n2oWidget, ValidateProcessor p) {
        if (!N2oForm.class.isAssignableFrom(n2oWidget.getWidgetClass()) && n2oWidget.getQueryId() == null && n2oWidget.getObjectId() == null)
            throw new N2oMetadataValidationException("В виджете \'" + n2oWidget.getId() + "\' не указаны ни выборка, ни объект!");
        if (n2oWidget.getQueryId() != null)
            p.checkForExists(n2oWidget.getQueryId(), N2oQuery.class,
                    "Виджет \'" + n2oWidget.getId() + "\' ссылается на несуществующую выборку \'" + n2oWidget.getQueryId() + "\'");
        if (n2oWidget.getObjectId() != null)
            p.checkForExists(n2oWidget.getObjectId(), N2oObject.class,
                    "Виджет \'" + n2oWidget.getId() + "\' ссылается на несуществующий объект \'" + n2oWidget.getObjectId() + "\'");
        if (n2oWidget.getToolbars() != null) {
            List<AbstractMenuItem> menuItems = new ArrayList<>();
            for (N2oToolbar toolbar : n2oWidget.getToolbars()) {
                if (toolbar.getItems() != null) {
                    for (ToolbarItem item : toolbar.getItems()) {
                        if (item instanceof N2oButton) {
                            menuItems.add((N2oButton) item);
                        } else if (item instanceof N2oSubmenu) {
                            menuItems.addAll(Arrays.asList(((N2oSubmenu) item).getMenuItems()));
                        }
                    }
                }
            }
            p.safeStreamOf(menuItems).forEach(menuItem -> p.validate(menuItem.getAction()));
            p.checkIdsUnique(menuItems, "MenuItem '%s' встречается более чем один раз в виджете '" + n2oWidget.getId() + "'!");
        }
        if (n2oWidget.getPreFilters() != null && n2oWidget.getDependsOn() == null && n2oWidget.getDetailFieldId() == null) {
            for (N2oPreFilter preFilter : n2oWidget.getPreFilters()) {
                if (preFilter.getRefWidgetId() == null && StringUtils.hasLink(preFilter.getValue())) {
                    throw new N2oMetadataValidationException("В виджете \'" + (n2oWidget.getId() == null ? "" : n2oWidget.getId()) + "\' значение префильтра \'" + preFilter.getFieldId() + "\' является ссылкой, но зависимость для нее прописана!");
                }
            }
        }
    }

    @Override
    public Class<? extends Source> getSourceClass() {
        return N2oWidget.class;
    }
}
