package net.n2oapp.framework.config.persister.widget;

import net.n2oapp.framework.api.metadata.global.view.widget.N2oHtmlWidget;
import org.jdom.Element;
import org.jdom.Namespace;

import static net.n2oapp.framework.config.persister.util.PersisterJdomUtil.setAttribute;
import static net.n2oapp.framework.config.persister.util.PersisterJdomUtil.setElementString;

public class HtmlWidgetXmlPersister extends WidgetXmlPersister<N2oHtmlWidget> {

    public Element getWidget(N2oHtmlWidget n2oHtmlWidget, Namespace namespace) {
        Element rootElement = new Element(getElementName(), namespace);
        setAttribute(rootElement, "url", n2oHtmlWidget.getUrl());
        setElementString(rootElement, "name", n2oHtmlWidget.getName());
        if(!n2oHtmlWidget.isDummyObject()) {
            setElementString(rootElement, "object-id", n2oHtmlWidget.getObjectId());
        }
        return rootElement;
    }

    @Override
    public Class<N2oHtmlWidget> getElementClass() {
        return N2oHtmlWidget.class;
    }

    @Override
    public String getElementName() {
        return "html";
    }
}
