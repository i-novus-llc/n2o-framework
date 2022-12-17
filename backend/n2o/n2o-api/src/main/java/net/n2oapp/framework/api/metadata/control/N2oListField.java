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
    @N2oAttribute("Возможность поиска по значению")
    protected Boolean search;
    protected Map<String, String>[] options;
    protected Boolean cache;
    @N2oAttribute("Размер запрашиваемых данных")
    private Integer size;
    private N2oPreFilter[] preFilters;
    private String queryId;
    @N2oAttribute("Поле, отвечающее за иконку варианта выбора")
    private String iconFieldId;
    @N2oAttribute("Поле, отвечающее за картинку варианта выбора")
    private String imageFieldId;
    @N2oAttribute("Поле, отвечающее за текст значка варианта выбора")
    private String badgeFieldId;
    @N2oAttribute("Поле, отвечающее за цвет значка варианта выбора")
    private String badgeColorFieldId;
    @N2oAttribute("Позиция значка")
    private Position badgePosition;
    @N2oAttribute("Форма значка")
    private ShapeType badgeShape;
    private String badgeImageFieldId;
    private Position badgeImagePosition;
    private ShapeType badgeImageShape;
    @N2oAttribute("Поле, отвечающее за группировку вариантов выбора")
    private String groupFieldId;
    private String searchFilterId;
    @N2oAttribute("Поле, отвечающее за отображение имени варианта выбора")
    private String labelFieldId;
    @N2oAttribute("Поле, отвечающее за значение варианта выбора")
    private String valueFieldId;
    @N2oAttribute("Поле, отвечающее за сортировку вариантов выбора")
    private String sortFieldId;
    @N2oAttribute("Формат выводимого текста")
    private String format;
    private Map<String, String> defValue;
    @N2oAttribute("Поле, отвечающее за статус варианта выбора")
    private String statusFieldId;
    @N2oAttribute("Поле, отвечающее за доступность варианта выбора")
    private String enabledFieldId;
    private String datasourceId;

    public abstract boolean isSingle();

}
