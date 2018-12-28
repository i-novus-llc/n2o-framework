package net.n2oapp.framework.test.engine;


import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class TestSpringService {

    public Collection<TestRow> testMethod() {
        Collection<TestRow> result = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            result.add(new TestRow(i, "value" + i));
        }
        return result;
    }
}
