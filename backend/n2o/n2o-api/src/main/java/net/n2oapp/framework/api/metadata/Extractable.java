package net.n2oapp.framework.api.metadata;

import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;

import java.util.List;
import java.util.Map;

/**
 * Возможность извлечь вложенные исходные модели виджетов
 */
public interface Extractable {

    void extractInWidgetList(List<N2oWidget> result, Map<String, Integer> ids, String prefix);
}
