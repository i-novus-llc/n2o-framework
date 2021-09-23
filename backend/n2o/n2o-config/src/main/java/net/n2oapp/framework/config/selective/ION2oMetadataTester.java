package net.n2oapp.framework.config.selective;

import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.api.pack.PersistersBuilder;
import net.n2oapp.framework.api.pack.ReadersBuilder;
import net.n2oapp.framework.config.selective.persister.SelectivePersister;
import net.n2oapp.framework.config.selective.reader.SelectiveReader;
import net.n2oapp.framework.config.util.FileSystemUtil;
import org.springframework.core.io.ClassPathResource;

import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * @author operehod
 * @since 23.04.2015
 */
public class ION2oMetadataTester implements ReadersBuilder<ION2oMetadataTester>, PersistersBuilder<ION2oMetadataTester> {

    private SelectiveReader selectiveReader = new SelectiveReader();
    private SelectivePersister selectivePersister = new SelectivePersister();

    public ION2oMetadataTester() {
    }

    public ION2oMetadataTester(SelectiveReader selectiveReader, SelectivePersister selectivePersister) {
        this.selectiveReader = selectiveReader;
        this.selectivePersister = selectivePersister;
    }

    public ION2oMetadataTester addIO(NamespaceIO io){
        this.selectivePersister.addPersister(io);
        this.selectiveReader.addReader(io);
        return this;
    }

    public ION2oMetadataTester addPack(MetadataPack<? super ION2oMetadataTester> pack) {
        pack.build(this);
        return this;
    }

    public <T extends NamespaceUriAware> boolean isCheck(String path, Consumer<T> checker){
        String source = FileSystemUtil.getContentFromResource(new ClassPathResource(path));
        T t = selectiveReader.read(source);
        if (checker != null)
            checker.accept(t);
        return selectivePersister.persistAndCompareWithSample(t, source);
    }

    public <T extends NamespaceUriAware> boolean check(String path, Consumer<T> checker) {
        boolean check = isCheck(path, checker);
        assert check;
        return true;
    }

    public boolean check(String path) {
        assert isCheck(path, null);
        return true;
    }

    @Override
    @SafeVarargs
    public final ION2oMetadataTester ios(NamespaceIO<? extends NamespaceUriAware>... ios) {
        Stream.of(ios).forEach(this::addIO);
        return this;
    }
}
