package net.n2oapp.framework.api.metadata.compile.building;

/**
 * Преобразование собранной метаданной из исходной
 * @param <O> Тип собранной метаданной
 * @param <I> Тип исходной метаданной
 */
public interface CompileMapper<O, I> {
    void map(I source, O compiled);
}
