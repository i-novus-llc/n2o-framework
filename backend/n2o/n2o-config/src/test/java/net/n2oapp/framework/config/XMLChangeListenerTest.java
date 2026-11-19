package net.n2oapp.framework.config;

import net.n2oapp.framework.api.event.MetadataChangedEvent;
import net.n2oapp.framework.api.event.N2oEventBus;
import net.n2oapp.framework.api.metadata.global.view.page.N2oPage;
import net.n2oapp.framework.api.register.MetaType;
import net.n2oapp.framework.api.register.MetadataRegister;
import net.n2oapp.framework.api.register.SourceTypeRegister;
import net.n2oapp.framework.config.register.N2oMetadataRegister;
import net.n2oapp.framework.config.register.N2oSourceTypeRegister;
import net.n2oapp.framework.config.register.XmlInfo;
import net.n2oapp.framework.config.register.scanner.XmlInfoScanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Тестирование слушателя изменений xml файлов в папке переопределений
 */
class XMLChangeListenerTest {
    private static final String DEFAULT_PATTERN = "classpath*:net/n2oapp/framework/config/xmlchangelistener/**/*.xml";
    private static final String CONTENT = "<?xml version='1.0' encoding='UTF-8'?>" +
            "<simple-page xmlns=\"http://n2oapp.net/framework/config/schema/page-4.0\"/>";

    @TempDir
    Path configDir;

    private MetadataRegister register;
    private XMLChangeListener listener;
    private final List<MetadataChangedEvent> events = new ArrayList<>();

    @BeforeEach
    void setUp() {
        SourceTypeRegister sourceTypeRegister = new N2oSourceTypeRegister();
        sourceTypeRegister.add(new MetaType("page", N2oPage.class));
        register = new N2oMetadataRegister();
        register.addAll(new XmlInfoScanner(DEFAULT_PATTERN, sourceTypeRegister).scan());
        N2oEventBus eventBus = new N2oEventBus(e -> events.add((MetadataChangedEvent) e));
        listener = new XMLChangeListener(Set.of(configDir.toString()), register, sourceTypeRegister,
                eventBus, DEFAULT_PATTERN);
    }

    @Test
    void testRestoreDefaultOnFileDelete() throws IOException {
        assertThat(uri("person"), containsString("xmlchangelistener/sub/person.page.xml"));

        Path file = Files.writeString(configDir.resolve("person.page.xml"), CONTENT);
        listener.fileCreated(file);
        assertThat(uri("person"), not(containsString("xmlchangelistener")));

        Files.delete(file);
        listener.fileDeleted(file);
        assertThat(uri("person"), containsString("xmlchangelistener/sub/person.page.xml"));
        assertThat(events, hasSize(2));
    }

    @Test
    void testRemoveOnFileDeleteWithoutDefault() throws IOException {
        Path file = Files.writeString(configDir.resolve("other.page.xml"), CONTENT);
        listener.fileCreated(file);
        assertThat(register.contains("other", N2oPage.class), is(true));

        Files.delete(file);
        listener.fileDeleted(file);
        assertThat(register.contains("other", N2oPage.class), is(false));
    }

    @Test
    void testRestoreDefaultOnDirectoryDelete() throws IOException {
        Path dir = Files.createDirectory(configDir.resolve("sub"));
        Path person = Files.writeString(dir.resolve("person.page.xml"), CONTENT);
        Path other = Files.writeString(dir.resolve("other.page.xml"), CONTENT);
        listener.fileCreated(person);
        listener.fileCreated(other);
        assertThat(uri("person"), not(containsString("xmlchangelistener")));

        Files.delete(person);
        Files.delete(other);
        Files.delete(dir);
        listener.fileDeleted(dir);
        assertThat(uri("person"), containsString("xmlchangelistener/sub/person.page.xml"));
        assertThat(register.contains("other", N2oPage.class), is(false));
    }

    @Test
    void testRestoreDefaultWithCaseInsensitiveId() throws IOException {
        Path file = Files.writeString(configDir.resolve("Person.page.xml"), CONTENT);
        listener.fileCreated(file);
        assertThat(uri("person"), not(containsString("xmlchangelistener")));

        Files.delete(file);
        listener.fileDeleted(file);
        assertThat(uri("person"), containsString("xmlchangelistener/sub/person.page.xml"));
        assertThat(events, hasSize(2));
    }

    private String uri(String id) {
        return ((XmlInfo) register.get(id, N2oPage.class)).getUri().replace("\\", "/");
    }
}