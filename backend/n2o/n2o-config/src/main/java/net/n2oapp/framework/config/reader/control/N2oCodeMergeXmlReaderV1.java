package net.n2oapp.framework.config.reader.control;

import net.n2oapp.framework.api.metadata.control.N2oCodeMerge;
import net.n2oapp.framework.api.metadata.control.plain.CodeLanguageEnum;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getAttributeInteger;
import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getAttributeString;
@Component
public class N2oCodeMergeXmlReaderV1 extends N2oStandardControlReaderV1<N2oCodeMerge> {
    @Override
    public N2oCodeMerge read(Element element, Namespace namespace) {
        N2oCodeMerge codeMerge = new N2oCodeMerge();
        codeMerge.setMergeView(ReaderJdomUtil.getAttributeEnum(element, "merge-view", N2oCodeMerge.MergeView.class));
        codeMerge.setShowDifferences(ReaderJdomUtil.getAttributeBoolean(element, "show-differences"));
        codeMerge.setConnectAlign(ReaderJdomUtil.getAttributeBoolean(element, "connect-align"));
        codeMerge.setCollapseIdentical(ReaderJdomUtil.getAttributeBoolean(element, "collapse-identical"));
        codeMerge.setAllowEditingOriginals(ReaderJdomUtil.getAttributeBoolean(element, "allow-editing-originals"));
        codeMerge.setLanguage(ReaderJdomUtil.getAttributeEnum(element, "language", CodeLanguageEnum.class));
        codeMerge.setRows(getAttributeInteger(element, "rows"));
        codeMerge.setLeftLabel(getAttributeString(element, "left-label"));
        codeMerge.setRightLabel(getAttributeString(element, "right-label"));
        getControlFieldDefinition(element, codeMerge);
        return codeMerge;
    }

    @Override
    public Class<N2oCodeMerge> getElementClass() {
        return N2oCodeMerge.class;
    }

    @Override
    public String getElementName() {
        return "code-merge";
    }
}
