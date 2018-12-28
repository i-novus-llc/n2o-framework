package net.n2oapp.framework.config.reader.tools.showModal;

import net.n2oapp.framework.api.metadata.reader.TypedElementReader;
import org.jdom.Element;
import net.n2oapp.framework.api.metadata.control.ShowModalPageFromClassifier;
import net.n2oapp.framework.config.reader.util.ReaderJdomUtil;

import static net.n2oapp.framework.config.reader.tools.showModal.N2oStandardShowModalReaderUtil.*;

/**
 * User: iryabov
 * Date: 03.07.13
 * Time: 13:16
 */
public class ShowModalFromClassifierReaderV1 implements TypedElementReader<ShowModalPageFromClassifier> {
    public static final ShowModalFromClassifierReaderV1 instance = new ShowModalFromClassifierReaderV1();

    public static ShowModalFromClassifierReaderV1 getInstance() {
        return instance;
    }

    @Override
    public ShowModalPageFromClassifier read(Element element) {
        ShowModalPageFromClassifier showModal = new ShowModalPageFromClassifier();
        getShowModalDefinition(element, showModal);
        showModal.setLabelFieldId(ReaderJdomUtil.getAttributeString(element, "label-field-id"));
        showModal.setValueFieldId(ReaderJdomUtil.getAttributeString(element, "value-field-id"));
        return showModal;
    }

    @Override
    public Class<ShowModalPageFromClassifier> getElementClass() {
        return ShowModalPageFromClassifier.class;
    }

    @Override
    public String getElementName() {
        return "show-modal-form";
    }
}
