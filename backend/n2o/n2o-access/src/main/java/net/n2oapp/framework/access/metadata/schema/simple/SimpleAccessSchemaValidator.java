package net.n2oapp.framework.access.metadata.schema.simple;

import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.access.functions.StreamUtil;
import net.n2oapp.framework.access.metadata.accesspoint.AccessPoint;
import net.n2oapp.framework.access.metadata.accesspoint.model.N2oObjectAccessPoint;
import net.n2oapp.framework.access.metadata.accesspoint.model.N2oObjectFiltersAccessPoint;
import net.n2oapp.framework.access.metadata.accesspoint.model.N2oPageAccessPoint;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.global.view.page.N2oPage;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.validation.TypedMetadataValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import org.springframework.stereotype.Component;

@Component
public class SimpleAccessSchemaValidator extends TypedMetadataValidator<N2oSimpleAccessSchema> {
    @Override
    public Class<N2oSimpleAccessSchema> getSourceClass() {
        return N2oSimpleAccessSchema.class;
    }

    @Override
    public void validate(N2oSimpleAccessSchema metadata, SourceProcessor p) {
        StreamUtil.safeStreamOf(metadata.getN2oPermissions()).flatMap(ap -> p.safeStreamOf(ap.getAccessPoints())).forEach(ap -> validate(ap, p));
        StreamUtil.safeStreamOf(metadata.getN2oRoles()).flatMap(ap -> StreamUtil.safeStreamOf(ap.getAccessPoints())).forEach(ap -> validate(ap, p));
        StreamUtil.safeStreamOf(metadata.getN2oUserAccesses()).flatMap(ap -> p.safeStreamOf(ap.getAccessPoints())).forEach(ap -> validate(ap, p));
        StreamUtil.safeStreamOf(metadata.getPermitAllPoints()).forEach(ap -> validate(ap, p));
        StreamUtil.safeStreamOf(metadata.getAuthenticatedPoints()).forEach(ap -> validate(ap, p));
    }

    private void validate(AccessPoint accessPoint, SourceProcessor processor) {
        if (accessPoint instanceof N2oObjectAccessPoint) {
            checkObjectAccess(((N2oObjectAccessPoint) accessPoint), processor);
        }
        if (accessPoint instanceof N2oObjectFiltersAccessPoint) {
            checkObjectFiltersAccess(((N2oObjectFiltersAccessPoint) accessPoint));
        }
        if (accessPoint instanceof N2oPageAccessPoint) {
            checkPageAccess((N2oPageAccessPoint)accessPoint, processor);
        }
    }

    private void checkObjectFiltersAccess(N2oObjectFiltersAccessPoint accessPoint) {
        if (accessPoint.getFilters() != null) {
            for (N2oPreFilter f : accessPoint.getFilters()) {
                if (f.getFieldId() == null)
                    throw new N2oMetadataValidationException("n2o.fieldIdNotSpecified").addData(accessPoint.getObjectId());
                if ((f.getType() == null || !f.getType().arity.equals(FilterType.Arity.nullary)) && f.getValueAttr() == null && (f.getValues() == null || f.getValues().length < 1))
                    throw new N2oMetadataValidationException("n2o.filterValueNotSpecified").addData(accessPoint.getObjectId());
            }
        }
    }

    private void checkObjectAccess(N2oObjectAccessPoint accessPoint, SourceProcessor p) {
        p.checkNotNull(accessPoint.getObjectId(), "Не задан object-id в object-access");
        p.checkForExists(accessPoint.getObjectId(), N2oObject.class, "Объект {0} заданный в object-access не существует");
    }

    private void checkPageAccess(N2oPageAccessPoint pageAccessPoint, SourceProcessor p) {
        p.checkNotNull(pageAccessPoint.getPage(), "Не задан page-id в page-access");
        p.checkForExists(pageAccessPoint.getPage(), N2oPage.class, "Страница {0} заданая в page-access не существует");
    }
}
