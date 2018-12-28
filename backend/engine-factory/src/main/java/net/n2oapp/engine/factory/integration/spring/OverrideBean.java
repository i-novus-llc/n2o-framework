package net.n2oapp.engine.factory.integration.spring;

import java.util.*;

/**
 * Бин, который переопределяет другой бин.
 *
 */
public interface OverrideBean {

    /**
     * Имя бина, который переопределяем
    */
    String getOverrideBeanName();

    /**
     * Функция удаления переопределяемых бинов
     * @param beans - все бины
     * @param <G> - тип бина
     * @return бины без переопределенных
     */
    static <G> Map<String, G> removeOverriddenBeans(Map<String, G> beans) {
        Map<String, G> copiedBeans = new HashMap<>(beans);
        beans.keySet().stream()
                .filter(e -> beans.get(e) instanceof OverrideBean)
                .map(e -> ((OverrideBean) beans.get(e)).getOverrideBeanName())
                .forEach(copiedBeans::remove);
        return copiedBeans;
    }
}
