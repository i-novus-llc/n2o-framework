package net.n2oapp.framework.engine.validation.engine.info;

import lombok.Getter;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.data.validation.Validation;

import java.util.List;

@Getter
public class ObjectValidationInfo extends ValidationInfo {

    private String objectId;

    public ObjectValidationInfo(String objectId, List<Validation> validations,
                                DataSet dataSet, String messageForm) {
        super(dataSet, validations, messageForm);
        this.objectId = objectId;
    }
}
