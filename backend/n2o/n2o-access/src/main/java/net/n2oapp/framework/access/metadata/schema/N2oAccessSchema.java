package net.n2oapp.framework.access.metadata.schema;

import net.n2oapp.framework.api.metadata.global.N2oMetadata;

/**
 * Схема доступа
 */
public abstract class N2oAccessSchema extends N2oMetadata {

    @Override
    public String getPostfix() {
        return "access";
    }

    @Override
    public abstract Class<? extends N2oAccessSchema> getSourceBaseClass();
}
