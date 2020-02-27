package net.n2oapp.framework.config.metadata.compile.widget;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.ReduxModel;

/**
 * Информация о поисковой строке
 */
@Getter
@Setter
@NoArgsConstructor
public class SearchBarScope {
    private ReduxModel modelPrefix = ReduxModel.FILTER;
    private String widgetId;
    private String modelKey;

    public SearchBarScope(String widgetId, String modelKey) {
        this.widgetId = widgetId;
        this.modelKey = modelKey;
    }
}
