package net.n2oapp.framework.api.metadata.meta;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

/**
 * Клиентская модель значения по умолчанию
 */
@Deprecated
@Getter
@Setter
public class FilterDefaultValue implements Serializable {
    private String bindLink;
    private String value;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FilterDefaultValue that = (FilterDefaultValue) o;

        if (bindLink != null ? !bindLink.equals(that.bindLink) : that.bindLink != null) return false;
        return !(value != null ? !value.equals(that.value) : that.value != null);

    }

    @Override
    public int hashCode() {
        int result = bindLink != null ? bindLink.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
