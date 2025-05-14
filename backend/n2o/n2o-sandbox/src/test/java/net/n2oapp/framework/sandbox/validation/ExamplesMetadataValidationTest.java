package net.n2oapp.framework.sandbox.validation;

import net.n2oapp.framework.access.metadata.pack.AccessSchemaPack;
import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.validation.exception.N2oMetadataValidationException;
import net.n2oapp.framework.api.register.MetaType;
import net.n2oapp.framework.api.register.SourceInfo;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.compile.pipeline.N2oEnvironment;
import net.n2oapp.framework.config.metadata.pack.*;
import net.n2oapp.framework.config.register.InfoConstructor;
import net.n2oapp.framework.config.register.N2oMetadataRegister;
import net.n2oapp.framework.config.register.OriginEnum;
import net.n2oapp.framework.config.register.route.RouteUtil;
import net.n2oapp.framework.config.register.scanner.XmlInfoScanner;
import net.n2oapp.framework.config.test.N2oTestBase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.UUID.randomUUID;

class ExamplesMetadataValidationTest extends N2oTestBase {

    private static final String CLASSES = "/classes/";
    private static final String SKIPPED = "examples/crud_localstorage/form.page.xml";
    private final Pattern uuidPattern = Pattern.compile("\\p{XDigit}{8}-\\p{XDigit}{4}-\\p{XDigit}{4}-\\p{XDigit}{4}-\\p{XDigit}{12}");
    private TestMetaDataRegister testMetaDataRegister = new TestMetaDataRegister();
    private String[] foldersToScan = {"/examples", "/uxcomponents", "/interactions", "/uxcases", "/versions", "/applications"};

    private static String truncateFilePath(String uri) {
        return uri.substring(uri.indexOf(CLASSES) + CLASSES.length());
    }

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @Override
    protected void configure(N2oApplicationBuilder b) {
        super.configure(b);
        ((N2oEnvironment) builder.getEnvironment()).setMetadataRegister(testMetaDataRegister);
        b.packs(new N2oAllPack(), new N2oApplicationPack(), new AccessSchemaPack(), new N2oAllValidatorsPack());
        XmlInfoScanner[] scanners = new XmlInfoScanner[foldersToScan.length + 1];
        for (int i = 0; i < foldersToScan.length; i++)
            scanners[i] = new XmlInfoScanner("classpath*:" + foldersToScan[i] + "/**/*.xml");
        scanners[scanners.length - 1] = (new XmlInfoScanner("classpath*:META-INF/conf/**/*.xml"));
        b.scanners(scanners);
        builder.scan();
    }

    @Test
    void validate() {
        List<String> errors = new LinkedList<>();
        int fileCount = 0;
        for (SourceInfo sourceInfo : builder.getEnvironment().getMetadataRegister().find(i -> true)) {
            fileCount++;
            try {
                String uri = truncateFilePath(((InfoConstructor) sourceInfo).getXmlURI());
                testMetaDataRegister.currentUri = uri.substring(0, uri.lastIndexOf('/'));
                builder.read().validate().get(sourceInfo.getId(), sourceInfo.getBaseSourceClass());
            } catch (N2oMetadataValidationException e) {
                String message = e.getMessage();
                Matcher uuidMatcher = uuidPattern.matcher(message);
                if (uuidMatcher.find())
                    message = uuidMatcher.replaceFirst(testMetaDataRegister.uuidIdMap.get(uuidMatcher.group(0)));
//                данный пример требует компиляции.
                if (!SKIPPED.equals(truncateFilePath(((InfoConstructor) sourceInfo).getXmlURI())))
                    errors.add(String.format("Error at file: '%s' \n Error message: %s \n \n", truncateFilePath(((InfoConstructor) sourceInfo).getUri()), message));
            }
        }

        errors.forEach(System.out::print);
        System.out.print("Total files checked: " + fileCount);
        if (errors.size() > 0)
            Assertions.fail("Total errors: " + errors.size());
    }

    public static class UriMetadataId {
        public String id;
        public String uuid;
        public Class metadataClass;

        public UriMetadataId(String id, String uuid) {
            this.id = id;
            this.uuid = uuid;
        }

        public UriMetadataId(String id, String uuid, Class metadataClass) {
            this.id = id;
            this.uuid = uuid;
            this.metadataClass = metadataClass;
        }
    }

    public static class TestMetaDataRegister extends N2oMetadataRegister {

        private static final String DEFAULT_XML = "META-INF/conf";
        private final Pattern uuidPattern = Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$");
        public Map<String, List<UriMetadataId>> uriUUIDMap = new HashMap<>();
        public Map<String, String> uuidIdMap = new HashMap<>();
        public String currentUri = null;

        @Override
        public <I extends SourceInfo> void add(I info) {
            InfoConstructor casted = ((InfoConstructor) info);

            String uuid = randomUUID().toString();
            InfoConstructor newInfo = new InfoConstructor(casted.getId().contains("?") ? casted.getId() : uuid, new MetaType(casted.getType(), casted.getBaseSourceClass()));
            newInfo.setLocalPath(casted.getLocalPath());
            newInfo.setUri(casted.getUri());
            newInfo.setScannerClass(casted.getScannerClass());
            newInfo.setOrigin(OriginEnum.xml);
            uuidIdMap.put(uuid, casted.getId());
            String truncatedFilePath = truncateFilePath(newInfo.getXmlURI());
            uriUUIDMap.computeIfAbsent(truncatedFilePath.substring(0, truncatedFilePath.lastIndexOf('/')), k -> new ArrayList<>()).add(new UriMetadataId(casted.getId(), uuid, newInfo.getBaseSourceClass()));
            super.add(newInfo);
        }

        @Override
        public SourceInfo get(String id, Class<? extends SourceMetadata> sourceClass) {
            return super.get(resolveId(id, sourceClass), sourceClass);
        }

        @Override
        public boolean contains(String id, Class<? extends SourceMetadata> sourceClass) {
            return super.contains(resolveId(id, sourceClass), sourceClass);
        }

        private String resolveId(String id, Class sourceClass) {
            if (uuidPattern.matcher(id).matches())
                return id;
            String parsedPath = RouteUtil.parsePath(id);
            Optional<UriMetadataId> first = uriUUIDMap.get(currentUri).stream().filter(uriId -> uriId.id.equals(parsedPath) && uriId.metadataClass.isAssignableFrom(sourceClass)).findFirst();
            if (first.isPresent())
                return first.get().uuid;
            else
                return uriUUIDMap.get(DEFAULT_XML).stream().filter(uriId -> uriId.id.equals(parsedPath) && uriId.metadataClass.isAssignableFrom(sourceClass)).findFirst().orElse(new UriMetadataId(id, id)).uuid;
        }
    }
}

