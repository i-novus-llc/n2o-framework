package net.n2oapp.properties.example;

import net.n2oapp.properties.StaticProperties;
import net.n2oapp.properties.web.WebApplicationProperties;

import static net.n2oapp.properties.StaticProperties.get;

/**
 * @author iryabov
 * @since 01.03.2016
 */
public class WebApplicationPropertiesExample {

    public static void main(String[] args) {
        WebApplicationProperties appProps = new WebApplicationProperties("META-INF/default.properties",
                "META-INF/build.properties",
                "placeholders.properties");
        new StaticProperties().setProperties(appProps);
        System.out.println("level 1 : " + get("level1"));
        System.out.println("level 2 : " + get("level2"));
        System.out.println("level 3 : " + get("level3"));
    }
}
