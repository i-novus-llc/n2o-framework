package net.n2oapp.framework.api.metadata.control;

import lombok.Getter;
import lombok.Setter;
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
    protected Boolean search;
    protected Map<String, String>[] options;
    protected Boolean cache;
    private PopupScaling popupScaling;
    private Integer size;
    private String placeholder;
    private N2oPreFilter[] preFilters;
    private String queryId;
    private String iconFieldId;
    private String imageFieldId;
    private String badgeFieldId;
    private String badgeColorFieldId;
    private Position badgePosition;
    private ShapeType badgeShape;
    private String badgeImageFieldId;
    private Position badgeImagePosition;
    private ShapeType badgeImageShape;
    private String groupFieldId;
    private String searchFilterId;
    private String labelFieldId;
    private String valueFieldId;
    private String sortFieldId;
    private String format;
    private Map<String, String> defValue;
    private String statusFieldId;
    private String enabledFieldId;

    public abstract boolean isSingle();

    public enum PopupScaling {
        auto, nowrap, normal
    }

}
