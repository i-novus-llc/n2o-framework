package net.n2oapp.framework.config.io.page.v4;

import net.n2oapp.framework.api.metadata.global.view.page.N2oSimplePage;
import net.n2oapp.framework.api.metadata.global.view.widget.N2oWidget;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.config.io.widget.v5.WidgetIOv5;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись страницы с единственным виджетом версии 4.0
 */
@Component
public class SimplePageElementIOv4 extends AbstractPageElementIOv4<N2oSimplePage> {

    @Override
    public void io(Element e, N2oSimplePage m, IOProcessor p) {
        super.io(e, m, p);
        p.anyChild(e, null, m::getWidget, m::setWidget,
                p.anyOf(N2oWidget.class).ignore("breadcrumbs"), WidgetIOv5.NAMESPACE);
        m.setNamespaceUri(getNamespaceUri());
    }

    @Override
    public Class<N2oSimplePage> getElementClass() {
        return N2oSimplePage.class;
    }

    @Override
    public N2oSimplePage newInstance(Element element) {
        return new N2oSimplePage();
    }

    @Override
    public String getElementName() {
        return "simple-page";
    }
}
