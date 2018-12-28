package net.n2oapp.framework.config.audit.git.util.mock;

import net.n2oapp.framework.config.register.InfoConstructor;
import net.n2oapp.framework.config.register.scanner.FolderInfoScanner;

import java.util.List;

public class FolderInfoScannerMock  extends FolderInfoScanner {

    public FolderInfoScannerMock(String configPath) {
        super(configPath);
    }

    @Override
    public List<InfoConstructor> scan() {
        return null;
    }

}
