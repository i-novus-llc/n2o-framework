package net.n2oapp.framework.config.register.event;

import net.n2oapp.framework.api.event.N2oEvent;
import net.n2oapp.framework.config.register.XmlInfo;

/**
 * Событие удаления Info
 * */
public class MetadataRemovedEvent extends N2oEvent {
    private XmlInfo info;

    public MetadataRemovedEvent(Object source, XmlInfo info) {
        super(source);
        this.info = info;
    }

    public XmlInfo getInfo() {
        return info;
    }

    public void setInfo(XmlInfo info) {
        this.info = info;
    }
}
