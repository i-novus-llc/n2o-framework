package net.n2oapp.framework.context.test;

import net.n2oapp.framework.api.context.Context;
import net.n2oapp.framework.context.smart.impl.api.MemoryContextProvider;
import org.junit.Test;

import java.util.*;

/**
 * @author operehod
 * @since 02.12.2015
 */
public class MemoryContextProviderTest {
    @Test
    public void test() throws Exception {

        Context admin = new OnlyUserContext("admin");
        Context oleg = new OnlyUserContext("oleg");

        MemoryContextProvider provider = new MemoryContextProvider(new HashSet<>(Arrays.asList("adminMode", "logMode")), new HashSet<>(Arrays.asList("username", "contextId")));
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("adminMode", null);
        map.put("logMode", null);
        assert map.equals(provider.get(admin));
        LinkedHashMap<String, Object> map1 = new LinkedHashMap<>();
        map1.put("adminMode", true);
        map1.put("logMode", false);
        provider.set(admin, map1);
        LinkedHashMap<String, Object> map2 = new LinkedHashMap<>();
        map2.put("adminMode", true);
        map2.put("logMode", false);
        assert map2.equals(provider.get(admin));
        LinkedHashMap<String, Object> map3 = new LinkedHashMap<>();
        map3.put("adminMode", null);
        map3.put("logMode", null);
        assert map3.equals(provider.get(oleg));
        LinkedHashMap<String, Object> map4 = new LinkedHashMap<>();
        map4.put("adminMode", false);
        map4.put("logMode", true);
        provider.set(oleg, map4);
        LinkedHashMap<String, Object> map5 = new LinkedHashMap<>(2);
        map5.put("adminMode", true);
        map5.put("logMode", false);
        assert map5.equals(provider.get(admin));
        LinkedHashMap<String, Object> map6 = new LinkedHashMap<>(2);
        map6.put("adminMode", false);
        map6.put("logMode", true);
        assert map6.equals(provider.get(oleg));
    }

    private static class OnlyUserContext implements Context {
        public OnlyUserContext(String admin) {
            this.admin = admin;
        }

        @Override
        public Object get(String name) {
            return admin;
        }

        @Override
        public void set(Map<String, Object> dataSet) {
        }

        public String getAdmin() {
            return admin;
        }

        public void setAdmin(String admin) {
            this.admin = admin;
        }

        private String admin;
    }
}
