package net.n2oapp.properties.example;

import net.n2oapp.properties.StaticProperties;
import net.n2oapp.properties.reader.PropertiesReader;

import java.util.Properties;

import static net.n2oapp.properties.StaticProperties.*;

/**
 * @author iryabov
 * @since 01.03.2016
 */
public class OverrideExample {
    public static void main(String[] args) {
        Properties myProps = PropertiesReader.getPropertiesFromClasspath("META-INF/override.properties", "META-INF/my.properties");
        new StaticProperties().setProperties(myProps); //can be called only first once
        System.out.println("----- override.properties > my.properties -----");
        System.out.println("name    : " + get("name"));
        System.out.println("age     : " + getInt("age"));
        System.out.println("married : " + getBoolean("married"));
    }
}
