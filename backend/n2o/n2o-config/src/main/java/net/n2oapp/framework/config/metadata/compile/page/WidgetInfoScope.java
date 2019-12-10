package net.n2oapp.framework.config.metadata.compile.page;

import lombok.Getter;
import net.n2oapp.framework.config.metadata.compile.IndexScope;

/**
 * Информация о виджете
 */
@Getter
public class WidgetInfoScope {

    private String widgetPrefix;
    private IndexScope indexScope;

    public WidgetInfoScope(String widgetPrefix, IndexScope indexScope) {
        this.widgetPrefix = widgetPrefix;
        this.indexScope = indexScope;
    }
}
