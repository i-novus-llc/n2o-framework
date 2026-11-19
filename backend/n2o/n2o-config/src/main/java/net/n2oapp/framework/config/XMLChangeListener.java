package net.n2oapp.framework.config;

import net.n2oapp.framework.api.event.MetadataChangedEvent;
import net.n2oapp.framework.api.event.N2oEventBus;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.register.MetadataRegister;
import net.n2oapp.framework.api.register.SourceInfo;
import net.n2oapp.framework.api.register.SourceTypeRegister;
import net.n2oapp.framework.config.register.FileInfo;
import net.n2oapp.framework.config.register.InfoConstructor;
import net.n2oapp.framework.config.register.RegisterUtil;
import net.n2oapp.framework.config.register.scanner.XmlInfoScanner;
import net.n2oapp.framework.config.register.storage.Node;
import net.n2oapp.watchdir.FileChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;
import java.util.List;

/**
 * Слушатель измениний xml файлов метаданных в папке ${n2o.config.path && n2o.project.path}
 */
public class XMLChangeListener implements FileChangeListener {
    private static final Logger log = LoggerFactory.getLogger(XMLChangeListener.class);
    private final Collection<String> watchFolderPaths;
    private final MetadataRegister configRegister;
    private final SourceTypeRegister sourceTypeRegister;
    private final N2oEventBus eventBus;
    private final List<InfoConstructor> defaultInfos;

    public XMLChangeListener(Collection<String> configPath,
                             MetadataRegister configRegister,
                             SourceTypeRegister sourceTypeRegister,
                             N2oEventBus eventBus,
                             String pattern) {
        this.watchFolderPaths = configPath;
        this.configRegister = configRegister;
        this.sourceTypeRegister = sourceTypeRegister;
        this.eventBus = eventBus;
        this.defaultInfos = new XmlInfoScanner(pattern, sourceTypeRegister).scan();
    }

    @Override
    public void fileModified(Path file) {
        try {
            if (file.toFile().isDirectory() || !isXMl(file)) {
                return;
            }
            SourceInfo info = getSourceInfo(file.toAbsolutePath().toString());

            configRegister.add(info);
            eventBus.publish(new MetadataChangedEvent(this, info.getId(), info.getBaseSourceClass()));
            log.debug("Modified handled: {}", file);
        } catch (Exception e) {
            log.error("Fail modified handled: {}", file, e);
        }
    }

    @Override
    public void fileCreated(Path file) {
        try {
            if (file.toFile().isDirectory() || !isXMl(file)) {
                processDirectoryOrNonXmlFile(file);
                return;
            }
            addSourceFromMemory(file);
        } catch (Exception e) {
            log.error("Fail created handled {}", file, e);
        }
    }

    private void processDirectoryOrNonXmlFile(Path file) {
        try {
            Files.walkFileTree(file, new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (isXMl(file)) addSourceFromMemory(file);
                    return super.visitFile(file, attrs);
                }
            });
        } catch (IOException ignored) {
            if (file.toFile().isDirectory())
                log.error("Created not handled: error add directory {}", file);
        }
    }

    @Override
    public void fileDeleted(Path file) {
        try {
            if (!file.toFile().isDirectory() && isXMl(file)) {
                deleteSourceFromMemory(file.toAbsolutePath().toString());
            } else {
                deleteSourceFromMemoryByPath(file.toAbsolutePath().toString());
            }
        } catch (Exception e) {
            log.error("Fail deleted handled: {}", file, e);
        }
    }

    private void deleteSourceFromMemory(String path) {
        SourceInfo info = getSourceInfo(path);
        configRegister.remove(info.getId(), info.getBaseSourceClass());
        restoreDefault(info.getId(), info.getBaseSourceClass());
        eventBus.publish(new MetadataChangedEvent(this, info.getId(), info.getBaseSourceClass()));
        log.debug("Deleted handled: {}", path);
    }

    private void deleteSourceFromMemoryByPath(String path) {
        if (Path.of(path).getFileName().toString().contains(".")) return;

        Node node = Node.byAbsolutePath(path, getConfigPath(path));
        List<SourceInfo> sourceInfoList = configRegister.find(s -> s instanceof FileInfo fileInfo && fileInfo.getLocalPath().startsWith(node.getLocalPath()));
        for (SourceInfo info : sourceInfoList) {
            configRegister.remove(info.getId(), info.getBaseSourceClass());
            restoreDefault(info.getId(), info.getBaseSourceClass());
            eventBus.publish(new MetadataChangedEvent(this, info.getId(), info.getBaseSourceClass()));
        }
        log.debug("Deleted handled: {}", path);
    }

    /**
     * Восстанавливает дефолтный classpath-файл, если он есть.
     * Порядок разрешения дубликатов — как в N2oMetadataRegister.addAll()
     * (последовательный put → побеждает последний), поэтому итерируем с конца.
     * Сравнение id — как в MetaKey.equals(): без учёта регистра.
     */
    private void restoreDefault(String id, Class<? extends SourceMetadata> baseSourceClass) {
        for (int i = defaultInfos.size() - 1; i >= 0; i--) {
            InfoConstructor defaultInfo = defaultInfos.get(i);
            if (defaultInfo.getId().equalsIgnoreCase(id) && defaultInfo.getBaseSourceClass().equals(baseSourceClass)) {
                configRegister.add(defaultInfo);
                log.debug("Restored default from classpath: {}", defaultInfo.getUri());
                return;
            }
        }
        log.debug("No default in classpath for {}", id);
    }

    private SourceInfo getSourceInfo(String path) {
        return RegisterUtil.createFolderInfo(Node.byAbsolutePath(path, getConfigPath(path)), sourceTypeRegister);
    }

    private void addSourceFromMemory(Path path) {
        SourceInfo info = getSourceInfo(path.toString());
        configRegister.add(info);
        eventBus.publish(new MetadataChangedEvent(this, info.getId(), info.getBaseSourceClass()));
        log.debug("Created handled: {}", path);
    }

    private static boolean isXMl(Path file) {
        return ("" + file.getFileName()).toLowerCase().endsWith(".xml");
    }

    private String getConfigPath(String file) {
        for (String path : watchFolderPaths) {
            if (file.startsWith(path)) return path;
        }
        return "";
    }
}