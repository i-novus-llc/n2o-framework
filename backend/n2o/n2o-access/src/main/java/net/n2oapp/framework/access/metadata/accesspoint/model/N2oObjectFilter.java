package net.n2oapp.framework.access.metadata.accesspoint.model;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;

/**
 * Фильтр для object-filters в схеме прав доступа
 */
@Getter
@Setter
public class N2oObjectFilter extends N2oPreFilter {

    private String id;
}
