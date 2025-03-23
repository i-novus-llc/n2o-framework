package net.n2oapp.framework.config.metadata.pack;

import net.n2oapp.framework.api.pack.MetadataPack;
import net.n2oapp.framework.api.pack.XmlIOBuilder;
import net.n2oapp.framework.config.io.action.*;

/**
 * Набор считывателей действий версии 1.0
 */
public class N2oActionsIOV1Pack implements MetadataPack<XmlIOBuilder<?>> {
    @Override
    public void build(XmlIOBuilder<?> b) {
        b.ios(new InvokeActionElementIOV1(),
                new ShowModalElementIOV1(),
                new OpenPageElementIOV1(),
                new OpenDrawerElementIOV1(),
                new AnchorElementIOV1(),
                new CloseActionElementIOV1(),
                new SetValueElementIOV1(),
                new CopyActionElementIOV1(),
                new ClearActionElementIOV1(),
                new PrintActionElementIOV1(),
                new RefreshActionElementIOV1());
    }
}