package net.n2oapp.framework.config.register.mock;

import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.global.N2oMetadata;
import net.n2oapp.framework.api.metadata.global.view.page.N2OStandardPage;
import net.n2oapp.framework.api.metadata.global.view.page.N2oPage;
import net.n2oapp.framework.api.register.DynamicMetadataProvider;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class TestDynamicMetadataProvider implements DynamicMetadataProvider {

    String code;
    List<? extends N2oMetadata> metadataList;
    boolean autogen;

    public TestDynamicMetadataProvider(String code, List<? extends N2oMetadata> metadataList) {
        this.code = code;
        this.metadataList = metadataList;
        this.autogen = false;
    }

    public TestDynamicMetadataProvider(String code) {
        this.code = code;
        this.autogen = true;
    }

    @Override
    public Collection<Class<? extends SourceMetadata>> getMetadataClasses() {
        return Arrays.asList(N2oPage.class);
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public List<? extends N2oMetadata> read(String context) {
        if (autogen) {
            N2oPage page = new N2OStandardPage();
            page.setId(code+'$'+context);
            return Arrays.asList(page);
        }
        return metadataList;
    }

    @Override
    public boolean cache(String params) {
        return true;
    }
}
