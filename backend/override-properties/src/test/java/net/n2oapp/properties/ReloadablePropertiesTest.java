package net.n2oapp.properties;

import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ReloadablePropertiesTest {

    @Test
    void checkingExistingPropertiesThanUpdatingThemAndCreatingNew() {
        ReloadableProperties properties = new ReloadableProperties(new ClassPathResource("prop1.properties"));
        properties.setCacheTime(1);
        assertEquals("prop1.properties", properties.getResource().getFilename());
        assertEquals("one", properties.getProperty("test.one"));
        assertEquals("error", properties.getProperty("test.two"));
        assertEquals("error", properties.getProperty("test.three"));

        //меняем исходный файл
        properties.setResource(new ClassPathResource("prop2.properties"));
        assertEquals("prop2.properties", properties.getResource().getFilename());

        //properties не меняются
        assertEquals("one", properties.getProperty("test.one"));
        assertEquals("error", properties.getProperty("test.two"));
        assertEquals("error", properties.getProperty("test.three"));

        //ждем полторы секунды, пока не поменяются проперти
        Awaitility.await()
                .atMost(1500, TimeUnit.MILLISECONDS)
                .until(()-> "two".equals(properties.getProperty("test.two")));

        //properties поменялись
        assertEquals("prop2.properties", properties.getResource().getFilename());
        assertEquals("two", properties.getProperty("test.two"));
        assertEquals("three", properties.getProperty("test.three"));
        assertEquals("four", properties.getProperty("test.four"));

        properties.updateProperty("newProperty", "newValue");
        assertEquals("newValue", properties.getProperty("newProperty"));
    }

    @Test
    void checkingPropertiesIfFileNotExist() {
        FileSystemResource fileSystemResource = new FileSystemResource(System.getProperty("user.home") + File.separator + "test.properties");
        fileSystemResource.getFile().deleteOnExit();

        ReloadableProperties properties = new ReloadableProperties(fileSystemResource);
        properties.setCacheTime(1);
        properties.updateProperty("newProperty", "newValue");

        assertEquals("newValue", properties.getProperty("newProperty"));

        properties.removeProperty("newProperty");

        assertNull(properties.getProperty("newProperty"));
    }
}
