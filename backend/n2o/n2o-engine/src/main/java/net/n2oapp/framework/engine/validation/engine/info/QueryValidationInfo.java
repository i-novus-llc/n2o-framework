package net.n2oapp.framework.engine.validation.engine.info;

import lombok.Getter;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.metadata.local.CompiledObject;

import java.util.List;

@Getter
public class QueryValidationInfo extends ValidationInfo {

    private CompiledObject object;

    public QueryValidationInfo(CompiledObject object, List<Validation> validations,
                               DataSet dataSet, String messageForm) {
        super(dataSet, validations, messageForm);
        this.object = object;
    }
}
