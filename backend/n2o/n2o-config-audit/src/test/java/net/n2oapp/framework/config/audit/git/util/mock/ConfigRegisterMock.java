package net.n2oapp.framework.config.audit.git.util.mock;

import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.register.MetadataRegister;
import net.n2oapp.framework.api.register.SourceInfo;
import net.n2oapp.framework.api.register.SourceTypeRegister;
import net.n2oapp.framework.api.util.ToListConsumer;
import net.n2oapp.framework.config.register.*;

/**
 * @author dfirstov
 * @since 22.09.2015
 */
public class ConfigRegisterMock extends N2oMetadataRegister {

    private ToListConsumer<ConfigId> consumer;
    private SourceTypeRegister metaModelRegister;


    public ConfigRegisterMock(ToListConsumer<ConfigId> consumer, SourceTypeRegister metaModelRegister) {
        this.consumer = consumer;
        this.metaModelRegister = metaModelRegister;
        this.clearAll();
    }

    @Override
    public  <I extends SourceInfo> void update(I info) {
        super.update(info);
        consumer.accept(new ConfigId(info.getId(), metaModelRegister.get(info.getBaseSourceClass())));
    }

    @Override
    public void remove(String id, Class<? extends SourceMetadata> sourceClass) {
        super.remove(id, sourceClass);
        consumer.accept(new ConfigId(id, metaModelRegister.get(sourceClass)));
    }

}
