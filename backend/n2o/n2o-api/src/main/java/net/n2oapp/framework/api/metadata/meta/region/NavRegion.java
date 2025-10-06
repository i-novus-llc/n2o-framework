package net.n2oapp.framework.api.metadata.meta.region;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;
import net.n2oapp.framework.api.metadata.global.view.region.N2oNavRegion;

/**
 * Клиентская модель региона {@code <nav>}
 */
@Getter
@Setter
public class NavRegion extends Region {
    @JsonProperty
    private N2oNavRegion.DirectionTypeEnum direction;
    @JsonProperty
    private String datasource;
    @JsonProperty
    private ReduxModelEnum model;
}
