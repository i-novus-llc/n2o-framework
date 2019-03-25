package net.n2oapp.properties;

import java.util.*;

/**
 * OverrideProperties class allows you to override value from base properties.
 * @author operehod
 * @since 05.06.2015
 */
public class OverrideProperties extends Properties {


    private Properties baseProperties;

    public OverrideProperties(Properties baseProperties) {
        this.baseProperties = baseProperties;
    }

    public OverrideProperties() {
    }


    public void setBaseProperties(Properties baseProperties) {
        this.baseProperties = baseProperties;
    }

    @Override
    public synchronized int size() {
        return stringPropertyNames().size();
    }

    @Override
    public Set<String> stringPropertyNames() {
        Set<String> res = new HashSet<>();
        res.addAll(getBaseProperties().stringPropertyNames());
        res.addAll(super.stringPropertyNames());
        return res;
    }

    /**
     * получить property с учетом наследования
     */
    @Override
    public String getProperty(String key) {
        String res = superGetProperty(key);
        if (res == null)
            res = getBaseProperties().getProperty(key);
        return res;
    }


    /**
     * получить property текущего уровня, без учета наследования
     */
    public String superGetProperty(String key) {
        return super.getProperty(key);
    }


    @Override
    public synchronized Object get(Object key) {
        Object res = super.get(key);
        if (res == null)
            res = getBaseProperties().get(key);
        return res;
    }

    public String getCurrentLvlProperty(String key) {
        Object oval = super.get(key);
        String sval = (oval instanceof String) ? (String) oval : null;
        return ((sval == null) && (defaults != null)) ? defaults.getProperty(key) : sval;
    }

    @Override
    public synchronized boolean contains(Object value) {
        return super.contains(value) || getBaseProperties().contains(value);
    }

    @Override
    public synchronized boolean containsKey(Object key) {
        return super.containsKey(key) || getBaseProperties().containsKey(key);
    }

    @Override
    public synchronized boolean containsValue(Object value) {
        return super.containsValue(value) || getBaseProperties().containsValue(value);
    }

    private static final Properties EMPTY_PROPERTIES = new Properties();

    public Properties getBaseProperties() {
        if (baseProperties != null)
            return baseProperties;
        return EMPTY_PROPERTIES;
    }


    //метод реализуются потому что спринг вызывает аго на старте
    @Override
    @SuppressWarnings("unchecked")
    public synchronized Enumeration<Object> keys() {
        Enumeration[] enums = new Enumeration[]{getBaseProperties().keys(), super.keys()};
        return new Enumeration<Object>() {
            private int index = 0;

            private boolean next() {
                while (this.index < enums.length) {
                    if (enums[this.index] != null && enums[this.index].hasMoreElements()) {
                        return true;
                    }

                    ++this.index;
                }

                return false;
            }

            public boolean hasMoreElements() {
                return this.next();
            }

            public Object nextElement() {
                if (!this.next()) {
                    throw new NoSuchElementException();
                } else {
                    return enums[this.index].nextElement();
                }
            }
        };
    }


}
