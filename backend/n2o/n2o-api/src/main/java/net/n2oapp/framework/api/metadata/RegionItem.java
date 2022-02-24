package net.n2oapp.framework.api.metadata;

import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;

import java.util.List;
import java.util.Map;

/**
 * Возможность сбора в список вложенных исходных моделей виджетов
 */
public interface RegionItem {

    void collectWidgets(List<N2oWidget> result, Map<String, Integer> ids, String prefix);
}
