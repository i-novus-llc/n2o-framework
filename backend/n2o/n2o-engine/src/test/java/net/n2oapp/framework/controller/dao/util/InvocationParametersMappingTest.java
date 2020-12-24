package net.n2oapp.framework.controller.dao.util;

import net.n2oapp.framework.api.criteria.N2oPreparedCriteria;
import net.n2oapp.framework.api.data.DomainProcessor;
import net.n2oapp.framework.api.metadata.dataprovider.N2oJavaDataProvider;
import net.n2oapp.framework.api.metadata.global.dao.invocation.model.Argument;
import net.n2oapp.framework.api.metadata.global.dao.object.InvocationParameter;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.engine.util.InvocationParametersMapping;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

/**
 * Тестирование преобразования аргументов при вызове действий
 */
public class InvocationParametersMappingTest {

    @Test
    public void testExtractMapping() {
        Map<String, InvocationParameter> parameters = new LinkedHashMap<>();
        parameters.put("a", new N2oObject.Parameter("a", "x"));
        Map<String, String> mapping = InvocationParametersMapping.extractMapping(parameters.values());
        assertThat(mapping.get("a"), is("x"));

        parameters = new LinkedHashMap<>();
        parameters.put("a", new N2oObject.Parameter("a", null));
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
}
