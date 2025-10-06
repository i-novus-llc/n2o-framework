package net.n2oapp.framework.api.metadata.menu;


import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.aware.DatasourceIdAware;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.aware.ModelAware;
import net.n2oapp.framework.api.metadata.control.N2oComponent;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeTypeEnum;
import net.n2oapp.framework.api.metadata.meta.badge.PositionEnum;

/**
 * Исходная модель абстрактного элемента меню
 */
@Getter
@Setter
public abstract class N2oAbstractMenuItem extends N2oComponent implements IdAware, DatasourceIdAware, ModelAware {
    private String id;
    private String label;
    private String icon;
    private PositionEnum iconPosition;
    private String image;
    private ShapeTypeEnum imageShape;
    private String datasourceId;
    private ReduxModelEnum model;
    private String visible;
    private String enabled;
}