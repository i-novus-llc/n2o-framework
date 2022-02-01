package net.n2oapp.framework.config.io.datasource;

import net.n2oapp.framework.api.metadata.application.N2oStompDatasource;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись STOMP-источника данных
 */
@Component
public class StompDatasourceIO extends AbstractDatasourceIO<N2oStompDatasource> {

    @Override
    public Class<N2oStompDatasource> getElementClass() {
        return N2oStompDatasource.class;
    }

    @Override
    public String getElementName() {
        return "stomp-datasource";
    }

    @Override
    public void io(Element e, N2oStompDatasource ds, IOProcessor p) {
        super.io(e, ds, p);
        p.attribute(e, "destination", ds::getDestination, ds::setDestination);
        p.childrenToMap(e, "values", "value", ds::getValues, ds::setValues);
    }
}
