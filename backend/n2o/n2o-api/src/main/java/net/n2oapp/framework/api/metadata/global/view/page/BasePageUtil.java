package net.n2oapp.framework.api.metadata.global.view.page;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.global.view.region.N2oRegion;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.region.Region;
import net.n2oapp.framework.api.metadata.meta.region.TabsRegion;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Утилита для работы с базовой страницей
 */
public class BasePageUtil {

    /**
     * Получение виджетов скомпилированной страницы
     * @param page Клиентская модель стандартной страницы
     * @return Список виджетов
     */
    public static List<Widget> getCompiledWidgets(StandardPage page) {
        List<Widget> widgets = new ArrayList<>();
        List<Region> regions = page.getRegions().values().stream().flatMap(Collection::stream).collect(Collectors.toList());
        for (Region r : regions) {
            if (r instanceof TabsRegion) {
                if (((TabsRegion)r).getItems() != null)
                    for (Region.Item tab : ((TabsRegion)r).getItems())
                        addWidgets(widgets, tab.getContent());
            } else {
                if (r.getContent() != null)
                    addWidgets(widgets, r.getContent());
            }
        }
        return widgets;
    }

    private static void addWidgets(List<Widget> widgets, List<Compiled> items) {
        for (Compiled i : items)
            if (i instanceof Widget)
                widgets.add((Widget) i);
            else if (i instanceof TabsRegion) {
                if (((TabsRegion) i).getItems() != null)
                    for (Region.Item tab : ((TabsRegion) i).getItems())
                        addWidgets(widgets, tab.getContent());
            } else if (i instanceof Region && ((Region) i).getContent() != null)
                addWidgets(widgets, ((Region) i).getContent());
    }

    /**
     * Сбор виджетов в список
     * @param containers  результирующий список виджетов
     * @param items    список регионв и виджетов
     */
    public static void collectWidgets(List<N2oWidget> containers, SourceComponent[] items) {
        if (items != null && items.length != 0)
            for (SourceComponent item : items)
                if (item instanceof N2oWidget)
                    containers.add((N2oWidget) item);
                else if (item instanceof N2oRegion)
                    collectWidgets(containers, ((N2oRegion) item).getContent());
    }
}
