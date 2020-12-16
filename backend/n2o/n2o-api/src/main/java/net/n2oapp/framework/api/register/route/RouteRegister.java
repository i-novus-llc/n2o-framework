package net.n2oapp.framework.api.register.route;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileContext;

import java.util.function.BiPredicate;

/**
 * Реестр URL адресов метаданных
 */
public interface RouteRegister {

    /**
     * Регистрация URL адреса метаданной
     *
     * @param urlPattern шаблон URl адреса метаданной
     * @param context    контекст сборки метаданной
     */
    void addRoute(String urlPattern, CompileContext<? extends Compiled, ?> context);

    /**
     * Удаление информации об адресах метаданных, начинающихся с определенного URL.
     *
     * @param startPattern URL начального адреса
     */
    void clear(String startPattern);

    /**
     * Поиск контекста по фильтру
     * @param filter
     * @return
     */
    CompileContext find(BiPredicate<RouteInfoKey, CompileContext> filter);
}
