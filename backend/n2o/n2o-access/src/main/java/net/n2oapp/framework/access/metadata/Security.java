package net.n2oapp.framework.access.metadata;

import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Права доступа на клиенте
 */
@Setter
public class Security extends ArrayList<Map<String, SecurityObject>> implements  Serializable {

    public Security() {
    }

    public Security(Collection<? extends Map<String, SecurityObject>> c) {
        super(c);
    }

    /**
     * Ключ объекта Security в properties
     */
    public static final String SECURITY_PROP_NAME = "security";

    /**
     * Ключ объекта Field Security в properties
     */
    public static final String FIELD_SECURITY_PROP_NAME = "fieldSecurity";

    /**
     * Ключ мапы Security для in-полей операции в properties
     */
    public static final String IN_FIELD_SECURITY_PROP_NAME = "inFieldSecurity";

    /**
     * Ключ мапы Security для out-полей операции в properties
     */
    public static final String OUT_FIELD_SECURITY_PROP_NAME = "outFieldSecurity";
}
