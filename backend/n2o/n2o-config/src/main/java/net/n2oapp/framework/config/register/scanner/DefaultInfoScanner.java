package net.n2oapp.framework.config.register.scanner;

import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.metadata.aware.MetadataEnvironmentAware;
import net.n2oapp.framework.api.register.SourceTypeRegister;
import net.n2oapp.framework.api.register.scan.MetadataScanner;
import net.n2oapp.framework.config.register.InfoConstructor;
import net.n2oapp.framework.config.register.RegisterUtil;
import net.n2oapp.framework.config.util.FileSystemUtil;
import org.springframework.stereotype.Component;

import java.util.List;

import static net.n2oapp.framework.config.register.RegisterUtil.collectInfo;

/**
 * Сканер XML метаданных, которые используются по умолчанию
 * такие как index, главный header
 */
@Component
public class DefaultInfoScanner implements MetadataScanner<InfoConstructor>, MetadataEnvironmentAware, ScannerComparable {
    public static final String DEFAULT_PATTERN = "classpath*:net/n2oapp/framework/config/default/**/*.xml";

    private String pattern = DEFAULT_PATTERN;
    private SourceTypeRegister sourceTypeRegister;

    public DefaultInfoScanner() {
    }

    public DefaultInfoScanner(String pattern) {
        this.pattern = pattern;
    }

    public DefaultInfoScanner(String pattern, SourceTypeRegister sourceTypeRegister) {
        this.pattern = pattern;
        this.sourceTypeRegister = sourceTypeRegister;
    }

    @Override
    public List<InfoConstructor> scan() {
        List<InfoConstructor> infoConstructors = collectInfo(
                FileSystemUtil.getNodesByLocationPattern(pattern),
                (node) ->
                        RegisterUtil.createXmlInfo(node, sourceTypeRegister));
        return infoConstructors;
    }

    @Override
    public void setEnvironment(MetadataEnvironment environment) {
        this.sourceTypeRegister = environment.getSourceTypeRegister();
    }

    public SourceTypeRegister getSourceTypeRegister() {
        return sourceTypeRegister;
    }

    @Override
    public Integer getOrder() {
        return 0;
    }
}
