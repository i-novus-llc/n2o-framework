package net.n2oapp.properties;

import net.n2oapp.properties.io.PropertiesRewriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Enumeration;
import java.util.InvalidPropertiesFormatException;
import java.util.Set;
import java.util.concurrent.Semaphore;


/**
 * ReloadableProperties reads values from .properties file every {@link #cacheTime} seconds
 * User: operhod
 * Date: 10.10.13
 * Time: 17:05
 */
public class ReloadableProperties extends OverrideProperties {

    private volatile long timeStamp = Long.MIN_VALUE;
    private volatile int cacheTime = 60;
    private volatile boolean exists;
    private volatile Resource resource;
    private final Semaphore available = new Semaphore(1);

    public ReloadableProperties(URI uri) throws MalformedURLException {
        this.resource = new UrlResource(uri);
    }

    public ReloadableProperties(Resource resource) {
        this.resource = resource;
    }


    public ReloadableProperties() {
    }


    private Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public Set<String> stringPropertyNames() {
        refresh();
        return super.stringPropertyNames();
    }

    @Override
    public Enumeration<?> propertyNames() {
        refresh();
        return super.propertyNames();
    }

    @Override
    public synchronized Enumeration<Object> keys() {
        refresh();
        return super.keys();
    }

    //переопределяем, чтобы "дернуть" наш специальный get
    public String superGetProperty(String key) {
        Object oval = get(key);
        String sval = (oval instanceof String) ? (String) oval : null;
        return ((sval == null) && (defaults != null)) ? defaults.getProperty(key) : sval;
    }

    @Override
    public synchronized Object get(Object key) {
        refresh();
        return super.get(key);
    }

    @Override
    public synchronized Object put(Object key, Object value) {
        refresh();
        return super.put(key, value);
    }

    @Override
    public synchronized Object remove(Object key) {
        refresh();
        return super.remove(key);
    }

    @Override
    public String getCurrentLvlProperty(String key) {
        refresh();
        return super.getCurrentLvlProperty(key);
    }

    private void refresh() {
        if (isExpired()) {
            load();
        }
    }

    private synchronized void load() {
        if (resource == null)
            return;
        if (isExpired())
            clear();
        try (InputStream resourceAsStream = resource.getInputStream()) {
            available.acquire();
            load(resourceAsStream);
            exists = true;
        } catch (Exception e) {
            timeStamp = System.currentTimeMillis();
            exists = false;
            //ситуация не является особо ошибочной, т.к. файла по этому пути может и не быть
            logger.debug(String.format("error while trying reload properties from [%s]", resource.toString()));
        } finally {
            available.release();
        }
    }

    public void updateProperty(Object key, Object value) {
        try {
            available.acquire();
            put(key, value);
            PropertiesRewriter.updateProperty(resource, key, value);
        } catch (InterruptedException | IOException e) {
            exists = false;
            if (key == null) key = "null";
            if (value == null) value = "null";
            logger.debug(String.format("error while trying update property (key: %s, value: %s) from [%s]",
                    key.toString(), value.toString(), resource.toString()));
            throw new IllegalStateException(e);
        } finally {
            available.release();
        }
    }

    public void removeProperty(Object key) {
        try {
            available.acquire();
            if (containsKey(key)) {
                remove(key);
                PropertiesRewriter.removeProperty(resource, key);
            }
        } catch (InterruptedException | IOException e) {
            exists = false;
            if (key == null) key = "null";
            logger.debug(String.format("error while trying remove property (key: %s) from [%s]",
                    key.toString(),  resource.toString()));
            throw new IllegalStateException(e);
        } finally {
            available.release();
        }
    }

    public boolean isExists() {
        return exists;
    }

    public boolean isExpired() {
        long currentTime = System.currentTimeMillis();
        long cacheTimeLong = cacheTime * 1000;
        return (timeStamp + cacheTimeLong) < currentTime;
    }

    public synchronized void load(Reader reader) throws IOException {
        timeStamp = System.currentTimeMillis();
        super.load(reader);
    }

    public synchronized void load(InputStream inStream) throws IOException {
        timeStamp = System.currentTimeMillis();
        super.load(inStream);
    }

    public synchronized void loadFromXML(InputStream in)
            throws IOException {
        timeStamp = System.currentTimeMillis();
        super.loadFromXML(in);
    }

    public int getCacheTime() {
        return cacheTime;
    }

    public void setCacheTime(int cacheTime) {
        this.cacheTime = cacheTime;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }
}
