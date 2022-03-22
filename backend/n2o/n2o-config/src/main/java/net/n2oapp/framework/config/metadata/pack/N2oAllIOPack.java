package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.object.ObjectElementIOv3;
import net.n2oapp.framework.config.io.object.ObjectElementIOv4;
import net.n2oapp.framework.config.io.query.QueryElementIOv4;
import net.n2oapp.framework.config.metadata.compile.application.ApplicationIO;
import net.n2oapp.framework.config.metadata.compile.application.ApplicationIOv2;
import net.n2oapp.framework.config.metadata.compile.menu.SimpleMenuIOv2;
import net.n2oapp.framework.config.metadata.compile.menu.SimpleMenuIOv3;

/**
 * Набор всех считывателей метаданных
 */
public class N2oAllIOPack implements MetadataPack<N2oApplicationBuilder> {
    @Override
    public void build(N2oApplicationBuilder b) {
        b.ios(new ApplicationIO(), new SimpleMenuIOv2(), new ApplicationIOv2(), new SimpleMenuIOv3());

        b.ios(new ObjectElementIOv3(), new ObjectElementIOv4());

        b.ios(new QueryElementIOv4());

        b.packs(new N2oRegionsV2IOPack(), new N2oWidgetsIOPack(), new N2oWidgetsV5IOPack(),
                new N2oFieldSetsIOPack(), new N2oFieldSetsV5IOPack(), new N2oControlsV2IOPack(),
                new N2oDataProvidersIOPack(), new N2oCellsIOPack(), new N2oCellsV3IOPack(),
                new N2oChartsIOPack());
    }
}
