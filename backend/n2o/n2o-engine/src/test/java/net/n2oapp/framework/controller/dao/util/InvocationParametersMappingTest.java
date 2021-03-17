package net.n2oapp.framework.controller.dao.util;

import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.api.criteria.N2oPreparedCriteria;
import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.metadata.dataprovider.N2oJavaDataProvider;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.Argument;
import net.n2oapp.framework.api.metadata.global.dao.object.AbstractParameter;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectSimpleField;
import net.n2oapp.framework.engine.util.InvocationParametersMapping;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Тестирование преобразования аргументов при вызове действий
 */
public class InvocationParametersMappingTest {

    @Test
    public void testExtractMapping() {
        Map<String, AbstractParameter> parameters = new LinkedHashMap<>();
        ObjectSimpleField param = new ObjectSimpleField();
        param.setId("a");
        param.setMapping("x");
        parameters.put("a", param);
        Map<String, String> mapping = InvocationParametersMapping.extractMapping(parameters.values());
        assertThat(mapping.get("a"), is("x"));

        parameters = new LinkedHashMap<>();
        param = new ObjectSimpleField();
        param.setId("a");
        parameters.put("a", param);
        mapping = InvocationParametersMapping.extractMapping(parameters.values());
        assertThat(mapping.containsKey("a"), is(true));
        assertThat(mapping.get("a"), is(nullValue()));
    }

    @Test
    public void testArgumentsDefaultValue() {
        N2oJavaDataProvider javaDataProvider = new N2oJavaDataProvider();
        Argument arg1 = new Argument();
        arg1.setName("a");
        arg1.setType(Argument.Type.PRIMITIVE);
        arg1.setDefaultValue("true");
        Argument arg2 = new Argument();
        arg2.setType(Argument.Type.PRIMITIVE);
        arg2.setName("b");
        Argument arg3 = new Argument();
        arg3.setName("c");
        arg3.setType(Argument.Type.PRIMITIVE);
        arg3.setDefaultValue("123");
        javaDataProvider.setArguments(new Argument[]{arg1, arg2, arg3});

        Object[] objects = InvocationParametersMapping.prepareArgsForQuery(javaDataProvider, null,
                new N2oPreparedCriteria(), null, new DomainProcessor());

        assertThat(objects[0], is(true));
        assertThat(objects[1], nullValue());
        assertThat(objects[2], is(123));
    }

    @Test
    public void normalizeValue() {
        ExpressionParser parser = new SpelExpressionParser();
        BeanFactory beanFactory = mock(BeanFactory.class);
        when(beanFactory.getBean("myBean")).thenReturn(new MyBean());
        String obj = "test";
        DataSet data = new DataSet();
        data.put("name", "John");
        assertThat(InvocationParametersMapping.normalizeValue(obj, "#this", data, parser, beanFactory), is("test"));
        assertThat(InvocationParametersMapping.normalizeValue(obj, "#data['name']", data, parser, beanFactory), is("John"));
        assertThat(InvocationParametersMapping.normalizeValue(obj, "@myBean.call()", data, parser, beanFactory), is("Doe"));
    }

    public static class MyBean {
        public String call() {
            return "Doe";
        }
    }
}
