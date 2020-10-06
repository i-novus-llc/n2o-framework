package net.n2oapp.framework.api.metadata.global.view.page;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.global.view.region.N2oRegion;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.Itemable;
import net.n2oapp.framework.api.metadata.meta.region.Region;
import net.n2oapp.framework.api.metadata.meta.region.RegionItem;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Утилита для работы с базовой страницей
 */
public class BasePageUtil {

    /**
     * Получение виджетов скомпилированной страницы
     *
     * @param page Клиентская модель стандартной страницы
     * @return Список виджетов
     */
    public static List<Widget> getCompiledWidgets(StandardPage page) {
        List<Widget> widgets = new ArrayList<>();
        List<Region> regions = page.getRegions().values().stream().flatMap(Collection::stream).collect(Collectors.toList());
        for (Region r : regions) {
            if (r instanceof Itemable) {
                Itemable<RegionItem> region = (Itemable) r;
                if (region.getItems() != null)
                    for (RegionItem regionItem : region.getItems())
                        addWidgets(widgets, regionItem.getContent());
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
            else if (i instanceof Itemable) {
                Itemable<RegionItem> region = ((Itemable) i);
                if (region.getItems() != null)
                    for (RegionItem regionItem : region.getItems())
                        addWidgets(widgets, regionItem.getContent());
            } else if (i instanceof Region && ((Region) i).getContent() != null)
                addWidgets(widgets, ((Region) i).getContent());
    }

    /**
     * Сбор всех виджетов из массива регионов и виджетов.
     * Регионы могут содержать, как виджеты, так и регионы, поэтому производится глубокий поиск.
     *
     * @param items Массив компонентов(регионов и виджетов)
     * @return Список всех виджетов
     */
    public static List<N2oWidget> collectWidgets(SourceComponent[] items) {
        List<N2oWidget> widgets = new ArrayList<>();
        if (items != null && items.length != 0)
            resolveRegionItems(items,
                    item -> widgets.addAll(collectWidgets(item.getContent())),
                    widgets::add);
        return widgets;
    }

    public static void resolveRegionItems(SourceComponent[] items, Consumer<N2oRegion> regionConsumer,
                                          Consumer<N2oWidget> widgetConsumer) {
        for (SourceComponent item : items)
            if (item instanceof N2oWidget)
                widgetConsumer.accept((N2oWidget) item);
            else if (item instanceof N2oRegion)
                regionConsumer.accept((N2oRegion) item);
    }
}
