package net.n2oapp.framework.api.metadata.global.view.fieldset;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.global.N2oMetadata;
import net.n2oapp.framework.api.metadata.local.view.CssClassAware;

import java.util.Set;


/**
 * Абстрактная исходная модель филдсета
 */
@Getter
@Setter
public abstract class N2oFieldSet extends N2oMetadata implements CssClassAware {

    private NamespaceUriAware[] items;
    private String label;
    private String src;
    private String cssClass;
    private String style;
    private FieldLabelLocation fieldLabelLocation;
    private FieldLabelAlign fieldLabelAlign;
    private String labelWidth;
    private String dependencyCondition;
    private String[] dependsOn;
    private Set<String> visibilityConditions;
    private String enablingCondition;
    private String enablingConditionOn;
    private String visible;
    private String enabled;

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
