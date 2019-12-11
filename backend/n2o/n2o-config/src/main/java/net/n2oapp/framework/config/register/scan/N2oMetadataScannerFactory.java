package net.n2oapp.framework.config.register.scan;

import net.n2oapp.engine.factory.integration.spring.OverrideBean;
import net.n2oapp.framework.api.MetadataEnvironment;
import net.n2oapp.framework.api.factory.MetadataFactory;
import net.n2oapp.framework.api.metadata.aware.MetadataEnvironmentAware;
import net.n2oapp.framework.api.register.SourceInfo;
import net.n2oapp.framework.api.register.scan.MetadataScanner;
import net.n2oapp.framework.api.register.scan.MetadataScannerFactory;
import net.n2oapp.framework.config.factory.AwareFactorySupport;
import net.n2oapp.framework.config.register.scanner.DefaultInfoScanner;
import net.n2oapp.framework.config.register.scanner.OverrideInfoScanner;

import java.util.*;

public class N2oMetadataScannerFactory implements MetadataFactory<MetadataScanner>, MetadataScannerFactory, MetadataEnvironmentAware {

    private LinkedList<MetadataScanner> scanners;

    public N2oMetadataScannerFactory() {
        scanners = new LinkedList<>();
    }

    public N2oMetadataScannerFactory(Map<String, MetadataScanner> scanners) {
        ArrayList<MetadataScanner> scannersList = new ArrayList<>(OverrideBean.removeOverriddenBeans(scanners).values());
        this.scanners = getSortedScanners(scannersList);
    }

    @Override
    public List<? extends SourceInfo> scan() {
        List<SourceInfo> infoList = new ArrayList<>();
        for (MetadataScanner scanner : scanners) {
            infoList.addAll(scanner.scan());
        }
        return infoList;
    }

    private LinkedList<MetadataScanner> getSortedScanners(List<MetadataScanner> allScaners) {
        if (allScaners == null || allScaners.isEmpty())
            return new LinkedList<>();
        LinkedList<MetadataScanner> sorted = new LinkedList<>();
        List<OverrideInfoScanner> overrideInfoScanners = new ArrayList<>();
        for (MetadataScanner scanner : allScaners) {
            if (scanner instanceof OverrideInfoScanner) {
                overrideInfoScanners.add((OverrideInfoScanner) scanner);
            } else if (scanner instanceof DefaultInfoScanner){
                sorted.addFirst(scanner);
            } else {
                sorted.addLast(scanner);
            }
        }
        overrideInfoScanners.forEach(s -> sorted.addLast(s));
        return sorted;
    }

    @Override
    public N2oMetadataScannerFactory add(MetadataScanner... scanners) {
        if (scanners == null)
            return this;
        List<MetadataScanner> allScanners = new ArrayList<>();
        allScanners.addAll(Arrays.asList(scanners));
        allScanners.addAll(this.scanners);
        this.scanners = getSortedScanners(allScanners);
        return this;
    }

    @Override
    public void setEnvironment(MetadataEnvironment environment) {
        scanners.forEach(o -> AwareFactorySupport.enrich(o, environment));
    }
}
