package net.n2oapp.framework.test;

import de.flapdoodle.embed.mongo.spring.autoconfigure.EmbeddedMongoAutoConfiguration;
import net.n2oapp.framework.boot.N2oMongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

@TestConfiguration
@Import({N2oMongoAutoConfiguration.class,
        MongoAutoConfiguration.class,
        EmbeddedMongoAutoConfiguration.class})
public class TestMongoConfiguration {
}
