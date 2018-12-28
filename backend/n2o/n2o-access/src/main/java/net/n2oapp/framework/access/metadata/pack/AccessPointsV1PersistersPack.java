package net.n2oapp.framework.access.metadata.pack;

import net.n2oapp.framework.access.metadata.accesspoint.persister.*;
import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.api.pack.PersistersBuilder;

public class AccessPointsV1PersistersPack implements MetadataPack<PersistersBuilder> {
    @Override
    public void build(PersistersBuilder b) {
        b.persisters(new N2oContainerAccessPointPersister(),
                new N2oMenuAccessPointPersister(),
                new N2oModuleAccessPointPersister(),
                new N2oObjectAccessPointPersister(),
                new N2oPageAccessPointPersister(),
                new N2oReferenceAccessPointPersister(),
                new N2oUrlAccessPointPersister(),
                new N2oFilterAccessPointPersister(),
                new N2oColumnAccessPointPersister());
    }
}
