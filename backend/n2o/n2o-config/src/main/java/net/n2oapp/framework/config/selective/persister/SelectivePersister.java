package net.n2oapp.framework.config.selective.persister;

import net.n2oapp.framework.api.metadata.aware.NamespaceUriAware;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.IOProcessorAware;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import net.n2oapp.framework.api.metadata.io.ProxyNamespaceIO;
import net.n2oapp.framework.api.metadata.persister.NamespacePersister;
import net.n2oapp.framework.api.pack.PersistersBuilder;
import net.n2oapp.framework.config.io.IOProcessorImpl;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.ElementNameQualifier;
import org.custommonkey.xmlunit.XMLUnit;
import org.jdom2.Element;
import org.jdom2.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.util.function.BiFunction;
import java.util.stream.Stream;

import static net.n2oapp.framework.config.util.FileSystemUtil.getContentFromResource;
import static net.n2oapp.framework.config.util.XmlUtil.N2O_FORMAT;


/**
 * @author operehod
 * @since 22.04.2015
 */
public class SelectivePersister implements PersistersBuilder<SelectivePersister> {

    static {
        XMLUnit.setIgnoreComments(true);
        XMLUnit.setIgnoreWhitespace(true);
    }

    private static final Logger logger = LoggerFactory.getLogger(SelectivePersister.class);
    private static final BiFunction<String, String, Boolean> CANONICAL_COMPARATOR = (String s1, String s2) -> {
        try {
            Diff diff = XMLUnit.compareXML(s1, s2);
            diff.overrideElementQualifier(new ElementNameQualifier());
            boolean similar = diff.similar();
            logger.debug("Comparing two xml..." +
                    "\nSource:\n{}\nPersisted:\n{}\nSimilar? {}\nIdentical? {}", s2, s1, similar, diff.identical());
            logger.debug(diff.toString());
            return similar;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    };

    private static final XMLOutputter XML_OUTPUTTER = new XMLOutputter(N2O_FORMAT);

    private PersisterFactoryByMap persisterFactory;
    private IOProcessor persisterProcessor;


    public SelectivePersister() {
        this.persisterFactory = new PersisterFactoryByMap();
        this.persisterProcessor = new IOProcessorImpl(persisterFactory);
    }

    public SelectivePersister addPersister(NamespacePersister persister) {
        persisterFactory.register(persister);
        if (persister instanceof IOProcessorAware)
            ((IOProcessorAware) persister).setIOProcessor(persisterProcessor);
        return this;
    }

    public SelectivePersister addPersister(NamespaceIO io) {
        return addPersister(new ProxyNamespaceIO(io, this.persisterProcessor));
    }

    public boolean persistAndCompareWithSampleByPath(NamespaceUriAware n2o, String samplerPath) {
        return persistAndCompareWithSampleByPath(n2o, samplerPath, false);
    }

    public boolean persistAndCompareWithSampleByPath(NamespaceUriAware n2o, String samplerPath, boolean fullEquality) {
        return compareWithSampleByPath(n2o, samplerPath, chooseComparator(fullEquality));
    }

    public boolean persistAndCompareWithSample(NamespaceUriAware n2o, String sample) {
        return compareWithSample(n2o, sample, CANONICAL_COMPARATOR);
    }

    public boolean persistAndCompareWithSample(NamespaceUriAware n2o, String sample, boolean fullEquality) {
        return compareWithSample(n2o, sample, chooseComparator(fullEquality));
    }

    private static BiFunction<String, String, Boolean> chooseComparator(boolean fullEquality) {
        BiFunction<String, String, Boolean> comparator;
        if (fullEquality)
            comparator = String::equals;
        else
            comparator = CANONICAL_COMPARATOR;
        return comparator;
    }


    private boolean compareWithSampleByPath(NamespaceUriAware n2o, String samplerPath, BiFunction<String, String, Boolean> comparator) {
        return compareWithSample(n2o, getContentFromResource(new ClassPathResource(samplerPath)), comparator);
    }

    private boolean compareWithSample(NamespaceUriAware n2o, String sample, BiFunction<String, String, Boolean> comparator) {
        return comparator.apply(toString(n2o), sample);
    }


    @SuppressWarnings("unchecked")
    private String toString(NamespaceUriAware n2o) {
        Element element = persisterFactory.produce(n2o.getClass(), n2o.getNamespace()).persist(n2o, n2o.getNamespace());
        return XML_OUTPUTTER.outputString(element);
    }

    @Override
    public SelectivePersister persisters(NamespacePersister<? extends NamespaceUriAware>... persisters) {
        Stream.of(persisters).forEach(this::addPersister);
        return this;
    }

    @Override
    public SelectivePersister ios(NamespaceIO<? extends NamespaceUriAware>... ios) {
        Stream.of(ios).forEach(io -> persisterFactory.add(new ProxyNamespaceIO(io)));
        return this;
    }
}
