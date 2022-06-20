package net.n2oapp.framework.config.io.datasource;

import net.n2oapp.framework.api.metadata.ReduxModel;
import net.n2oapp.framework.api.metadata.global.view.page.N2oBrowserStorageDatasource;
import net.n2oapp.framework.api.metadata.io.IOProcessor;
import net.n2oapp.framework.api.metadata.io.NamespaceIO;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.springframework.stereotype.Component;

/**
 * Чтение\запись источника данных BrowserStorage
 */
@Component
public class BrowserStorageDatasourceIO extends AbstractDatasourceIO<N2oBrowserStorageDatasource> implements NamespaceIO<N2oBrowserStorageDatasource> {

    public static Namespace NAMESPACE = Namespace.getNamespace("http://n2oapp.net/framework/config/schema/datasource-1.0");

    @Override
    public void io(Element e, N2oBrowserStorageDatasource ds, IOProcessor p) {
        super.io(e, ds, p);
        p.attribute(e, "key", ds::getKey, ds::setKey);
        p.attributeEnum(e, "storage-type", ds::getStorageType, ds::setStorageType, N2oBrowserStorageDatasource.BrowserStorageType.class);
        p.attributeInteger(e, "size", ds::getSize, ds::setSize);
        p.child(e, null, "submit", ds::getSubmit, ds::setSubmit, N2oBrowserStorageDatasource.Submit::new, this::submit);
        p.anyChildren(e, "dependencies", ds::getDependencies, ds::setDependencies,
                p.oneOf(N2oBrowserStorageDatasource.Dependency.class)
                        .add("fetch", N2oBrowserStorageDatasource.FetchDependency.class, this::fetch));
    }

    private void fetch(Element e, N2oBrowserStorageDatasource.FetchDependency t, IOProcessor p) {
        p.attribute(e, "on", t::getOn, t::setOn);
        p.attributeEnum(e, "model", t::getModel, t::setModel, ReduxModel.class);
    }

    private void submit(Element e, N2oBrowserStorageDatasource.Submit t, IOProcessor p) {
        p.attribute(e, "key", t::getKey, t::setKey);
        p.attributeBoolean(e, "auto", t::getAuto, t::setAuto);
        p.attributeEnum(e, "storage-type", t::getStorageType, t::setStorageType, N2oBrowserStorageDatasource.BrowserStorageType.class);
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
