package net.n2oapp.framework.api.metadata.global.view.widget.table;


import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Source;

import java.util.Map;

@Getter
@Setter
public class N2oSwitch implements Source {

    private final static String KEY_VALUE_FORMAT = "%s:%s";

    private Map<String, String> cases;
    private Map<Object, String> resolvedCases;
    private String defaultCase;
    private String fieldId;
    private String valueFieldId;
}
