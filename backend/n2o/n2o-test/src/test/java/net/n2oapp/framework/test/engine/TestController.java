package net.n2oapp.framework.test.engine;

import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.criteria.api.SortingDirectionEnum;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("testController")
public class TestController {

    public CollectionPage<TestRow> getCollectionPage(TestCriteria criteria) {
        List<TestRow> result = new ArrayList<>();
        if (criteria.getId() == null) {
            assert criteria.getSorting().getField().equals("value");
            assert criteria.getSorting().getDirection().equals(SortingDirectionEnum.DESC);
            for (int i = 0; i < 10; i++) {
                result.add(new TestRow(i, "value" + i));
            }
            return new CollectionPage<>(15, result, criteria);
        } else {
            result.add(new TestRow(criteria.getId(), "value" + criteria.getId()));
            return new CollectionPage<>(1, result, criteria);
        }
    }
}