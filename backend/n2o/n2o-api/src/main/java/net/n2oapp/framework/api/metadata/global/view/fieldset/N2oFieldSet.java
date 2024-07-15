package net.n2oapp.framework.api.metadata.global.view.fieldset;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.aware.ExtensionAttributesAware;
import net.n2oapp.framework.api.metadata.aware.FieldsetItem;
import net.n2oapp.framework.api.metadata.global.N2oMetadata;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeType;
import net.n2oapp.framework.api.metadata.jackson.ExtAttributesSerializer;
import net.n2oapp.framework.api.metadata.meta.badge.BadgeAware;
import net.n2oapp.framework.api.metadata.meta.badge.Position;

import java.util.Map;


/**
 * Абстрактная исходная модель филдсета
 */
@Getter
@Setter
public abstract class N2oFieldSet extends N2oMetadata implements ExtensionAttributesAware, SourceComponent, FieldsetItem, BadgeAware {
    private String src;
    private String cssClass;
    private String style;
    private String label;
    private String description;
    private FieldLabelLocation fieldLabelLocation;
    private FieldLabelAlign fieldLabelAlign;
    private String fieldLabelWidth;
    private String visible;
    private String enabled;
    private String help;
    private String[] dependsOn;
    private String badge;
    private String badgeColor;
    private Position badgePosition;
    private ShapeType badgeShape;
    private String badgeImage;
    private Position badgeImagePosition;
    private ShapeType badgeImageShape;
    @ExtAttributesSerializer
    private Map<N2oNamespace, Map<String, String>> extAttributes;
    private FieldsetItem[] items;

    @Override
    public final String getPostfix() {
        return "fieldset";
    }

    @Override
    public final Class<? extends N2oMetadata> getSourceBaseClass() {
        return N2oFieldSet.class;
    }
}
