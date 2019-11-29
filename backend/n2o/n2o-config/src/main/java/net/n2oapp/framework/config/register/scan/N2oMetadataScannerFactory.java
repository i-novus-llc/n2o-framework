package net.n2oapp.framework.config.register.scan;

import net.n2oapp.engine.factory.integration.spring.OverrideBean;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.factory.MetadataFactory;
import net.n2oapp.framework.api.metadata.aware.MetadataEnvironmentAware;
import net.n2oapp.framework.api.register.SourceInfo;
import net.n2oapp.framework.api.register.scan.MetadataScanner;
import net.n2oapp.framework.api.register.scan.MetadataScannerFactory;
import net.n2oapp.framework.config.factory.AwareFactorySupport;

import java.util.*;

public class N2oMetadataScannerFactory implements MetadataFactory<MetadataScanner>, MetadataScannerFactory, MetadataEnvironmentAware {

    private SortedSet<MetadataScanner> scanners;

    public N2oMetadataScannerFactory() {
        scanners = new TreeSet<>();
    }

    public N2oMetadataScannerFactory(Map<String, MetadataScanner> scanners) {
        ArrayList<MetadataScanner> scannersList = new ArrayList<>(OverrideBean.removeOverriddenBeans(scanners).values());
        this.scanners = new TreeSet<>(scannersList);
    }

    @Override
    public List<? extends SourceInfo> scan() {
        List<SourceInfo> infoList = new ArrayList<>();
        for (MetadataScanner scanner : scanners) {
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
