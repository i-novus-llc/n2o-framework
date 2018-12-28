package net.n2oapp.framework.api.register.route;

/**
 * Реестр URL адресов метаданных
 */
public interface RouteRegister extends Iterable<RouteInfo> {

    /**
     * Регистрация URL адреса метаданной
     *
     * @param routeInfo информация о хранении метаданой
     */
    void addRoute(RouteInfo routeInfo);

    /**
     * Удаление информации об адресах метаданных, начинающихся с определенного URL.
     *
     * @param startPattern URL начального адреса
     */
    void clear(String startPattern);
}
