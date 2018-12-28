package net.n2oapp.framework.api.metadata.compile;

import net.n2oapp.framework.api.metadata.SourceMetadata;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Слияние двух метаданных в одну
 *
 * @param <S> Тип метаданной
 */
@FunctionalInterface
public interface SourceMerger<S> {

    /**
     * Заменить свойства исходной метаданной значениями перекрывающей метаданной, если они не пусты
     *
     * @param source   Исходная метаданная
     * @param override перекрывающая метаданная
     * @return Исходная метаданная с перекрытыми свойствами
     */
    S merge(S source, S override);

    /**
     * Установить значение в сеттер, если в геттере оно не null
     * @param setter Сеттер
     * @param getter Геттер
     * @param <D> Тип данных
     */
    default <D> void setIfNotNull(Consumer<D> setter, Supplier<D> getter) {
        D d = getter.get();
        if (d != null) {
            setter.accept(d);
        }
    }

    /**
     * Добавить элементы из второго геттера в массив элементов первого геттера
     */
    default <T, D> void addIfNotNull(T source, T override, BiConsumer<T, D[]> setter, Function<T, D[]> getter) {
        D[] b = getter.apply(override);
        if (b != null && b.length > 0) {
            D[] a = getter.apply(source);
            if (a != null && a.length > 0) {
                int aLen = a.length;
                int bLen = b.length;
                @SuppressWarnings("unchecked")
                D[] c = (D[]) Array.newInstance(a.getClass().getComponentType(), aLen + bLen);
                System.arraycopy(a, 0, c, 0, aLen);
                System.arraycopy(b, 0, c, aLen, bLen);
                setter.accept(source, c);
            } else {
                setter.accept(source, b);
            }
        }
    }
}
