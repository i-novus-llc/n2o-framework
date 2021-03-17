package net.n2oapp.framework.engine.validation.engine;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.data.validation.ConditionValidation;
import net.n2oapp.framework.api.data.validation.ConstraintValidation;
import net.n2oapp.framework.api.data.validation.MandatoryValidation;
import net.n2oapp.framework.api.data.validation.Validation;
import net.n2oapp.framework.api.exception.N2oValidationException;
import net.n2oapp.framework.api.exception.SeverityType;
import net.n2oapp.framework.api.metadata.global.dao.object.AbstractParameter;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectSimpleField;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;
import net.n2oapp.framework.api.metadata.local.CompiledObject;
import net.n2oapp.framework.api.metadata.meta.control.InputText;
import net.n2oapp.framework.api.metadata.meta.control.StandardField;
import net.n2oapp.framework.engine.data.N2oInvocationProcessor;
import net.n2oapp.framework.engine.validation.engine.info.ObjectValidationInfo;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static net.n2oapp.framework.api.exception.SeverityType.*;
import static net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation.ServerMoment.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ValidationProcessorTest {

    /**
     * Проверка сортировки валидаций. Заданы все типы SeverityType, Field/Widget, тип валидации
     * <p>
     * Порядок сортировки:
     * Danger - Field - Mandatory
     * Danger - Field - Condition
     * Danger - Field - Constraint
     * Danger - Widget - Mandatory
     * ...
     * Success - Widget - Constraint
     */
    @Test
    public void testSorting() {
        ObjectSimpleField inParam = new ObjectSimpleField();
        inParam.setId("id");
        inParam.setRequired(true);
        List<AbstractParameter> inParamList = Arrays.asList(inParam);

        Validation dfm = mandatoryValidation("id", danger, beforeOperation);
        Validation dfCond = conditionValidation(null, "id", danger, beforeOperation, "id != null");
        Validation dfConstr = constraintValidation("id", danger, beforeOperation, inParamList);

        Validation dwm = mandatoryValidation(null, danger, beforeOperation);
        Validation dwCond = conditionValidation(null, null, danger, beforeOperation, "id != null");
        Validation dwConstr = constraintValidation(null, danger, beforeOperation, inParamList);

        Validation wfm = mandatoryValidation("id", warning, beforeOperation);
        Validation wfCond = conditionValidation(null, "id", warning, beforeOperation, "id != null");
        Validation wfConstr = constraintValidation("id", warning, beforeOperation, inParamList);

        Validation wwm = mandatoryValidation(null, warning, beforeOperation);
        Validation wwCond = conditionValidation(null, null, warning, beforeOperation, "id != null");
        Validation wwConstr = constraintValidation(null, warning, beforeOperation, inParamList);

        Validation ifm = mandatoryValidation("id", info, beforeOperation);
        Validation ifCond = conditionValidation(null, "id", info, beforeOperation, "id != null");
        Validation ifConstr = constraintValidation("id", info, beforeOperation, inParamList);

        Validation iwm = mandatoryValidation(null, info, beforeOperation);
        Validation iwCond = conditionValidation(null, null, info, beforeOperation, "id != null");
        Validation iwConstr = constraintValidation(null, info, beforeOperation, inParamList);


        Validation sfm = mandatoryValidation("id", success, beforeOperation);
        Validation sfCond = conditionValidation(null, "id", success, beforeOperation, "id != null");
        Validation sfConstr = constraintValidation("id", success, beforeOperation, inParamList);

        Validation swm = mandatoryValidation(null, success, beforeOperation);
        Validation swCond = conditionValidation(null, null, success, beforeOperation, "id != null");
        Validation swConstr = constraintValidation(null, success, beforeOperation, inParamList);


        List<Validation> validations = Arrays.asList(
                dfm, dfCond, dfConstr, dwm, dwCond, dwConstr,
                wfm, wfCond, wfConstr, wwm, wwCond, wwConstr,
                ifm, ifCond, ifConstr, iwm, iwCond, iwConstr,
                sfm, sfCond, sfConstr, swm, swCond, swConstr
        );

        Collections.shuffle(validations);

        ValidationUtil.sort(validations);

        assertThat(validations.get(0).getSeverity(), is(SeverityType.danger));
        assertThat(validations.get(0).getType(), is("required"));
        assertThat(validations.get(0).isForField(), is(true));
        assertThat(validations.get(1).getSeverity(), is(SeverityType.danger));
        assertThat(validations.get(1).getType(), is("condition"));
        assertThat(validations.get(1).isForField(), is(true));
        assertThat(validations.get(2).getSeverity(), is(SeverityType.danger));
        assertThat(validations.get(2).getType(), is("constraint"));
        assertThat(validations.get(2).isForField(), is(true));

        assertThat(validations.get(3).getSeverity(), is(SeverityType.danger));
        assertThat(validations.get(3).getType(), is("required"));
        assertThat(validations.get(3).isForField(), is(false));
        assertThat(validations.get(4).getSeverity(), is(SeverityType.danger));
        assertThat(validations.get(4).getType(), is("condition"));
        assertThat(validations.get(4).isForField(), is(false));
        assertThat(validations.get(5).getSeverity(), is(SeverityType.danger));
        assertThat(validations.get(5).getType(), is("constraint"));
        assertThat(validations.get(5).isForField(), is(false));

        assertThat(validations.get(6).getSeverity(), is(SeverityType.warning));
        assertThat(validations.get(6).getType(), is("required"));
        assertThat(validations.get(6).isForField(), is(true));
        assertThat(validations.get(7).getSeverity(), is(SeverityType.warning));
        assertThat(validations.get(7).getType(), is("condition"));
        assertThat(validations.get(7).isForField(), is(true));
        assertThat(validations.get(8).getSeverity(), is(SeverityType.warning));
        assertThat(validations.get(8).getType(), is("constraint"));
        assertThat(validations.get(8).isForField(), is(true));

        assertThat(validations.get(9).getSeverity(), is(SeverityType.warning));
        assertThat(validations.get(9).getType(), is("required"));
        assertThat(validations.get(9).isForField(), is(false));
        assertThat(validations.get(10).getSeverity(), is(SeverityType.warning));
        assertThat(validations.get(10).getType(), is("condition"));
        assertThat(validations.get(10).isForField(), is(false));
        assertThat(validations.get(11).getSeverity(), is(SeverityType.warning));
        assertThat(validations.get(11).getType(), is("constraint"));
        assertThat(validations.get(11).isForField(), is(false));

        assertThat(validations.get(12).getSeverity(), is(SeverityType.info));
        assertThat(validations.get(12).getType(), is("required"));
        assertThat(validations.get(12).isForField(), is(true));
        assertThat(validations.get(13).getSeverity(), is(SeverityType.info));
        assertThat(validations.get(13).getType(), is("condition"));
        assertThat(validations.get(13).isForField(), is(true));
        assertThat(validations.get(14).getSeverity(), is(SeverityType.info));
        assertThat(validations.get(14).getType(), is("constraint"));
        assertThat(validations.get(14).isForField(), is(true));

        assertThat(validations.get(15).getSeverity(), is(SeverityType.info));
        assertThat(validations.get(15).getType(), is("required"));
        assertThat(validations.get(15).isForField(), is(false));
        assertThat(validations.get(16).getSeverity(), is(SeverityType.info));
        assertThat(validations.get(16).getType(), is("condition"));
        assertThat(validations.get(16).isForField(), is(false));
        assertThat(validations.get(17).getSeverity(), is(SeverityType.info));
        assertThat(validations.get(17).getType(), is("constraint"));
        assertThat(validations.get(17).isForField(), is(false));

        assertThat(validations.get(18).getSeverity(), is(SeverityType.success));
        assertThat(validations.get(18).getType(), is("required"));
        assertThat(validations.get(18).isForField(), is(true));
        assertThat(validations.get(19).getSeverity(), is(SeverityType.success));
        assertThat(validations.get(19).getType(), is("condition"));
        assertThat(validations.get(19).isForField(), is(true));
        assertThat(validations.get(20).getSeverity(), is(SeverityType.success));
        assertThat(validations.get(20).getType(), is("constraint"));
        assertThat(validations.get(20).isForField(), is(true));

        assertThat(validations.get(21).getSeverity(), is(SeverityType.success));
        assertThat(validations.get(21).getType(), is("required"));
        assertThat(validations.get(21).isForField(), is(false));
        assertThat(validations.get(22).getSeverity(), is(SeverityType.success));
        assertThat(validations.get(22).getType(), is("condition"));
        assertThat(validations.get(22).isForField(), is(false));
        assertThat(validations.get(23).getSeverity(), is(SeverityType.success));
        assertThat(validations.get(23).getType(), is("constraint"));
        assertThat(validations.get(23).isForField(), is(false));


    }

    /**
     * Проверяется, что на одно поле выполняется не больше одной валидации
     */
    @Test
    public void testFieldValidationLimit() {
        ValidationProcessor processor = new ValidationProcessor(null);

        DataSet dataSet = new DataSet();
        dataSet.put("id", null);

        StandardField f = new StandardField();
        f.setControl(new InputText());
        f.getControl().setId("id");
        Validation mandatory = mandatoryValidation("id", SeverityType.warning, N2oValidation.ServerMoment.beforeOperation);

        ((MandatoryValidation) mandatory).setField(f);
        Validation condition = conditionValidation(null, "id", SeverityType.warning, N2oValidation.ServerMoment.beforeOperation, "id !== null");

        CompiledObject.Operation operation =new CompiledObject.Operation();
        operation.setValidationList(Arrays.asList(mandatory, condition));
        ObjectValidationInfo info = new ObjectValidationInfo(null, operation.getValidationList(), dataSet, null, null);

        List<FailInfo> fails = processor.validate(info, beforeOperation);
        assertThat(fails.size(), is(1));
        assertThat(fails.get(0).getValidationClass(), is("MandatoryValidation"));
    }

    /**
     * Проверяется, что для виджета выполняется не больше одной валидации
     */
    @Test
    public void testWidgetValidationLimit() {
        ValidationProcessor processor = new ValidationProcessor(null);

        DataSet dataSet = new DataSet();
        dataSet.put("id", null);
        dataSet.put("name", null);

        Validation condition1 = conditionValidation(null, "id", SeverityType.warning, N2oValidation.ServerMoment.beforeOperation, "id !== null");
        Validation condition2 = conditionValidation(null, "id", SeverityType.warning, N2oValidation.ServerMoment.beforeOperation, "name !== null");

        CompiledObject.Operation operation =new CompiledObject.Operation();

        operation.setValidationList(Arrays.asList(condition1, condition2));
        ObjectValidationInfo info = new ObjectValidationInfo(null, operation.getValidationList(), dataSet, null, null);

        List<FailInfo> fails = processor.validate(info, beforeOperation);
        assertThat(fails.size(), is(1));
    }


    /**
     * Проверяется, что валидация c одним и тем же id выполняется не больше одного раза
     */
    @Test
    public void testValidationLimit() {
        ValidationProcessor processor = new ValidationProcessor(null);

        DataSet dataSet = new DataSet();
        dataSet.put("id", null);


        StandardField f1 = new StandardField();
        f1.setControl(new InputText());
        f1.getControl().setId("id");
        Validation mandatory1 = mandatoryValidation("id", SeverityType.warning, N2oValidation.ServerMoment.beforeOperation);
        mandatory1.setFieldId("id");
        ((MandatoryValidation) mandatory1).setField(f1);

        StandardField f2 = new StandardField();
        f2.setControl(new InputText());
        f2.getControl().setId("widgetId");
        Validation mandatory2 = mandatoryValidation("id", SeverityType.warning, N2oValidation.ServerMoment.beforeOperation);
        mandatory2.setFieldId("widgetId");
        ((MandatoryValidation) mandatory2).setField(f2);

        CompiledObject.Operation operation =new CompiledObject.Operation();

        operation.setValidationList(Arrays.asList(mandatory1, mandatory2));
        ObjectValidationInfo info = new ObjectValidationInfo(null, operation.getValidationList(), dataSet, null, null);

        List<FailInfo> fails = processor.validate(info, beforeOperation);
        assertThat(fails.size(), is(1));
    }

    /**
     * danger валидации должны выбрасывать N2oValidationException
     */
    @Test
    public void testDangerValidations() {
        ValidationProcessor processor = new ValidationProcessor(null);

        MandatoryValidation mandatory1 = mandatoryValidation("id", SeverityType.danger, N2oValidation.ServerMoment.beforeOperation);
        MandatoryValidation mandatory2 = mandatoryValidation("name", SeverityType.danger, N2oValidation.ServerMoment.beforeOperation);

        StandardField f1 = new StandardField();
        f1.setControl(new InputText());
        f1.getControl().setId("id");
        mandatory1.setField(f1);
        mandatory1.setFieldId("id");

        StandardField f2 = new StandardField();
        f2.setControl(new InputText());
        f2.getControl().setId("name");
        mandatory2.setField(f2);
        mandatory2.setFieldId("name");

        DataSet dataSet = new DataSet();
        dataSet.put("id", null);
        dataSet.put("name", null);

        CompiledObject.Operation operation =new CompiledObject.Operation();
        operation.setValidationList(Arrays.asList(mandatory1, mandatory2));
        ObjectValidationInfo info = new ObjectValidationInfo(null, operation.getValidationList(), dataSet, null, null);

        try {
            processor.validate(info, beforeOperation);
            assert false;
        } catch (N2oValidationException e) {
            assertThat(e.getMessages().size(), is(2));

            assertThat(e.getMessages().get(0).getFieldId(), is("id"));
            assertThat(e.getMessages().get(0).getMessage(), is("Field id required"));
            assertThat(e.getMessages().get(0).getValidationId(), is("idRequired"));

            assertThat(e.getMessages().get(1).getFieldId(), is("name"));
            assertThat(e.getMessages().get(1).getMessage(), is("Field name required"));
            assertThat(e.getMessages().get(1).getValidationId(), is("nameRequired"));

        }

    }

    @Test
    public void testEnabled() {
        ValidationProcessor processor = new ValidationProcessor(null);

        ConditionValidation condition1 = conditionValidation("checkEmailContainsAt", null,
                SeverityType.warning, N2oValidation.ServerMoment.beforeOperation, "email.indexOf('@') > -1");

        CompiledObject.Operation operation =new CompiledObject.Operation();
        operation.setValidationList(Arrays.asList(condition1));
        DataSet dataSet = new DataSet();
        dataSet.put("email", "person_mail.com");

        ObjectValidationInfo info = new ObjectValidationInfo(null, operation.getValidationList(), dataSet, null, null);

        //enabled == null - валидация должна сработать
        List<FailInfo> fails = processor.validate(info, beforeOperation);
        assertThat(fails.size(), is(1));

        //enabled == true - валидация снова должна сработать
        condition1.setEnabled(true);
        fails = processor.validate(info, beforeOperation);
        assertThat(fails.size(), is(1));

        //enabled == false - валидация НЕ должна сработать
        condition1.setEnabled(false);
        fails = processor.validate(info, beforeOperation);
        assertThat(fails.size(), is(0));

        //enabled == null - валидация должна сработать,
        //но не сработает, т.к. не выполнится enablingCondition
        condition1.setEnabled(null);
        condition1.setEnablingConditions(Arrays.asList("email != person_mail.com"));
        fails = processor.validate(info, beforeOperation);
        assertThat(fails.size(), is(0));

        dataSet.put("email", null);
        fails = processor.validate(info, beforeOperation);
        assertThat(fails.size(), is(0));
    }

    @Test
    public void testServerMoment() {
        ValidationProcessor processor = new ValidationProcessor(null);

        ConditionValidation condition1 = conditionValidation("checkEmailContainsAt1", "email",
                SeverityType.warning, N2oValidation.ServerMoment.beforeOperation, "email.indexOf('@') > -1");
        ConditionValidation condition2 = conditionValidation("checkEmailContainsAt2", "email",
                SeverityType.warning, N2oValidation.ServerMoment.afterSuccessOperation, "email.indexOf('@') > -1");
        ConditionValidation condition3 = conditionValidation("checkEmailContainsAt3", "email",
                SeverityType.warning, N2oValidation.ServerMoment.afterFailOperation, "email.indexOf('@') > -1");

        CompiledObject.Operation operation =new CompiledObject.Operation();
        operation.setValidationList(Arrays.asList(condition1, condition2, condition3));
        DataSet dataSet = new DataSet();
        dataSet.put("email", "person_mail.com");

        ObjectValidationInfo info = new ObjectValidationInfo(null, operation.getValidationList(), dataSet, null, null);

        List<FailInfo> fails = processor.validate(info, beforeOperation);
        assertThat(fails.size(), is(1));
        assertThat(fails.get(0).getValidationId(), is("checkEmailContainsAt1"));

        fails = processor.validate(info, afterSuccessOperation);
        assertThat(fails.size(), is(1));
        assertThat(fails.get(0).getValidationId(), is("checkEmailContainsAt2"));

        fails = processor.validate(info, afterFailOperation);
        assertThat(fails.size(), is(1));
        assertThat(fails.get(0).getValidationId(), is("checkEmailContainsAt3"));
    }

    @Test
    public void testMandatory() {
        ValidationProcessor processor = new ValidationProcessor(null);
        DataSet dataSet = new DataSet();
        dataSet.put("id", null);
        dataSet.put("oneMoreId", "");
        dataSet.put("name", "notNullName");
        dataSet.put("number", 1);

        StandardField f = new StandardField();
        f.setControl(new InputText());
        f.getControl().setId("id");
        Validation mandatory1 = mandatoryValidation("id", SeverityType.warning, N2oValidation.ServerMoment.beforeOperation);
        ((MandatoryValidation) mandatory1).setField(f);

        StandardField f2 = new StandardField();
        f2.setControl(new InputText());
        f2.getControl().setId("oneMoreId");
        Validation mandatory2 = mandatoryValidation("oneMoreId", SeverityType.warning, N2oValidation.ServerMoment.beforeOperation);
        ((MandatoryValidation) mandatory2).setField(f2);

        StandardField f3 = new StandardField();
        f3.setControl(new InputText());
        f3.getControl().setId("name");
        Validation mandatory3 = mandatoryValidation("name", SeverityType.warning, N2oValidation.ServerMoment.beforeOperation);
        ((MandatoryValidation) mandatory3).setField(f3);

        StandardField f4 = new StandardField();
        f4.setControl(new InputText());
        f4.getControl().setId("number");
        Validation mandatory4 = mandatoryValidation("number", SeverityType.warning, N2oValidation.ServerMoment.beforeOperation);
        ((MandatoryValidation) mandatory4).setField(f4);

        CompiledObject.Operation operation =new CompiledObject.Operation();

        operation.setValidationList(Arrays.asList(mandatory1, mandatory2, mandatory3, mandatory4));
        ObjectValidationInfo info = new ObjectValidationInfo(null, operation.getValidationList(), dataSet, null, null);

        List<FailInfo> fails = processor.validate(info, beforeOperation);
        assertThat(fails.size(), is(2));
        assertThat(fails.get(0).getFieldId(), is("id"));
        assertThat(fails.get(1).getFieldId(), is("oneMoreId"));
    }

    @Test
    public void testCondition() {
        ValidationProcessor processor = new ValidationProcessor(null);
        DataSet dataSet = new DataSet();
        dataSet.put("id", null);
        dataSet.put("oneMoreId", null);
        dataSet.put("name", "notNullName");
        dataSet.put("date", new Date(0));

        Validation condition1 = conditionValidation("id", "id",
                SeverityType.warning, N2oValidation.ServerMoment.beforeOperation, "id !== null");
        Validation condition2 = conditionValidation("oneMoreId", "oneMoreId",
                SeverityType.warning, N2oValidation.ServerMoment.beforeOperation, "oneMoreId === null");
        Validation condition3 = conditionValidation("date", "date",
                SeverityType.warning, N2oValidation.ServerMoment.beforeOperation, "oneMoreId === null && date === '"
                        + new SimpleDateFormat(DomainProcessor.JAVA_DATE_FORMAT).format(new Date(0)) + "'");
        ((ConditionValidation) condition3).setExpressionOn("date,oneMoreId");

        CompiledObject.Operation operation =new CompiledObject.Operation();

        operation.setValidationList(Arrays.asList(condition1, condition2, condition3));
        ObjectValidationInfo info = new ObjectValidationInfo(null, operation.getValidationList(), dataSet, null, null);

        List<FailInfo> fails = processor.validate(info, beforeOperation);
        assertThat(fails.size(), is(1));
        assertThat(fails.get(0).getFieldId(), is("id"));
    }

    @Test
    public void testConstraint() {
        ObjectSimpleField inParam = new ObjectSimpleField();
        inParam.setName("id");
        inParam.setId("id");
        inParam.setDomain("integer");
        inParam.setRequired(true);
        List<AbstractParameter> inParamList = Arrays.asList(inParam);

        N2oInvocationProcessor invocationProcessor = mock(N2oInvocationProcessor.class);
        ValidationProcessor processor = new ValidationProcessor(invocationProcessor);

        DataSet dataSet = new DataSet();
        dataSet.put("id", 1);
        DataSet mockedDataSet = new DataSet();
        mockedDataSet.put("validation", false);
        when(invocationProcessor.invoke(null, dataSet, inParamList, null)).thenReturn(mockedDataSet);
        dataSet.put("id", "1");

        ConstraintValidation constraint1 = constraintValidation("id", SeverityType.warning,
                N2oValidation.ServerMoment.beforeOperation, inParamList);

        CompiledObject.Operation operation =new CompiledObject.Operation();
        operation.setValidationList(Arrays.asList(constraint1));
        ObjectValidationInfo info = new ObjectValidationInfo(null, operation.getValidationList(), dataSet, null, null);


        //invocation parameter с атрибутом required=true, датасет содержит такой ключ, поэтому валидация должна сработать
        List<FailInfo> fails = processor.validate(info, beforeOperation);
        assertThat(fails.size(), is(1));

        //Проверяем, что если убрать из датасета запись, то эта валидация не сработает
        dataSet.remove("id");
        fails = processor.validate(info, beforeOperation);
        assertThat(fails.size(), is(0));
    }

    private MandatoryValidation mandatoryValidation(String fieldId, SeverityType severity, N2oValidation.ServerMoment moment) {
        MandatoryValidation mandatory = new MandatoryValidation();
        mandatory.setId(fieldId + "Required");
        mandatory.setMoment(moment);
        mandatory.setSeverity(severity);
        mandatory.setFieldId(fieldId);
        mandatory.setMessage("Field " + fieldId + " required");
        return mandatory;
    }

    private ConditionValidation conditionValidation(String id, String fieldId, SeverityType severity,
                                                    N2oValidation.ServerMoment moment, String expression) {
        ConditionValidation condition = new ConditionValidation();
        condition.setId(id == null ? fieldId + "Required" : id);
        condition.setMoment(moment);
        condition.setSeverity(severity);
        condition.setFieldId(fieldId);
        condition.setExpression(expression);
        condition.setMessage("Field " + fieldId + " validated");
        return condition;
    }

    private ConstraintValidation constraintValidation(String fieldId, SeverityType severity,
                                                      N2oValidation.ServerMoment moment, List<AbstractParameter> inParams) {
        ConstraintValidation constraint = new ConstraintValidation();
        constraint.setId(fieldId + "Constraint");
        constraint.setMoment(moment);
        constraint.setSeverity(severity);
        constraint.setFieldId(fieldId);
        constraint.setInParametersList(inParams);
        constraint.setMessage("Field " + fieldId + " validated");
        return constraint;
    }
}
