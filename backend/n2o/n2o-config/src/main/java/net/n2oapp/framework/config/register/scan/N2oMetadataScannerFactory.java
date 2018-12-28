package net.n2oapp.framework.config.register.scan;

import net.n2oapp.engine.factory.integration.spring.OverrideBean;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.factory.MetadataFactory;
import net.n2oapp.framework.api.metadata.aware.MetadataEnvironmentAware;
import net.n2oapp.framework.api.register.SourceInfo;
import net.n2oapp.framework.api.register.scan.MetadataScanner;
import net.n2oapp.framework.api.register.scan.MetadataScannerFactory;
import net.n2oapp.framework.config.factory.AwareFactorySupport;
import net.n2oapp.framework.config.register.scanner.OverrideInfoScanner;

import java.util.*;

public class N2oMetadataScannerFactory implements MetadataFactory<MetadataScanner>, MetadataScannerFactory, MetadataEnvironmentAware {

    private List<MetadataScanner> scanners;

    public N2oMetadataScannerFactory() {
        scanners = new ArrayList<>();
    }

    public N2oMetadataScannerFactory(Map<String, MetadataScanner> scanners) {
        this.scanners = new ArrayList<>(OverrideBean.removeOverriddenBeans(scanners).values());
    }

    @Override
    public List<? extends SourceInfo> scan() {
        LinkedList<MetadataScanner> sorted = new LinkedList<>();
        for (MetadataScanner scanner : scanners) {
            if (scanner instanceof OverrideInfoScanner)
                sorted.addLast(scanner);
            else
                sorted.addFirst(scanner);
        }
        List<SourceInfo> infoList = new ArrayList<>();
        for (MetadataScanner scanner : sorted) {
            infoList.addAll(scanner.scan());
        }
        return infoList;
    }


    @Override
    public N2oMetadataScannerFactory add(MetadataScanner... scanners) {
        this.scanners.addAll(Arrays.asList(scanners));
        return this;
    }

    @Override
    public void setEnvironment(MetadataEnvironment environment) {
        scanners.forEach(o -> AwareFactorySupport.enrich(o, environment));
    }
}
