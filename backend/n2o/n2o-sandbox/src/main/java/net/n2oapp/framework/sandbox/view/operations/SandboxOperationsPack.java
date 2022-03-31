package net.n2oapp.framework.sandbox.view.operations;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.compile.pipeline.operation.*;
import org.springframework.cache.CacheManager;

@AllArgsConstructor
@NoArgsConstructor
public class SandboxOperationsPack implements MetadataPack<N2oApplicationBuilder> {

    private String projectId;
    private CacheManager cacheManager;

    @Override
    public void build(N2oApplicationBuilder b) {
        b.operations(new ReadOperation<>(),
                new MergeOperation<>(),
                new ValidateOperation<>(),
                new CopyOperation<>(),
                new SandboxSourceCacheOperation<>(projectId, cacheManager),
                new SourceTransformOperation<>(),
                new CompileOperation<>(),
                new CompileTransformOperation<>(),
                new BindOperation<>());
    }
}
