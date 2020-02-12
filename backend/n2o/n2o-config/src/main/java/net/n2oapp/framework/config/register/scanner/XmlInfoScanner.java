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
 * Сканер XML метаданных
 */
@Component
public class XmlInfoScanner implements MetadataScanner<InfoConstructor>, MetadataEnvironmentAware {
    public static final String DEFAULT_PATTERN = "classpath*:META-INF/conf/**/*.xml";

    private String pattern = DEFAULT_PATTERN;
    private SourceTypeRegister sourceTypeRegister;

    public XmlInfoScanner() {
    }

    public XmlInfoScanner(String pattern) {
        this.pattern = pattern;
    }

    public XmlInfoScanner(String pattern, SourceTypeRegister sourceTypeRegister) {
        this.pattern = pattern;
        this.sourceTypeRegister = sourceTypeRegister;
    }

    @Override
    public List<InfoConstructor> scan() {
        return collectInfo(
                FileSystemUtil.getNodesByLocationPattern(pattern),
                (node) -> RegisterUtil.createXmlInfo(node, sourceTypeRegister));
    }

    @Override
    public void setEnvironment(MetadataEnvironment environment) {
        this.sourceTypeRegister = environment.getSourceTypeRegister();
    }

    public SourceTypeRegister getSourceTypeRegister() {
        return sourceTypeRegister;
    }

    public String getPattern() {
        return pattern;
    }
}
