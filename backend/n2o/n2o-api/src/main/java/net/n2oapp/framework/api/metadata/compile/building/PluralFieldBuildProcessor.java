package net.n2oapp.framework.api.metadata.compile.building;

import net.n2oapp.framework.api.metadata.Compiled;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collector;

/**
 * Процесс сборки спискового поля
 *
 * @param <D> Тип собранной метаданной
 * @param <S> Тип исходной метаданной
 * @param <T> Тип поля
 * @param <L> Тип списка
 */
public interface PluralFieldBuildProcessor<D, S, T, L> {
    void set(BiConsumer<D, ? super L> listSetter);

    PluralFieldBuildProcessor<D, S, T, L> collect(Collector<T, ?, L> collector);

    <R> PluralFieldBuildProcessor<D, S, R, L> map(Function<T, R> mapper);

    <R extends Compiled> PluralFieldBuildProcessor<D, S, R, L> compile(Class<? super R> compiledClass);

    <R extends Compiled> PluralFieldBuildProcessor<D, S, R, L> compile(CompileConstructor<R, T> constructor,
                                                                       CompileBuilder<R, T> builder);
}
