package net.n2oapp.framework.api.metadata.global.view.widget.toolbar;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.N2oAttribute;
import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.aware.DatasourceIdAware;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.control.N2oComponent;
import net.n2oapp.framework.api.metadata.global.view.action.LabelType;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeType;
import net.n2oapp.framework.api.metadata.meta.badge.BadgeAware;
import net.n2oapp.framework.api.metadata.meta.badge.Position;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Абстрактная модель пункта меню.
 */
@Getter
@Setter
public abstract class N2oAbstractButton extends N2oComponent implements GroupItem, IdAware, BadgeAware, DatasourceIdAware {
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

    @Override
    public ToolbarItem clone() {
        try {
            Class<?> cl = this.getClass();
            Constructor<?> cons = cl.getConstructor();
            N2oAbstractButton button = (N2oAbstractButton) cons.newInstance();
            button.setId(id);
            button.setLabel(label);
            button.setIcon(icon);
            button.setType(type);
            button.setBadge(badge);
            button.setBadgeColor(badgeColor);
            button.setBadgePosition(badgePosition);
            button.setBadgeShape(badgeShape);
            button.setBadgeImage(badgeImage);
            button.setBadgeImagePosition(badgeImagePosition);
            button.setBadgeImageShape(badgeImageShape);
            button.setColor(color);
            button.setDescription(description);
            button.setTooltipPosition(tooltipPosition);
            button.setModel(model);
            button.setDatasourceId(datasourceId);
            button.setVisible(visible);
            button.setEnabled(enabled);
            return button;
        } catch (NoSuchMethodException | SecurityException |
                InstantiationException | IllegalAccessException |
                IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
