package net.n2oapp.framework.api.metadata.control;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.N2oAttribute;
import net.n2oapp.framework.api.metadata.aware.PreFiltersAware;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeType;
import net.n2oapp.framework.api.metadata.meta.badge.BadgeAware;
import net.n2oapp.framework.api.metadata.meta.badge.Position;

import java.util.Map;

/**
 * Абстрактная реализация спискового контрола
 */
@Getter
@Setter
public abstract class N2oListField extends N2oStandardField implements PreFiltersAware, BadgeAware {
    @N2oAttribute
    protected Boolean search;
    protected Map<String, String>[] options;
    protected Boolean cache;
    @N2oAttribute
    private Integer size;
    @N2oAttribute
    private String placeholder;
    private N2oPreFilter[] preFilters;
    private String queryId;
    @N2oAttribute
    private String iconFieldId;
    @N2oAttribute
    private String imageFieldId;
    @N2oAttribute
    private String badgeFieldId;
    @N2oAttribute
    private String badgeColorFieldId;
    @N2oAttribute
    private Position badgePosition;
    @N2oAttribute
    private ShapeType badgeShape;
    @N2oAttribute
    private String badgeImageFieldId;
    @N2oAttribute
    private Position badgeImagePosition;
    @N2oAttribute
    private ShapeType badgeImageShape;
    @N2oAttribute
    private String groupFieldId;
    private String searchFilterId;
    @N2oAttribute
    private String labelFieldId;
    @N2oAttribute
    private String valueFieldId;
    @N2oAttribute
    private String sortFieldId;
    @N2oAttribute
    private String format;
    private Map<String, String> defValue;
    @N2oAttribute
    private String statusFieldId;
    @N2oAttribute
    private String enabledFieldId;

    public abstract boolean isSingle();

}
