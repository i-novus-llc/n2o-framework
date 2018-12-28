package net.n2oapp.framework.controller.dao.util;

import org.junit.Test;
import net.n2oapp.framework.api.metadata.global.dao.object.InvocationParameter;
import net.n2oapp.framework.api.metadata.global.dao.object.N2oObject;
import net.n2oapp.framework.engine.util.InvocationParametersMapping;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * User: iryabov
 * Date: 31.10.13
 * Time: 12:17
 */
public class InvocationParametersMappingTest {

    @Test
    public void testExtractMapping() throws Exception {
        Map<String, InvocationParameter> parameters = new LinkedHashMap<>();
        parameters.put("a", new N2oObject.Parameter(N2oObject.Parameter.Type.in, "a", "x"));
        Map<String, String> mapping = InvocationParametersMapping.extractMapping(parameters.values());
        assert mapping.get("a").equals("x");

        parameters = new LinkedHashMap<>();
        parameters.put("a", new N2oObject.Parameter( N2oObject.Parameter.Type.in, "a", null));
        mapping = InvocationParametersMapping.extractMapping(parameters.values());
        assert mapping.containsKey("a");
        assert mapping.get("a") == null;
    }
}
