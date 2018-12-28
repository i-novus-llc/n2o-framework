package net.n2oapp.framework.config.reader.tools;

import org.jdom.Element;
import net.n2oapp.framework.api.metadata.global.view.tools.N2oCounter;
import net.n2oapp.framework.api.metadata.reader.TypedElementReader;

/**
 * Считывание счетчика, отображающегося на вкладке/кнопке/шапке контейнера
 */
public class CounterReaderV1 implements TypedElementReader<N2oCounter> {
    private static final CounterReaderV1 instance = new CounterReaderV1();

    public static CounterReaderV1 getInstance() {
        return instance;
    }

    private CounterReaderV1() {
    }

    @Override
    public N2oCounter read(Element element) {
        N2oCounter counter = new N2oCounter();
//        counter.setQueryId(getAttributeString(element, "query-id"));
        Element preFilters = element.getChild("pre-filters", element.getNamespace());
        counter.setPreFilters(PreFilterReaderV1Util.getControlPreFilterListDefinition(preFilters));
        return counter;
    }

    @Override
    public Class<N2oCounter> getElementClass() {
        return N2oCounter.class;
    }

    @Override
    public String getElementName() {
        return "counter";
    }
}
