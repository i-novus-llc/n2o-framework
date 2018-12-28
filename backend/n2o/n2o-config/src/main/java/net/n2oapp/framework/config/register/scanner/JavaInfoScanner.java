package net.n2oapp.framework.config.register.scanner;

import net.n2oapp.framework.api.register.DynamicMetadataProvider;
import net.n2oapp.framework.api.register.scan.MetadataScanner;
import net.n2oapp.framework.config.register.JavaInfo;
import net.n2oapp.framework.config.register.dynamic.N2oDynamicMetadataProviderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Заполнение регистра информацией о провайдерах метаданных
 */
@Component
public class JavaInfoScanner implements MetadataScanner<JavaInfo> {
    private N2oDynamicMetadataProviderFactory factory;

    public JavaInfoScanner(@Autowired N2oDynamicMetadataProviderFactory factory) {
        this.factory = factory;
    }

    @Override
    public List<JavaInfo> scan() {
        List<JavaInfo> list = new ArrayList<>();
        List<DynamicMetadataProvider> providers = factory.produceList((f, g) -> true, true);
        providers.stream().filter(p -> p.getMetadataClasses() != null)
                .forEach(p -> p.getMetadataClasses().forEach(mc -> list.add(new JavaInfo(p.getCode(), mc))));
        return list;
    }

}
