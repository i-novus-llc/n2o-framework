package net.n2oapp.framework.api.metadata.control.plain;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * Компонент ввода текста с автоподбором
 */
@Getter
@Setter
public class N2oAutoComplete extends N2oPlainField {
    private String queryId;
    private String valueFieldId;
    private String searchFilterId;
    private Boolean tags;
    private Map<String, String>[] options;
}
