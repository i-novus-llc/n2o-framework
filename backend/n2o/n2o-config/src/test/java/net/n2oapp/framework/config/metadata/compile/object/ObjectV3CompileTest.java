package net.n2oapp.framework.config.metadata.compile.object;

import net.n2oapp.framework.api.data.validation.ConditionValidation;
import net.n2oapp.framework.api.data.validation.ConstraintValidation;
import net.n2oapp.framework.api.data.validation.MandatoryValidation;
import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.exception.SeverityType;
import net.n2oapp.framework.api.metadata.dataprovider.N2oSqlDataProvider;
import net.n2oapp.framework.api.metadata.global.dao.object.InvocationParameter;
import net.n2oapp.framework.api.metadata.global.dao.object.MapperType;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.api.metadata.global.dao.object.PluralityType;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.metadata.compile.context.ObjectContext;
import net.n2oapp.framework.config.metadata.pack.N2oDataProvidersPack;
import net.n2oapp.framework.config.metadata.pack.N2oInvocationV2ReadersPack;
import net.n2oapp.framework.config.metadata.pack.N2oObjectsPack;
import net.n2oapp.framework.config.selective.CompileInfo;
import net.n2oapp.framework.config.test.SourceCompileTestBase;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/*
 * Тест на компциляцию object-3.0
 */
public class ObjectV3CompileTest extends SourceCompileTestBase {
    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder builder) {
        super.configure(builder);
        builder.packs(new N2oInvocationV2ReadersPack(), new N2oObjectsPack(), new N2oDataProvidersPack())
                .sources(new CompileInfo("net/n2oapp/framework/config/metadata/compile/object/testObjectRefFields.object.xml"));
    }

    @Test
    public void testCompileActions() {
        CompiledObject object = compile("net/n2oapp/framework/config/metadata/compile/object/testObjectOperations.object.xml")
                .get(new ObjectContext("testObjectOperations"));
        CompiledObject.Operation op1 = object.getOperations().get("op1");
        assertThat(object.getOperations().size(), is(2));
        assertThat(op1.getId(), is("op1"));
        assertThat(op1.getName(), is("name"));
        assertThat(op1.getFormSubmitLabel(), is("test"));
        assertThat(op1.getDescription(), is("description"));
        assertThat(op1.getConfirmationText(), is("test"));
        assertThat(op1.getFailText(), is("test"));
        assertThat(op1.getSuccessText(), is("test"));
        assertThat(op1.getConfirm(), is(true));
        assertThat(op1.getInParamsSet().size(), is(1));
        assertThat(op1.getOutParamsSet().size(), is(1));
        assertThat(((N2oSqlDataProvider) op1.getInvocation()).getQuery(), is("select 1"));
        assertThat(object.getOperations().get("op2").getId(), is("op2"));

    }

    @Test
    public void testCompileOperationValidations() {
        CompiledObject object = compile("net/n2oapp/framework/config/metadata/compile/object/testObjectValidations.object.xml")
                .get(new ObjectContext("testObjectValidations"));
        assertThat(object.getOperations().get("nothing").getValidationList(), is(nullValue()));
        assertThat(object.getOperations().get("white").getValidationList().size(), is(1));
        assertThat(object.getOperations().get("white").getValidationList().get(0).getId(), is("v1"));
        assertThat(object.getOperations().get("white").getWhiteListValidationsMap().size(), is(1));
        assertThat(object.getOperations().get("black").getValidationList().size(), is(2));
        assertThat(object.getOperations().get("black").getValidationList().get(0).getId(), is("v2"));
        assertThat(object.getOperations().get("onlyInline").getValidationList().size(), is(1));
        assertThat(object.getOperations().get("onlyInline").getValidationList().get(0).getId(), is("v3"));
        assertThat(object.getOperations().get("inline").getValidationList().size(), is(2));
        assertThat(object.getOperations().get("inline").getWhiteListValidationsMap().size(), is(2));
        assertThat(object.getOperations().get("inline").getValidationList().get(0).getId(), is("v1"));
        assertThat(object.getOperations().get("inline").getValidationList().get(1).getId(), is("v4"));
        assertThat(object.getOperations().get("allObjectValidations").getValidationList().size(), is(3));
    }


    @Test
    public void testCompileValidations() {
        CompiledObject object = compile("net/n2oapp/framework/config/metadata/compile/object/testObjectValidations.object.xml")
                .get(new ObjectContext("testObjectValidations"));
        Validation v1 = object.getValidations().get(0);
        Validation v2 = object.getValidations().get(1);
        Validation v3 = object.getValidations().get(2);
        assertThat(v1.getId(), is("v1"));
        assertThat(v1.getSeverity(), is(SeverityType.danger));
        assertThat(((ConditionValidation) v1).getExpressionOn(), is("a,b"));
        assertThat(((ConditionValidation) v1).getExpression(), is("a == b"));
        assertThat(v2.getId(), is("v2"));
        assertThat(v2.getFieldId(), is("fieldId"));
        assertThat(v2.getMoment(), is(N2oValidation.ServerMoment.afterFailOperation));
        assertThat(v1.getMessage(), is("test"));
        assertThat(((N2oSqlDataProvider) ((ConstraintValidation) v2).getInvocation()).getQuery(), is("select 1"));
        assertThat(v2.getMessage(), is("test"));
        assertThat(((ConstraintValidation) v2).getInParametersList().size(), is(1));
        assertThat(((ConstraintValidation) v2).getOutParametersList().size(), is(2));
        assertThat(v3.getId(), is("v3"));
        assertThat(((MandatoryValidation) v3).getEnablingExpression(), is("a==b"));
        assertThat(((MandatoryValidation) v3).getExpressionOn(), is("test"));
    }

    @Test
    public void testCompileFields() {
        CompiledObject object = compile("net/n2oapp/framework/config/metadata/compile/object/testObjectFields.object.xml")
                .get(new ObjectContext("testObjectFields"));
        assertThat(object.getObjectFields().size(), is(7));
        assertThat(object.getObjectFieldsMap().size(), is(7));
        assertThat(object.getObjectReferenceFieldsMap().size(), is(5));
        N2oObject.Parameter operationInParam = object.getOperations().get("test").getInParametersMap().get("test");
        InvocationParameter validationInParam = ((ConstraintValidation) object.getValidationsMap().get("id1")).getInParametersList().get(0);
        assertThat(operationInParam.getId(), is("test"));
        assertThat(operationInParam.getDomain(), is("string"));
        assertThat(operationInParam.getMapping(), is("birth_date"));
        assertThat(operationInParam.getMapper(), is(MapperType.dataset));
        assertThat(operationInParam.getNormalize(), is("#{ T(java.lang.Math).random() * 100.0 }"));
        assertThat(operationInParam.getDefaultValue(), is("test"));

        assertThat(object.getOperations().get("test").getInParametersMap().get("test7").getNullIgnore(), is(true));

        InvocationParameter inParam = object.getOperations().get("test").getInParametersMap().get("test5").getChildParams()[0];
        assertThat(inParam.getId(), is("testField1"));
        assertThat(inParam.getMapping(), is("testField1"));
        assertThat(inParam.getDomain(), is("string"));
        assertThat(inParam.getNormalize(), is("TEST"));

        assertThat(object.getOperations().get("test").getInParametersMap().get("test5").getEntityClass(), is("java.lang.Object"));
        inParam = object.getOperations().get("test").getInParametersMap().get("test5").getChildParams()[1];
        assertThat(inParam.getId(), is("testField2"));
        assertThat(inParam.getMapping(), is("testField2"));
        assertThat(inParam.getDefaultValue(), is("defValue"));
        assertThat(inParam.getDomain(), is("boolean"));
        assertThat(inParam.getNormalize(), is("TEST"));

        assertThat(object.getOperations().get("test").getInParametersMap().get("test4").getEntityClass(), is("java.lang.Object"));
        inParam = object.getOperations().get("test").getInParametersMap().get("test4").getChildParams()[0];
        assertThat(inParam.getId(), is("home"));
        assertThat(inParam.getMapping(), is("homeMapping"));
        assertThat(inParam.getDefaultValue(), is("def"));
        assertThat(inParam.getDomain(), is("string"));
        assertThat(inParam.getNormalize(), is("TEST"));

        operationInParam = object.getOperations().get("test").getInParametersMap().get("test3");
        assertThat(operationInParam.getId(), is("test3"));
        assertThat(operationInParam.getNullIgnore(), is(true));

        assertThat(validationInParam.getId(), is("test"));
        assertThat(validationInParam.getDomain(), is("string"));
        assertThat(validationInParam.getMapping(), is("birth_date"));
        assertThat(validationInParam.getMapper(), is(MapperType.dataset));
        assertThat(validationInParam.getNormalize(), is("#{ T(java.lang.Math).random() * 100.0 }"));
        assertThat(validationInParam.getDefaultValue(), is("test"));

        assertThat(object.getObjectReferenceFieldsMap().get("test3").getObjectReferenceFields().size(), is(1));
        assertThat(object.getObjectReferenceFieldsMap().get("test3").getObjectReferenceFields().get(0).getId(), is("id"));

        assertThat(object.getObjectReferenceFieldsMap().get("test4").getObjectReferenceFields().size(), is(2));
        assertThat(object.getObjectReferenceFieldsMap().get("test4").getObjectReferenceFields().get(0).getId(), is("home"));
        assertThat(object.getObjectReferenceFieldsMap().get("test4").getObjectReferenceFields().get(1).getId(), is("work"));

        assertThat(object.getObjectReferenceFieldsMap().get("test5").getObjectReferenceFields().size(), is(2));
        assertThat(object.getObjectReferenceFieldsMap().get("test5").getPluralityType(), is(PluralityType.list));
        assertThat(object.getObjectReferenceFieldsMap().get("test5").getObjectReferenceFields().get(0).getId(), is("testField1"));
        assertThat(object.getObjectReferenceFieldsMap().get("test5").getObjectReferenceFields().get(1).getId(), is("testField2"));

        assertThat(object.getObjectReferenceFieldsMap().get("test6").getObjectReferenceFields().size(), is(2));
        assertThat(object.getObjectReferenceFieldsMap().get("test6").getPluralityType(), is(PluralityType.set));
        assertThat(object.getObjectReferenceFieldsMap().get("test6").getObjectReferenceFields().get(0).getId(), is("testField3"));
        assertThat(object.getObjectReferenceFieldsMap().get("test6").getObjectReferenceFields().get(1).getId(), is("testField4"));

        assertThat(object.getObjectReferenceFieldsMap().get("test7").getObjectReferenceFields().size(), is(1));
        assertThat(object.getObjectReferenceFieldsMap().get("test7").getObjectReferenceFields().get(0).getId(), is("id"));
    }
}