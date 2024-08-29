package net.n2oapp.framework.api.metadata.menu;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.action.N2oAction;
import net.n2oapp.framework.api.metadata.aware.DatasourceIdAware;
import net.n2oapp.framework.api.metadata.aware.ExtensionAttributesAware;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.global.N2oMetadata;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeType;
import net.n2oapp.framework.api.metadata.jackson.ExtAttributesSerializer;
import net.n2oapp.framework.api.metadata.meta.badge.BadgeAware;
import net.n2oapp.framework.api.metadata.meta.badge.Position;

import java.util.Map;

/**
 * Простое меню навигации
 */
@Getter
@Setter
public class N2oSimpleMenu extends N2oMenu implements ExtensionAttributesAware {
    private String src;
    private String refId;

    private AbstractMenuItem[] menuItems;
    @ExtAttributesSerializer
    private Map<N2oNamespace, Map<String, String>> extAttributes;

    @Override
    public final Class<? extends N2oMetadata> getSourceBaseClass() {
        return N2oSimpleMenu.class;
    }

    /**
     * Абстрактный элемент меню
     */
    @Getter
    @Setter
    public abstract static class AbstractMenuItem implements Source, IdAware, ExtensionAttributesAware, DatasourceIdAware {
        private String id;
        private String name;
        private String datasourceId;
        private String icon;
        private Position iconPosition;
        private String image;
        private ShapeType imageShape;
        private String src;
        @ExtAttributesSerializer
        private Map<N2oNamespace, Map<String, String>> extAttributes;
    }

    /**
     * Элемент меню
     */
    @Getter
    @Setter
    public static class MenuItem extends AbstractMenuItem implements BadgeAware {
        private String badge;
        private String badgeColor;
        private Position badgePosition;
        private ShapeType badgeShape;
        private String badgeImage;
        private Position badgeImagePosition;
        private ShapeType badgeImageShape;
        private String cssClass;
        private String style;

        private N2oAction action;
    }

    /**
     * Выпадающее меню
     */
    @Getter
    @Setter
    public static class DropdownMenuItem extends AbstractMenuItem {
        private AbstractMenuItem[] menuItems;
    }
}