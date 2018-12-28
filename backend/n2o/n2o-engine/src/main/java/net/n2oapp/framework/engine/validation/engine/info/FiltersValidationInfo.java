package net.n2oapp.framework.engine.validation.engine.info;

import lombok.Getter;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.local.CompiledQuery;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;

import java.util.List;

import static net.n2oapp.framework.engine.validation.engine.ValidationUtil.calculateVisibleValidation;

@Getter
public class FiltersValidationInfo {

    private CompiledObject object;
    private CompiledQuery query;
    private DataSet dataSet;
    private List<Validation> filtersValidation;
    private String messagesForm;


    @SuppressWarnings("unchecked")
    public FiltersValidationInfo(CompiledQuery query, DataSet queryData, String messagesForm) {
        this.query = query;
        this.object = query.getObject();
        this.filtersValidation = calculateVisibleValidation(query.getValidations(), queryData);
        this.dataSet = queryData;
        this.messagesForm = messagesForm;
    }
}
