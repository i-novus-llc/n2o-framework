package net.n2oapp.framework.api.metadata.global.view.widget.table;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.String.format;

/**
 * User: operhod
 * Date: 04.02.14
 * Time: 15:19
 */
@Getter
@Setter
public class N2oSwitch implements Serializable {

    private final static String KEY_VALUE_FORMAT = "%s:%s";

    private Map<String, String> cases;
    private Map<Object, String> resolvedCases;
    private String defaultCase;
    private String fieldId;
    private String valueFieldId;

    public String getSwitchString() {
        String cases = this.cases != null ? this.cases.entrySet().stream()
                .map(e -> format(KEY_VALUE_FORMAT, e.getKey(), e.getValue()))
                .collect(Collectors.joining(";")) : "";
        return defaultCase != null && !defaultCase.isEmpty() ? cases + ";" + format(KEY_VALUE_FORMAT, "$", defaultCase)
                : cases;
    }

}
