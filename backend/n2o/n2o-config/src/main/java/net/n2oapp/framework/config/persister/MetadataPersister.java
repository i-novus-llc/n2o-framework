package net.n2oapp.framework.config.persister;

import net.n2oapp.framework.api.event.N2oEventBus;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.global.N2oMetadata;
import net.n2oapp.framework.api.metadata.persister.NamespacePersisterFactory;
import net.n2oapp.framework.api.metadata.reader.ConfigMetadataLocker;
import net.n2oapp.framework.api.register.MetadataRegister;
import net.n2oapp.framework.api.register.SourceInfo;
import net.n2oapp.framework.api.register.SourceTypeRegister;
import net.n2oapp.framework.config.ConfigStarter;
import net.n2oapp.framework.config.reader.XmlMetadataLoader;
import net.n2oapp.framework.config.register.ConfigId;
import net.n2oapp.framework.config.register.InfoConstructor;
import net.n2oapp.framework.config.register.InfoStatus;
import net.n2oapp.framework.config.register.XmlInfo;
import net.n2oapp.framework.config.register.event.ConfigPersistEvent;
import net.n2oapp.framework.config.register.event.MetadataRemovedEvent;
import net.n2oapp.framework.config.register.storage.PathUtil;
import net.n2oapp.framework.config.util.FileSystemUtil;
import net.n2oapp.framework.config.util.XmlUtil;
import net.n2oapp.watchdir.WatchDir;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.XMLOutputter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;

import static net.n2oapp.framework.config.register.RegisterUtil.createXmlInfo;
import static net.n2oapp.framework.config.register.storage.PathUtil.convertRootPathToUrl;
import static net.n2oapp.framework.config.register.storage.PathUtil.normalize;
import static net.n2oapp.framework.config.util.FileSystemUtil.removeContentByUri;
import static net.n2oapp.framework.config.util.FileSystemUtil.saveContentToFile;

/**
 * Сохраняет метаданные в файлы
 */
public class MetadataPersister {
    @Autowired
    private N2oEventBus eventBus;
    @Autowired
    private ConfigMetadataLocker configMetadataLocker;
    @Autowired
    private NamespacePersisterFactory persisterFactory;
    @Autowired
    private WatchDir watchDir;
    @Autowired
    private MetadataRegister metadataRegister;
    @Autowired
    private XmlMetadataLoader metadataReader;
    @Autowired
    private SourceTypeRegister metaModelRegister;
    private boolean readonly;

    @Autowired
    private ConfigStarter configStarter;


    public MetadataPersister() {
        this.readonly = false;
    }

    public MetadataPersister(boolean readonly) {
        this.readonly = readonly;
    }

    public boolean isReadonly() {
        return readonly;
    }

    private void checkLock() {
        if (isReadonly() || configMetadataLocker.isLocked())
            MetadataPersisterException.throwPersisterLock();
    }

    public <T extends N2oMetadata> void persist(T n2o) {
        persist(n2o, null);
    }

    @SuppressWarnings("unchecked")
    public <T extends N2oMetadata> void persist(T n2o, String directory) {
        boolean isCreate = metadataRegister.contains(n2o.getId(), n2o.getClass());
        checkLock();
        Element element = persisterFactory.produce(n2o).persist(n2o, n2o.getNamespace());
        Document doc = new Document();
        doc.addContent(element);
        XMLOutputter xmlOutput = new XMLOutputter();
        xmlOutput.setFormat(XmlUtil.N2O_FORMAT);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            xmlOutput.output(doc, outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        InfoConstructor info = findOrCreateXmlInfo(n2o, directory);
        String path = PathUtil.convertUrlToAbsolutePath(info.getURI());
        if (path == null)
            throw new IllegalStateException();
        watchDir.skipOn(path);
        try {
            saveContentToFile(new ByteArrayInputStream(outputStream.toByteArray()), new File(path));
            metadataRegister.update(info);//if exists
            metadataRegister.add(info);
            eventBus.publish(new ConfigPersistEvent(this, info, isCreate));
        } finally {
            watchDir.takeOn(path);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends SourceMetadata> void persist(String id, Class<T> metadataClass, InputStream xml, String directory) {
        boolean isCreate = !metadataRegister.contains(id, metadataClass);
        checkLock();
        T n2o = metadataReader.read(id, xml, metadataClass);
        InfoConstructor infoC = findOrCreateXmlInfo(n2o, directory);
        String path = PathUtil.convertUrlToAbsolutePath(infoC.getURI());
        if (path == null)
            throw new IllegalStateException();
        try {
            watchDir.skipOn(path);
            FileSystemUtil.saveContentToFile(xml, new File(path));
            metadataRegister.update(infoC);//if exists
            metadataRegister.add(infoC);
            eventBus.publish(new ConfigPersistEvent(this, infoC, isCreate));
        } finally {
            watchDir.takeOn(path);
        }
    }


    private <T extends SourceMetadata> InfoConstructor findOrCreateXmlInfo(T n2o, String directory) {
        if (metadataRegister.contains(n2o.getId(), n2o.getClass())) {
            SourceInfo info = metadataRegister.get(n2o.getId(), n2o.getClass());
            if (info instanceof XmlInfo) {
                XmlInfo xmlInfo = (XmlInfo) info;
                if (directory != null && !normalize(xmlInfo.getDirectory()).equalsIgnoreCase(normalize(directory))) {
                    throw new IllegalArgumentException("Attempt to make a duplicate file.");
                }
                if (InfoStatus.isSystemFile(xmlInfo)) {
                    //create server file from system file
                    InfoConstructor infoC = new InfoConstructor(xmlInfo);
                    infoC.setAncestor(null);//why??
                    return infoC;
                } else {
                    //update server file
                    InfoConstructor infoConstructor = new InfoConstructor(xmlInfo);
                    return infoConstructor;
                }
            }
        }
        //create server file
        String localPath = PathUtil.concatFileNameAndBasePath(n2o.getId() + "." + n2o.getMetadataType() + ".xml", directory);
        String path = convertRootPathToUrl(configStarter.getConfigPath());
        String uri = PathUtil.concatAbsoluteAndLocalPath(path, localPath);
        return createXmlInfo(localPath, uri, metaModelRegister);
    }


    public void remove(String id, Class<? extends N2oMetadata> metadataClass) {
        checkLock();
        XmlInfo info = (XmlInfo) metadataRegister.get(id, metadataClass);
        if (info != null && info.getURI() != null) {
            String path = PathUtil.convertUrlToAbsolutePath(info.getURI());
            if (path == null)
                throw new IllegalStateException();
            watchDir.skipOn(info.getURI());
            try {
                if (removeContentByUri(info.getURI())) {
                    ConfigId configId = info.getConfigId();
                    metadataRegister.remove(configId.getId(), configId.getBaseSourceClass());
                    eventBus.publish(new MetadataRemovedEvent(this, info));
                } else {
                    throw new N2oException("n2o.couldNotDeleteFile").addData(info.getLocalPath());
                }
            } finally {
                watchDir.takeOn(path);
            }
        }
    }

    public enum RestoreMode {
        REMOVE, REPLACE
    }

    public void setEventBus(N2oEventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void setConfigMetadataLocker(ConfigMetadataLocker configMetadataLocker) {
        this.configMetadataLocker = configMetadataLocker;
    }

    public void setPersisterFactory(NamespacePersisterFactory persisterFactory) {
        this.persisterFactory = persisterFactory;
    }

    public void setWatchDir(WatchDir watchDir) {
        this.watchDir = watchDir;
    }

    public void setMetadataRegister(MetadataRegister metadataRegister) {
        this.metadataRegister = metadataRegister;
    }

    public void setMetadataReader(XmlMetadataLoader metadataReader) {
        this.metadataReader = metadataReader;
    }

    public void setMetaModelRegister(SourceTypeRegister metaModelRegister) {
        this.metaModelRegister = metaModelRegister;
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }
}

