package net.n2oapp.framework.sandbox.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Класс для работы с xsd схемами.
 * Позволяет получать схему по входному неймспейсу.
 */
@Component
public class XsdSchemaParser {

    @Autowired
    private ResourceLoader resourceLoader;

    @Value("${n2o.sandbox.schemas.path}")
    private String xsdFolder;

    private Map<String, Resource> resourceBySchemaName = new HashMap<>();

    private static final String XSD_HEADER_START_TAG = "<?xml";
    private static final String XSD_END_TAG = "</xs:schema>";
    private static final String XSD_NAMESPACE = "xmlns";
    private static final String CLOSE_TAG = ">";
    private static final String TYPE_ATTRIBUTE = "type=";
    private static final String BASE_ATTRIBUTE = "base=";
    private static final String NAME_ATTRIBUTE = "name=";
    private static final Map<String, String> startEndDefinitionTags;
    private static final String GLOBAL_SCHEMA = "http://www.w3.org/2001/XMLSchema";
    private static final String XSD_EXTENSION = ".xsd";
    private static final String DEF_PREFIX_SEPARATOR = "__";

    static {
        startEndDefinitionTags = Map.of(
                "<xs:complexType", "</xs:complexType>",
                "<xs:simpleType", "</xs:simpleType>"
        );
    }


    /**
     * Получение ресурса схемы по ее неймспейсу
     *
     * @param schemaNamespace Неймспейс схемы
     * @return XSD cхема
     * @throws IOException
     */
    public Resource getSchema(String schemaNamespace) throws IOException {
        return prepareSchema(schemaNamespace);
    }

    /**
     * Получение ресурса схемы и ее преобразование в случае, если она содержит зависимости на другие схемы
     *
     * @param schemaNamespace Неймспейс схемы
     * @return XSD схема
     * @throws IOException
     */
    private Resource prepareSchema(String schemaNamespace) throws IOException {
        String schemaName = getSchemaNameByNamespace(schemaNamespace);

        if (resourceBySchemaName.containsKey(schemaName))
            return resourceBySchemaName.get(schemaName);

        Resource resource = Arrays.stream(ResourcePatternUtils
                .getResourcePatternResolver(resourceLoader)
                .getResources("classpath*:/**" + xsdFolder + "**/" + schemaName + XSD_EXTENSION))
                .findFirst().orElseThrow(() -> new FileNotFoundException("Schema " + schemaName + XSD_EXTENSION + " is not found"));

        try (Stream<String> lines = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)).lines()) {
            List<String> linesList = lines.collect(Collectors.toList());
            Map<String, String> schemaNamespacesByAlias = getSchemaNamespaceByAliasMap(linesList);
            if (schemaNamespacesByAlias.isEmpty())
                resourceBySchemaName.put(schemaName, resource);
            else {
                Map<String, List<String>> definitionRowsByName = new HashMap<>();
                for (String namespace : schemaNamespacesByAlias.values()) {
                    Resource subSchema = prepareSchema(namespace);
                    fillSchemaDefinitions(subSchema, definitionRowsByName);
                    continue;
                }

                Resource mergedSchema = mergeSchemas(linesList, definitionRowsByName, schemaName, schemaNamespacesByAlias);
                resourceBySchemaName.put(schemaName, mergedSchema);
            }
        }

        return resourceBySchemaName.get(schemaName);
    }

    /**
     * Заполнение Map определениями схемы
     *
     * @param schema            XSD схема
     * @param schemaDefinitions Map, которая будет заполняться определениями схемы
     * @throws IOException
     */
    private void fillSchemaDefinitions(Resource schema, Map<String, List<String>> schemaDefinitions) throws IOException {
        String prefix = getSchemaPrefix(schema.getFilename());

        try (Stream<String> lines = new BufferedReader(new InputStreamReader(schema.getInputStream(), StandardCharsets.UTF_8)).lines()) {
            List<String> linesList = lines.collect(Collectors.toList());
            int i = 0;
            while (i < linesList.size()) {
                String trimmedStr = linesList.get(i).trim();

                Optional<String> startDefTag = startEndDefinitionTags.keySet().stream().filter(trimmedStr::startsWith).findFirst();
                if (startDefTag.isPresent()) {
                    List<String> defRows = new ArrayList<>();
                    String nameValue = getAttributeValue(trimmedStr, NAME_ATTRIBUTE);
                    if (nameValue == null) {
                        i++;
                        continue;
                    }

                    // add prefix to def if not contains
                    if (isContainsPrefixSeparator(nameValue)) {
                        defRows.add(linesList.get(i));
                        schemaDefinitions.put(nameValue, defRows);
                    } else {
                        defRows.add(addPrefixToAttributeValue(linesList.get(i), nameValue, prefix));
                        schemaDefinitions.put(prefix + nameValue, defRows);
                    }

                    // for nested definition
                    int defCount = 1;
                    do {
                        i++;
                        trimmedStr = linesList.get(i).trim();

                        if (startEndDefinitionTags.keySet().stream().anyMatch(trimmedStr::startsWith))
                            defCount++;
                        else if (startEndDefinitionTags.values().stream().anyMatch(trimmedStr::startsWith))
                            defCount--;

                        String typeValue = getAttributeValue(trimmedStr, BASE_ATTRIBUTE, TYPE_ATTRIBUTE);
                        String line = linesList.get(i);
                        if (typeValue != null && !isExternalSchemeType(typeValue))
                            line = addPrefixToAttributeValue(line, typeValue, prefix);
                        defRows.add(line);
                    } while (defCount != 0);
                }
                i++;
            }
        }
    }

    /**
     * Получение значение атрибута в указанной строке
     *
     * @param line       Строка, в которой будет искаться значение атрибута
     * @param attribute  Имя главного атрибута
     * @param attributes Список дополнительных атрибутов, которые по порядку будут искаться в строке в случае,
     *                   если главный атрибут не был найден
     * @return Значение атрибута или null, если ни один из указанных атрибутов не был найден
     */
    private String getAttributeValue(String line, String attribute, String... attributes) {
        int attrIdx = line.indexOf(attribute);
        String attr = attribute;

        if (attrIdx == -1 && attributes != null) {
            for (String s : attributes) {
                attrIdx = line.indexOf(s);
                if (attrIdx != -1) {
                    attr = s;
                    break;
                }
            }
        }

        if (attrIdx == -1)
            return null;

        int beginNameIdx = attrIdx + attr.length() + 1;
        int lastQuoteIndex = line.indexOf("\"", beginNameIdx);
        return line.substring(beginNameIdx, lastQuoteIndex);
    }

    /**
     * Получение неймспейсов схем, от которых зависит текущая схема
     *
     * @param linesList Список строк схемы
     * @return Map, где в качестве ключа псевдоним схемы, а в качестве значения ее неймспейс
     */
    private Map<String, String> getSchemaNamespaceByAliasMap(List<String> linesList) {
        Map<String, String> schemaNamespaceByAlias = new HashMap<>();
        for (String line : linesList) {
            String trimmedStr = line.trim();
            if (trimmedStr.startsWith(XSD_HEADER_START_TAG))
                continue;

            int idx = trimmedStr.indexOf(XSD_NAMESPACE);
            if (idx != -1) {
                String[] split = trimmedStr.split("=");
                String alias = split[0].trim().substring(XSD_NAMESPACE.length() + 1);
                String trimmedNamespace = split[1].trim();
                int suffixLength = trimmedNamespace.endsWith(CLOSE_TAG) ? 2 : 1;
                String extSchemaNamespace = trimmedNamespace.substring(1, trimmedNamespace.length() - suffixLength);
                if (!extSchemaNamespace.equals(GLOBAL_SCHEMA))
                    schemaNamespaceByAlias.put(alias, extSchemaNamespace);
            }

            if (trimmedStr.endsWith(CLOSE_TAG))
                break;
        }
        return schemaNamespaceByAlias;
    }

    /**
     * Слияние определений внешних схем в текущую схему
     *
     * @param lines                   Строки текущей схемы
     * @param extSchemaDefinitions    Определения внешних схем, от которых зависит текущая схема
     * @param schemaName              Имя текущей схемы
     * @param schemaNamespacesByAlias Map неймспейсов внешних схем по псевдонимам, используемым в текущей схеме
     * @return Новая XSD схема, содержащая как текущую, так и определения внешних схем, от которых она зависит
     * @throws IOException
     */
    private Resource mergeSchemas(List<String> lines, Map<String, List<String>> extSchemaDefinitions,
                                  String schemaName, Map<String, String> schemaNamespacesByAlias) throws IOException {
        // get index of end schema line
        int endSchemaIdx = lines.size() - 1;
        while (!lines.get(endSchemaIdx).trim().startsWith(XSD_END_TAG))
            endSchemaIdx--;

        // replace type with link to external schema with external schema prefix
        // act:actionDefinition -> action__actionDefinition
        List<String> mergedSchemaLines = new ArrayList<>();
        for (int i = 0; i < endSchemaIdx; i++) {
            String line = lines.get(i);
            String typeValue = getAttributeValue(lines.get(i), BASE_ATTRIBUTE, TYPE_ATTRIBUTE);
            if (typeValue != null) {
                String[] split = typeValue.split(":");
                if (split.length == 2 && schemaNamespacesByAlias.containsKey(split[0]))
                    line = line.replace(split[0] + ":",
                            getSchemaPrefix(getSchemaNameByNamespace(schemaNamespacesByAlias.get(split[0]))));
            }
            mergedSchemaLines.add(line);
        }
        for (List<String> def : extSchemaDefinitions.values())
            mergedSchemaLines.addAll(def);
        mergedSchemaLines.add(lines.get(endSchemaIdx));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(outputStream);
        for (String line : mergedSchemaLines)
            out.write((line + System.lineSeparator()).getBytes(StandardCharsets.UTF_8));
        out.flush();
        byte[] bytes = outputStream.toByteArray();

        return new ByteArrayResource(bytes) {
            @Override
            public String getFilename() {
                return schemaName + XSD_EXTENSION;
            }
        };
    }

    /**
     * Добавление префикса к значению атрибута в заданной строке
     *
     * @param line           Входная строка
     * @param attributeValue Значение атрибута
     * @param prefix         Префикс
     * @return Строка с заданным префиксом у значения атрибута
     */
    private String addPrefixToAttributeValue(String line, String attributeValue, String prefix) {
        int attrIdx = line.indexOf("\"" + attributeValue + "\"");
        return line.substring(0, attrIdx + 1) + prefix + line.substring(attrIdx + 1);
    }

    /**
     * Получение префикса схемы по ее имени
     * Нужно для добавления префикса внешним определениям схемы
     *
     * @param schemaName Имя схемы
     * @return Префикс схемы
     */
    private String getSchemaPrefix(String schemaName) {
        return schemaName.substring(0, schemaName.lastIndexOf("-")) + DEF_PREFIX_SEPARATOR;
    }

    /**
     * Получение имени схемы по ее неймспейсу
     *
     * @param schemaNamespace Неймспейс схемы
     * @return Имя схемы
     */
    private String getSchemaNameByNamespace(String schemaNamespace) {
        return schemaNamespace.substring(schemaNamespace.lastIndexOf("/") + 1);
    }

    /**
     * Содержит ли входящая строка разделить префикса
     *
     * @param text Входящая строка
     * @return true, если содержит, false, если - нет
     */
    private boolean isContainsPrefixSeparator(String text) {
        return text.matches(".+" + DEF_PREFIX_SEPARATOR + ".+");
    }

    /**
     * Является ли входящая строка типом из внешней схемы
     *
     * @param type Входящая строка
     * @return true, если является, false, если - нет
     */
    private boolean isExternalSchemeType(String type) {
        return type.matches(".+(:|" + DEF_PREFIX_SEPARATOR + ").+");
    }
}
