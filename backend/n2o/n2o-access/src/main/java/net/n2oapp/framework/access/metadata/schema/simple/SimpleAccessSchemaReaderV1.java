package net.n2oapp.framework.access.metadata.schema.simple;

import net.n2oapp.framework.access.metadata.accesspoint.AccessPoint;
import net.n2oapp.framework.access.metadata.schema.AccessSchemaReader;
import net.n2oapp.framework.access.metadata.schema.permission.PermissionReader;
import net.n2oapp.framework.access.metadata.schema.role.RoleReader;
import net.n2oapp.framework.access.metadata.schema.user.UserReader;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getChildren;

@Component
public class SimpleAccessSchemaReaderV1 extends AccessSchemaReader<N2oSimpleAccessSchema> {

    public static final String DEFAULT_ACCESSPOINT_LIB = "http://n2oapp.net/framework/config/schema/n2o-access-point-1.0";

    @Override
    public String getNamespaceUri() {
        return "http://n2oapp.net/framework/config/schema/access-schema-1.0";
    }

    @Override
    public N2oSimpleAccessSchema read(Element element, Namespace namespace) {
        N2oSimpleAccessSchema schema = new N2oSimpleAccessSchema();
        getAbstractAccessSchemaDefinition(element, namespace, schema);
        schema.setN2oPermissions(getChildren(element, null, "permission", new PermissionReader(readerFactory)));
        schema.setN2oRoles(getChildren(element, null, "role", new RoleReader(readerFactory)));
        schema.setNamespaceUri(getNamespaceUri());

        Element guest = element.getChild("guest-access", namespace);
        if (guest != null) {
            AccessPoint[] guestPoints = ReaderJdomUtil.getChildren(guest, null, readerFactory,
                    SimpleAccessSchemaReaderV1.DEFAULT_ACCESSPOINT_LIB, AccessPoint.class);
            schema.setPermitAllPoints(guestPoints);
        }
        Element authenticated = element.getChild("authenticated", namespace);
        if (authenticated != null) {
            AccessPoint[] authenticatedPoints = ReaderJdomUtil.getChildren(authenticated, null, readerFactory,
                    SimpleAccessSchemaReaderV1.DEFAULT_ACCESSPOINT_LIB, AccessPoint.class);
            schema.setAuthenticatedPoints(authenticatedPoints);
        }
        schema.setN2oUserAccesses(getChildren(element, null, "user", new UserReader(readerFactory)));

        return schema;
    }

    @Override
    public Class<N2oSimpleAccessSchema> getElementClass() {
        return N2oSimpleAccessSchema.class;
    }

    @Override
    public String getElementName() {
        return "access";
    }
}
