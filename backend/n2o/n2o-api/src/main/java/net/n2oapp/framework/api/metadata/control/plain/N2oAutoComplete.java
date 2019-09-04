package net.n2oapp.framework.api.metadata.control.plain;

import lombok.Getter;
import lombok.Setter;

/**
 * Компонент ввода текста
 */
@Getter
@Setter
public class N2oAutoComplete extends N2oPlainField {

    public N2oAutoComplete(String id) {
        setId(id);
    }

    public N2oAutoComplete() {
    }

    private String queryId;
    private String valueFieldId;
    private String searchFilterId;

}
