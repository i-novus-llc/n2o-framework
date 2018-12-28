package net.n2oapp.framework.api.reader;

import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.register.SourceInfo;

/**
 * Загрузчик метаданных из внешних источников
 *
 * @param <I> Тип мета информации
 */
@FunctionalInterface
public interface SourceLoader<I extends SourceInfo> {
    /**
     * Считать метаданную по мета информации
     *
     * @param info Мета информация
     * @param <S>  Тип считанной метаданной
     * @return Считанная метаданная
     */
    <S extends SourceMetadata> S load(I info, String params);

}
