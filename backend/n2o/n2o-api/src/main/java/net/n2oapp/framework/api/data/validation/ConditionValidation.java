package net.n2oapp.framework.api.data.validation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.data.InvocationProcessor;
import net.n2oapp.framework.api.script.ScriptProcessor;

import javax.script.ScriptException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Клиентская модель валидации условия значений полей
 */
@Getter
@Setter
@NoArgsConstructor
public class ConditionValidation extends Validation {
    @JsonProperty
    private String expression;
    private String expressionOn;
    private DomainProcessor domainProcessor;

    public ConditionValidation(ConditionValidation validation) {
        super(validation);
        this.expression = validation.getExpression();
        this.expressionOn = validation.getExpressionOn();
        this.domainProcessor = new DomainProcessor();
    }

    public void setExpression(String expression) {
        if (expression != null)
            this.expression = expression.replaceAll("\n|\r", "").trim();
    }

    public void setDomainProcessor(DomainProcessor domainProcessor) {
        this.domainProcessor = domainProcessor;
    }

    private Set<String> getExpressionsOn() {
        if (expressionOn == null || expressionOn.length() <= 0)
            return Collections.emptySet();
        return Arrays.stream(expressionOn.split(","))
                .map(String::trim)
                .collect(Collectors.toSet());
    }

    private DataSet getCopiedDataSet (DataSet dataSet) {
        DataSet copiedDataSet = new DataSet(dataSet);
        getExpressionsOn().forEach(key -> {
            Object value = dataSet.get(key);
            copiedDataSet.put(key, (value instanceof Date) ?
                domainProcessor.serialize(value) : value);
        });
        return copiedDataSet;
    }

    @Override
    public void validate(DataSet dataSet, InvocationProcessor serviceProvider, ValidationFailureCallback callback) {
        try {
            DataSet copiedDataSet = getCopiedDataSet(dataSet);
            if (!(boolean) ScriptProcessor.eval(getExpression(), copiedDataSet))
                callback.onFail(StringUtils.resolveLinks(getMessage(), copiedDataSet));
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getType() {
        return "condition";
    }
}
