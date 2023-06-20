package net.n2oapp.framework.config.register;

import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.register.SourceInfo;
import net.n2oapp.framework.api.register.SourceTypeRegister;
import net.n2oapp.framework.api.register.scan.MetadataScanner;
import net.n2oapp.framework.config.reader.XmlMetadataLoader;
import net.n2oapp.framework.config.register.dynamic.JavaSourceLoader;
import net.n2oapp.framework.config.register.storage.Node;
import net.n2oapp.framework.config.register.storage.PathUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;


/**
 * Утилитный класс для работы с регистром
 */
public class RegisterUtil {
    private static final Logger log = LoggerFactory.getLogger(RegisterUtil.class);
    public static String DEFAULT_DYNAMIC_LOCAL_PATH = "java/override/";

    /**
     * Получить ConfigId по локальному пути файла
     * @param path локальный путь к файлу
     * @return ConfigId
     */
    public static ConfigId getConfigIdByLocalPath(String path, SourceTypeRegister sourceTypeRegister) {
        String[] parts = getIdAndPostfix(path);
        return new ConfigId(parts[0], sourceTypeRegister.get(parts[1]));
    }

    public static String[] getIdAndPostfix(String absolutePath) {
        String crossOsPath = absolutePath.replace('\\', '/');
        int idx = crossOsPath.lastIndexOf("/") + 1;
        String filename = crossOsPath.substring(idx);
        String[] parts = filename.split("\\.");
        if (parts.length != 3) {
            throw new IllegalStateException(
                    "Metadata filename '" + absolutePath + "' should canResolved identifier, type of n2oMetadata, xml extension. For example: ResourceGroup.object.xml");
        }
        return parts;
    }

    /*
    * Создает динамическое info
    * */
    public static InfoConstructor createDynamicInfo(String id, Class<? extends SourceMetadata> sourceClass,
                                                    SourceTypeRegister sourceTypeRegister) {
        InfoConstructor info = new InfoConstructor(new ConfigId(id, sourceTypeRegister.get(sourceClass)));
        info.setLocalPath(PathUtil.concatFileNameAndBasePath(info.getConfigId().getId()
                + "." + info.getConfigId().getType() + ".dynamic", DEFAULT_DYNAMIC_LOCAL_PATH));
        baseForDynamicInfo(info);
        return info;
    }


    public static InfoConstructor createFolderInfo(Node node, SourceTypeRegister sourceTypeRegister) {
        InfoConstructor info = createXmlInfo(node, sourceTypeRegister);
        info.setOverride(true);
        return info;
    }

    public static InfoConstructor createXmlInfo(Node node, SourceTypeRegister sourceTypeRegister) {
        return createXmlInfo(node.getLocalPath(), node.getURI(), sourceTypeRegister);
    }


    public static InfoConstructor createXmlInfo(String localPath, String uri, SourceTypeRegister sourceTypeRegister) {
        return createXmlInfo(null, localPath, uri, sourceTypeRegister);
    }

    public static InfoConstructor createXmlInfo(Class<? extends MetadataScanner> scannerClass, String localPath, String uri, SourceTypeRegister sourceTypeRegister) {
        InfoConstructor info = new InfoConstructor(getConfigIdByLocalPath(localPath, sourceTypeRegister));
        info.setLocalPath(localPath);
        info.setUri(uri);
        info.setScannerClass(scannerClass);
        baseForXmlInfo(info);
        return info;
    }

    @Deprecated
    private static void baseForDynamicInfo(InfoConstructor info) {
        info.setOrigin(Origin.dynamic);
        info.setReaderClass(JavaSourceLoader.class);
    }

    private static void baseForXmlInfo(InfoConstructor info) {
        info.setOrigin(Origin.xml);
        info.setReaderClass(XmlMetadataLoader.class);
    }

    public static <T extends SourceInfo> List<T> collectInfo(List<Node> nodes,
                                                         Function<Node, T> mapper) {
        List<T> infoList = new ArrayList<>();
        for (Node node : nodes) {
            try {
                infoList.add(mapper.apply(node));
            } catch (Exception e) {
                log.error("Bad format config file [" + node.getName() + "]", e);
            }
        }
        return infoList;
    }

    public static List<XmlInfo> retrieveInfoTree(XmlInfo info, List<XmlInfo> infos) {
        infos.add(info);
        XmlInfo ancestor = info.getAncestor();
        if (ancestor != null) {
            retrieveInfoTree(ancestor, infos);
        }
        return infos;
    }


}
