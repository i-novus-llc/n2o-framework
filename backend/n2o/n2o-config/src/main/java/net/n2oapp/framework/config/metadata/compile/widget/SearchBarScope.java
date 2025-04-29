package net.n2oapp.framework.config.metadata.compile.widget;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModelEnum;

/**
 * Информация о поисковой строке
 */
@Getter
@Setter
@NoArgsConstructor
public class SearchBarScope {
    private ReduxModelEnum modelPrefix = ReduxModelEnum.filter;
    private String datasource;
    private String filterId;
    private String param;

    public SearchBarScope(String datasource, String filterId) {
        this.datasource = datasource;
        this.filterId = filterId;
    }
}
