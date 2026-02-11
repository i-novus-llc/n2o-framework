package net.n2oapp.demo;

import net.n2oapp.framework.boot.N2oCsrfAutoConfiguration;
import net.n2oapp.framework.boot.N2oFrameworkAutoConfiguration;
import net.n2oapp.framework.boot.sql.jdbc.RoutingDataSourceAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.HttpEncodingAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
//@EnableRoutingDataSource
@Configuration
@Import({
//        MessageSourceAutoConfiguration.class,
//        JndiDataSourceAutoConfiguration.class,
//        EmbeddedDataSourceConfiguration.class,
//        DataSourceAutoConfiguration.class,
//        JdbcTemplateAutoConfiguration.class,
//        TransactionAutoConfiguration.class,
        N2oCsrfAutoConfiguration.class,
        DispatcherServletAutoConfiguration.class,
//        ErrorMvcAutoConfiguration.class,
        HttpEncodingAutoConfiguration.class,
        HttpMessageConvertersAutoConfiguration.class,
//        JacksonAutoConfiguration.class,
//        PropertyPlaceholderAutoConfiguration.class,
//        ThymeleafAutoConfiguration.class,
//        WebSocketAutoConfiguration.class,
        RoutingDataSourceAutoConfiguration.class,
        N2oFrameworkAutoConfiguration.class
})
public class DemoApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(DemoApplication.class);
    }

    @Bean
    public WebMvcConfigurer forwardToIndex() {
        return new WebMvcConfigurer() {
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                // forward requests to /admin and /user to their index.html
                registry.addViewController("/docs/manual/").setViewName(
                        "forward:/docs/manual/index.html");
                registry.addViewController("/docs/xml/").setViewName(
                        "forward:/docs/xml/index.html");
                registry.addViewController("/docs/esdoc/").setViewName(
                        "forward:/docs/esdoc/index.html");

            }

//            @Override
//            public void addResourceHandlers(ResourceHandlerRegistry registry) {
//                registry.addResourceHandler("/{x:^(?!docs$).*$}/**")
//                        .addResourceLocations("classpath:/META-INF/resources/")
//                        .resourceChain(true)
//                        .addResolver(new SPAResolver());
//            }
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, Customizer<CsrfConfigurer<HttpSecurity>> n2oCsrfCustomizer) throws Exception {
        // Настройка CSRF защиты
        http.csrf(n2oCsrfCustomizer);
        http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }
}
