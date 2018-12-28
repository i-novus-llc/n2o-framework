package net.n2oapp.framework.api.exception;

import java.util.List;
import java.util.Map;

import static java.lang.String.valueOf;
import static java.util.stream.Collectors.toList;

/**
 * @author operehod
 * @since 15.10.2015
 */
public class N2oExceptionUtil {

    public static List<String> parametersToList(Map<String, Object> map) {
        if (map == null)
            return null;
        return map.entrySet().stream()
                .sorted((e1, e2) -> valueOf(e1.getValue()).compareTo(valueOf(e2.getValue())))
                .map(e -> e.getKey() + " = " + e.getValue())
                .collect(toList());
    }

}
