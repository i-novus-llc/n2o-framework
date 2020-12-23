package net.n2oapp.framework.config.metadata.compile.widget;

import lombok.Getter;
import net.n2oapp.framework.api.metadata.global.N2oMetadata;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Информация об исходных виджетах страницы
 */
@Getter
public class PageSourceWidgetsScope {
    private Map<String, N2oWidget> widgets;

    public PageSourceWidgetsScope(List<N2oWidget> widgets) {
        this.widgets = widgets.stream().collect(
                Collectors.toMap(N2oMetadata::getId, Function.identity())
        );
    }
}
