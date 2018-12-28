package net.n2oapp.framework.config.selective;

import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import net.n2oapp.framework.api.metadata.io.ProxyNamespaceIO;
import net.n2oapp.framework.api.metadata.reader.NamespaceReader;
import net.n2oapp.framework.api.metadata.reader.NamespaceReaderFactory;
import net.n2oapp.framework.api.reader.SourceLoader;
import net.n2oapp.framework.config.selective.reader.ReaderFactoryByMap;

/**
 * Считыватель тестовых метаданных из xml
 */
public class SelectiveMetadataLoader implements SourceLoader<CompileInfo> {
    protected NamespaceReaderFactory readerFactory;

    public SelectiveMetadataLoader(NamespaceReaderFactory readerFactory) {
        this.readerFactory = readerFactory;
    }

    public SelectiveMetadataLoader() {
        readerFactory = new ReaderFactoryByMap();
    }

    public SelectiveMetadataLoader add(NamespaceReader<? extends NamespaceUriAware> reader) {
        readerFactory.add(reader);
        return this;
    }

    public SelectiveMetadataLoader add(NamespaceIO<? extends NamespaceUriAware> io) {
        readerFactory.add(new ProxyNamespaceIO(io));
        return this;
    }

    @Override
    public <S extends SourceMetadata> S load(CompileInfo info, String params) {
        return SelectiveUtil.readByPath(info.getId(), info.getPath(), readerFactory);
    }

    public void setReaderFactory(NamespaceReaderFactory readerFactory) {
        this.readerFactory = readerFactory;
    }


}