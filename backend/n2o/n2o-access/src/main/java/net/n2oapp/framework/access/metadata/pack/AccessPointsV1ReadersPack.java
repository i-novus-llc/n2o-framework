package net.n2oapp.framework.access.metadata.pack;

import net.n2oapp.framework.access.metadata.accesspoint.reader.*;
import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.api.pack.ReadersBuilder;

public class AccessPointsV1ReadersPack implements MetadataPack<ReadersBuilder> {
    @Override
    public void build(ReadersBuilder b) {
        b.readers(new N2oContainerAccessPointReader(),
                new N2oMenuAccessPointReader(),
                new N2oFilterAccessPointReader(),
                new N2oColumnAccessPointReader(),
                new N2oUrlAccessPointReader(),
                new N2oReferenceAccessPointReader(),
                new N2oPageAccessPointReader(),
                new N2oObjectAccessPointReader(),
                new N2oModuleAccessPointReader());
    }
}
