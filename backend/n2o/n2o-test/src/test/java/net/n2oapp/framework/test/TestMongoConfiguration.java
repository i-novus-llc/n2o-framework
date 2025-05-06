package net.n2oapp.framework.test;

import de.flapdoodle.embed.mongo.spring.autoconfigure.EmbeddedMongoAutoConfiguration;
import net.n2oapp.criteria.dataset.DataList;
import net.n2oapp.framework.boot.N2oMongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

import java.util.StringJoiner;

@TestConfiguration
@Import({N2oMongoAutoConfiguration.class,
        MongoAutoConfiguration.class,
        EmbeddedMongoAutoConfiguration.class})
public class TestMongoConfiguration {
    // normalize method for testFilters() method
    public static String mapIdIn(DataList ids) {
        StringJoiner res = new StringJoiner(",", "[", "]");
        for (Object o : ids)
            res.add("new ObjectId('" + o + "')");
        return res.toString();
    }
}
