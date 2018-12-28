package net.n2oapp.framework.access.api;

import net.n2oapp.framework.access.api.model.ObjectPermission;
import net.n2oapp.framework.access.api.model.Permission;
import net.n2oapp.framework.api.user.UserContext;

import java.util.List;

/**
 * User: operhod
 * Date: 29.10.13
 * Time: 10:28
 * Ключевой интерфейс для модуля проверки прав. Сервис, реализующий этот интерфейс, использутеся при проверки доступа к данным и метаданным.
 */
public interface AuthorizationApi {
    ObjectPermission getPermissionForObject(UserContext user, String objectId, String action);

    default ObjectPermission getPermissionForObject(UserContext user, String objectId) {
        return getPermissionForObject(user, objectId, "read");
    }

    ObjectPermission getReferencePermissionForObject(UserContext user, String objectId);

    Permission getPermissionForPage(UserContext user, String pageId);

    Permission getPermissionForUrlPattern(UserContext user, String pattern);

    Permission getPermissionForColumn(UserContext userContext, String pageId, String containerId, String columnId);

    Permission getPermissionForFilter(UserContext userContext, String queryId, String filterId);
}
