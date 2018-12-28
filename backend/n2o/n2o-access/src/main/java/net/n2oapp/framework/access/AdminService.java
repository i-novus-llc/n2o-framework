package net.n2oapp.framework.access;

import java.util.List;

/**
 * Сервис проверки пользователя на суперадмина
 */
public class AdminService {

    private List<String> admins;

    public AdminService(List<String> admins) {
        this.admins = admins;
    }

    /**
     * Проверяет является ли пользователь суперадмином.
     * Пользователь является суперадмином если прописан в настройке n2o.access.admins
     * @param userName    имя пользователя
     * @return            true если пользователь суперадмин, false иначе
     */
    public boolean isAdmin(String userName) {
        return admins.contains(userName);
    }
}
