package net.n2oapp.framework.api.metadata.compile.building;

import net.n2oapp.framework.api.metadata.Compiled;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * Процесс сборки поля
 * @param <D> Тип собранной метаданной
 * @param <S> Тип исходной метаданной
 * @param <T> Тип поля
 */
public interface FieldBuildProcessor<D, S, T> {
    void set(BiConsumer<D, ? super T> setter);

    void put(Function<D, ? extends Map<String, Object>> mapGetter, String key);

    <R> FieldBuildProcessor<D, S, R> map(Function<T, R> mapper);

    <R extends Compiled> FieldBuildProcessor<D, S, R> compile(Class<? super R> compiledClass);

    <R extends Compiled> FieldBuildProcessor<D, S, R> compile(CompileConstructor<R, T> constructor,
                                                              CompileBuilder<R, T> builder);

    FieldBuildProcessor<D, S, T> defaults(T defaultValue);

    FieldBuildProcessor<D, S, T> defaults(Function<S, T> defaultValue);

    FieldBuildProcessor<D, S, String> resolve();

    <R> FieldBuildProcessor<D, S, R> resolve(Class<R> resolvedClass);

}
