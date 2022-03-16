package net.n2oapp.framework.api.metadata.global.view.fieldset;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.aware.ExtensionAttributesAware;
import net.n2oapp.framework.api.metadata.global.N2oMetadata;
import net.n2oapp.framework.api.metadata.global.util.N2oMapSerializer;
import net.n2oapp.framework.api.metadata.global.util.N2oNamespaceDeserializer;
import net.n2oapp.framework.api.metadata.global.util.N2oNamespaceSerializer;

import java.util.Map;


/**
 * Абстрактная исходная модель филдсета
 */
@Getter
@Setter
public abstract class N2oFieldSet extends N2oMetadata implements ExtensionAttributesAware, SourceComponent {
    private SourceComponent[] items;
    private String label;
    private String description;
    private String src;
    private String cssClass;
    private String style;
    private FieldLabelLocation fieldLabelLocation;
    private FieldLabelAlign fieldLabelAlign;
    private String fieldLabelWidth;
    private String dependencyCondition;
    private String[] dependsOn;
    private String visible;
    private String enabled;
    private String help;
    @JsonDeserialize(keyUsing = N2oNamespaceDeserializer.class)
    @JsonSerialize(keyUsing = N2oNamespaceSerializer.class, contentUsing = N2oMapSerializer.class)
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
