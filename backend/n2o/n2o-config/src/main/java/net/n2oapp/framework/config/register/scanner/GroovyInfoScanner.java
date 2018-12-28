package net.n2oapp.framework.config.register.scanner;

import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.metadata.aware.MetadataEnvironmentAware;
import net.n2oapp.framework.api.register.SourceTypeRegister;
import net.n2oapp.framework.api.register.scan.MetadataScanner;
import net.n2oapp.framework.config.register.GroovyInfo;
import net.n2oapp.framework.config.register.InfoConstructor;
import net.n2oapp.framework.config.register.RegisterUtil;
import net.n2oapp.framework.config.register.storage.Node;
import net.n2oapp.framework.config.util.FileSystemUtil;
import org.springframework.stereotype.Component;

import java.util.List;

import static net.n2oapp.framework.config.register.RegisterUtil.collectInfo;

/**
 * Сканер groovy метаданных
 */
@Component
public class GroovyInfoScanner implements MetadataScanner<GroovyInfo>, MetadataEnvironmentAware {

    private String pattern = "classpath*:META-INF/conf/**/*.*.groovy";
    private SourceTypeRegister sourceTypeRegister;

    public GroovyInfoScanner() {
    }

    public GroovyInfoScanner(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public List<GroovyInfo> scan() {
        return collectInfo(FileSystemUtil.getNodesByLocationPattern(pattern),
                n -> RegisterUtil.createScriptInfo(n, sourceTypeRegister));
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public void setEnvironment(MetadataEnvironment environment) {
        this.sourceTypeRegister = environment.getSourceTypeRegister();
    }
}
