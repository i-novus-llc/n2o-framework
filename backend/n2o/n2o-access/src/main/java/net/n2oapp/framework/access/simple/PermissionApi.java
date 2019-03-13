package net.n2oapp.framework.access.simple;


import net.n2oapp.framework.api.user.UserContext;

/**
 * Интерфейс для проверки прав и ролей пользователя
 */
public interface PermissionApi {

    /**
     * Проверка наличия прав у пользователя
     * @param user контекст пользователя
     * @param permissionId индефикатор прав доступа
     * @return наличие прав
     */
    boolean hasPermission(UserContext user, String permissionId);

    /**
     * Проверка наличия роли у пользователя
     * @param user контекст пользователя
     * @param roleId индефикатор роли
     * @return наличие роли
     */
    boolean hasRole(UserContext user, String roleId);

    /**
     * Проверка прохождения пользователем аутентификации
     * @param user контекст пользователя
     * @return наличие аутентификации
     */
    boolean hasAuthentication(UserContext user);


    /**
     * Проверка соответствия имени пользователя одному из заданным в схеме доступа
     * @param user контекст пользователя
     * @param name имя пользователя
     * @return соответствие юзернэйма одному из заданных
     */
    boolean hasUsername(UserContext user, String name);

}
