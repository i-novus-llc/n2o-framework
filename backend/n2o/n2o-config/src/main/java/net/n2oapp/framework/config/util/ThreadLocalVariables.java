package net.n2oapp.framework.config.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * User: operhod
 * Date: 11.03.14
 * Time: 15:13
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ThreadLocalVariables {

    public static final ThreadLocal<Boolean> IS_VALIDATE = new ThreadLocalBoolean();
    public static final ThreadLocal<Boolean> IS_PUT_IN_CACHE = new ThreadLocalBoolean();


    public static class ThreadLocalBoolean extends ThreadLocal<Boolean> {
        @Override
        protected Boolean initialValue() {
            return true;
        }
    }


}
