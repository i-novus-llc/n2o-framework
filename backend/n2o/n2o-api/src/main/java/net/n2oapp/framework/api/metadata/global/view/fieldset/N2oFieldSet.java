package net.n2oapp.framework.api.metadata.global.view.fieldset;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.VisualAttribute;
import net.n2oapp.framework.api.metadata.aware.ExtensionAttributesAware;
import net.n2oapp.framework.api.metadata.global.N2oMetadata;
import net.n2oapp.framework.api.metadata.jackson.ExtAttributesSerializer;

import java.util.Map;


/**
 * Абстрактная исходная модель филдсета
 */
@Getter
@Setter
public abstract class N2oFieldSet extends N2oMetadata implements ExtensionAttributesAware, SourceComponent {
    @VisualAttribute
    private SourceComponent[] items;
    @VisualAttribute
    private String label;
    @VisualAttribute
    private String description;
    private String src;
    @VisualAttribute
    private String cssClass;
    @VisualAttribute
    private String style;
    @VisualAttribute
    private FieldLabelLocation fieldLabelLocation;
    @VisualAttribute
    private FieldLabelAlign fieldLabelAlign;
    @VisualAttribute
    private String fieldLabelWidth;
    private String dependencyCondition;
    private String[] dependsOn;
    @VisualAttribute
    private String visible;
    @VisualAttribute
    private String enabled;
    @VisualAttribute
    private String help;
    @ExtAttributesSerializer
    private Map<N2oNamespace, Map<String, String>> extAttributes;

    @Override
    public final String getPostfix() {
        return "fieldset";
    }

    @Override
    public final Class<? extends N2oMetadata> getSourceBaseClass() {
        return N2oFieldSet.class;
    }

    public enum FieldLabelLocation {
        top, left, right
    }

    public enum FieldLabelAlign {
        left, right
    }

}
