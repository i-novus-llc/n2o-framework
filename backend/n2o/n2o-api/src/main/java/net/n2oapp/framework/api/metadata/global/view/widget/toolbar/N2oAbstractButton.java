package net.n2oapp.framework.api.metadata.global.view.widget.toolbar;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.N2oAttribute;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.control.N2oComponent;
import net.n2oapp.framework.api.metadata.global.view.action.LabelType;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeType;
import net.n2oapp.framework.api.metadata.meta.badge.BadgeAware;
import net.n2oapp.framework.api.metadata.meta.badge.Position;

/**
 * Абстрактная модель пункта меню.
 */
@Getter
@Setter
public abstract class N2oAbstractButton extends N2oComponent implements GroupItem, IdAware, BadgeAware {
    @N2oAttribute("Идентификатор")
    private String id;
    @N2oAttribute("Заголовок")
    private String label;
    @N2oAttribute("Иконка")
    private String icon;
    @N2oAttribute("Тип заголовка")
    private LabelType type;
    @N2oAttribute("Текст значка")
    private String badge;
    @N2oAttribute("Цвет значка")
    private String badgeColor;
    @N2oAttribute("Позиция значка")
    private Position badgePosition;
    @N2oAttribute("Форма значка")
    private ShapeType badgeShape;
    @N2oAttribute("Картинка в значке")
    private String badgeImage;
    @N2oAttribute("Позиция картинки в значке")
    private Position badgeImagePosition;
    @N2oAttribute("Форма картинки в значке")
    private ShapeType badgeImageShape;
    @N2oAttribute("Цвет")
    private String color;
    @N2oAttribute("Описание")
    private String description;
    @N2oAttribute("Позиция тултипа")
    private String tooltipPosition;
    private ReduxModel model;
    private String datasourceId;
    @N2oAttribute("Условие видимости")
    private String visible;
    @N2oAttribute("Условие доступности")
    private String enabled;

    @Deprecated
    public String getWidgetId() {
        return datasourceId;
    }

    @Deprecated
    public void setWidgetId(String widgetId) {
        this.datasourceId = widgetId;
    }
}
