package net.n2oapp.framework.api.metadata.control.plain;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.aware.PreFiltersAware;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;

import java.util.Map;

/**
 * Компонент ввода текста с автоподбором
 */
@Getter
@Setter
public class N2oAutoComplete extends N2oPlainField implements PreFiltersAware {
    private String queryId;
    private String valueFieldId;
    private String searchFilterId;
    private Boolean tags;
    private Map<String, String>[] options;
    private N2oPreFilter[] preFilters;
    private Integer maxTagTextLength;
}
