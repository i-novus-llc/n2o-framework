package net.n2oapp.framework.access.metadata.schema.simple;

import net.n2oapp.framework.access.metadata.accesspoint.AccessPoint;
import net.n2oapp.framework.access.metadata.accesspoint.model.N2oObjectAccessPoint;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.validate.ValidateProcessor;
import net.n2oapp.framework.api.metadata.validation.TypedMetadataValidator;
import net.n2oapp.framework.access.functions.StreamUtil;
import org.springframework.stereotype.Component;

@Component
public class SimpleAccessSchemaValidator extends TypedMetadataValidator<N2oSimpleAccessSchema> {
    @Override
    public Class<N2oSimpleAccessSchema> getSourceClass() {
        return N2oSimpleAccessSchema.class;
    }

    @Override
    public void validate(N2oSimpleAccessSchema metadata, ValidateProcessor p) {
        StreamUtil.safeStreamOf(metadata.getN2oPermissions()).flatMap(ap -> p.safeStreamOf(ap.getAccessPoints())).forEach(ap -> validate(ap, p));
        StreamUtil.safeStreamOf(metadata.getN2oRoles()).flatMap(ap -> StreamUtil.safeStreamOf(ap.getAccessPoints())).forEach(ap -> validate(ap, p));
        StreamUtil.safeStreamOf(metadata.getN2oUserAccesses()).flatMap(ap -> p.safeStreamOf(ap.getAccessPoints())).forEach(ap -> validate(ap, p));
        StreamUtil.safeStreamOf(metadata.getPermitAllPoints()).forEach(ap -> validate(ap, p));
        StreamUtil.safeStreamOf(metadata.getAuthenticatedPoints()).forEach(ap -> validate(ap, p));
    }

    private void validate(AccessPoint accessPoint, ValidateProcessor processor) {
        if (accessPoint instanceof N2oObjectAccessPoint) {
            checkObjectAccess(((N2oObjectAccessPoint) accessPoint), processor);
        }
    }

    private void checkObjectAccess(N2oObjectAccessPoint accessPoint, ValidateProcessor p) {
        p.checkNotNull(accessPoint.getObjectId(), "Не задан object-id в object-access");
        p.checkForExists(accessPoint.getObjectId(), N2oObject.class, "Объект {0} заданный в object-access не существует");
    }
}
