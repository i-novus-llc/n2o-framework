package net.n2oapp.framework.access.metadata.schema.io;

import net.n2oapp.framework.access.metadata.accesspoint.AccessPoint;
import net.n2oapp.framework.access.metadata.schema.permission.N2oPermission;
import net.n2oapp.framework.access.metadata.schema.role.N2oRole;
import net.n2oapp.framework.access.metadata.schema.simple.N2oSimpleAccessSchema;
import net.n2oapp.framework.access.metadata.schema.user.N2oUserAccess;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

/**
 * Имплементация IO для схемы доступа 'access' v2.0
 * net/n2oapp/framework/config/schema/access-2.0.xsd
 */

@Component
public class SimpleAccessIOv2 implements NamespaceIO<N2oSimpleAccessSchema>, AccessSchemaIOv2  {

    private Namespace accessPointNamespace = Namespace.getNamespace("http://n2oapp.net/framework/config/schema/access-point-2.0");

    @Override
    public Class<N2oSimpleAccessSchema> getElementClass() {
        return N2oSimpleAccessSchema.class;
    }

    @Override
    public String getElementName() {
        return "access";
    }

    @Override
    public void io(Element e, N2oSimpleAccessSchema m, IOProcessor p) {
        p.children(e, null, "permission", m::getN2oPermissions, m::setN2oPermissions, N2oPermission.class, this::permissionAccessIOv2);
        p.children(e, null, "role", m::getN2oRoles, m::setN2oRoles, N2oRole.class, this::roleAccessIOv2 );
        p.children(e, null, "user", m::getN2oUserAccesses, m::setN2oUserAccesses, N2oUserAccess.class, this::userAccessIOv2);

        p.anyChildren(e, "authenticated", m::getAuthenticatedPoints, m::setAuthenticatedPoints, p.anyOf(AccessPoint.class), accessPointNamespace);
        p.anyChildren(e, "anonymous", m::getAnonymousPoints, m::setAnonymousPoints, p.anyOf(AccessPoint.class), accessPointNamespace);
        p.anyChildren(e, "permit-all", m::getPermitAllPoints, m::setPermitAllPoints, p.anyOf(AccessPoint.class), accessPointNamespace);
    }

    private void roleAccessIOv2(Element e, N2oRole m, IOProcessor p) {
        p.attribute(e, "id", m::getId, m::setId);
        p.attribute(e, "name", m::getName, m::setName);
        p.anyChildren(e, null, m::getAccessPoints, m::setAccessPoints, p.anyOf(AccessPoint.class), accessPointNamespace);
    }

    private void permissionAccessIOv2(Element e, N2oPermission m, IOProcessor p) {
        p.attribute(e, "id", m::getId, m::setId);
        p.attribute(e, "name", m::getName, m::setName);
        p.anyChildren(e, null, m::getAccessPoints, m::setAccessPoints, p.anyOf(AccessPoint.class), accessPointNamespace);
    }

    private void userAccessIOv2(Element e, N2oUserAccess m, IOProcessor p) {
        p.attribute(e, "username", m::getId, m::setId);
        p.anyChildren(e, null, m::getAccessPoints, m::setAccessPoints, p.anyOf(AccessPoint.class), accessPointNamespace);
    }

    protected void setAccessPointNamespace(Namespace accessPointNamespace) {
        this.accessPointNamespace = accessPointNamespace;
    }
}
