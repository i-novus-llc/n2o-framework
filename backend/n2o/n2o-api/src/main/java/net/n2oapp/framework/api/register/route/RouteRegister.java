package net.n2oapp.framework.api.register.route;

import net.n2oapp.framework.api.metadata.Compiled;
import net.n2oapp.framework.api.metadata.compile.CompileContext;

import java.util.Map;

/**
 * Реестр URL адресов метаданных
 */
public interface RouteRegister extends Iterable<Map.Entry<RouteInfoKey, CompileContext>> {

    /**
     * Регистрация URL адреса метаданной
     *
     * @param urlPattern Шаблон URl адреса метаданной
     * @param context    Контекст сборки метаданной
     */
    void addRoute(String urlPattern, CompileContext<? extends Compiled, ?> context);

    /**
     * Удаление информации об адресах метаданных
     */
    void clearAll();

    /**
     * Синхронизация с репозиторием
     * @return true, если появились новые данные
     */
    default boolean synchronize() {
        return false;
    }
}
