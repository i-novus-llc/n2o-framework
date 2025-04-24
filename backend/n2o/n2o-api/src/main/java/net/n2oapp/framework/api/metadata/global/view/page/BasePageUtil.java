package net.n2oapp.framework.api.metadata.global.view.page;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.global.view.region.N2oRegion;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.meta.page.StandardPage;
import net.n2oapp.framework.api.metadata.meta.region.CompiledRegionItem;
import net.n2oapp.framework.api.metadata.meta.region.Region;
import net.n2oapp.framework.api.metadata.meta.widget.Widget;
import org.apache.commons.collections.CollectionUtils;

import java.util.*;
import java.util.function.Consumer;

/**
 * Утилитный класс для работы с базовой страницей
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BasePageUtil {

    /**
     * Получение всех виджетов скомпилированной страницы
     *
     * @param page Клиентская модель стандартной страницы
     * @return Список виджетов
     */
    public static List<Widget<?>> getCompiledWidgets(StandardPage page) {
        List<Region> regions = page.getRegions().values().stream().flatMap(Collection::stream).toList();
        return new ArrayList<>(getRegionWidgets(regions));
    }

    /**
     * Получение списка виджетов скомпилированного региона с учетом вложенности.
     * Регионы могут содержать, как виджеты, так и регионы, поэтому производится глубокий поиск.
     *
     * @param items Список элементов региона (вложенные регионы и виджеты)
     * @return Список виджетов скомпилированного региона
     */
    private static List<Widget<?>> getRegionWidgets(List<? extends CompiledRegionItem> items) {
        List<Widget<?>> widgets = new ArrayList<>();
        if (!CollectionUtils.isEmpty(items))
            for (CompiledRegionItem item : items)
                item.collectWidgets(widgets);
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
        if (items != null) {
            Map<String, Integer> ids = new HashMap<>();
            for (SourceComponent item : items) {
                if (item instanceof net.n2oapp.framework.api.metadata.RegionItem)
                    ((net.n2oapp.framework.api.metadata.RegionItem) item).collectWidgets(widgets, ids, "w");
            }
        }
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
}
