package net.n2oapp.framework.api.metadata.control;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.aware.PreFiltersAware;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeTypeEnum;
import net.n2oapp.framework.api.metadata.meta.badge.BadgeAware;
import net.n2oapp.framework.api.metadata.meta.badge.PositionEnum;

import java.util.Map;

/**
 * Абстрактная реализация спискового контрола
 */
@Getter
@Setter
public abstract class N2oListField extends N2oStandardField implements PreFiltersAware, BadgeAware {
    protected Boolean search;
    protected Map<String, String>[] options;
    protected Boolean cache;
    private Integer size;
    private N2oPreFilter[] preFilters;
    private String queryId;
    private String iconFieldId;
    private String imageFieldId;
    private String badgeFieldId;
    private String badgeColorFieldId;
    private PositionEnum badgePosition;
    private ShapeTypeEnum badgeShape;
    private String badgeImageFieldId;
    private PositionEnum badgeImagePosition;
    private ShapeTypeEnum badgeImageShape;
    private String groupFieldId;
    private String searchFilterId;
    private String labelFieldId;
    private String valueFieldId;
    private String sortFieldId;
    private String format;
    private Map<String, String> defValue;
    private Map<String, String>[] defValuesArray;
    private String statusFieldId;
    private String enabledFieldId;
    private String datasourceId;

    public abstract boolean isSingle();

}
