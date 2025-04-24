package net.n2oapp.framework.engine.util.json;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * User: iryabov
 * Date: 26.08.13
 * Time: 12:18
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateFormatHolder {
    private static final ThreadLocal<String> contextHolder =
            new ThreadLocal<String>();

    public static void setDateFormat(String dateFormat) {
        contextHolder.set(dateFormat);
    }

    public static String getDateFormat() {
        return contextHolder.get();
    }

    public static void clearDateFormat() {
        contextHolder.remove();
    }


}
