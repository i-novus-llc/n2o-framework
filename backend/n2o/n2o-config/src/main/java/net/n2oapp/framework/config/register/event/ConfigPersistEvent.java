package net.n2oapp.framework.config.register.event;

import net.n2oapp.framework.api.event.N2oEvent;
import net.n2oapp.framework.config.register.XmlInfo;

/**
 * Событие добавления Info
 * */
public class ConfigPersistEvent extends N2oEvent {
    private XmlInfo info;
    private boolean isCreate;

    public ConfigPersistEvent(Object source, XmlInfo info, boolean isCreate) {
        super(source);
        this.info = info;
        this.isCreate = isCreate;
    }

    public XmlInfo getInfo() {
        return info;
    }

    public void setInfo(XmlInfo info) {
        this.info = info;
    }

    public boolean isCreate() {
        return isCreate;
    }

    public void setIsCreate(boolean isCreate) {
        this.isCreate = isCreate;
    }
}
