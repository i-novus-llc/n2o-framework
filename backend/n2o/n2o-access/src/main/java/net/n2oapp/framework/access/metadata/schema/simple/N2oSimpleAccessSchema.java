package net.n2oapp.framework.access.metadata.schema.simple;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.access.metadata.accesspoint.AccessPoint;
import net.n2oapp.framework.access.metadata.schema.N2oAccessSchema;
import net.n2oapp.framework.access.metadata.schema.permission.N2oPermission;
import net.n2oapp.framework.access.metadata.schema.role.N2oRole;
import net.n2oapp.framework.access.metadata.schema.user.N2oUserAccess;

/**
 * @author V. Alexeev.
 */
@Getter
@Setter
public class N2oSimpleAccessSchema extends N2oAccessSchema {

    private N2oPermission[] n2oPermissions;
    private N2oRole[] n2oRoles;
    private N2oUserAccess[] n2oUserAccesses;
    private AccessPoint[] authenticatedPoints;
    private AccessPoint[] anonymousPoints;
    private AccessPoint[] permitAllPoints;

    @Override
    public Class<? extends N2oAccessSchema> getSourceBaseClass() {
        return N2oSimpleAccessSchema.class;
    }

}
