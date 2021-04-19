package net.n2oapp.framework.engine.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

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
        private Set<InnerInnerEntity> innerInnerObjSet;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class InnerInnerEntity {
            private String innerName;
        }
    }
}

