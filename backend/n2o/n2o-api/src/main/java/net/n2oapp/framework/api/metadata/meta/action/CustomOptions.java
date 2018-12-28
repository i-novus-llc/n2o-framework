package net.n2oapp.framework.api.metadata.meta.action;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Настройки кастомизируемого действия
 */
public class CustomOptions extends HashMap<String, Object> implements ActionOptions {

    public CustomOptions(Map<? extends String, ?> m) {
        super(m != null ? m : Collections.emptyMap());
    }
}
