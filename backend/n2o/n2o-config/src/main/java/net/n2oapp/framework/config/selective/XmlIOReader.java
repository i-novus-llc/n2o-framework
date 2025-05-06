package net.n2oapp.framework.config.selective;

import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.IOProcessorAware;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.api.pack.XmlIOBuilder;
import net.n2oapp.framework.config.io.IOProcessorImpl;
import net.n2oapp.framework.config.selective.persister.PersisterFactoryByMap;
import net.n2oapp.framework.config.selective.reader.ReaderFactoryByMap;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.ElementNameQualifier;
import org.custommonkey.xmlunit.XMLUnit;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiPredicate;
import java.util.stream.Stream;


/**
 * Чтение XML файлов через сервисы чтения/записи
 */
public class XmlIOReader extends SelectiveMetadataLoader implements XmlIOBuilder<XmlIOReader> {

    static {
        XMLUnit.setIgnoreComments(true);
        XMLUnit.setIgnoreWhitespace(true);
    }

    private static final Logger logger = LoggerFactory.getLogger(XmlIOReader.class);
    private static final BiPredicate<String, String> CANONICAL_COMPARATOR = (String s1, String s2) -> {
        try {
            Diff diff = XMLUnit.compareXML(s1, s2);
            diff.overrideElementQualifier(new ElementNameQualifier());
            boolean similar = diff.similar();
            logger.debug("""
                    Comparing two xml...
                    Source:
                    {}
                    Persisted:
                    {}
                    Similar? {}
                    Identical? {}
                    """, s2, s1, similar, diff.identical());
            if (logger.isErrorEnabled())
                logger.debug(diff.toString());
            return similar;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    };

    private static final XMLOutputter XML_OUTPUTTER = new XMLOutputter(Format.getPrettyFormat());
    private final PersisterFactoryByMap persisterFactory;
    private final IOProcessor persisterProcessor;

    public XmlIOReader() {
        super(new ReaderFactoryByMap());
        this.persisterFactory = new PersisterFactoryByMap();
        this.persisterProcessor = new IOProcessorImpl(persisterFactory);
    }

    public XmlIOReader addIO(NamespaceIO<? extends NamespaceUriAware> io) {
        add(io);
        persisterFactory.register(io);
        if (io instanceof IOProcessorAware ioProcessorAware)
            ioProcessorAware.setIOProcessor(persisterProcessor);
        return this;
    }

    public <T> T read(String source) {
        return SelectiveUtil.read(source, readerFactory);
    }

    @Override
    @SafeVarargs
    public final XmlIOReader ios(NamespaceIO<? extends NamespaceUriAware>... ios) {
        Stream.of(ios).forEach(this::addIO);
        return this;
    }

    @SafeVarargs
    @Override
    public final XmlIOReader packs(MetadataPack<? super XmlIOReader>... packs) {
        Stream.of(packs).forEach(p -> p.build(this));
        return this;
    }

    public boolean persistAndCompareWithSample(NamespaceUriAware n2o, String sample) {
        return compareWithSample(n2o, sample);
    }

    private boolean compareWithSample(NamespaceUriAware n2o, String sample) {
        return XmlIOReader.CANONICAL_COMPARATOR.test(toString(n2o), sample);
    }

    private String toString(NamespaceUriAware n2o) {
        Element element = persisterFactory.produce(n2o.getClass(), n2o.getNamespace()).persist(n2o, n2o.getNamespace());
        return XML_OUTPUTTER.outputString(element);
    }
}
