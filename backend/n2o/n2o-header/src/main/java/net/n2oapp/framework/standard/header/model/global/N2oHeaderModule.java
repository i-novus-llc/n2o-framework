package net.n2oapp.framework.standard.header.model.global;

import net.n2oapp.framework.api.metadata.global.N2oMetadata;
import net.n2oapp.framework.api.metadata.global.N2oReference;
import net.n2oapp.framework.api.metadata.global.aware.IdAware;

import java.io.Serializable;

/**
 * User: operhod
 * Date: 17.02.14
 * Time: 12:13
 */
public abstract class N2oHeaderModule<T extends N2oHeaderModule> extends N2oMetadata
        implements IdAware, Serializable, N2oReference<T> {

    @Override
    public final String getPostfix() {
        return "module";
    }

    @Override
    public final Class<? extends N2oMetadata> getSourceBaseClass() {
        return N2oHeaderModule.class;
    }

}
