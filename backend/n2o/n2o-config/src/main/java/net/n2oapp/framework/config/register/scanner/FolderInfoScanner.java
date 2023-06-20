package net.n2oapp.framework.config.register.scanner;

import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.metadata.aware.MetadataEnvironmentAware;
import net.n2oapp.framework.api.register.SourceTypeRegister;
import net.n2oapp.framework.config.register.InfoConstructor;
import net.n2oapp.framework.config.register.RegisterUtil;
import net.n2oapp.framework.config.register.storage.PathUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static net.n2oapp.framework.config.register.RegisterUtil.collectInfo;
import static net.n2oapp.framework.config.register.storage.PathUtil.convertRootPathToUrl;
import static net.n2oapp.framework.config.util.FileSystemUtil.getNodesByLocationPattern;

/**
 * Сканер папки переопределений на диске сервера
 */
@Component
public class FolderInfoScanner implements OverrideInfoScanner<InfoConstructor>, MetadataEnvironmentAware {

    private final Collection<String> configPaths;
    private SourceTypeRegister sourceTypeRegister;

    public FolderInfoScanner(@Value("${n2o.config.path}") String configPath,
                             @Value("${n2o.project.path:}") List<String> projectPaths,
                             @Value("${n2o.config.ignores}") List<String> ignores,
                             XmlInfoScanner xmlInfoScanner) {
        configPaths = PathUtil.getConfigPaths(configPath, projectPaths, xmlInfoScanner.getPattern(), ignores);
    }

    @Override
    public List<InfoConstructor> scan() {
        List<String> xmlPattern = new ArrayList<>();
        for (String s : configPaths) {
            String uri = convertRootPathToUrl(s);
            xmlPattern.add(PathUtil.convertUrlToPattern(uri, "xml", "*.*"));
        }

        return collectInfo(getNodesByLocationPattern(xmlPattern),
                n -> RegisterUtil.createFolderInfo(n, sourceTypeRegister));
    }

    @Override
    public void setEnvironment(MetadataEnvironment environment) {
        this.sourceTypeRegister = environment.getSourceTypeRegister();
    }
}
