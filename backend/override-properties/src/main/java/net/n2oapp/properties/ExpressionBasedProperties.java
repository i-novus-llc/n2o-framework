package net.n2oapp.properties;

import org.springframework.core.env.StandardEnvironment;
import org.springframework.expression.*;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Properties;

/**
 * ExpressionBasedProperties evaluates properties values of expressions in SpEL
 * @author iryabov
 * @since 25.02.2016
 */
public class ExpressionBasedProperties extends OverrideProperties {

    private static final ExpressionParser EXPRESSION_PARSER = new SpelExpressionParser();
    private static final ParserContext PARSER_CONTEXT = new ParserContext() {
        public boolean isTemplate() {
            return true;
        }

        public String getExpressionPrefix() {
            return "#{";
        }

        public String getExpressionSuffix() {
            return "}";
        }
    };

    private EvaluationContext evaluationContext = new StandardEvaluationContext(new StandardEnvironment());

    public ExpressionBasedProperties(Properties baseProperties) {
        super(baseProperties);
    }

    public ExpressionBasedProperties() {
        super();
    }

    public void setEvaluationContext(EvaluationContext context) {
        this.evaluationContext = context;
    }


    @Override
    public String getProperty(String key) {
        String value = super.getProperty(key);
        return extractExpression(value);
    }


    @Override
    public synchronized Object get(Object key) {
        Object value = super.get(key);
        if (value instanceof String)
            value = extractExpression(value.toString());
        return value;
    }


    private String extractExpression(String value) {
        if ((value == null) || (value.isEmpty())) return value;
        int idxStart = value.indexOf("#{");
        if (idxStart == -1) return value;
        String value1 = null;
        try {
            value1 = EXPRESSION_PARSER.parseExpression(value, PARSER_CONTEXT).getValue(evaluationContext, String.class);
        } catch (EvaluationException e) {
            throw new IllegalStateException("Error evaluating expression " + value, e);
        } catch (ParseException e) {
            throw new IllegalStateException("Error parsing expression " + value, e);
        }
        return value1 == null ? "" : value1;
    }

}
