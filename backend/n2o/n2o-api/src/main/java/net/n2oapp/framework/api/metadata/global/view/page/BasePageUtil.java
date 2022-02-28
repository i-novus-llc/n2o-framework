package net.n2oapp.framework.api.metadata.global.view.page;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.Itemable;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.global.view.region.N2oRegion;
import net.n2oapp.framework.api.metadata.global.view.region.N2oTabsRegion;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.region.Region;
import net.n2oapp.framework.api.metadata.meta.region.RegionItem;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;
import net.n2oapp.framework.api.metadata.meta.widget.toolbar.PerformButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Утилитный класс для работы с базовой страницей
 */
public class BasePageUtil {

    /**
     * Получение всех виджетов скомпилированной страницы
     *
     * @param page Клиентская модель стандартной страницы
     * @return Список виджетов
     */
    public static List<Widget<?>> getCompiledWidgets(StandardPage page) {
        List<Region> regions = page.getRegions().values().stream().flatMap(Collection::stream).collect(Collectors.toList());
        return new ArrayList<>(getRegionWidgets(regions));
    }

    /**
     * Получение списка виджетов скомпилированного региона с учетом вложенности.
     * Регионы могут содержать, как виджеты, так и регионы, поэтому производится глубокий поиск.
     *
     * @param items Список элементов региона (вложенные регионы и виджеты)
     * @return Список виджетов скомпилированного региона
     */
    private static List<Widget<?>> getRegionWidgets(List<? extends Compiled> items) {
        List<Widget<?>> widgets = new ArrayList<>();
        if (items != null) {
            for (Compiled i : items)
                if (i instanceof Widget)
                    widgets.add((Widget) i);
                else if (i instanceof Itemable) {
                    Itemable<RegionItem> region = ((Itemable) i);
                    if (region.getItems() != null)
                        for (RegionItem regionItem : region.getItems())
                            widgets.addAll(getRegionWidgets(regionItem.getContent()));
                } else if (i instanceof Region && ((Region) i).getContent() != null)
                    widgets.addAll(getRegionWidgets(((Region) i).getContent()));
        }
        return widgets;
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
                    item -> {
                        if (item.getTabs() != null)
                            for (N2oTabsRegion.Tab tab : item.getTabs())
                                if (tab.getContent() != null)
                                    widgets.addAll(collectWidgets(tab.getContent()));

                    },
                    widgets::add);
        return widgets;
    }

    /**
     * Позволяет выполнять установленные операции над элементами региона (вложенными регионами и виджетами)
     * в зависимости от типа элемента.
     *
     * @param items          Массив компонентов (регионов и виджетов)
     * @param regionConsumer Действия, выполняемые над регионом
     * @param widgetConsumer Действия, выполняемые над виджетом
     */
    public static void resolveRegionItems(SourceComponent[] items, Consumer<N2oRegion> regionConsumer,
                                          Consumer<N2oWidget> widgetConsumer) {
        for (SourceComponent item : items)
            if (item instanceof N2oWidget)
                widgetConsumer.accept((N2oWidget) item);
            else if (item instanceof N2oRegion)
                regionConsumer.accept((N2oRegion) item);
    }

    /**
     * Позволяет выполнять установленные операции над элементами региона (вложенными регионами и виджетами)
     * в зависимости от типа элемента.
     *
     * @param items              Массив компонентов (регионов и виджетов)
     * @param regionConsumer     Действия, выполняемые над регионом
     * @param tabsRegionConsumer Действия, выполняемые над регионом с вкладками
     * @param widgetConsumer     Действия, выполняемые над виджетом
     */
    public static void resolveRegionItems(SourceComponent[] items, Consumer<N2oRegion> regionConsumer,
                                          Consumer<N2oTabsRegion> tabsRegionConsumer,
                                          Consumer<N2oWidget> widgetConsumer) {
        for (SourceComponent item : items)
            if (item instanceof N2oWidget)
                widgetConsumer.accept((N2oWidget) item);
            else if (item instanceof N2oTabsRegion)
                tabsRegionConsumer.accept((N2oTabsRegion) item);
            else if (item instanceof N2oRegion)
                regionConsumer.accept((N2oRegion) item);
    }
}
