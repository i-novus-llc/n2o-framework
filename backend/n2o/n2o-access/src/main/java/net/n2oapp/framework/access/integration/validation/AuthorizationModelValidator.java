package net.n2oapp.framework.access.integration.validation;

import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.validation.MetadataValidator;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.n2oapp.properties.StaticProperties.getProperty;

/**
 * User: operhod
 * Date: 02.04.14
 * Time: 10:28
 */
public class AuthorizationModelValidator implements MetadataValidator, InitializingBean {
    private String objectId;
    private String queryId;

    private static final List<String> displayQueryFields = Arrays.asList("filterId", "value", "type", "domain");
    private static final List<String> searchQueryFields = Arrays.asList("username", "objectId", "action");

    private static final String actionId = "check";
    private static final List<String> actionInParams = Arrays.asList("username", "objectId", "action");
    private static final List<String> actionOutParams = Arrays.asList("exists");

    @Override
    public void afterPropertiesSet() {
        objectId = getProperty("n2o.access.permission.object.id");
        queryId = getProperty("n2o.access.permission.query.id");
    }

    @Override
    public List<Class> getGlobalMetadataClasses() {
        return Arrays.asList(N2oObject.class, N2oQuery.class);
    }

    @Override
    public void validate(SourceMetadata n2oMetadata) throws N2oMetadataValidationException {
        Class<? extends SourceMetadata> clazz = n2oMetadata.getClass();
        if (clazz.isAssignableFrom(N2oObject.class) && objectId.equals(n2oMetadata.getId()))
            validateObject((N2oObject) n2oMetadata);
        else if (clazz.isAssignableFrom(N2oQuery.class) && queryId.equals(n2oMetadata.getId()))
            validateQuery((N2oQuery) n2oMetadata);
    }

    private void validateQuery(N2oQuery query) {
        //обязательные display поля
        List<String> requiredFields = new ArrayList<>(displayQueryFields);
        for (N2oQuery.Field field : query.getFields()) {
            if (requiredFields.remove(field.getId()))
                if (field.getNoDisplay() != null && field.getNoDisplay())
                    throw new N2oMetadataValidationException(
                            String.format("Field '%s' in query '%s' must be display field!",
                                    field.getId(), queryId)
                    );
        }
        if (requiredFields.size() != 0) throw new N2oMetadataValidationException(
                String.format("Query '%s' must contain fields '%s'", queryId, requiredFields.get(0)));
        //обязательные search поля
        requiredFields = new ArrayList<>(searchQueryFields);
        for (N2oQuery.Field field : query.getFields()) {
            if (requiredFields.remove(field.getId()))
                if (field.isSearchUnavailable())
                    throw new N2oMetadataValidationException(
                            String.format("Field '%s' in query '%s' must be search field!",
                                    field.getId(), queryId)
                    );
        }
        if (requiredFields.size() != 0) throw new N2oMetadataValidationException(
                String.format("Query '%s' must contain fields '%s'", queryId, requiredFields.get(0)));
    }

    private void validateObject(N2oObject object) {
        N2oObject.Operation operation = null;
        //проверка на action
        for (N2oObject.Operation act : object.getOperations()) {
            if (act.getId().equals(actionId)) operation = act;
        }
        if (operation == null) throw new N2oMetadataValidationException(
                String.format("Object '%s' must contain action '%s'", objectId, actionId));
        //проверка входящих параметров action
        List<String> requiredFields = new ArrayList<>(actionInParams);
        for (N2oObject.Parameter p : operation.getInParameters()) {
            if (requiredFields.remove(p.getId())) {
                if (p.getDomain() != null && !p.getDomain().toLowerCase().equals("string"))
                    throw new N2oMetadataValidationException(
                            String.format("Domain for parameter '%s' in object '%s' must be String", p.getId(),
                                    objectId
                            )
                    );
            }
        }
        if (requiredFields.size() != 0) throw new N2oMetadataValidationException(
                String.format("Action '%s' in object '%s' must contain in-parameter '%s'", objectId, actionId,
                        requiredFields.get(0))
        );
        //проверка исходящих параметров action
        requiredFields = new ArrayList<>(actionOutParams);
        for (N2oObject.Parameter p : operation.getOutParameters()) {
            if (requiredFields.remove(p.getId())) {
                if (p.getDomain() != null && !p.getDomain().toLowerCase().equals("boolean"))
                    throw new N2oMetadataValidationException(
                            String.format("Domain for parameter '%s' in object '%s' must be Boolean", p.getId(),
                                    objectId
                            )
                    );
            }
        }
        if (requiredFields.size() != 0) throw new N2oMetadataValidationException(
                String.format("Action '%s' in object '%s' must contain out-parameter '%s'", objectId, actionId,
                        requiredFields.get(0))
        );
    }
}
