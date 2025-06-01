package net.n2oapp.framework.api.metadata.global.dao.object;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.n2oapp.framework.api.metadata.aware.N2oEnum;

/*
 * Тип способа маппинга в параметры провайдера
 */
@RequiredArgsConstructor
@Getter
public enum MapperTypeEnum implements N2oEnum {
    DATASET("dataset"),
    SPEL("spel"),
    JAVASCRIPT("javascript"),
    GROOVY("groovy");

    private final String id;
}
