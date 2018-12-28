package net.n2oapp.properties.example;

import net.n2oapp.properties.ExpressionBasedProperties;
import net.n2oapp.properties.reader.PropertiesReader;

import java.util.Properties;

/**
 * @author iryabov
 * @since 01.03.2016
 */
public class ExpressionPropertiesExample {
    public static void main(String[] args) {
        Properties exprProps = new ExpressionBasedProperties(PropertiesReader.getPropertiesFromClasspath("META-INF/expression.properties"));
        System.out.println(exprProps.get("num"));
        System.out.println(exprProps.get("mydir"));
    }
}
