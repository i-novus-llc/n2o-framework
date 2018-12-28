package net.n2oapp.framework.config.register.scanner;

import net.n2oapp.framework.api.register.SourceInfo;
import net.n2oapp.framework.api.register.scan.MetadataScanner;
import net.n2oapp.framework.config.register.InfoConstructor;

import java.util.ArrayList;
import java.util.List;

public class MockInfoScanner implements MetadataScanner {
    private List<InfoConstructor> infoList = new ArrayList<>();

    public MockInfoScanner() {
    }

    public MockInfoScanner(List<InfoConstructor> infoList) {
        this.infoList = infoList;
    }

    @Override
    public List<? extends SourceInfo> scan() {
        return infoList;
    }
}
