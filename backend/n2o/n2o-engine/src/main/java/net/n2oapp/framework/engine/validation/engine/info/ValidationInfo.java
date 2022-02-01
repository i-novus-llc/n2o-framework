package net.n2oapp.framework.engine.validation.engine.info;

import lombok.Getter;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.data.validation.Validation;

import java.util.List;

@Getter
public abstract class ValidationInfo {
    private DataSet dataSet;
    private List<Validation> validations;
    private String messageForm;

    public ValidationInfo(DataSet dataSet, List<Validation> validations, String messageForm) {
        this.dataSet = dataSet;
        this.validations = validations;
        this.messageForm = messageForm;
    }
}
