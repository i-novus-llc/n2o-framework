package net.n2oapp.framework.boot.sql.jdbc;

import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JndiDataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Маркер включения динамического источника данных с маршрутизатором
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@AutoConfigurationPackage
@Import({JndiDataSourceAutoConfiguration.class,
        DataSourceAutoConfiguration.class,
        RoutingDataSourceAutoConfiguration.class})
public @interface EnableRoutingDataSource {
}
