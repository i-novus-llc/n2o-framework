package net.n2oapp.framework.api.metadata.local.util;

import org.springframework.util.SerializationUtils;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Supplier;

public class CompileUtil {

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
     * Добавить элементы в новый массив
     *
     * @param arr      массив
     * @param elements элементы
     * @param <T>      тип данных массива
     * @return новый массив
     */
    @SafeVarargs
    public static <T> T[] append(T[] arr, T... elements) {
        final int N = arr.length;
        arr = Arrays.copyOf(arr, N + elements.length);
        for (int i = 0; i < elements.length; i++) {
            arr[N + i] = elements[0];
        }
        return arr;
    }

    /**
     * Привести значение к значению по умолчанию, если оно null.
     * Если первое значение по умолчанию тоже null, берется следующее и т.д.
     * следует использовать только когда значние по умолчанию это константа или его легко получить
     * в другом случае использовать метод с Supplier
     *
     * @param value              Исходное значение
     * @param defaultValue      Первое значения по умолчанию
     * @param otherDefaultValues Следующие значения по умолчанию
     * @param <T>                Тип значения
     * @return Значение приведенное к значению по умолчанию
     */
    @SafeVarargs
    public static <T> T castDefault(T value, T defaultValue, T... otherDefaultValues) {
        if (value != null) return value;
        if (defaultValue != null) return defaultValue;
        if (otherDefaultValues != null)
            for (T defValue : otherDefaultValues) {
                if (defValue != null) {
                    return defValue;
                }
            }
        return null;
    }

    /**
     * Привести значение к значению по умолчанию, если оно null.
     * Если первое значение по умолчанию тоже null, берется следующее и т.д.
     * Следует использовать, когда получение значения по умолчанию является ресурсно или трудозатратным
     *
     * @param value                 Исходное значение
     * @param defaultValueFunctions Значения по умолчанию, получаемое через функцию
     * @param <T>                   Тип значения
     * @return Значение приведенное к значению по умолчанию
     */
    @SafeVarargs
    public static <T> T castDefault(T value, Supplier<T>... defaultValueFunctions) {
        if (value != null) return value;
        if (defaultValueFunctions != null) {
            for (Supplier<T> func : defaultValueFunctions) {
                T v = func.get();
                if (v != null)
                    return v;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T copy(T cloningObject) {
        return (T) SerializationUtils.deserialize(SerializationUtils.serialize(cloningObject));
    }
}
