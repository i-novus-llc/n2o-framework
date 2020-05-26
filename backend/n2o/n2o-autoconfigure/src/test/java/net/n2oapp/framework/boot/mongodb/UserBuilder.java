package net.n2oapp.framework.boot.mongodb;

import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserBuilder {
    public static List<Document> testData() {
        List<Document> data = new ArrayList<>();

        Map<String, Object> inParams = new HashMap<>();
        inParams.put("name", "Сёмина Мария Васильевна");
        inParams.put("age", 77);
        inParams.put("birthday", "27.03.1941 00:00:00");
        inParams.put("vip", true);
        Map<String, Object> gender = new HashMap<>();
        gender.put("name", "Женский");
        gender.put("id", 2);
        inParams.put("gender", gender);
        data.add(new Document(inParams));

        inParams = new HashMap<>();
        inParams.put("name", "Патронов Алексей Иванович");
        inParams.put("age", 9);
        inParams.put("birthday", "10.05.2009 00:00:00");
        inParams.put("vip", true);
        gender = new HashMap<>();
        gender.put("name", "Мужской");
        gender.put("id", 1);
        inParams.put("gender", gender);
        data.add(new Document(inParams));

        inParams = new HashMap<>();
        inParams.put("name", "Кежватова Анастасия Викторовна");
        inParams.put("age", 23);
        inParams.put("birthday", "08.04.1995 00:00:00");
        inParams.put("vip", false);
        gender = new HashMap<>();
        gender.put("name", "Не определенный");
        gender.put("id", 3);
        inParams.put("gender", gender);
        data.add(new Document(inParams));

        inParams = new HashMap<>();
        inParams.put("name", "Чекашкина Людмила Ивановна");
        inParams.put("age", 53);
        inParams.put("birthday", "28.11.1964 00:00:00");
        inParams.put("vip", true);
        gender = new HashMap<>();
        gender.put("name", "Женский");
        gender.put("id", 2);
        inParams.put("gender", gender);
        data.add(new Document(inParams));

        inParams = new HashMap<>();
        inParams.put("name", "Ативанова Елена Александровна");
        inParams.put("age", 47);
        inParams.put("birthday", "27.02.1971 00:00:00");
        inParams.put("vip", false);
        gender = new HashMap<>();
        gender.put("name", "Не определенный");
        gender.put("id", 3);
        inParams.put("gender", gender);
        data.add(new Document(inParams));

        return data;
    }
}
