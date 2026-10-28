package net.n2oapp.framework.config.selective;

import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.IOProcessorAware;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.api.pack.XmlIOBuilder;
import net.n2oapp.framework.config.io.IOProcessorImpl;
import net.n2oapp.framework.config.selective.persister.PersisterFactoryByMap;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.Difference;
import org.xmlunit.diff.ElementSelectors;

import java.util.function.BiPredicate;
import java.util.stream.Stream;


/**
 * Чтение XML файлов через сервисы чтения/записи
 */
public class XmlIOReader extends SelectiveMetadataLoader implements XmlIOBuilder<XmlIOReader> {

    private static final Logger logger = LoggerFactory.getLogger(XmlIOReader.class);
    private static final BiPredicate<String, String> CANONICAL_COMPARATOR = (String s1, String s2) -> {
        try {
            // similar — допускает другой порядок дочерних элементов, CDATA вместо текста, другие префиксы namespace
            Diff similarDiff = DiffBuilder.compare(s2)
                    .withTest(s1)
                    .ignoreComments()
                    .ignoreWhitespace()
                    .withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byName))
                    .checkForSimilar()
                    .build();

            boolean similar = !similarDiff.hasDifferences();

            if (logger.isDebugEnabled()) {
                // identical — любое расхождение, включая перечисленные выше; нужно только для лога
                Diff identicalDiff = DiffBuilder.compare(s2)
                        .withTest(s1)
                        .ignoreComments()
                        .ignoreWhitespace()
                        .withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byName))
                        .build();

                boolean identical = !identicalDiff.hasDifferences();

                StringBuilder logMessage = new StringBuilder()
                        .append("Comparing two xml...\n")
                        .append("Source:\n").append(s2).append("\n")
                        .append("Persisted:\n").append(s1).append("\n")
                        .append("Similar? ").append(similar).append("\n")
                        .append("Identical? ").append(identical).append("\n");
                if (!similar) {
                    for (Difference d : similarDiff.getDifferences()) {
                        logMessage.append("\t").append(d).append("\n");
                    }
                }
                logger.debug(logMessage.toString());
            }
            return similar;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    };

    private static final XMLOutputter XML_OUTPUTTER = new XMLOutputter(Format.getPrettyFormat());
    private final PersisterFactoryByMap persisterFactory;
    private final IOProcessor persisterProcessor;

    public XmlIOReader(MetadataEnvironment env) {
        super(env);
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