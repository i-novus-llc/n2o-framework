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
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;

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
    private final transient AtomicReference<Resource> resource = new AtomicReference<>();
    private final Semaphore available = new Semaphore(1);
    private static final Logger logger = LoggerFactory.getLogger(ReloadableProperties.class);

    public ReloadableProperties(URI uri) throws MalformedURLException {
        this.resource.set(new UrlResource(uri));
    }

    public ReloadableProperties(Resource resource) {
        this.resource.set(resource);
    }

    public ReloadableProperties() {
    }

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
    @Override
    public String superGetProperty(String key) {
        Object oval = get(key);
        String sval = (oval instanceof String ovalString) ? ovalString : null;
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
        Resource res = resource.get();
        if (res == null)
            return;
        if (isExpired())
            clear();
        try (InputStream resourceAsStream = res.getInputStream()) {
            available.acquire();
            load(resourceAsStream);
            exists = true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            onLoadFailure();
        } catch (Exception e) {
            onLoadFailure();
        } finally {
            available.release();
        }
    }

    private void onLoadFailure() {
        timeStamp = System.currentTimeMillis();
        exists = false;
        logger.debug("error while trying reload properties from [{}]", resource.get());
    }

    public void updateProperty(Object key, Object value) {
        try {
            available.acquire();
            put(key, value);
            PropertiesRewriter.updateProperty(resource.get(), key, value);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw onUpdateFailure(key, value, e);
        } catch (IOException e) {
            throw onUpdateFailure(key, value, e);
        } finally {
            available.release();
        }
    }

    private IllegalStateException onUpdateFailure(Object key, Object value, Exception e) {
        exists = false;
        logger.debug("error while trying update property (key: {}, value: {}) from [{}]",
                key, value, resource.get());
        return new IllegalStateException(e);
    }

    public void removeProperty(Object key) {
        try {
            available.acquire();
            if (containsKey(key)) {
                remove(key);
                PropertiesRewriter.removeProperty(resource.get(), key);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw onRemoveFailure(key, e);
        } catch (IOException e) {
            throw onRemoveFailure(key, e);
        } finally {
            available.release();
        }
    }

    private IllegalStateException onRemoveFailure(Object key, Exception e) {
        exists = false;
        logger.debug("error while trying remove property (key: {}) from [{}]",
                key, resource.get());
        return new IllegalStateException(e);
    }

    public boolean isExists() {
        return exists;
    }

    public boolean isExpired() {
        long currentTime = System.currentTimeMillis();
        long cacheTimeLong = (long) cacheTime * 1000L;
        return (timeStamp + cacheTimeLong) < currentTime;
    }

    @Override
    public synchronized void load(Reader reader) throws IOException {
        timeStamp = System.currentTimeMillis();
        super.load(reader);
    }

    @Override
    public synchronized void load(InputStream inStream) throws IOException {
        timeStamp = System.currentTimeMillis();
        super.load(inStream);
    }

    @Override
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
        return resource.get();
    }

    public void setResource(Resource resource) {
        this.resource.set(resource);
    }

    @Override
    public synchronized boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReloadableProperties that = (ReloadableProperties) o;
        return super.equals(o) &&
                cacheTime == that.cacheTime &&
                Objects.equals(resource.get(), that.resource.get());
    }

    @Override
    public synchronized int hashCode() {
        return Objects.hash(super.hashCode(), cacheTime, resource.get());
    }
}