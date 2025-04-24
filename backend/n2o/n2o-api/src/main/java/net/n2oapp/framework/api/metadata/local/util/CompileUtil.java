package net.n2oapp.framework.api.metadata.local.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.SerializationUtils;

import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CompileUtil {

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
