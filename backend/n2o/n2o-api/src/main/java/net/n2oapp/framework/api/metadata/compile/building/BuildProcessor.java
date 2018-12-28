package net.n2oapp.framework.api.metadata.compile.building;

import java.util.List;
import java.util.function.Function;

/**
 * Процессор сборки объектов
 */
public interface BuildProcessor<O, I> {

    BuildProcessor<O, I> compile(CompileBuilder<O, I> builder);

    <R> BuildProcessor<R, R> optionalCast(Class<R> clazz);

    <R> BuildProcessor<R, R> cast(Class<R> clazz);

    <T> FieldBuildProcessor<O, I, T> get(Function<I, ? extends T> getter);

    <T> PluralFieldBuildProcessor<O, I, T, ?> getArray(Function<I, ? extends T[]> arrayGetter);

    <T> PluralFieldBuildProcessor<O, I, T, ?> getList(Function<I, List<? extends T>> listGetter);
}
