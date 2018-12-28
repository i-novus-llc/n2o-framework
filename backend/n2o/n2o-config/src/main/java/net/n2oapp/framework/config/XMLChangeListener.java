package net.n2oapp.framework.config;

import net.n2oapp.framework.api.event.MetadataChangedEvent;
import net.n2oapp.framework.api.event.N2oEventBus;
import net.n2oapp.framework.api.register.MetadataRegister;
import net.n2oapp.framework.api.register.SourceInfo;
import net.n2oapp.framework.api.register.SourceTypeRegister;
import net.n2oapp.framework.config.register.RegisterUtil;
import net.n2oapp.framework.config.register.storage.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.n2oapp.watchdir.FileChangeListener;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Слушатель измениний xml файлов метаданных в папке ${n2o.config.path}
 */
public class XMLChangeListener implements FileChangeListener {
    private static final String JET_BRAINS_SUFFIX = "_jb_";
    private static final Logger log = LoggerFactory.getLogger(XMLChangeListener.class);
    private String watchFolderPath;
    private MetadataRegister configRegister;
    private SourceTypeRegister sourceTypeRegister;
    private N2oEventBus eventBus;

    public XMLChangeListener(String configPath,
                             MetadataRegister configRegister,
                             SourceTypeRegister sourceTypeRegister,
                             N2oEventBus eventBus) {
        this.watchFolderPath = configPath;
        this.configRegister = configRegister;
        this.sourceTypeRegister = sourceTypeRegister;
        this.eventBus = eventBus;
    }

    @Override
    public void fileModified(Path file) {
        try {
            if (isJetBrainsTempFile(file))
                return;
            if (Files.isDirectory(file) || !isXMl(file)) {
                return;
            }
            SourceInfo info = getSourceInfo(file.toAbsolutePath().toString());

            configRegister.update(info);
            eventBus.publish(new MetadataChangedEvent(this, info.getId(), info.getBaseSourceClass()));
            log.debug("Modified handled: " + file);
        } catch (Exception e) {
            log.error("Fail modified handled: " + file, e);
        }
    }

    @Override
    public void fileCreated(Path file) {
        try {
            if (isJetBrainsTempFile(file))
                return;
            if (Files.isDirectory(file) || !isXMl(file)) {
                try {
                    Files.walkFileTree(file,
                            new SimpleFileVisitor<Path>() {
                                @Override
                                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws
                                        IOException {
                                    addSourceFromMemory(file);
                                    return super.visitFile(file, attrs);
                                }
                            }
                    );
                } catch (IOException ignored) {
                    log.error("Created not handled: error add directory " + file);
                }
                return;
            }
            addSourceFromMemory(file);
        } catch (Exception e) {
            log.error("Fail created handled: " + file, e);
        }
    }

    @Override
    public void fileDeleted(Path file) {
        try {
            if (isJetBrainsTempFile(file))
                return;
            if (!Files.isDirectory(file) && isXMl(file)) {
                deleteSourceFromMemory(file.toAbsolutePath().toString());
            }
        } catch (Exception e) {
            log.error("Fail deleted handled: " + file, e);
        }
    }

    private void deleteSourceFromMemory(String path) {
        SourceInfo info = getSourceInfo(path);
        configRegister.remove(info.getId(), info.getBaseSourceClass());
        eventBus.publish(new MetadataChangedEvent(this, info.getId(), info.getBaseSourceClass()));
        log.debug("Deleted handled: " + path);
    }

    private SourceInfo getSourceInfo(String path) {
        return RegisterUtil.createFolderInfo(Node.byAbsolutePath(path, watchFolderPath), sourceTypeRegister);
    }

    private void addSourceFromMemory(Path path) {
        SourceInfo info = getSourceInfo(path.toString());
        configRegister.add(info);
        eventBus.publish(new MetadataChangedEvent(this, info.getId(), info.getBaseSourceClass()));
        log.debug("Created handled: " + path);
    }

    private static boolean isXMl(Path file) {
        String extension = "";
        String fileName = file.getFileName().toString();
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i + 1);
        }
        return "xml".equals(extension);
    }

    /**
     * @param file - файл
     * @return проверяет реальный файл (false) или временный от JetBrains (true)
     */
    private static boolean isJetBrainsTempFile(Path file) {
        String fileName = file.normalize().toString();
        return fileName.indexOf(JET_BRAINS_SUFFIX) > 0;
    }

}
