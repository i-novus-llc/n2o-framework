package net.n2oapp.framework.config.io.object;

import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oSqlQuery;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectReferenceField;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectScalarField;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oConstraint;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidationCondition;
import net.n2oapp.framework.config.selective.ION2oMetadataTester;
import net.n2oapp.framework.config.selective.persister.SelectiveStandardPersister;
import net.n2oapp.framework.config.selective.reader.SelectiveStandardReader;
import org.junit.Test;

import java.util.Arrays;

/**
 * Test for reading and persisting object
 *
 * @author igafurov
 * @since 25.04.2017
 */
public class ObjectXmlIOTest {
    ION2oMetadataTester tester = new ION2oMetadataTester()
            .addReader(new SelectiveStandardReader().addObjectReader().addInvocationsReader2().addDataProviders())
            .addPersister(new SelectiveStandardPersister().addObjectPersister().addInvocationObjectPersister().addDataProviders());

    @Test
    public void testObjectV3XmlIO() {
        assert tester.check("net/n2oapp/framework/config/io/object/testObjectIOv3.object.xml");
    }

    /**
     * Reading and persisting object by ObjectXmlReaderV2(object-2.0.xsd)
     *
     * @result Object will be reading and persisting without any errors
     */
    @Test
    public void testObjectV2XmlIO() {
        assert tester.check("net/n2oapp/framework/config/io/object/testObjectReaderV2.object.xml",
                (N2oObject object) -> {
                    assert object.getTableName().equals("public.user");
                    assert object.getEntityClass().equals("com.example.admin.User");
                    assert object.getAppName().equals("estore");
                    assert object.getModuleName().equals("admin");
                    assert object.getServiceClass().equals("com.example.admin.UserService");
                    assert object.getServiceName().equals("userService");

                    assert object.getObjectFields().length == 2;
                    ObjectScalarField field = (ObjectScalarField) object.getObjectFields()[0];
                    assert field.getDomain().equals("string");
                    assert field.getName().equals("test");
                    assert field.getId().equals("id");
                    assert field.getMapping().equals("testMapping");
                    assert field.getRequired().equals(true);

                    ObjectReferenceField field1 = (ObjectReferenceField) object.getObjectFields()[1];
                    assert field1.getReferenceObjectId().equals("id");
                    assert field1.getId().equals("id");
                    assert field1.getName().equals("test");
                    assert field1.getMapping().equals("testMapping");

                    N2oValidationCondition condition = (N2oValidationCondition) object.getN2oValidations()[0];
                    assert condition.getId().equals("id");
                    assert condition.getSeverity().name().equals("danger");
                    assert condition.getServerMoment().name().equals("afterFailOperation");
                    assert condition.getMessage().equals("test");
                    assert condition.getExpression().equals("test");
                    assert condition.getExpressionOn().equals("id");

                    N2oConstraint constraint = (N2oConstraint) object.getN2oValidations()[1];
                    assert constraint.getId().equals("id");
                    assert constraint.getSeverity().name().equals("warning");
                    assert constraint.getServerMoment().name().equals("afterSuccessOperation");
                    assert constraint.getMessage().equals("test");
                    N2oSqlQuery sqlQuery = (N2oSqlQuery) constraint.getN2oInvocation();
                    assert sqlQuery.getQuery().equals("test");
                    for (N2oObject.Parameter parameter : Arrays.asList(constraint.getInParameters()[0], constraint.getOutParameters()[0])) {
                        assert parameter.getId().equals("test");
                        assert parameter.getDomain().equals("boolean");
                        assert parameter.getMapping().equals("test");
                        assert parameter.getDefaultValue().equals("test");
                    }
                    assert constraint.getResult().equals("test");
                    N2oObject.Operation operation = object.getOperations()[0];
                    assert operation.getId().equals("create");
                    assert operation.getFormSubmitLabel().equals("test");
                    assert operation.getName().equals("test");
                    assert operation.getConfirmationText().equals("test");
                    assert operation.getBulkConfirmationText().equals("{$bulk.count}");
                    assert object.getOperations()[1].getBulkConfirmationText() == null;
                    assert operation.getFailText().equals("test");
                    assert operation.getSuccessText().equals("test");
                    assert operation.getDescription().equals("testDescription");
                    assert operation.getNote().equals("test");
                    N2oSqlQuery query = (N2oSqlQuery) operation.getInvocation();
                    assert query.getQuery().equals("test");
                    for (N2oObject.Parameter parameter : Arrays.asList(operation.getInParameters()[0], operation.getOutParameters()[0])) {
                        assert parameter.getId().equals("test");
                        assert parameter.getDomain().equals("boolean");
                        assert parameter.getMapping().equals("test");
                        assert parameter.getDefaultValue().equals("test");
                    }
                    N2oObject.Operation.Validations validation = operation.getValidations();
                    assert validation.getActivate().name().equals("all");
                    assert validation.getRefValidations()[0].getRefId().equals("test");
                });
    }

    @Test
    public void testObjectV1XmlIO() {
        assert tester.check("net/n2oapp/framework/config/io/object/testObjectReader.object.xml",
                (N2oObject object) -> {
                    assert object.getName().equals("test");
                    assert object.getParent().equals("test");

                    assert object.getObjectFields().length == 2;
                    ObjectScalarField field = (ObjectScalarField) object.getObjectFields()[0];
                    assert field.getDomain().equals("string");
                    assert field.getName().equals("test");
                    assert field.getId().equals("id");
                    assert field.getMapping().equals("testMapping");
                    assert field.getRequired().equals(true);

                    ObjectReferenceField field1 = (ObjectReferenceField) object.getObjectFields()[1];
                    assert field1.getReferenceObjectId().equals("id");
                    assert field1.getId().equals("id");
                    assert field1.getName().equals("test");
                    assert field1.getMapping().equals("testMapping");

                    N2oValidationCondition condition = (N2oValidationCondition) object.getN2oValidations()[0];
                    assert condition.getId().equals("id");
                    assert condition.getLevel().name().equals("error");
                    assert condition.getServerMoment().name().equals("afterFailOperation");
                    assert condition.getMessage().equals("test");
                    assert condition.getExpression().equals("test");
                    assert condition.getExpressionOn().equals("id");

                    N2oConstraint constraint = (N2oConstraint) object.getN2oValidations()[1];
                    assert constraint.getId().equals("id");
                    assert constraint.getLevel().name().equals("warning");
                    assert constraint.getServerMoment().name().equals("afterSuccessOperation");
                    assert constraint.getMessage().equals("test");
                    N2oSqlQuery sqlQuery = (N2oSqlQuery) constraint.getN2oInvocation();
                    assert sqlQuery.getQuery().equals("test");
                    for (N2oObject.Parameter parameter : Arrays.asList(constraint.getInParameters()[0], constraint.getOutParameters()[0])) {
                        assert parameter.getId().equals("test");
                        assert parameter.getDomain().equals("boolean");
                        assert parameter.getMapping().equals("test");
                        assert parameter.getDefaultValue().equals("test");
                    }
                    assert constraint.getResult().equals("test");

                    N2oObject.Operation operation = object.getOperations()[0];
                    assert operation.getId().equals("create");
                    assert operation.getFormSubmitLabel().equals("test");
                    assert operation.getName().equals("test");
                    assert operation.getConfirmationText().equals("test");
                    assert operation.getBulkConfirmationText().equals("{$bulk.count}");
                    assert object.getOperations()[1].getBulkConfirmationText() == null;
                    assert operation.getFailText().equals("test");
                    assert operation.getSuccessText().equals("test");
                    assert operation.getNote().equals("test");
                    N2oSqlQuery query = (N2oSqlQuery) operation.getInvocation();
                    assert query.getQuery().equals("test");
                    for (N2oObject.Parameter parameter : Arrays.asList(operation.getInParameters()[0], operation.getOutParameters()[0])) {
                        assert parameter.getId().equals("test");
                        assert parameter.getDomain().equals("boolean");
                        assert parameter.getMapping().equals("test");
                        assert parameter.getDefaultValue().equals("test");
                    }
                    N2oObject.Operation.Validations validation = operation.getValidations();
                    assert validation.getActivate().name().equals("all");
                    assert validation.getRefValidations()[0].getRefId().equals("test");
                });
    }
}
