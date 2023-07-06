package net.n2oapp.framework.api.metadata.local.util;

import org.springframework.util.SerializationUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Set;
import java.util.function.Supplier;

public class CompileUtil {

    public static boolean isTrue(Boolean bool) {
        return bool != null && bool;
    }

    public static boolean castDefault(Boolean value, boolean defaultValue) {
        return value == null ? defaultValue : value;

    }

    public static boolean castDefault(Boolean value, Boolean defaultValue1, Boolean... defaultValues) {
        if (value != null) return value;
        if (defaultValue1 != null) return defaultValue1;
        if (defaultValues == null) return false;
        for (Boolean defaultValue : defaultValues) {
            if (defaultValue != null) {
                return defaultValue;
            }
        }
        return false;
    }

    public static String collectLinks(Set<String> strings) {
        String res = "";
        boolean begin = true;
        for (String s : strings) {
            if (!begin) res += ",";
            res += s;
            begin = false;
        }
        return res;
    }

    /**
     * Привести значение к значению по умолчанию, если оно null.
     * Если первое значение по умолчанию тоже null, берется следующее и т.д.
     * Следует использовать только когда значние по умолчанию это константа или его легко получить,
     * в другом случае использовать метод с Supplier
     *
     * @param value          Исходное значение
     * @param defaultValues  Значения по умолчанию
     * @return               Значение приведенное к значению по умолчанию
     */
    @SafeVarargs
    public static String castDefault(String value, String... defaultValues) {
        if (value != null) return value;
        if (defaultValues == null) return null;
        for (String defaultValue : defaultValues) {
            if (defaultValue != null) {
                return defaultValue;
            }
        }
        return null;
    }

    /**
     * Привести значение к значению по умолчанию, если оно null.
     * Если первое значение по умолчанию тоже null, берется следующее и т.д.
     * Следует использовать, когда получение значения по умолчанию является ресурсно или трудозатратным
     *
     * @param value          Исходное значение
     * @param defaultValues  Значения по умолчанию, получаемое через функцию
     * @return               Значение приведенное к значению по умолчанию
     */
    @SafeVarargs
    public static String castDefault(String value, Supplier<String>... defaultValues) {
        if (value != null) return value;
        if (defaultValues == null) return null;
        for (Supplier<String> defaultValue : defaultValues) {
            if (defaultValue != null) {
                return defaultValue.get();
            }
        }
        return null;
    }

    public static Integer castDefault(Integer value, Integer defaultValue1, Integer... defaultValues) {
        if (value != null) return value;
        if (defaultValue1 != null) return defaultValue1;
        if (defaultValues == null) return null;
        for (Integer defaultValue : defaultValues) {
            if (defaultValue != null) {
                return defaultValue;
            }
        }
        return null;
    }

    public static BigDecimal castDefault(BigDecimal value, BigDecimal defValue1, BigDecimal... defValues) {
        if (value != null) return value;
        if (defValue1 != null) return defValue1;
        if (defValues == null) return null;
        for (BigDecimal defValue : defValues) {
            if (defValue != null) {
                return defValue;
            }
        }
        return null;
    }

    public static <T extends Enum<?>> T castDefault(T value, T defaultValue) {
        return value == null ? defaultValue : value;

    }

    @SafeVarargs
    public static <T extends Enum<?>> T castDefault(T value, T defaultValue1, T... defaultValues) {
        if (value != null) return value;
        if (defaultValue1 != null) return defaultValue1;
        if (defaultValues == null) return null;
        for (T defaultValue : defaultValues) {
            if (defaultValue != null) {
                return defaultValue;
            }
        }
        return null;
    }



    /**
     * Добавить элементы в новый массив
     * @param arr массив
     * @param elements элементы
     * @param <T> тип данных массива
     * @return новый массив
     */
    @SafeVarargs
    public static <T> T[] append(T[] arr, T... elements) {
        final int N = arr.length;
        arr = Arrays.copyOf(arr, N + elements.length);
        for (int i = 0; i < elements.length; i++) {
            arr[N+i] = elements[0];
        }
        return arr;
    }

    @SuppressWarnings("unchecked")
    public static <T> T copy(T cloningObject) {
        return (T) SerializationUtils.deserialize(SerializationUtils.serialize(cloningObject));
    }
}
