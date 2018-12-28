package net.n2oapp.framework.api.metadata.compile;

import net.n2oapp.framework.api.factory.MetadataFactory;
import net.n2oapp.framework.api.metadata.Compiled;

/**
 * Фабрика трансформаторов собранных метаданных {@link CompileTransformer}
 */
public interface CompileTransformerFactory extends MetadataFactory<CompileTransformer> {

    /**
     * Трансформировать собранные метаданные
     * @param compiled Собранные метаданные
     * @param context Контекст сборки
     * @return Трансформированные собранные метаданные
     */
    <D extends Compiled> D transform(D compiled, CompileContext<?, ?> context, CompileProcessor p);

}
