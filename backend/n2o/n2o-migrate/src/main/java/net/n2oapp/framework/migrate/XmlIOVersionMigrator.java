package net.n2oapp.framework.migrate;

import net.n2oapp.framework.api.metadata.SourceMetadata;
import net.n2oapp.framework.api.metadata.io.ProxyNamespaceIO;
import net.n2oapp.framework.api.metadata.persister.NamespacePersisterFactory;
import net.n2oapp.framework.api.metadata.reader.NamespaceReaderFactory;
import net.n2oapp.framework.config.N2oApplicationBuilder;
import net.n2oapp.framework.config.selective.SelectiveUtil;
import net.n2oapp.framework.config.selective.persister.PersisterFactoryByMap;
import net.n2oapp.framework.config.selective.reader.ReaderFactoryByMap;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Component
public class XmlIOVersionMigrator {
    private final NamespaceReaderFactory readerFactory;
    private final NamespacePersisterFactory persisterFactory;
    private static final XMLOutputter XML_OUTPUTTER = new XMLOutputter(Format.getPrettyFormat().setIndent("    "));
    private static final String XML_HEADER = "<?xml version='1.0' encoding='UTF-8'?>\r\n";
    private static final Map<String, String> namespaceUriMapping = Map.of(
            "http://n2oapp.net/framework/config/schema/query-4.0", "http://n2oapp.net/framework/config/schema/query-5.0",
            "http://n2oapp.net/framework/config/schema/application-2.0", "http://n2oapp.net/framework/config/schema/application-3.0"
    );

    public XmlIOVersionMigrator(N2oApplicationBuilder builder) {
        ReaderFactoryByMap readerFactoryByMap = new ReaderFactoryByMap(builder.getEnvironment());
        readerFactoryByMap.setIOProcessor(new MigratorIOProcessorImpl(readerFactoryByMap, builder.getEnvironment()));
        if (builder.getEnvironment().getNamespaceReaderFactory() instanceof ReaderFactoryByMap factoryByMap) {
            factoryByMap.getMap().values().stream()
                    .flatMap(v -> v.values().stream())
                    .map(ProxyNamespaceIO.class::cast)
                    .forEach(proxy -> readerFactoryByMap.add(new MigratorProxyNamespaceIO<>(proxy.getIo())));
        }
        this.readerFactory = readerFactoryByMap;

        PersisterFactoryByMap persisterFactoryByMap = new PersisterFactoryByMap();
        persisterFactoryByMap.setIOProcessor(new MigratorIOProcessorImpl(persisterFactoryByMap, builder.getEnvironment()));
        if (builder.getEnvironment().getNamespacePersisterFactory() instanceof PersisterFactoryByMap factoryByMap) {
            factoryByMap.getMap().values().stream()
                    .flatMap(v -> v.values().stream())
                    .map(ProxyNamespaceIO.class::cast)
                    .forEach(proxy -> persisterFactoryByMap.add(new MigratorProxyNamespaceIO<>(proxy.getIo())));
        }
        this.persisterFactory = persisterFactoryByMap;
    }

    /**
     * Миграция xml на новую версию
     *
     * @param source xml старой версии
     * @return xml новой версии
     */
    public String migrate(String source) {
        SourceMetadata sourceObject = SelectiveUtil.read(source, readerFactory);
        String oldNamespaceUri = sourceObject.getNamespaceUri();
        String newNamespaceUri = namespaceUriMapping.get(oldNamespaceUri);
        if (newNamespaceUri == null) {
            return null;
        }
        sourceObject.setNamespaceUri(newNamespaceUri);
        Element xmlElement = persisterFactory
                .produce(sourceObject.getClass(), sourceObject.getNamespace())
                .persist(sourceObject, sourceObject.getNamespace());
        MigratorInfoHolder.clear();
        return XML_HEADER + XML_OUTPUTTER.outputString(xmlElement)
                .replace(" />", "/>")
                .replace(" xmlns=\"" + oldNamespaceUri + "\"", "");
    }

    /**
     * Миграция xml-файла в classpath
     *
     * @param filePath  путь файла
     * @param directory Директория файла
     */
    public void migrateFile(String filePath, String directory) throws IOException {
        Path path = Paths.get(directory + filePath);
        String oldVersionXml = Files.readString(path);
        String migratedXml = migrate(oldVersionXml);
        if (migratedXml != null) {
            Files.writeString(path, migratedXml);
        }
    }

    /**
     * Миграция xml-файлов заданного типа из директории в classpath.
     *
     * @param fileType     Расширение файла, которое необходимо искать (например, ".query.xml").
     * @param namespaceUri URI пространства имен, который должны содержать XML-файлы.
     * @param directory    Директория для поиска файлов.
     * @return Возвращает true, если миграция прошла успешно
     */
    public boolean migrateFiles(String fileType, String namespaceUri, String directory) throws IOException {
        List<String> oldVersionXmlFiles = new ArrayList<>();
        Path dir = Paths.get(directory);
        try (Stream<Path> paths = Files.walk(dir)) {
            paths.filter(path -> path.toString().endsWith(fileType))
                    .forEach(path -> {
                        try {
                            String oldVersionXml = Files.readString(path);
                            if (oldVersionXml.contains(namespaceUri) && !oldVersionXml.contains("${")) {
                                oldVersionXmlFiles.add(path.toString()
                                        .replace("\\", "/")
                                        .replace(directory, ""));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        }
        for (String file : oldVersionXmlFiles) {
            migrateFile(file, directory);
        }

        return true;
    }
}