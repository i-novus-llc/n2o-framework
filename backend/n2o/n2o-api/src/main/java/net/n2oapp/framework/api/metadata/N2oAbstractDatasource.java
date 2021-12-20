package net.n2oapp.framework.api.metadata;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.control.N2oComponent;

@Getter
@Setter
public abstract class N2oAbstractDatasource extends N2oComponent implements IdAware {

    private String id;
}
