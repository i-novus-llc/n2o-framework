package net.n2oapp.framework.api.data.validation;

import net.n2oapp.framework.api.exception.SeverityType;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.N2oInvocation;
import net.n2oapp.framework.api.metadata.global.dao.object.InvocationParameter;
import net.n2oapp.framework.api.metadata.global.dao.validation.N2oValidation;
import net.n2oapp.framework.api.metadata.local.util.CompileUtil;
import net.n2oapp.framework.api.script.ScriptProcessor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author operehod
 * @since 03.04.2015
 */
public class ValidationBuilder {

    public static ConditionValidation createConditionValidation(String id, String message, String expression, String expressionOn) {
        return createConditionValidation(id, SeverityType.danger, N2oValidation.ServerMoment.beforeOperation, message, expression, expressionOn);
    }

    public static ConditionValidation createConditionValidation(String id, String message, String expression) {
        return createConditionValidation(id, SeverityType.danger, N2oValidation.ServerMoment.beforeOperation, message, expression);
    }


    public static ConstraintValidation createConstraintValidation(
            String id, SeverityType severity, N2oValidation.ServerMoment moment, String message, N2oInvocation invocation,
            List<InvocationParameter> inParameterList, List<InvocationParameter> outParameterList) {
        ConstraintValidation constraint = new ConstraintValidation();
        buildBaseForValidation(id, severity, moment, message, constraint);
        Set<String> fields = inParameterList.stream().map(InvocationParameter::getId).collect(Collectors.toSet());
        constraint.setFields(fields);
        constraint.setInParameterList(inParameterList);
        constraint.setOutParametersList(outParameterList);
        constraint.setInvocation(invocation);
        return constraint;
    }


    public static ConditionValidation createConditionValidation(String id, SeverityType level, N2oValidation.ServerMoment moment, String message, String expression, String expressionOn) {
        ConditionValidation condition = createBaseForConditionValidation(id, level, moment, message, expression);
        Set<String> fields;
        fields = collectFields(expressionOn);
        expressionOn = ScriptProcessor.simplifyArrayLinks(expressionOn);
        condition.setExpressionOn(expressionOn);
        condition.setFields(fields);
        return condition;
    }

    public static ConditionValidation createConditionValidation(String id, SeverityType level, N2oValidation.ServerMoment moment, String message, String expression) {
        ConditionValidation condition = createBaseForConditionValidation(id, level, moment, message, expression);
        Set<String> fields;
        fields = ScriptProcessor.extractVars(expression);
        String expressionOn = CompileUtil.collectLinks(fields);
        expressionOn = ScriptProcessor.simplifyArrayLinks(expressionOn);
        condition.setExpressionOn(expressionOn);
        condition.setFields(fields);
        return condition;
    }

    private static ConditionValidation createBaseForConditionValidation(String id, SeverityType level, N2oValidation.ServerMoment moment, String message, String expression) {
        ConditionValidation condition = new ConditionValidation();
        buildBaseForValidation(id, level, moment, message, condition);
        condition.setExpression(expression);
        return condition;
    }

    private static void buildBaseForValidation(String id, SeverityType severity, N2oValidation.ServerMoment moment, String message, Validation validation) {
        validation.setId(id);
        validation.setSeverity(severity);
        validation.setMessage(message);
        validation.setMoment(moment);
    }


  /*  public static Validation compileObjectValidation(N2oValidation source, WidgetContainerContext compileContext) {

        Validation result = null;

        if (source instanceof N2oValidationCondition) {
            N2oValidationCondition condition = (N2oValidationCondition) source;
            if (condition.getExpressionOn() != null) {
                result = createConditionValidation(
                        condition.getId(), condition.getLevel(), condition.getMoment(), condition.getMessage(), condition.getExpression(), condition.getExpressionOn());
            } else {
                result = createConditionValidation(
                        condition.getId(), condition.getLevel(), condition.getMoment(), condition.getMessage(), condition.getExpression());
            }
        } else if (source instanceof N2oConstraint) {
            N2oConstraint constraint = (N2oConstraint) source;
            String actionId = N2oConstraint.PREFIX_FOR_ACTION_ID + constraint.getId();
            ConstraintValidation validation = createConstraintValidation(
                    constraint.getId(), constraint.getLevel(), constraint.getMoment(), constraint.getMessage(),
                    constraint.getN2oInvocation(), constraint.getInParameterList(), constraint.getOutParametersList()
            );
            validation.setPageId(compileContext.getPageId());
            validation.setContainerId(compileContext.getContainerId());
            validation.setAction(actionId);
            result = validation;
        }
        return result;
    }*/


    private static Set<String> collectFields(String expressionOn) {
        Set<String> fields;
        fields = new HashSet<>();
        for (String field : expressionOn.split(",")) {
            fields.add(field.trim());
        }
        return fields;
    }


}
