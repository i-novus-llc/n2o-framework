package net.n2oapp.framework.access.metadata.schema.simple;

import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.access.metadata.accesspoint.AccessPoint;
import net.n2oapp.framework.access.metadata.accesspoint.model.N2oObjectAccessPoint;
import net.n2oapp.framework.access.metadata.accesspoint.model.N2oObjectFiltersAccessPoint;
import net.n2oapp.framework.api.metadata.global.dao.N2oPreFilter;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.validation.TypedMetadataValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.config.metadata.validation.ValidationUtil;
import net.n2oapp.framework.access.functions.StreamUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class SimpleAccessSchemaValidator extends TypedMetadataValidator<N2oSimpleAccessSchema> {
    @Override
    public Class<N2oSimpleAccessSchema> getMetadataClass() {
        return N2oSimpleAccessSchema.class;
    }

    @Override
    public void check(N2oSimpleAccessSchema metadata) {
        StreamUtil.safeStreamOf(metadata.getN2oPermissions()).flatMap(p -> StreamUtil.safeStreamOf(p.getAccessPoints())).forEach(this::validate);
        StreamUtil.safeStreamOf(metadata.getN2oRoles()).flatMap(p -> StreamUtil.safeStreamOf(p.getAccessPoints())).forEach(this::validate);
        StreamUtil.safeStreamOf(metadata.getN2oUserAccesses()).flatMap(p -> StreamUtil.safeStreamOf(p.getAccessPoints())).forEach(this::validate);
        StreamUtil.safeStreamOf(metadata.getPermitAllPoints()).forEach(this::validate);
        StreamUtil.safeStreamOf(metadata.getAuthenticatedPoints()).forEach(this::validate);
    }

    private void validate(AccessPoint accessPoint) {
        if (accessPoint instanceof N2oObjectAccessPoint) {
            checkObjectAccess(((N2oObjectAccessPoint) accessPoint));
        }
        if (accessPoint instanceof N2oObjectFiltersAccessPoint) {
            checkObjectFiltersAccess(((N2oObjectFiltersAccessPoint) accessPoint));
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

    private void checkObjectAccess(N2oObjectAccessPoint accessPoint) {
        if (accessPoint.getObjectId() == null)
            throw new N2oMetadataValidationException("n2o.idNotSpecified");
        N2oObject n2oObject = ValidationUtil.getOrNull(accessPoint.getObjectId(), N2oObject.class);
        if (n2oObject == null) {
            throw new N2oMetadataValidationException("n2o.objectNotExists").addData(accessPoint.getObjectId());
        }
        if(accessPoint.getAction() == null || accessPoint.getAction().isEmpty()) {
            return;
        }
        String[] actions = accessPoint.getAction().split(",");
        if(n2oObject.getOperations() != null) {
            List<String> objectActionsIds = getActionsIds(Arrays.asList(n2oObject.getOperations()));
            for (String action : actions) {
                if (!action.trim().equals("*") && !action.trim().equals("read")) {
                    if (!objectActionsIds.contains(action.trim())) {
                        throw new N2oMetadataValidationException("n2o.actionNotSpecified").addData(n2oObject.getName(), n2oObject.getId(), action);
                    }
                }
            }
        }
    }

    private List<String> getActionsIds(List<N2oObject.Operation> operations) {
        List<String> ids = new ArrayList<>();
        if(operations != null) {
            for(N2oObject.Operation operation : operations) {
                ids.add(operation.getId());
            }
        }
        return ids;
    }

}
