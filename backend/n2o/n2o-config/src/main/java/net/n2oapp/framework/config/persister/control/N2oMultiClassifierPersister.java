package net.n2oapp.framework.config.persister.control;

import net.n2oapp.framework.api.metadata.control.multi.N2oMultiClassifier;
import net.n2oapp.framework.config.persister.util.PersisterJdomUtil;
import org.jdom.Element;
import org.jdom.Namespace;
import org.springframework.stereotype.Component;

/**
 * User: iryabov
 * Date: 20.11.13
 * Time: 9:31
 */
@Component
public class N2oMultiClassifierPersister extends N2oControlXmlPersister<N2oMultiClassifier> {
    public Element persist(N2oMultiClassifier control, Namespace namespace) {
        Element element = new Element(getElementName(), getNamespacePrefix(), getNamespaceUri());
        setControl(element, control);
        setField(element, control);
        setListField(element, control);
        setListQuery(element, control);
        setOptionsList(element, control.getOptions());
        PersisterJdomUtil.setAttribute(element, "search-as-you-type", control.getSearchAsYouType());
        return element;
    }

    @Override
    public Class<N2oMultiClassifier> getElementClass() {
        return N2oMultiClassifier.class;
    }

    @Override
    public String getElementName() {
        return "multi-classifier";
    }

}
