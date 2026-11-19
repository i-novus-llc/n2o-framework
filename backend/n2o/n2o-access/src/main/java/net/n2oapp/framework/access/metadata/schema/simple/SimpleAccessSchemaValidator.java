package net.n2oapp.framework.access.metadata.schema.simple;

import net.n2oapp.framework.access.functions.StreamUtil;
import net.n2oapp.framework.access.metadata.accesspoint.AccessPoint;
import net.n2oapp.framework.access.metadata.accesspoint.model.N2oObjectAccessPoint;
import net.n2oapp.framework.access.metadata.accesspoint.model.N2oObjectFilter;
import net.n2oapp.framework.access.metadata.accesspoint.model.N2oObjectFiltersAccessPoint;
import net.n2oapp.framework.access.metadata.accesspoint.model.N2oPageAccessPoint;
import net.n2oapp.framework.access.metadata.accesspoint.model.N2oUrlAccessPoint;
import net.n2oapp.framework.api.criteria.filters.FilterTypeEnum;
import net.n2oapp.framework.api.metadata.compile.SourceProcessor;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.global.view.page.N2oPage;
import net.n2oapp.framework.api.metadata.validation.TypedMetadataValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.validation.standard.ValidationUtils;
import org.springframework.stereotype.Component;

@Component
public class SimpleAccessSchemaValidator extends TypedMetadataValidator<N2oSimpleAccessSchema> {
    @Override
    public Class<N2oSimpleAccessSchema> getSourceClass() {
        return N2oSimpleAccessSchema.class;
    }

    @Override
    public void validate(N2oSimpleAccessSchema metadata, SourceProcessor p) {
        StreamUtil.safeStreamOf(metadata.getN2oPermissions()).forEach(permission ->
                p.checkNotNull(permission.getId(), "В элементе \"<permission>\" схемы доступа не задан обязательный атрибут 'id'"));
        StreamUtil.safeStreamOf(metadata.getN2oRoles()).forEach(role ->
                p.checkNotNull(role.getId(), "В элементе \"<role>\" схемы доступа не задан обязательный атрибут 'id'"));
        StreamUtil.safeStreamOf(metadata.getN2oUserAccesses()).forEach(user ->
                p.checkNotNull(user.getId(), "В элементе \"<user>\" схемы доступа не задан обязательный атрибут 'username'"));

        StreamUtil.safeStreamOf(metadata.getN2oPermissions())
                .flatMap(ap -> p.safeStreamOf(ap.getAccessPoints())).forEach(ap -> validate(ap, p));
        StreamUtil.safeStreamOf(metadata.getN2oRoles())
                .flatMap(ap -> StreamUtil.safeStreamOf(ap.getAccessPoints())).forEach(ap -> validate(ap, p));
        StreamUtil.safeStreamOf(metadata.getN2oUserAccesses())
                .flatMap(ap -> p.safeStreamOf(ap.getAccessPoints())).forEach(ap -> validate(ap, p));
        StreamUtil.safeStreamOf(metadata.getPermitAllPoints())
                .forEach(ap -> validate(ap, p));
        StreamUtil.safeStreamOf(metadata.getAuthenticatedPoints())
                .forEach(ap -> validate(ap, p));
        StreamUtil.safeStreamOf(metadata.getAnonymousPoints())
                .forEach(ap -> validate(ap, p));
    }

    private void validate(AccessPoint accessPoint, SourceProcessor p) {
        if (accessPoint instanceof N2oObjectAccessPoint objectAccessPoint) {
            checkObjectAccess(objectAccessPoint, p);
        }
        if (accessPoint instanceof N2oObjectFiltersAccessPoint objectFiltersAccessPoint) {
            checkObjectFiltersAccess(objectFiltersAccessPoint, p);
        }
        if (accessPoint instanceof N2oUrlAccessPoint urlAccessPoint) {
            p.checkNotNull(urlAccessPoint.getPattern(), "В элементе \"<url-access>\" схемы доступа не задан обязательный атрибут 'pattern'");
        }
        if (accessPoint instanceof N2oPageAccessPoint pageAccessPoint) {
            checkPageAccess(pageAccessPoint, p);
        }
    }

    private void checkObjectFiltersAccess(N2oObjectFiltersAccessPoint accessPoint, SourceProcessor p) {
        p.checkNotNull(accessPoint.getObjectId(), "В элементе \"<object-filters>\" схемы доступа не задан обязательный атрибут 'object-id'");
        if (accessPoint.getFilters() != null) {
            for (N2oObjectFilter f : accessPoint.getFilters()) {
                p.checkNotNull(f.getId(), String.format(
                        "В фильтре элемента \"<object-filters>\" объекта %s не задан обязательный атрибут 'id'",
                        ValidationUtils.getIdOrEmptyString(accessPoint.getObjectId())));
                p.checkNotNull(f.getFieldId(), String.format(
                        "В фильтре %s элемента \"<object-filters>\" объекта %s не задан обязательный атрибут 'field-id'",
                        ValidationUtils.getIdOrEmptyString(f.getId()),
                        ValidationUtils.getIdOrEmptyString(accessPoint.getObjectId())));
                if ((f.getType() == null || !f.getType().arity.equals(FilterTypeEnum.ArityEnum.NULLARY)) && f.getValue() == null && (f.getValues() == null || f.getValues().length < 1))
                    throw new N2oMetadataValidationException("n2o.filterValueNotSpecified").addData(accessPoint.getObjectId());
            }
        }
    }

    private void checkObjectAccess(N2oObjectAccessPoint accessPoint, SourceProcessor p) {
        p.checkNotNull(accessPoint.getObjectId(), "Не задан object-id в object-access");
        p.checkForExists(accessPoint.getObjectId(), N2oObject.class,
                String.format("Объект %s, заданный в object-access не существует",
                        ValidationUtils.getIdOrEmptyString(accessPoint.getObjectId())));
    }

    private void checkPageAccess(N2oPageAccessPoint pageAccessPoint, SourceProcessor p) {
        p.checkNotNull(pageAccessPoint.getPage(), "Не задан page-id в page-access");
        p.checkForExists(pageAccessPoint.getPage(), N2oPage.class,
                String.format("Страница %s, заданная в page-access не существует",
                        ValidationUtils.getIdOrEmptyString(pageAccessPoint.getPage())));
    }
}
