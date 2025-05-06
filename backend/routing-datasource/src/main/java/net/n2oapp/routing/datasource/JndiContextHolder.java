package net.n2oapp.routing.datasource;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * User: iryabov
 * Date: 26.08.13
 * Time: 12:18
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JndiContextHolder {
    private static final ThreadLocal<String> contextHolder =
            new ThreadLocal<>();

    public static void setJndiContext(String jndiName) {
        contextHolder.set(jndiName);
    }

    public static String getJndiName() {
        return contextHolder.get();
    }

    public static void clearJndiContext() {
        contextHolder.remove();
    }


}
