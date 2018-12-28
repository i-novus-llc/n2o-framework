package net.n2oapp.properties.example;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import static net.n2oapp.properties.StaticProperties.*;

/**
 * @author iryabov
 * @since 01.03.2016
 */
public class SprintIntegrationExample {

    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("META-INF/application-context.xml");
        System.out.println("level 1 : " + get("level1"));
        System.out.println("level 2 : " + get("level2"));
        System.out.println("level 3 : " + get("level3"));
    }
}
