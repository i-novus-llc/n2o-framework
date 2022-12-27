package net.n2oapp.framework.api.metadata.global.view.fieldset;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.metadata.N2oAttribute;
import net.n2oapp.framework.api.metadata.SourceComponent;
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
    @N2oAttribute("Элементы филдсета")
    private SourceComponent[] items;
    @N2oAttribute("Заголовок")
    private String label;
    @N2oAttribute("Описание")
    private String description;
    private String src;
    @N2oAttribute("Css класс")
    private String cssClass;
    @N2oAttribute("Стиль")
    private String style;
    private FieldLabelLocation fieldLabelLocation;
    private FieldLabelAlign fieldLabelAlign;
    private String fieldLabelWidth;
    private String dependencyCondition;
    private String[] dependsOn;
    @N2oAttribute("Условие видимости")
    private String visible;
    @N2oAttribute("Условие доступности")
    private String enabled;
    @N2oAttribute("Подсказка")
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
