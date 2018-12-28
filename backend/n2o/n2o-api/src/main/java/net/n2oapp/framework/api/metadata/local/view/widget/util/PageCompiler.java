package net.n2oapp.framework.api.metadata.local.view.widget.util;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.global.view.page.N2oPage;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.local.GlobalMetadataProvider;
import net.n2oapp.framework.api.metadata.local.N2oMetadataMerger;
import net.n2oapp.framework.api.metadata.local.context.PageContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * User: operehod
 * Date: 06.03.2015
 * Time: 11:20
 */
@Deprecated
public class PageCompiler {

    /**
     * Найти результирующий контейнер на странице
     *
     * @param page страница
     * @return результирующий контейнер или null, если вручную result не указан и на странице несколько независимых контейнеров
     */
    public static Optional<N2oWidget> findResultContainer(N2oPage page) {
        List<N2oWidget> independentContainers = findIndependentContainers(page);
        return Optional.ofNullable(independentContainers.size() > 1 || independentContainers.isEmpty() ? null : independentContainers.get(0));
    }

    /**
     * Найти результирующий контейнер на странице по её контексту
     *
     * @param context сначала result ищется тут context.getResultContainerId()
     * @param page    потом тут page.getResultContainer()
     * @return результирующий контейнер или null, если вручную result не указан и на странице несколько независимых контейнеров
     */
    public static Optional<N2oWidget> findResultContainer(N2oPage page, PageContext context) {
        if (context != null && context.getResultContainerId() != null) {
            return Optional.of(findContainer(context.getResultContainerId(), page));
        } else {
            return findResultContainer(page);
        }
    }

    /**
     * найти все независимые контейнеры на странице
     *
     * @param page страница
     * @return первый попавшийся независимый контейнер
     */
    public static List<N2oWidget> findIndependentContainers(N2oPage page) {
        return getAllContainers(page, c -> c.getDependsOn() == null)
                .stream().collect(Collectors.toList());
    }

    /**
     * найти единственный независимый контейнер на странице
     *
     * @param page страница
     * @return единственный независимый контейнер или null
     */
    public static Optional<N2oWidget> findUniqueIndependentContainer(N2oPage page) {
        List<N2oWidget> independentContainers = findIndependentContainers(page);
        return Optional.ofNullable(independentContainers.size() > 1 || independentContainers.isEmpty() ? null : independentContainers.get(0));
    }

    /**
     * Найти фильтрующий контейнер на странице
     *
     * @param page страница
     * @return фильтрующий контейнер выбранный вручную или первый попавшийся независимый контейнер
     */
    public static Optional<N2oWidget> findFilterContainer(N2oPage page) {
        if (page.getFilterContainer() != null) {
            return Optional.ofNullable(findContainer(page.getFilterContainer(), page));
        } else {
            return findUniqueIndependentContainer(page);
        }
    }

    /**
     * Найти фильтрующий контейнер на странице
     *
     * @param page    страница
     * @param context контекст
     * @return фильтрующий контейнер выбранный вручную или первый попавшийся независимый контейнер
     */
    public static Optional<N2oWidget> findFilterContainer(N2oPage page, PageContext context) {
        if (context != null && context.getFilterContainerId() != null) {
            return Optional.of(findContainer(context.getFilterContainerId(), page));
        } else {
            return findFilterContainer(page);
        }
    }

    public static N2oWidget resolveWidget(N2oWidget widget, GlobalMetadataProvider compiler) {
        if (widget.getRefId() != null) {
            N2oWidget source = compiler.getGlobal(widget.getRefId(), N2oWidget.class);
            N2oMetadataMerger<N2oWidget> merger = widget.getMerger();
            if (merger != null) {
                return merger.merge(source, widget);
            }
        }
        return widget;
    }

    public static List<N2oWidget> getAllContainers(N2oPage page, Predicate<N2oWidget> predicate) {
        List<N2oWidget> res = new ArrayList<>();
        if (page.getContainers() != null)
            for (N2oWidget container : page.getContainers()) {
                if (predicate.test(container))
                    res.add(container);
            }
        return res;
    }

    /**
     * Найти контейнер без компиляции страницы
     *
     * @param containerId идентификатор контейнера
     * @param page        страница
     * @return найденный контейнер
     * @throws N2oException если контейнер не найден
     */
    public static N2oWidget findContainer(String containerId, N2oPage page) {
        Optional<N2oWidget> first = getAllContainers(page, c -> c.getId().equals(containerId)).stream().findFirst();
        if (!first.isPresent())
            throw new N2oException("Container " + containerId + " not found on page " + page.getId());
        return first.get();
    }

    /**
     * Найти результирующий виджет на странице
     *
     * @param resultContainerId результирующий контейнер
     * @param page              страница
     * @param compiler          компилятор
     * @return результирующий виджет
     * @throws N2oException результирующий виджет найти не удалось
     */
    public static N2oWidget findResultWidget(String resultContainerId, N2oPage page, GlobalMetadataProvider compiler) {
        if (resultContainerId != null) {
            return resolveWidget(findContainer(resultContainerId, page), compiler);
        } else {
            Optional<N2oWidget> resultContainer = findResultContainer(page);
            if (!resultContainer.isPresent())
                throw new N2oException("Cannot uniquely define resulting container on page " + page.getId());
            return resolveWidget(resultContainer.get(), compiler);
        }
    }


}
