package net.n2oapp.framework.api.metadata.control;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.ExtensionAttributesAware;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;
import net.n2oapp.framework.api.metadata.local.view.CssClassAware;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;


/**
 * Абстратная реализация контрола
 */
@Getter
@Setter
public abstract class N2oField implements Source, ExtensionAttributesAware, NamespaceUriAware {
    private String id;
    private Boolean visible;
    private String namespaceUri;
    private Validations validations;
    protected String src;
    private String fieldSrc;
    private Boolean required;
    private Boolean enabled;
    private String[] dependsOn;
    private N2oToolbar toolbar;
    private Dependency[] dependencies;
    private Map<N2oNamespace, Map<String, String>> extAttributes;

    public void addDependency(Dependency d) {
        if (d == null) return;
        if (dependencies == null) {
            dependencies = new Dependency[1];
            dependencies[0] = d;
        } else {
            dependencies = Arrays.copyOf(dependencies, dependencies.length + 1);
            dependencies[dependencies.length - 1] = d;
        }
    }

    @Getter
    @Setter
    public static class Validations implements Serializable {
        private N2oValidation[] inlineValidations;
        private String[] whiteList;

    }

    @Getter
    @Setter
    public static abstract class Dependency implements Serializable {
        private String on;
        private String value;
    }

    public static class EnablingDependency extends Dependency {
    }

    public static class RequiringDependency extends Dependency {
    }

    public static class SetValueDependency extends Dependency {
    }

    @Getter
    @Setter
    public static class VisibilityDependency extends Dependency {
        private Boolean reset;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + getId() + ")";
    }

}
