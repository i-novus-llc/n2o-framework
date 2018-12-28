package net.n2oapp.framework.config.reader.tools;

import org.jdom.Element;
import net.n2oapp.framework.api.metadata.control.N2oFieldCondition;
import net.n2oapp.framework.api.metadata.reader.TypedElementReader;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;

/**
 * Считывает condition валидации в объекте
 */
public class ConditionReaderV1 implements TypedElementReader<N2oFieldCondition> {
    private static final ConditionReaderV1 instance = new ConditionReaderV1();

    private ConditionReaderV1() {
    }

    public static ConditionReaderV1 getInstance() {
        return instance;
    }

    @Override
    public N2oFieldCondition read(Element element) {
        N2oFieldCondition res = new N2oFieldCondition();
        return getCondition(element, res);
    }

    public <C extends N2oFieldCondition> C getCondition(Element element, C res) {
        if (element == null) return null;
        res.setCondition(ReaderJdomUtil.getText(element));
        res.setOn(ReaderJdomUtil.getAttributeString(element, "on"));
        return res;
    }

    @Override
    public Class<N2oFieldCondition> getElementClass() {
        return N2oFieldCondition.class;
    }

    @Override
    public String getElementName() {
        throw new UnsupportedOperationException("ConditionReaderV1 can't generate with factory!");
    }
}
