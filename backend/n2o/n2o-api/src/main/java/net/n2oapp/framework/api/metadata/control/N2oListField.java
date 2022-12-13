package net.n2oapp.framework.api.metadata.control;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.VisualAttribute;
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
    @VisualAttribute
    protected Boolean search;
    @VisualAttribute
    protected Map<String, String>[] options;
    protected Boolean cache;
    @VisualAttribute
    private Integer size;
    @VisualAttribute
    private String placeholder;
    private N2oPreFilter[] preFilters;
    private String queryId;
    @VisualAttribute
    private String iconFieldId;
    @VisualAttribute
    private String imageFieldId;
    @VisualAttribute
    private String badgeFieldId;
    @VisualAttribute
    private String badgeColorFieldId;
    @VisualAttribute
    private Position badgePosition;
    @VisualAttribute
    private ShapeType badgeShape;
    @VisualAttribute
    private String badgeImageFieldId;
    @VisualAttribute
    private Position badgeImagePosition;
    @VisualAttribute
    private ShapeType badgeImageShape;
    @VisualAttribute
    private String groupFieldId;
    private String searchFilterId;
    @VisualAttribute
    private String labelFieldId;
    @VisualAttribute
    private String valueFieldId;
    @VisualAttribute
    private String sortFieldId;
    @VisualAttribute
    private String format;
    @VisualAttribute
    private Map<String, String> defValue;
    @VisualAttribute
    private String statusFieldId;
    @VisualAttribute
    private String enabledFieldId;

    public abstract boolean isSingle();

}
