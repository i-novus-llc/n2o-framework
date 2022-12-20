package net.n2oapp.framework.api.metadata.global.view.widget.toolbar;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.N2oAttribute;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.action.N2oAction;
import net.n2oapp.framework.api.metadata.aware.DatasourceIdAware;

import java.util.ArrayList;
import java.util.List;

/**
 * Исходная модель тулбара
 */
@Getter
@Setter
public class N2oToolbar implements Source, GroupItems<ToolbarItem>, DatasourceIdAware {
    @N2oAttribute("Css класс")
    private String cssClass;
    @N2oAttribute("Стиль")
    private String style;
    @N2oAttribute("Место расположения меню")
    private String place;
    private String[] generate;
    @N2oAttribute("Элементы меню")
    private ToolbarItem[] items;
    @Deprecated
    private String targetWidgetId;
    private String datasourceId;

    public N2oToolbar(String[] generate, ToolbarItem[] items) {
        this.generate = generate;
        this.items = items;
    }

    public N2oToolbar() {
    }

    public N2oToolbar(ToolbarItem[] items) {
        this.items = items;
    }

    public List<N2oAction> getAllActions() {
        List<N2oAction> actions = new ArrayList<>();
        if (items != null) {
            for (ToolbarItem item : items) {
                actions.addAll(item.getListActions());
            }
        }
        return actions;
    }
}
