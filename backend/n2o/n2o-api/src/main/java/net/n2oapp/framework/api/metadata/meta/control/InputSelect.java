package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.control.multi.N2oMultiListFieldUtil;

import static net.n2oapp.framework.api.metadata.control.list.ListType.checkboxes;
import static net.n2oapp.framework.api.metadata.control.list.ListType.multi;

@Getter
@Setter
public class InputSelect extends ListControl {
    @JsonProperty
    private Boolean multiSelect;
    @JsonProperty
    private String placeholder;
    @JsonProperty
    private Boolean hasCheckboxes;
    @JsonProperty
    private Boolean resetOnBlur;

    @Override
    public boolean containsHimself(DataSet dataSet) {
        if (multiSelect || hasCheckboxes) {
            return N2oMultiListFieldUtil.containsHimself(dataSet, this);
        } else return super.containsHimself(dataSet);
    }
}
