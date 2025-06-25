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
import org.apache.commons.lang3.ArrayUtils;

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
    @JsonProperty("on")
    private String[] expressionOn;

    public ConditionValidation(ConditionValidation validation) {
        super(validation);
        this.expression = validation.getExpression();
        this.expressionOn = validation.getExpressionOn();
    }

    public void setExpression(String expression) {
        if (expression != null)
            this.expression = expression.replaceAll("\n|\r", "").trim();
    }

    private Set<String> getExpressionsOn() {
        if (ArrayUtils.isEmpty(expressionOn))
            return Collections.emptySet();
        return Arrays.stream(expressionOn).collect(Collectors.toSet());
    }

    private DataSet getCopiedDataSet(DataSet dataSet, DomainProcessor domainProcessor) {
        DataSet copiedDataSet = new DataSet(dataSet);
        getExpressionsOn().forEach(key -> {
            Object value = dataSet.get(key);
            copiedDataSet.put(key, (value instanceof Date) ?
                    domainProcessor.serialize(value) : value);
        });
        return copiedDataSet;
    }

    @Override
    public void validate(DataSet dataSet, InvocationProcessor serviceProvider, ValidationFailureCallback callback,
                         DomainProcessor domainProcessor) {
        try {
            DataSet copiedDataSet = getCopiedDataSet(dataSet, domainProcessor);
            if (!(boolean) ScriptProcessor.eval(getExpression(), copiedDataSet))
                callback.onFail(StringUtils.resolveLinks(getMessage(), copiedDataSet));
        } catch (ScriptException e) {
            if (e.getLocalizedMessage().contains("ReferenceError:") && e.getLocalizedMessage().contains("is not defined in <eval>")) {
                String message = e.getLocalizedMessage().split("\"")[1];
                throw new RuntimeException(String.format(
                        "Ошибка серверной валидации. Поле \"%s\"  используется в выражении, но при этом отсутствует в атрибуте 'on' \"<condition>\" валидации.",
                        message));
            } else
                throw new IllegalStateException(e);
        }
    }

    @Override
    public String getType() {
        return "condition";
    }
}
