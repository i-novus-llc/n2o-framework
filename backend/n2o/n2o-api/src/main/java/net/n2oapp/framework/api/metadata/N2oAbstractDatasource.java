package net.n2oapp.framework.api.metadata;

import lombok.Getter;
import lombok.Setter;
import net.n2oapp.framework.api.metadata.aware.IdAware;

/**
 * Абстрактная модель источника данных
 */
@Getter
@Setter
public abstract class N2oAbstractDatasource implements Source, IdAware {

    private String id;
}
