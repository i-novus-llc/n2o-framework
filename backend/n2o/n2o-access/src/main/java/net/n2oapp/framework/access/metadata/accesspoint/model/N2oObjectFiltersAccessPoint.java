package net.n2oapp.framework.access.metadata.accesspoint.model;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.access.metadata.accesspoint.AccessPoint;

/**
 * Список фильтров для объекта
 */

@Getter
@Setter
public class N2oObjectFiltersAccessPoint extends AccessPoint {

    private String objectId;
    private N2oObjectFilter[] filters;
}
