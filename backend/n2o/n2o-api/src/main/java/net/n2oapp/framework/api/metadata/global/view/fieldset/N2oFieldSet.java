package net.n2oapp.framework.api.metadata.global.view.fieldset;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.aware.ExtensionAttributesAware;
import net.n2oapp.framework.api.metadata.aware.FieldsetItem;
import net.n2oapp.framework.api.metadata.global.N2oMetadata;
import net.n2oapp.framework.api.metadata.global.view.widget.table.ShapeTypeEnum;
import net.n2oapp.framework.api.metadata.jackson.ExtAttributesSerializer;
import net.n2oapp.framework.api.metadata.meta.badge.BadgeAware;
import net.n2oapp.framework.api.metadata.meta.badge.PositionEnum;

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
    private FieldLabelLocationEnum fieldLabelLocation;
    private FieldLabelAlignEnum fieldLabelAlign;
    private String fieldLabelWidth;
    private String visible;
    private String enabled;
    private String help;
    private String[] dependsOn;
    private String badge;
    private String badgeColor;
    private PositionEnum badgePosition;
    private ShapeTypeEnum badgeShape;
    private String badgeImage;
    private PositionEnum badgeImagePosition;
    private ShapeTypeEnum badgeImageShape;
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
