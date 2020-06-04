package net.n2oapp.framework.test;

import org.bson.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestUserBuilder {
    public static List<Document> testData() {
        List<Document> data = new ArrayList<>();

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("name", "Anna");
        inParams.put("age", 77);
        inParams.put("birthday", LocalDate.of(1941,3, 27));
        inParams.put("vip", true);
        Map<String, Object> gender = new HashMap<>();
        gender.put("name", "Women");
        gender.put("id", 2);
        inParams.put("gender", gender);
        inParams.put("info", "aaa");
        data.add(new Document(inParams));

        inParams = new HashMap<>();
        inParams.put("name", "Artur");
        inParams.put("age", 9);
        inParams.put("birthday", LocalDate.of(2009,5,10));
        inParams.put("vip", true);
        gender = new HashMap<>();
        gender.put("name", "Men");
        gender.put("id", 1);
        inParams.put("gender", gender);
        inParams.put("info", "bbb");
        data.add(new Document(inParams));

        inParams = new HashMap<>();
        inParams.put("name", "Inna");
        inParams.put("age", 23);
        inParams.put("birthday", LocalDate.of(1995,4,8));
        inParams.put("vip", false);
        gender = new HashMap<>();
        gender.put("name", "Not defined");
        gender.put("id", 3);
        inParams.put("gender", gender);
        inParams.put("info", "ccc");
        data.add(new Document(inParams));

        inParams = new HashMap<>();
        inParams.put("name", "Tanya");
        inParams.put("age", 53);
        inParams.put("birthday", LocalDate.of(1964, 11, 28));
        inParams.put("vip", true);
        gender = new HashMap<>();
        gender.put("name", "Women");
        gender.put("id", 2);
        inParams.put("gender", gender);
        inParams.put("info", null);
        data.add(new Document(inParams));

        inParams = new HashMap<>();
        inParams.put("name", "Valentine");
        inParams.put("age", 47);
        inParams.put("birthday", LocalDate.of(1971, 02, 27));
        inParams.put("vip", false);
        gender = new HashMap<>();
        gender.put("name", "Not defined");
        gender.put("id", 3);
        inParams.put("gender", gender);
        inParams.put("info", null);
        data.add(new Document(inParams));
        return data;
    }
}
