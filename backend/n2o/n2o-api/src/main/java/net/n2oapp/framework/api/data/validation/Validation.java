package net.n2oapp.framework.api.data.validation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.data.InvocationProcessor;
import net.n2oapp.framework.api.exception.SeverityType;
import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.control.N2oFieldCondition;
import net.n2oapp.framework.api.metadata.control.ValidationReference;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;

import java.util.*;

/**
 * Клиентская модель валидации
 */
@Getter
@Setter
@NoArgsConstructor
public abstract class Validation implements Compiled {
    @JsonProperty("validationKey")
    private String id;
    @JsonProperty("text")
    private String message;
    private Set<String> fields;
    private ValidationReference.Target target;
    @JsonProperty
    private SeverityType severity = SeverityType.danger;
    private N2oValidation.ServerMoment moment;
    private String side;

    private Boolean enabled;
    private List<String> enablingConditions;

    private String fieldId;
    private N2oFieldCondition fieldVisibilityCondition;
    private String fieldSetVisibilityCondition;

    public Validation(Validation validation) {
        this.id = validation.getId();
        this.message = validation.getMessage();
        this.fields = validation.getFields();
        this.target = validation.getTarget();
        this.severity = validation.getSeverity();
        this.moment = validation.getMoment();
        this.fieldId = validation.getFieldId();
        this.side = validation.getSide();
    }

    public void addEnablingCondition(String condition) {
        if (condition == null) return;
        if (enablingConditions == null)
            this.enablingConditions = new ArrayList<>();
        enablingConditions.add(condition);
    }

    public void addEnablingConditions(Collection<String> conditions) {
        if (conditions == null) return;
        if (enablingConditions == null)
            this.enablingConditions = new ArrayList<>();
        enablingConditions.addAll(conditions);
    }

    public Set<String> getRequiredFields() {
        return getFields();
    }

    public abstract void validate(DataSet dataSet, InvocationProcessor serviceProvider, ValidationFailureCallback callback);

    @JsonProperty("type")
    public abstract String getType();

    public boolean isForField() {
        return fieldId != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Validation)) return false;
        return Objects.equals(id, ((Validation) o).id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
