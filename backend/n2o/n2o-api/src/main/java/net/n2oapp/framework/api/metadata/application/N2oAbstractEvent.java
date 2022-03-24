package net.n2oapp.framework.api.metadata.application;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.Source;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.control.N2oComponent;

/**
 * Абстрактная модель события
 */
@Getter
@Setter
public abstract class N2oAbstractEvent extends N2oComponent implements Source, IdAware {

    private String id;
}
