package net.n2oapp.framework.api.metadata.global.view.region;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.SourceComponent;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.control.N2oComponent;

/**
 * Модель региона 2.0
 */
@Getter
@Setter
public abstract class N2oRegion extends N2oComponent implements SourceMetadata {
    private String id;
    private String width;
    @Deprecated
    private String place;
    private SourceComponent[] content;

    @Override
    public String getPostfix() {
        return "region";
    }

    public String getAlias() {
        return "w";
    }
}
