package net.n2oapp.framework.api.metadata.dataprovider;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractDataProvider implements DataProvider {
    private String resultMapping;
    private String namespaceUri;
}
