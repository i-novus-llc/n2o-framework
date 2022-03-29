package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.compile.pipeline.operation.*;

/**
 * Набор стандартных операций конвейера по сборке метаданных
 */
public class N2oOperationsPack implements MetadataPack<N2oApplicationBuilder> {
    @Override
    public void build(N2oApplicationBuilder b) {
        b.operations(new ReadOperation<>(),
                new MergeOperation<>(),
                new ValidateOperation<>(),
                new CopyOperation<>(),
                new SourceCacheOperation<>(),
                new SourceTransformOperation<>(),
                new CompileOperation<>(),
                new CompileTransformOperation<>(),
                new CompileCacheOperation<>(),
                new BindOperation<>(),
                new PersistOperation<>());
    }
}
