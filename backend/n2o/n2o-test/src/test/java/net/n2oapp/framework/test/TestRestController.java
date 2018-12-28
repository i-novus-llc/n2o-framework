package net.n2oapp.framework.test;

import net.n2oapp.criteria.api.CollectionPage;
import net.n2oapp.framework.test.engine.TestCriteria;
import net.n2oapp.framework.test.engine.TestRow;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@RestController
public class TestRestController {


    @RequestMapping(value = "/test/rest", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public CollectionPage<TestRow> list(@RequestParam("page") Integer page, @RequestParam("size") Integer size,
                                        @RequestParam("sort") String sorting) {
        List<TestRow> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            int id = (page - 1) * size + i;
            TestRow testRestModel = new TestRow(id, String.valueOf(id));
            list.add(testRestModel);
        }
        String[] split = sorting.split(",");
        String field = split[0];
        if ("value".equals(field)) {
            String dir = split[1].toLowerCase();
            Comparator<TestRow> comparing = Comparator.comparing(TestRow::getValue);
            if ("desc".equals(dir))
                list.sort(comparing.reversed());
            else
                list.sort(comparing);
        }
        return new CollectionPage<>(50, list, new TestCriteria());
    }

    @RequestMapping(value = "/test/rest/{id}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public TestRow findById(@PathVariable("id") Integer id) {
        return new TestRow(id, String.valueOf(id));
    }
}