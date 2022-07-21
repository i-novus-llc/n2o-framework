package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.criteria.dataset.DataSet;

import java.util.Collection;
import java.util.List;

/**
 * Клиентская модель компонента ввода текста с выбором из выпадающего списка
 */
@Getter
@Setter
public class InputSelect extends ListControl {
    @JsonProperty
    private Boolean multiSelect;
    @JsonProperty
    private Boolean hasCheckboxes;
    @JsonProperty
    private Boolean resetOnBlur;
    @JsonProperty
    private String descriptionFieldId;
    @JsonProperty
    private Integer maxTagTextLength;

    @Override
    public boolean containsHimself(DataSet dataSet) {
        if (multiSelect || hasCheckboxes) {
            return dataSet.get(getId()) instanceof Collection && ((List) dataSet.get(getId())).size() > 0;
        } else return super.containsHimself(dataSet);
    }
}
