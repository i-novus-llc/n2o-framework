package net.n2oapp.framework.api.metadata.global.view.page;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.global.view.region.N2oRegion;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.global.view.widget.toolbar.N2oToolbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Исходная модель страницы с полем поиска
 */
@Getter
@Setter
public class N2oSearchablePage extends N2oPage {
    private N2oSearchBar searchBar;
    private String layout;
    private N2oRegion[] regions;
    private N2oToolbar[] toolbars;

    @Override
    public List<N2oWidget> getContainers() {
        if (regions == null || regions.length == 0)
            return Collections.emptyList();
        List<N2oWidget> containers = new ArrayList<>();
        for (N2oRegion r : regions) {
            if (r.getWidgets() != null)
                containers.addAll(Arrays.asList(r.getWidgets()));
        }
        return containers;
    }

    @Getter
    @Setter
    public static class N2oSearchBar {
        private String className;
        private TriggerType trigger;
        private String placeholder;
        private String buttonLabel;
        private String buttonIcon;
        private String searchWidgetId;
        private String searchFilterId;
        private String searchParam;

        /**
         * Триггер запуска поиска
         */
        public enum TriggerType {
            change,
            enter,
            button
        }
    }
}
