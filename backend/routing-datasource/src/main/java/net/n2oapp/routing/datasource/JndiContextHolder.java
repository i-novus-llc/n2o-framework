package net.n2oapp.routing.datasource;

/**
 * User: iryabov
 * Date: 26.08.13
 * Time: 12:18
 */
public class JndiContextHolder {
    private static final ThreadLocal<String> contextHolder =
            new ThreadLocal<String>();

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
