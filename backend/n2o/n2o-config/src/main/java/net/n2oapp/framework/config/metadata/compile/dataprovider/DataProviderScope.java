package net.n2oapp.framework.config.metadata.compile.dataprovider;

import lombok.Getter;
import lombok.Setter;

/**
 * Информация о провайдере данных при сборке внутренних метаданных
 */
@Getter
@Setter
public class DataProviderScope {
    private String queryId;
    private String objectId;
}
