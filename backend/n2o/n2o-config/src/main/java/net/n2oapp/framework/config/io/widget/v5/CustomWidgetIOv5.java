package net.n2oapp.framework.config.io.widget.v5;

import net.n2oapp.framework.api.metadata.global.view.widget.N2oCustomWidget;
import org.springframework.stereotype.Component;

@Component
public class CustomWidgetIOv5 extends WidgetElementIOv5<N2oCustomWidget> {

    @Override
    public Class<N2oCustomWidget> getElementClass() {
        return N2oCustomWidget.class;
    }

    @Override
    public String getElementName() {
        return "widget";
    }
}
