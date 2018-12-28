package net.n2oapp.framework.config.reader.control;

import net.n2oapp.framework.api.metadata.control.N2oListField;
import net.n2oapp.framework.api.metadata.control.list.*;
import net.n2oapp.framework.config.reader.MetadataReaderException;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getAttributeEnum;
import static net.n2oapp.framework.config.reader.util.ReaderJdomUtil.getAttributeString;

/**
 * @author dfirstov
 * @since 16.07.2015
 */
@Component
public class N2oGroupClassifierXmlReaderV1 extends N2oStandardControlReaderV1<N2oListField> {
    @Override
    public String getElementName() {
        return "group-classifier";
    }

    @Override
    public N2oListField read(Element element, Namespace namespace) {
        GroupClassifierMode mode = getAttributeEnum(element, "mode", GroupClassifierMode.class);
        if (mode == null)
            throw new MetadataReaderException("n2o.attributeIsMissing").addData("[mode]");
        N2oListField groupClassifier = new N2oGroupClassifierSingle();
        if (mode.getId().equals("single")) {
            groupClassifier = new N2oGroupClassifierSingle();
        } else if (mode.getId().equals("multi")) {
            groupClassifier = new N2oGroupClassifierMulti();
            ((N2oGroupClassifierMulti) groupClassifier).setMode(GroupClassifierMode.MULTI);
        } else if(mode.getId().equals("multi-checkbox")) {
            groupClassifier = new N2oGroupClassifierMulti();
            ((N2oGroupClassifierMulti) groupClassifier).setMode(GroupClassifierMode.MULTI_CHECKBOX);
        }
        ((GroupFieldAware) groupClassifier).setGroupFieldId(getAttributeString(element, "group-field-id"));
        ((InfoFieldAware) groupClassifier).setInfoFieldId(getAttributeString(element, "info-field-id"));
        ((InfoFieldAware) groupClassifier).setInfoStyle(getAttributeString(element, "info-style"));
        return getQueryFieldDefinition(element, groupClassifier);
    }

    @Override
    public Class<N2oListField> getElementClass() {
        return N2oListField.class;
    }
}
