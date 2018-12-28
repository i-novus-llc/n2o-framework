package net.n2oapp.framework.access.metadata.schema.simple;

import net.n2oapp.framework.access.metadata.schema.AccessSchemaPersister;
import net.n2oapp.framework.access.metadata.schema.permission.PermissionPersister;
import net.n2oapp.framework.access.metadata.schema.role.RolePersister;
import net.n2oapp.framework.access.metadata.schema.user.UserPersister;
import net.n2oapp.framework.config.persister.util.PersisterJdomUtil;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

@Component
public class SimpleAccessSchemaPersister extends AccessSchemaPersister<N2oSimpleAccessSchema> {

    public SimpleAccessSchemaPersister() {
        setNamespaceUri("http://n2oapp.net/framework/config/schema/access-schema-1.0");
    }

    @Override
    public Class<N2oSimpleAccessSchema> getElementClass() {
        return N2oSimpleAccessSchema.class;
    }

    @Override
    public Element persist(N2oSimpleAccessSchema schema, Namespace namespace) {
        Element element = new Element(getElementName(), Namespace.getNamespace(namespacePrefix, namespaceUri));
        persistAbstractAccessSchema(element, schema);
        PersisterJdomUtil.setChildren(element, null, "permission", schema.getN2oPermissions(), new PermissionPersister(persisterFactory));
        PersisterJdomUtil.setChildren(element, null, "role", schema.getN2oRoles(), new RolePersister(persisterFactory));
        if (schema.getPermitAllPoints() != null) {
            Element guest = PersisterJdomUtil.setEmptyElement(element, "guest-access");
            PersisterJdomUtil.setChildren(guest, null, null, schema.getPermitAllPoints(),
                    persisterFactory, SimpleAccessSchemaReaderV1.DEFAULT_ACCESSPOINT_LIB);
        }
        if (schema.getAuthenticatedPoints() != null) {
            Element authenticated = PersisterJdomUtil.setEmptyElement(element, "authenticated");
            PersisterJdomUtil.setChildren(authenticated, null, null, schema.getAuthenticatedPoints(), persisterFactory, SimpleAccessSchemaReaderV1.DEFAULT_ACCESSPOINT_LIB);
        }
        PersisterJdomUtil.setChildren(element, null, "user", schema.getN2oUserAccesses(), new UserPersister(persisterFactory));
        return element;
    }

    @Override
    public String getElementName() {
        return "access";
    }
}
