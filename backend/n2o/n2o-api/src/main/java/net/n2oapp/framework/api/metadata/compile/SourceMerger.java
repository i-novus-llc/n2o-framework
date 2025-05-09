package net.n2oapp.framework.api.metadata.compile;

import net.n2oapp.framework.api.N2oNamespace;
import net.n2oapp.framework.api.metadata.aware.ExtensionAttributesAware;

import java.lang.reflect.Array;
import java.util.Map;
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
     * Слияние свойств исходной метаданной со значениями перекрывающей метаданной, если они не пусты
     *
     * @param ref    Метаданная, считанная по ref-id
     * @param source Исходная метаданная
     * @return Исходная метаданная с перекрытыми свойствами
     */
    S merge(S ref, S source);

    /**
     * Установить значение в сеттер, если в геттере оно не null
     *
     * @param sourceSetter Сеттер исходной метаданной
     * @param sourceGetter Геттер исходной метаданной
     * @param refGetter    Геттер перекрывающей метаданной
     * @param <D>          Тип данных
     */
    default <D> void setIfNotNull(Consumer<D> sourceSetter, Supplier<D> sourceGetter, Supplier<D> refGetter) {
        D d = sourceGetter.get();
        if (d == null) {
            sourceSetter.accept(refGetter.get());
        }
    }

    /**
     * Добавить элементы из второго геттера в массив элементов первого геттера
     */
    default <T, D> void addIfNotNull(T ref, T source, BiConsumer<T, D[]> setter, Function<T, D[]> getter) {
        D[] b = getter.apply(ref);
        if (b != null && b.length > 0) {
            D[] a = getter.apply(source);
            if (a != null && a.length > 0) {
                int aLen = a.length;
                int bLen = b.length;
                @SuppressWarnings("unchecked")
                D[] c = (D[]) Array.newInstance(a.getClass().getComponentType(), aLen + bLen);
                System.arraycopy(a, 0, c, bLen, aLen);
                System.arraycopy(b, 0, c, 0, bLen);
                setter.accept(source, c);
            } else {
                setter.accept(source, b);
            }
        }
    }

    /**
     * Слияние дополнительных атрибутов
     */
    default void mergeExtAttributes(ExtensionAttributesAware ref, ExtensionAttributesAware source) {
        Map<N2oNamespace, Map<String, String>> b = ref.getExtAttributes();
        if (b != null && !b.isEmpty()) {
            Map<N2oNamespace, Map<String, String>> a = source.getExtAttributes();
            if (a != null && !a.isEmpty()) {
                for (Map.Entry<N2oNamespace, Map<String, String>> entry : b.entrySet()) {
                    if (a.containsKey(entry.getKey())) {
                        entry.getValue().putAll(a.get(entry.getKey()));
                        a.get(entry.getKey()).putAll(entry.getValue());
                    } else {
                        a.put(entry.getKey(), entry.getValue());
                    }
                }
            } else {
                source.setExtAttributes(b);
            }
        }
    }
}
