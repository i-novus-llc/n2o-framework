package net.n2oapp.framework.api.metadata.meta.menu;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.Component;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.aware.PropertiesAware;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeTypeEnum;
import net.n2oapp.framework.api.metadata.meta.badge.PositionEnum;
import net.n2oapp.framework.api.metadata.meta.region.CompiledRegionItem;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;

import java.util.List;

/**
 * Клиентская модель базового элемента меню
 */
@Getter
@Setter
public class BaseMenuItem extends Component implements Compiled, PropertiesAware, IdAware, CompiledRegionItem {
    @JsonProperty
    private String id;
    @JsonProperty
    private String label;
    @JsonProperty
    private String icon;
    @JsonProperty
    private PositionEnum iconPosition;
    @JsonProperty
    private String imageSrc;
    @JsonProperty
    private ShapeTypeEnum imageShape;
    @JsonProperty
    private String datasource;
    @JsonProperty
    private ReduxModelEnum model;
    @JsonProperty
    private Object visible;
    @JsonProperty
    private Object enabled;

    @Override
    public void collectWidgets(List<Widget<?>> compiledWidgets) {
        // no widgets
    }
}
