package net.n2oapp.framework.test;

import org.bson.Document;

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
        inParams.put("birthday", "1941-03-27");
        inParams.put("vip", true);
        Map<String, Object> gender = new HashMap<>();
        gender.put("name", "Women");
        gender.put("id", 2);
        inParams.put("gender", gender);
        data.add(new Document(inParams));

        inParams = new HashMap<>();
        inParams.put("name", "Artur");
        inParams.put("age", 9);
        inParams.put("birthday", "2009-05-10");
        inParams.put("vip", true);
        gender = new HashMap<>();
        gender.put("name", "Men");
        gender.put("id", 1);
        inParams.put("gender", gender);
        data.add(new Document(inParams));

        inParams = new HashMap<>();
        inParams.put("name", "Inna");
        inParams.put("age", 23);
        inParams.put("birthday", "1995-04-08");
        inParams.put("vip", false);
        gender = new HashMap<>();
        gender.put("name", "Not defined");
        gender.put("id", 3);
        inParams.put("gender", gender);
        data.add(new Document(inParams));

        inParams = new HashMap<>();
        inParams.put("name", "Tanya");
        inParams.put("age", 53);
        inParams.put("birthday", "1964-11-28");
        inParams.put("vip", true);
        gender = new HashMap<>();
        gender.put("name", "Women");
        gender.put("id", 2);
        inParams.put("gender", gender);
        data.add(new Document(inParams));

        inParams = new HashMap<>();
        inParams.put("name", "Valentine");
        inParams.put("age", 47);
        inParams.put("birthday", "1971-02-27");
        inParams.put("vip", false);
        gender = new HashMap<>();
        gender.put("name", "Not defined");
        gender.put("id", 3);
        inParams.put("gender", gender);
        data.add(new Document(inParams));

        return data;
    }
}
