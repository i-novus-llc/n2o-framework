package net.n2oapp.framework.api.metadata.global.view.widget;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.RegionItem;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.aware.*;
import net.n2oapp.framework.api.metadata.global.N2oMetadata;
import net.n2oapp.framework.api.metadata.global.view.ActionBar;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oStandardDatasource;
import net.n2oapp.framework.api.metadata.global.view.widget.dependency.N2oDependency;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;
import net.n2oapp.framework.api.metadata.jackson.ExtAttributesSerializer;

import java.util.List;
import java.util.Map;

/**
 * Исходная модель виджета
 */
@Getter
@Setter
public abstract class N2oWidget extends N2oMetadata
        implements SourceComponent, ExtensionAttributesAware, RegionItem,
        DatasourceIdAware, ActionBarAware, ToolbarsAware, ModelAware {

    private String src;
    private String datasourceId;
    private N2oStandardDatasource datasource;
    private Boolean fetchOnInit;
    private Boolean fetchOnVisibility;
    private String cssClass;
    private String style;
    /**
     * Автоматическая установка фокуса на виджете
     */
    private Boolean autoFocus;
    private String visible;
    private ActionBar[] actions;
    private N2oToolbar[] toolbars;
    @ExtAttributesSerializer
    private Map<N2oNamespace, Map<String, String>> extAttributes;
    private N2oDependency[] dependencies;

    @Override
    public final String getPostfix() {
        return "widget";
    }

    @Override
    public final Class<? extends N2oMetadata> getSourceBaseClass() {
        return N2oWidget.class;
    }

    @Override
    public void collectWidgets(List<N2oWidget> result, Map<String, Integer> ids, String prefix) {
        ids.putIfAbsent(prefix, 1);
        if (getId() == null)
            setId(prefix + ids.put(prefix, ids.get(prefix) + 1));
        result.add(this);
    }

    @Override
    public ReduxModelEnum getModel() {
        return ReduxModelEnum.RESOLVE;
    }

    @Override
    public void setModel(ReduxModelEnum model) {
        throw new UnsupportedOperationException();
    }
}
