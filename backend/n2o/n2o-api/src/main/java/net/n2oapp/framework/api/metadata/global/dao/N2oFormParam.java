package net.n2oapp.framework.api.metadata.global.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Модель предустановленных параметров тела запроса
 */
@Getter
@Setter
@NoArgsConstructor
public class N2oFormParam extends N2oParam {
    /**
     * Идентификатор параметра
     */
    private String id;
}
