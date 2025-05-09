package net.n2oapp.framework.config.reader;

import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.reader.CurrentElementHolder;
import net.n2oapp.framework.api.reader.SourceLoader;
import net.n2oapp.framework.api.reader.SourceLoaderFactory;
import net.n2oapp.framework.api.register.SourceInfo;
import net.n2oapp.framework.config.factory.BaseMetadataFactory;
import net.n2oapp.framework.config.selective.CompileInfo;

import java.util.Map;

/**
 * Фабрика считывателей метаданных
 */
public class N2oSourceLoaderFactory extends BaseMetadataFactory<SourceLoader> implements SourceLoaderFactory {

    public N2oSourceLoaderFactory() {
    }

    public N2oSourceLoaderFactory(Map<String, SourceLoader> beans) {
        super(beans);
    }

    @Override
    public <S extends SourceMetadata, I extends SourceInfo> S read(I info, String params) {
        SourceLoader<I> reader = produce((g, i) -> info.getReaderClass().isAssignableFrom(g.getClass()), info);
        CurrentElementHolder.setSourceInfo(info);
        if (info instanceof CompileInfo compileInfo && compileInfo.getPath() == null)
            throw new MetadataNotFoundException(info.getId(), info.getBaseSourceClass());
        try {
            return reader.load(info, params);
        } finally {
            CurrentElementHolder.clear();
        }
    }

    @Override
    public N2oSourceLoaderFactory add(SourceLoader... g) {
        return (N2oSourceLoaderFactory) super.add(g);
    }
}
