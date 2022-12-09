package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.api.pack.XmlIOBuilder;
import net.n2oapp.framework.config.io.event.OnChangeEventIO;
import net.n2oapp.framework.config.io.event.StompEventIO;

public class N2oEventsIOPack implements MetadataPack<XmlIOBuilder<?>> {
    @Override
    public void build(XmlIOBuilder<?> b) {
        b.ios(new StompEventIO(), new OnChangeEventIO());
    }
}
