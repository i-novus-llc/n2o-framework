package net.n2oapp.framework.access.metadata.schema.simple;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.access.metadata.accesspoint.AccessPoint;
import net.n2oapp.framework.access.metadata.schema.CompiledAccessSchema;
import net.n2oapp.framework.access.metadata.schema.permission.N2oPermission;
import net.n2oapp.framework.access.metadata.schema.role.N2oRole;
import net.n2oapp.framework.access.metadata.schema.user.N2oUserAccess;

import java.util.List;
/**
 * Базовая схема прав доступа
 */
@Getter
@Setter
public class SimpleCompiledAccessSchema extends CompiledAccessSchema {
    private List<N2oPermission> n2oPermissions;
    private List<N2oRole> n2oRoles;
    private List<N2oUserAccess> n2oUserAccesses;
    private List<AccessPoint> authenticatedPoints;
    private List<AccessPoint> anonymousPoints;
    private List<AccessPoint> permitAllPoints;
}
