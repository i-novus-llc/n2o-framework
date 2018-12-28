package net.n2oapp.framework.config.register.scanner;

import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.metadata.aware.MetadataEnvironmentAware;
import net.n2oapp.framework.api.register.SourceTypeRegister;
import net.n2oapp.framework.config.register.InfoConstructor;
import net.n2oapp.framework.config.register.RegisterUtil;
import net.n2oapp.framework.config.register.storage.PathUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

import static net.n2oapp.framework.config.register.RegisterUtil.collectInfo;
import static net.n2oapp.framework.config.register.storage.PathUtil.convertRootPathToUrl;
import static net.n2oapp.framework.config.util.FileSystemUtil.getNodesByLocationPattern;

/**
 * Сканер папки переопределений на диске сервера
 */
@Component
public class FolderInfoScanner implements OverrideInfoScanner<InfoConstructor>, MetadataEnvironmentAware {

    private String configPath;
    private SourceTypeRegister sourceTypeRegister;

    public FolderInfoScanner(@Value("${n2o.config.path}") String configPath) {
        this.configPath = configPath;
    }

    @Override
    public List<InfoConstructor> scan() {
        String uri = convertRootPathToUrl(configPath);
        String xmlPattern = PathUtil.convertUrlToPattern(uri, "xml", "*.*");
       // String groovyPattern = PathUtil.convertUrlToPattern(path, "groovy", "*.*");
        /*nodes.addAll(collectInfo(getNodesByLocationPattern(groovyPattern), FolderInfoScanner.class,
                (node, scannerClass) -> RegisterUtil.createScriptInfo(node, scannerClass)));*/
        return collectInfo(getNodesByLocationPattern(xmlPattern),
                n -> RegisterUtil.createFolderInfo(n, sourceTypeRegister));
    }

    @Override
    public void setEnvironment(MetadataEnvironment environment) {
        this.sourceTypeRegister = environment.getSourceTypeRegister();
    }
}
