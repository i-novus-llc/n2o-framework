package net.n2oapp.framework.engine.util;

import lombok.Data;

import java.util.List;

/**
 * Модель для тестирования маппинга
 */
@Data
public class TestEntity {
    private String valueStr;
    private Integer valueInt;
    private InnerEntity innerObj;
    private List<InnerEntity> innerObjList;

    @Data
    public static class InnerEntity {
        private String valueStr;
        private Integer valueInt;
    }
}

