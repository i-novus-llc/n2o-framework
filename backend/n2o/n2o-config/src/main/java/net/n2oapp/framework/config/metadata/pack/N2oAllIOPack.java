package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.api.pack.XmlIOBuilder;
import net.n2oapp.framework.config.io.application.ApplicationIOv3;
import net.n2oapp.framework.config.io.application.sidebar.SidebarIOv3;
import net.n2oapp.framework.config.io.menu.ExtraMenuIOv3;
import net.n2oapp.framework.config.io.menu.NavMenuIOv3;
import net.n2oapp.framework.config.io.object.ObjectElementIOv4;
import net.n2oapp.framework.config.io.query.QueryElementIOv5;

/**
 * Набор всех считывателей метаданных
 */
public class N2oAllIOPack implements MetadataPack<XmlIOBuilder<? extends XmlIOBuilder<?>>> {
    @Override
    public void build(XmlIOBuilder<? extends XmlIOBuilder<?>> b) {
        b.ios(
                new ApplicationIOv3(),
                new SidebarIOv3(),
                new NavMenuIOv3(),
                new ExtraMenuIOv3()
        );

        b.ios(new ObjectElementIOv4());

        b.ios(
                new QueryElementIOv5()
        );

        b.packs(
                new N2oPagesIOv4Pack(),
                new N2oDatasourcesV1IOPack(),
                new N2oRegionsV3IOPack(),
                new N2oWidgetsV5IOPack(),
                new N2oFieldSetsV5IOPack(),
                new N2oControlsV3IOPack(),
                new N2oDataProvidersIOPack(),
                new N2oCellsV3IOPack(),
                new N2oEventsIOPack(),
                new N2oActionsIOV2Pack(),
                new N2oTableSettingsIOPack()
        );
    }
}
