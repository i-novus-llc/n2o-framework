package net.n2oapp.framework.config.io.widget.v4;

import net.n2oapp.framework.api.metadata.global.view.widget.N2oCustomWidget;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

@Component
public class CustomWidgetIOv4 extends WidgetElementIOv4<N2oCustomWidget> {

    @Override
    public void io(Element e, N2oCustomWidget m, IOProcessor p) {
        super.io(e, m, p);
    }

    @Override
    public Class<N2oCustomWidget> getElementClass() {
        return N2oCustomWidget.class;
    }

    @Override
    public String getElementName() {
        return "widget";
    }
}
