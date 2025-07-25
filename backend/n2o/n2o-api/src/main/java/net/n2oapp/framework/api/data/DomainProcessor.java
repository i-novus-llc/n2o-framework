package net.n2oapp.framework.api.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.criteria.dataset.Interval;
import net.n2oapp.framework.api.criteria.filters.FilterTypeEnum;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.domain.DomainEnum;
import net.n2oapp.framework.api.metadata.global.dao.object.AbstractParameter;
import net.n2oapp.framework.api.metadata.global.dao.object.field.ObjectSimpleField;
import net.n2oapp.framework.api.metadata.local.CompiledObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_TIME;

/**
 * Процессор приведения к типу
 */
public class DomainProcessor {
    public static final String JAVA_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    private final ObjectMapper objectMapper;

    public DomainProcessor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper.setDateFormat(new SimpleDateFormat(JAVA_DATE_FORMAT));
    }

    public DomainProcessor() {
        this(new ObjectMapper());
    }

    /**
     * Конвертировать значение в определенный тип
     *
     * @param value  Значение
     * @param domain Тип данных
     * @return Конвертированное значение
     */
    public Object deserialize(Object value, String domain) {
        if (value == null)
            return null;
        if (StringUtils.isDynamicValue(value))
            return value;
        if (value instanceof String strValue) {
            if (strValue.isEmpty())
                return null;
            if (StringUtils.isEscapedString(strValue)) {
                value = StringUtils.unwrapEscapedString(strValue);
                domain = DomainEnum.STRING.getName();
            }
        }

        if (domain == null) {
            //пытаемся подобрать домен по значению, если не подобрали - возвращаем значение как есть
            domain = findDomain(value);
            if (domain == null)
                return value;
        }

        domain = domain.toLowerCase();
        if (isArray(domain)) {
            return convertArray(value, domain);
        } else if (isInterval(domain)) {
            return convertInterval(value, domain);
        } else if (StringUtils.isJson(value)) {
            return convertObject(((String) value).substring(1, ((String) value).length() - 1), domain);
        }

        return convertObject(value, domain);
    }

    /**
     * Конвертировать значение в определенный класс
     *
     * @param value  Значение
     * @param domain Тип данных
     * @return Конвертированное значение
     * @throws ClassCastException Если конвертированное значение не соответствует классу
     */
    public Object deserialize(Object value, DomainEnum domain) {
        return deserialize(value, domain != null ? domain.getName() : null);
    }

    /**
     * Конвертировать значение в определенный класс
     *
     * @param value Значение
     * @param clazz Класс
     * @return Конвертированное значение
     * @throws ClassCastException Если конвертированное значение не соответствует классу
     */
    public Object deserialize(Object value, Class<?> clazz) {
        if (clazz.isEnum())
            return deserializeEnum(value, (Class<? extends Enum>) clazz);
        Object result = deserialize(value, DomainEnum.getByClass(clazz));
        if (result != null
                && !StringUtils.isDynamicValue(result)
                && !clazz.isAssignableFrom(result.getClass())) {
            throw new ClassCastException(String.format("Value [%s] is not a %s", value, clazz));
        }

        return result;
    }

    public Object deserialize(Object value) {
        return deserialize(value, (String) null);
    }

    /**
     * Конвертировать значение в Enum объект
     *
     * @param value     Значение
     * @param enumClass Enum класс
     * @return Enum объект или null
     */
    @SuppressWarnings("unchecked")
    public <T extends Enum<T>> T deserializeEnum(Object value, Class<T> enumClass) {
        if (value == null)
            return null;
        if (enumClass.isAssignableFrom(value.getClass()))
            return (T) value;
        if (value instanceof String strValue) {
            boolean idAware = IdAware.class.isAssignableFrom(enumClass);
            if (idAware) {
                for (Enum enumValue : enumClass.getEnumConstants()) {
                    IdAware idEnum = (IdAware) enumValue;
                    if (idEnum.getId().equalsIgnoreCase(strValue)) {
                        return (T) enumValue;
                    }
                }
            } else {
                for (Enum enumValue : enumClass.getEnumConstants()) {
                    if (enumValue.name().equalsIgnoreCase(strValue)) {
                        return (T) enumValue;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Конвертировать значение в строковый тип
     *
     * @param value Значение
     * @return Значение в виде строки
     */
    public String serialize(Object value) {
        if (value == null)
            return null;
        if (value instanceof String || value instanceof Boolean)
            return value.toString();
        try {
            return objectMapper.writeValueAsString(value).replace("\"", "");
        } catch (JsonProcessingException e) {
            throw new N2oException(e);
        }
    }

    public DataSet domainConversionByAction(DataSet inDataSet, CompiledObject.Operation operation) {
        return doDomainConversation(inDataSet, operation.getInParametersMap().values());
    }

    public DataSet doDomainConversation(DataSet inDataSet, Collection<AbstractParameter> values) {
        for (AbstractParameter param : values) {
            if (param instanceof ObjectSimpleField simpleField) {
                String paramName = param.getId();
                Object value = inDataSet.get(paramName);
                if (value == null)
                    continue;
                String domain = simpleField.getDomain();
                inDataSet.put(paramName, deserialize(value, domain));
            }
        }
        return inDataSet;
    }

    private Object convertObject(Object value, String domain) {
        //строку преобразуем в домен, а если значение не строка, то возвращаем как есть
        if (value instanceof String || value instanceof Number) {
            try {
                return toObject(domain, value.toString());
            } catch (ParseException | IOException e) {
                throw new IllegalStateException(String.format("failed to cast to type [%s] value [%s]", domain, value), e);
            }
        }

        return value;
    }

    /**
     * Если домен интервальный, например "interval{date}", приводим к домену начало и конец интервала
     *
     * @param value  Значение
     * @param domain Домен
     * @return Интервал из двух значений приведенных к домену
     */
    @SuppressWarnings("unchecked")
    private Interval<?> convertInterval(Object value, String domain) {
        Interval<?> res = new Interval<>();
        Object begin;
        Object end;
        String domainElement = domain.replace("interval{", "").replace("}", "");
        if (value instanceof String strValue
                && (strValue.startsWith("{")
                && strValue.endsWith("}"))) {
            //json
            try {
                value = objectMapper.readValue(strValue, Map.class);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
        if (value instanceof Collection collectionValue) {
            //array
            Iterator iterator = collectionValue.iterator();
            begin = iterator.hasNext() ? iterator.next() : null;
            end = iterator.hasNext() ? iterator.next() : null;
        } else if (value instanceof Map mapValue) {
            //map
            begin = mapValue.getOrDefault("begin", mapValue.get("from"));
            end = mapValue.getOrDefault("end", mapValue.get("to"));
        } else {
            throw new IllegalStateException("Value " + value + " is not an interval");
        }
        res.setBegin(deserialize(begin, domainElement));
        res.setEnd(deserialize(end, domainElement));
        return res;
    }

    /**
     * Если домен списковый, например "integer[]", приводим к домену его элементы
     *
     * @param value  значение
     * @param domain домен
     * @return Список элементов приведенных к домену
     */
    private Object convertArray(Object value, String domain) {
        List<Object> resultList = new ArrayList<>();
        String domainElement = domain.replace("[]", "");
        if (value instanceof String strValue
                && (strValue.startsWith("["))
                && (strValue.endsWith("]"))) {
            //json
            try {
                value = objectMapper.readValue(strValue, List.class);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
        if (value instanceof String strValue) {
            //string list
            String[] elements = strValue.split(",");
            for (String element : elements) {
                resultList.add(deserialize(element, domainElement));
            }
        } else if (value instanceof Collection collectionValue) {
            for (Object element : collectionValue) {
                resultList.add(deserialize(element, domainElement));
            }
        } else {
            throw new IllegalStateException("Value " + value + " is not a collection");
        }
        return resultList;
    }


    private boolean isInterval(String domain) {
        return domain.contains("interval");
    }

    private boolean isArray(String domain) {
        return domain.contains("[]");
    }

    private String findDomain(Object value) {
        if (value instanceof Collection collectionValue) {
            if (((Collection<?>) value).isEmpty())
                return "integer[]"; // неважно, какой тип элементов списка, если он пустой
            Optional<Object> firstElement = collectionValue.stream().filter(Objects::nonNull).findFirst();
            if (firstElement.isEmpty())
                return null;
            String elementsDomain = (findDomain(firstElement.get()));
            if (elementsDomain == null) return null;
            return elementsDomain + "[]";
        }
        if (value instanceof String strValue) {
            String val = strValue.toLowerCase();
            if (val.equals("true") || val.equals("false")) return DomainEnum.BOOLEAN.getName();
            if (val.length() <= 6 && val.chars().allMatch(Character::isDigit)) {
                try {
                    Integer.parseInt(val);
                } catch (NumberFormatException e) {
                    throw new N2oException("Value is not Integer [" + val + "]. Set domain explicitly!", e);
                }
                return DomainEnum.INTEGER.getName();
            }
            return DomainEnum.STRING.getName();
        }
        DomainEnum domain = DomainEnum.getByClass(value.getClass());//подбираем домен по классу значения
        return domain != null ? domain.getName() : null;
    }

    private Object toObject(String domain, String value) throws ParseException, IOException {
        if ((value == null) || (value.isEmpty()))
            return null;
        if (DomainEnum.STRING.getName().equals(domain))
            return value;
        if (DomainEnum.BOOLEAN.getName().equals(domain))
            return Boolean.parseBoolean(value);
        if (DomainEnum.DATE.getName().equals(domain))
            return objectMapper.getDateFormat().parse(value);
        if (DomainEnum.ZONEDDATETIME.getName().equals(domain))
            return ZonedDateTime.parse(value, DateTimeFormatter.ISO_ZONED_DATE_TIME);
        if (DomainEnum.OFFSETDATETIME.getName().equals(domain))
            return OffsetDateTime.parse(value, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        if (DomainEnum.LOCALDATETIME.getName().equals(domain))
            return LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        if (DomainEnum.LOCALDATE.getName().equals(domain))
            return LocalDate.parse(value, new DateTimeFormatterBuilder()
                    .append(ISO_LOCAL_DATE)
                    .optionalStart().appendLiteral('T')
                    .append(ISO_LOCAL_TIME).toFormatter());
        if (DomainEnum.BYTE.getName().equals(domain))
            return Byte.parseByte(value);
        if (DomainEnum.SHORT.getName().equals(domain))
            return Short.parseShort(value);
        if (DomainEnum.INTEGER.getName().equals(domain))
            return Integer.parseInt(value);
        if (DomainEnum.LONG.getName().equals(domain))
            return Long.parseLong(value);
        if (DomainEnum.NUMERIC.getName().equals(domain))
            return new BigDecimal(value.replace(",", "."));
        if (DomainEnum.OBJECT.getName().equals(domain))
            return objectMapper.readValue(value, DataSet.class);
        return value;
    }


    public static String getDomain(String simpleDomain, FilterTypeEnum type) {
        switch (type.arity) {
            case UNARY:
                return simpleDomain;
            case N_ARY:
                return simpleDomain != null ? simpleDomain + "[]" : null;
            case NULLARY:
                return "boolean";
        }
        throw new IllegalStateException(String.format("arity '%s' for filter-type '%s' is unknown", type.arity, type));
    }

}
