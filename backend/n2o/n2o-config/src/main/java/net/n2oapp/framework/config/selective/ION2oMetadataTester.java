package net.n2oapp.framework.config.selective;

import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.api.pack.XmlIOBuilder;
import net.n2oapp.framework.config.util.FileSystemUtil;
import org.springframework.core.io.ClassPathResource;

import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Сервис тестирования чтения/записи метаданных
 */
public class ION2oMetadataTester implements XmlIOBuilder<ION2oMetadataTester> {

    private XmlIOReader xmlIOReader = new XmlIOReader();

    public ION2oMetadataTester() {
    }

    public ION2oMetadataTester(XmlIOReader xmlIOReader) {
        this.xmlIOReader = xmlIOReader;
    }

    public ION2oMetadataTester addIO(NamespaceIO io) {
        this.xmlIOReader.addIO(io);
        return this;
    }

    public ION2oMetadataTester addPack(MetadataPack<? super ION2oMetadataTester> pack) {
        pack.build(this);
        return this;
    }

    public <T extends NamespaceUriAware> boolean isCheck(String path, Consumer<T> checker) {
        String source = FileSystemUtil.getContentFromResource(new ClassPathResource(path));
        T t = xmlIOReader.read(source);
        if (checker != null)
            checker.accept(t);
        return xmlIOReader.persistAndCompareWithSample(t, source);
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
