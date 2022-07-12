package net.n2oapp.framework.config.io.datasource;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.datasource.BrowserStorageType;
import net.n2oapp.framework.api.metadata.global.view.page.datasource.N2oBrowserStorageDatasource;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import org.jdom2.Element;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись источника, хранящего данные в браузере
 */
@Component
public class BrowserStorageDatasourceIO extends BaseDatasourceIO<N2oBrowserStorageDatasource> {

    @Override
    public void io(Element e, N2oBrowserStorageDatasource ds, IOProcessor p) {
        super.io(e, ds, p);
        p.attribute(e, "key", ds::getKey, ds::setKey);
        p.attributeEnum(e, "type", ds::getStorageType, ds::setStorageType, BrowserStorageType.class);
        p.child(e, null, "submit", ds::getSubmit, ds::setSubmit, N2oBrowserStorageDatasource.Submit::new, this::submit);
    }


    private void submit(Element e, N2oBrowserStorageDatasource.Submit t, IOProcessor p) {
        p.attribute(e, "key", t::getKey, t::setKey);
        p.attributeBoolean(e, "auto", t::getAuto, t::setAuto);
        p.attributeEnum(e, "model", t::getModel, t::setModel, ReduxModel.class);
        p.attributeEnum(e, "type", t::getStorageType, t::setStorageType, BrowserStorageType.class);
    }

    @Override
    public Class<N2oBrowserStorageDatasource> getElementClass() {
        return N2oBrowserStorageDatasource.class;
    }

    @Override
    public String getElementName() {
        return "browser-storage";
    }
}
