package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.io.object.ObjectElementIOv3;
import net.n2oapp.framework.config.io.object.ObjectElementIOv4;
import net.n2oapp.framework.config.io.query.QueryElementIOv4;
import net.n2oapp.framework.config.metadata.compile.header.SimpleHeaderIOv2;
import net.n2oapp.framework.config.metadata.compile.header.SimpleHeaderReader;
import net.n2oapp.framework.config.metadata.compile.menu.SimpleMenuIOv2;
import net.n2oapp.framework.config.metadata.compile.menu.SimpleMenuReader;
import net.n2oapp.framework.config.reader.header.CustomHeaderReader;
import net.n2oapp.framework.config.reader.query.QueryElementReaderV3;

/**
 * Набор всех считывателей метаданных
 */
public class N2oAllIOPack implements MetadataPack<N2oApplicationBuilder> {
    @Override
    public void build(N2oApplicationBuilder b) {
        b.readers(new SimpleHeaderReader(), new CustomHeaderReader(), new SimpleMenuReader());
        b.ios(new SimpleHeaderIOv2(), new SimpleMenuIOv2());

        b.ios(new ObjectElementIOv3(),
                new ObjectElementIOv4());

        b.readers(new QueryElementReaderV3());
        b.ios(new QueryElementIOv4());

        b.packs(new N2oRegionsV1IOPack(), new N2oWidgetsIOPack(),
                new N2oFieldSetsIOPack(), new N2oControlsV2IOPack(),
                new N2oControlsV1IOPack(), new N2oDataProvidersIOPack(),
                new N2oInvocationV2ReadersPack(), new N2oCellsIOPack(),
                new N2oChartsIOPack());
    }
}
