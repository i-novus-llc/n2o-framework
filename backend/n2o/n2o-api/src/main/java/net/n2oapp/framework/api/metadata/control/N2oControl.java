package net.n2oapp.framework.api.metadata.control;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.global.aware.IdAware;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;

import java.io.Serializable;

/**
 * Абстрактная реализация контрола.
 */
@Getter
@Setter
public abstract class N2oControl implements Serializable, IdAware, NamespaceUriAware {
    private String id;
    private String label;
    private Boolean visible;
    private String description;
    private String namespaceUri;
    private String placeholder;
    private Validations validations;
    private String help;

    public N2oControl() {
    }

    protected N2oControl(String id) {
        this.id = id;
    }

    @Getter
    @Setter
    public static class Validations implements Serializable {
        private N2oValidation[] inlineValidations;
        private String[] whiteList;

    }

    public Validations getValidations() {
        return validations;
    }

    public void setValidations(Validations validations) {
        this.validations = validations;
    }

    @Override
    public int hashCode() {
        int result = 17;
        String thisId = getId();
        result = 37 * result + (thisId == null ? 0 : thisId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!obj.getClass().equals(this.getClass()))
            return false;

        N2oControl cfg = ((N2oControl) obj);

        return (getId() == null && cfg.getId() == null && super.equals(obj))
                || (getId() != null && getId().equals(cfg.getId()));
    }

    @Override
    public String toString() {
        return id + "." + this.getClass().getSimpleName();
    }

}
