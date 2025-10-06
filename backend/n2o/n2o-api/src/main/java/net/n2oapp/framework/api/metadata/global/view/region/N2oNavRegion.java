package net.n2oapp.framework.api.metadata.global.view.region;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.aware.DatasourceIdAware;
import net.n2oapp.framework.api.metadata.aware.ModelAware;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;
import net.n2oapp.framework.api.metadata.menu.N2oAbstractMenuItem;

/**
 * Исходная модель региона {@code <nav>}
 */
@Getter
@Setter
public class N2oNavRegion extends N2oRegion implements DatasourceIdAware, ModelAware {
    private DirectionTypeEnum direction;
    private String datasourceId;
    private ReduxModelEnum model;
    private N2oAbstractMenuItem[] menuItems;

    @RequiredArgsConstructor
    @Getter
    public enum DirectionTypeEnum implements N2oEnum {
        ROW("row"),
        COLUMN("column");

        private final String id;
    }

    @Override
    public String getAlias() {
        return "nav";
    }
}
