package net.n2oapp.framework.api.metadata.meta.control;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.metadata.Component;
import net.n2oapp.framework.api.metadata.aware.IdAware;


/**
 * Клиентская модель элемента ввода
 */
@Getter
@Setter
public abstract class Control extends Component implements IdAware {

    @JsonProperty
    private String id;
    @JsonProperty
    private String placeholder;

    public boolean containsHimself(DataSet dataSet) {
        Object value = dataSet.get(getId());
        return value != null && !value.toString().isEmpty();
    }

}
