package net.n2oapp.framework.config.factory;

import net.n2oapp.framework.api.factory.MetadataFactory;

public abstract class MockMetadataFactory<G> implements MetadataFactory<G> {
    @Override
    public MetadataFactory<G> add(G... engine) {
        return this;
    }
}
