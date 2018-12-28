package net.n2oapp.framework.config.register;

import net.n2oapp.framework.api.metadata.MetaModel;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.local.context.CompileContext;
import net.n2oapp.framework.api.reader.SourceLoader;
import net.n2oapp.framework.api.register.MetaType;

import java.util.HashSet;
import java.util.Set;

/**
 * Конструктор для Info
 */
@Deprecated
public final class InfoConstructor extends XmlInfo {

    public InfoConstructor() {
    }

    public InfoConstructor(XmlInfo info) {
        override = info.isOverride();
        configId = info.getConfigId();
        context = info.getContext();
        dependents = new HashSet<>(info.getDependents());
        localPath = info.getLocalPath();
        origin = info.getOrigin();
        ancestor = info.getAncestor();
    }

    public InfoConstructor(ConfigId configId) {
        super(configId);
    }

    public InfoConstructor(String id, MetaType metaModel) {
        super(new ConfigId(id, metaModel));
    }

    public XmlInfo construct() {
        return new XmlInfo(this);
    }

    public String getId() {
        return configId.getId();
    }

    @SuppressWarnings("unchecked")
    public Class<? extends SourceMetadata> getBaseSourceClass() {
        return configId.getBaseSourceClass();
    }

    public void setConfigId(ConfigId configId) {
        this.configId = configId;
    }


    public void setContext(CompileContext context) {
        this.context = context;
    }

    public void setReaderClass(Class<? extends SourceLoader> readerClass) {
    }

    public void setDependents(Set<ConfigId> dependents) {
        this.dependents = dependents;
    }

    public void addDependent(ConfigId dependent) {
        dependents.add(dependent);
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setOrigin(Origin origin) {
        this.origin = origin;
    }

    public void setAncestor(XmlInfo ancestor) {
        this.ancestor = ancestor;
    }

    public void setOverride(boolean override) {
        this.override = override;
    }
}
