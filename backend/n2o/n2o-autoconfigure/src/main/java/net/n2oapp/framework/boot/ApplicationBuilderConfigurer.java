package net.n2oapp.framework.boot;

import net.n2oapp.framework.config.N2oApplicationBuilder;

/**
 * Интерфейс для конфигурирование {@link N2oApplicationBuilder}
 */
public interface ApplicationBuilderConfigurer {

    /**
     * @param builder конфигурируемый билдер
     */
    void configure(N2oApplicationBuilder builder);
}
