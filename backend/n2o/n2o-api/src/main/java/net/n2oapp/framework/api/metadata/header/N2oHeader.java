package net.n2oapp.framework.api.metadata.header;

import net.n2oapp.framework.api.metadata.global.N2oMetadata;

/**
 * Хедер
 */
public abstract class N2oHeader extends N2oMetadata {

    @Override
    public final String getPostfix() {
        return "header";
    }

    @Override
    public final Class<? extends N2oMetadata> getSourceBaseClass() {
        return N2oHeader.class;
    }

}
