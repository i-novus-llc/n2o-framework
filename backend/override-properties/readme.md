# Override Properties

**Override Properties** is a convenient library for flexible configuration of your application environment.

## Install

Using Maven: 
```xml
<dependency>
  <groupId>net.n2oapp.properties</groupId>  
  <artifactId>override-properties</artifactId>
  <version>1.0</version>
</dependency>
```

Using Gradle:
```groovy
compile 'net.n2oapp.properties:override-properties:1.0'
```

## Use

### How to start

Let's say you have a .properties file in your project. How to get its values?

```java
import static net.n2oapp.properties.StaticProperties.*;

Properties myProps = PropertiesReader.getPropertiesFromClasspath("META-INF/my.properties"); //load properties from file in classpath
new StaticProperties().setProperties(myProps); //can be called only first once

String name = get("name");
int age = getInt("age");
boolean married = getBoolean("married");

```

Ok. How to override the value using a second .properties file?

```java
Properties myProps = PropertiesReader.getPropertiesFromClasspath("META-INF/override.properties", "META-INF/my.properties");
```

How I can override properties from file in filesystem?

```java
OverrideProperties fsProps = PropertiesReader.getReloadableFromFilesystem("/env/placeholders.properties", 60);  // reload file every 60 seconds
fsProps.setBaseProperties(myProps);
```

### Web Application Properties 

You have 4 levels of configuration:
* default
* build
* environment
* application

```java
WebApplicationProperties appProperties = new WebApplicationProperties(
                "META-INF/default.properties",
                "META-INF/build.properties",
                "placeholders.properties");
appProperties.setServletContext(servletContext); // override [servletPath].properties                
```

### Spring integration

```xml
<bean id="webAppEnvironment" class="net.n2oapp.properties.web.WebAppEnvironment"/>

<bean id="appProperties" class="net.n2oapp.properties.web.WebApplicationProperties">
    <constructor-arg name="defaultPropertiesName" value="META-INF/default.properties"/>
    <constructor-arg name="buildPropertiesName" value="META-INF/build.properties"/>
    <constructor-arg name="environmentPropertiesName" value="placeholders.properties"/>
    <property name="webAppEnvironment" ref="webAppEnvironment"/>
</bean>

<bean class="net.n2oapp.properties.StaticProperties">
    <property name="properties" ref="appProperties"/>
</bean>

<context:property-placeholder properties-ref="appProperties"/>
```

```java
@Component
public class MyProfile {

    @Value("${name}")
    private String name;
     
    ...
}
```

### Expression Values

Property values can be written in the language SpEL.
```
num=#{T(java.lang.Math).random() * 100.0}
mydir=#{systemProperties['user.home']}
```

```java
Properties exprProps = new ExpressionBasedProperties(PropertiesReader.getPropertiesFromClasspath("META-INF/expression.properties"));
String num = exprProps.get("num");
String mydir = exprProps.get("mydir");
```