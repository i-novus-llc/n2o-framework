package net.n2oapp.framework.config.selective.reader;

import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import net.n2oapp.framework.api.pack.ReadersBuilder;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;
import net.n2oapp.framework.config.selective.SelectiveMetadataLoader;
import net.n2oapp.framework.config.selective.SelectiveUtil;

import java.util.stream.Stream;


/**
 * @author operehod
 * @since 22.04.2015
 */
public class SelectiveReader extends SelectiveMetadataLoader implements ReadersBuilder<SelectiveReader> {

    public SelectiveReader() {
        super(new ReaderFactoryByMap());
        ReaderJdomUtil.clearTextProcessing();
    }

    public SelectiveReader addReader(NamespaceIO<? extends NamespaceUriAware> io) {
        add(io);
        return this;
    }

    public <T> T read(String source) {
        return SelectiveUtil.read(source, readerFactory);
    }

    @Override
    @SafeVarargs
    public final SelectiveReader ios(NamespaceIO<? extends NamespaceUriAware>... ios) {
        Stream.of(ios).forEach(this::addReader);
        return this;
    }
}
