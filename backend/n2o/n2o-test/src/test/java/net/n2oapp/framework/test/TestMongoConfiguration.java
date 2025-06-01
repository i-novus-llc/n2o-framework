package net.n2oapp.framework.test;

import de.flapdoodle.embed.mongo.packageresolver.*;
import de.flapdoodle.embed.mongo.spring.autoconfigure.EmbeddedMongoAutoConfiguration;
import de.flapdoodle.embed.mongo.transitions.ImmutableMongod;
import de.flapdoodle.embed.mongo.transitions.Mongod;
import de.flapdoodle.embed.mongo.transitions.PackageOfCommandDistribution;
import de.flapdoodle.embed.process.config.store.FileSet;
import de.flapdoodle.embed.process.config.store.FileType;
import de.flapdoodle.embed.process.config.store.ImmutableFileSet;
import de.flapdoodle.embed.process.distribution.ArchiveType;
import de.flapdoodle.os.BitSize;
import de.flapdoodle.os.CPUType;
import de.flapdoodle.os.CommonOS;
import net.n2oapp.criteria.dataset.DataList;
import net.n2oapp.framework.boot.N2oMongoAutoConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import java.util.StringJoiner;

import static de.flapdoodle.embed.mongo.packageresolver.AbstractPackageFinder.match;

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

    @Bean
    @Primary
    public Mongod mongodCustom(Mongod mongod, @Value("${de.flapdoodle.mongodb.embedded.version}") String version) {

        ImmutableFileSet fileSet = FileSet.builder()
                .addEntry(FileType.Executable, Command.MongoD.commandName())
                .build();


        PackageFinderRules mongodPackageRules = PackageFinderRules.builder()
                .addRules(PackageFinderRule.builder()
                        .match(match(CommonOS.Linux, BitSize.B64, CPUType.X86)
                                .andThen(
                                        DistributionMatch.any(VersionRange.of(version))
                                ))
                        .finder(UrlTemplatePackageFinder.builder()
                                .fileSet(fileSet)
                                .archiveType(ArchiveType.TGZ)
                                .urlTemplate("/linux/mongodb-linux-x86_64-ubuntu2204-{version}.tgz")
                                .isDevVersion(true)
                                .build())
                        .build())
                .addRules(PackageFinderRule.builder()
                        .match(PlatformMatch.withOs(CommonOS.Windows))
                        .finder(new WindowsPackageFinder(Command.MongoD))
                        .build())
                .build();

        return ((ImmutableMongod) mongod).withPackageOfDistribution(PackageOfCommandDistribution.withDefaults()
                .withCommandPackageResolver(command -> distribution -> {
                    switch (command) {
                        case MongoD:
                            return mongodPackageRules.packageFor(distribution)
                                    .orElseThrow(() -> new IllegalArgumentException("could not find package for " + distribution));
                        default:
                            throw new IllegalArgumentException("not implemented");
                    }
                }));
    }
}
