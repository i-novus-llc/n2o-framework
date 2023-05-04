package net.n2oapp.framework.config.io.datasource;

import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oParentDatasource;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись источника данных родительской страницы
 */
@Component
public class ParentDatasourceIO extends AbstractDatasourceIO<N2oParentDatasource> {

    @Override
    public void io(Element e, N2oParentDatasource ds, IOProcessor p) {
        super.io(e, ds, p);
        p.attribute(e, "source-datasource", ds::getSourceDatasource, ds::setSourceDatasource);
    }

    @Override
    public Class<N2oParentDatasource> getElementClass() {
        return N2oParentDatasource.class;
    }

    @Override
    public String getElementName() {
        return "parent-datasource";
    }
}
