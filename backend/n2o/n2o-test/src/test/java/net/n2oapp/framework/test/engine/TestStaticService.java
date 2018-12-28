package net.n2oapp.framework.test.engine;


import java.util.ArrayList;
import java.util.Collection;

public class TestStaticService {

    public static Collection<TestRow> testMethod() {
        Collection<TestRow> result = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            result.add(new TestRow(i, "value" + i));
        }
        return result;
    }
}
