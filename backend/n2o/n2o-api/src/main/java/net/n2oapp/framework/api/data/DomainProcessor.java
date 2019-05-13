package net.n2oapp.framework.api.data;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.criteria.dataset.Interval;
import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.domain.Domain;
import net.n2oapp.framework.api.metadata.global.dao.object.InvocationParameter;
import net.n2oapp.framework.api.metadata.local.CompiledObject;


import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * User: iryabov
 * Date: 14.06.13
 * Time: 18:19
 */
public class DomainProcessor {
    private static DomainProcessor ourInstance = new DomainProcessor();
    private static final Map<Class, String> simpleDomainsMap = new HashMap<Class, String>();

    @Deprecated
    public static DomainProcessor getInstance() {
        return ourInstance;
    }

    static {
        simpleDomainsMap.put(Integer.class, "integer");
        simpleDomainsMap.put(String.class, "string");
        simpleDomainsMap.put(Boolean.class, "boolean");
        simpleDomainsMap.put(Date.class, "date");
        simpleDomainsMap.put(LocalDate.class, "localdate");
        simpleDomainsMap.put(LocalDateTime.class, "localdatetime");
        simpleDomainsMap.put(ZonedDateTime.class,"zoneddatetime");
        simpleDomainsMap.put(OffsetDateTime.class,"offsetdatetime");
        simpleDomainsMap.put(DataSet.class, "object");
        simpleDomainsMap.put(BigDecimal.class, "numeric");
        simpleDomainsMap.put(Long.class, "long");
        simpleDomainsMap.put(Byte.class, "byte");
        simpleDomainsMap.put(Short.class, "short");
    }

    private final ObjectMapper objectMapper;
    private final DateFormat dateFormat;

    public DomainProcessor(ObjectMapper objectMapper, DateFormat dateFormat) {
        this.objectMapper = objectMapper;
        this.dateFormat = dateFormat;
    }

    public DomainProcessor() {
        this.dateFormat = new StdDateFormat();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.setDateFormat(dateFormat);
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
        } else {
            return convertObject(value, domain);
        }
    }

    public Object deserialize(Object value) {
        return deserialize(value, (String) null);
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
        String domain = simpleDomainsMap.get(clazz);
        Object object = deserialize(value, domain);
        if (object != null && !StringUtils.isDynamicValue(object)) {
            if (!clazz.isAssignableFrom(object.getClass()))
                throw new ClassCastException(String.format("Value [%s] is not a %s", value, clazz));
        }
        return object;
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

    public DataSet doDomainConversation(DataSet inDataSet, Collection<? extends InvocationParameter> values) {
        for (InvocationParameter param : values) {
            String paramName = param.getId();
            Object value = inDataSet.get(paramName);
            if (value == null)
                continue;
            String domain = param.getDomain();
            inDataSet.put(paramName, deserialize(value, domain));
        }
        return inDataSet;
    }

    @Deprecated //use deserialize(Object, String)
    public Object doDomainConversion(String domain, Object value) {
        return deserialize(value, domain);
    }

    private Object convertObject(Object value, String domain) {
        //строку преобразуем в домен, а если значение не строка, то возвращаем как есть
        if (value instanceof String || value instanceof Number) {
            try {
                return toObject(domain, value.toString());
            } catch (ParseException | IOException e) {
                throw new IllegalStateException(String.format("failed to cast to type [%s] value [%s]", domain, value), e);
            }
        } else
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
        Object begin = null;
        Object end = null;
        String domainElement = domain.replaceAll("interval\\{", "").replaceAll("\\}", "");
        if (value instanceof String
                && (((String)value).startsWith("{")
                && ((String)value).endsWith("}"))) {
            //json
            try {
                value = objectMapper.readValue((String) value, Map.class);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
        if (value instanceof Collection) {
            //array
            Iterator iterator = ((Collection) value).iterator();
            begin = iterator.hasNext() ? iterator.next() : null;
            end = iterator.hasNext() ? iterator.next() : null;
        } else if (value instanceof Map) {
            //map
            Map<String, Object> map = (Map<String, Object>) value;
            begin = map.getOrDefault("begin", map.get("from"));
            end = map.getOrDefault("end", map.get("to"));
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
        String domainElement = domain.replaceAll("\\[\\]", "");
        if (value instanceof String
                && (((String) value).startsWith("["))
                && (((String) value).endsWith("]"))) {
            //json
            try {
                value = objectMapper.readValue((String)value, List.class);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
        if (value instanceof String) {
            //string list
            String string = (String) value;
            String[] elements = string.split(",");
            for (String element : elements) {
                resultList.add(deserialize(element, domainElement));
            }
        } else if (value instanceof Collection) {
            for (Object element : (Collection<?>) value) {
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
        if (value instanceof Collection) {
            if (((Collection) value).isEmpty())
                return "integer[]";//не важно какой тип элементов списка, если он пустой
            Object firstElement = ((Collection) value).iterator().next();
            String elementsDomain = (findDomain(firstElement));
            if (elementsDomain == null) return null;
            return elementsDomain + "[]";
        }
        if (value instanceof String) {
            String val = ((String) value).toLowerCase();
            if (val.equals("true") || value.equals("false")) return Domain.bool.getName();
            if (val.matches("([\\d]{1,6})")) {
                try {
                    Integer.parseInt(val);
                } catch (NumberFormatException e) {
                    throw new N2oException("Value is not Integer [" + val + "]. Set domain explicitly!", e);
                }
                return Domain.integer.getName();
            }
            return Domain.string.getName();
        }
        Domain domain = Domain.getByClass(value.getClass());//подбираем домен по классу значения
        return domain != null ? domain.getName() : null;
    }

    private Object toObject(String domain, String value) throws ParseException, IOException, NumberFormatException {
        if ((value == null) || (value.isEmpty())) return null;
        if (Domain.bool.getName().equals(domain)) return Boolean.parseBoolean(value);

        if (Domain.date.getName().equals(domain)) return dateFormat.parse(value);
        if (Domain.zoneddatetime.getName().equals(domain))
            return ZonedDateTime.parse(value, DateTimeFormatter.ISO_ZONED_DATE_TIME);
        if (Domain.offsetdatetime.getName().equals(domain))
            return OffsetDateTime.parse(value, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        if (Domain.localdate.getName().equals(domain))
            return LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE);
        if (Domain.localdatetime.getName().equals(domain))
            return LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        if (Domain.byte_.getName().equals(domain)) return Byte.parseByte(value);
        if (Domain.short_.getName().equals(domain)) return Short.parseShort(value);
        if (Domain.integer.getName().equals(domain)) return Integer.parseInt(value);
        if (Domain.long_.getName().equals(domain)) return Long.parseLong(value);
        if (Domain.object.getName().equals(domain)) return objectMapper.readValue(value, DataSet.class);
        if (Domain.numeric.getName().equals(domain)) return new BigDecimal(value.replace(",", "."));
        return value;
    }


    public static String getDomain(String simpleDomain, FilterType type) {
        switch (type.arity) {
            case unary:
                return simpleDomain;
            case n_ary:
                return simpleDomain != null ? simpleDomain + "[]" : null;
            case nullary:
                return "boolean";
        }
        throw new RuntimeException(String.format("arity '%s' for filter-type '%s' is unknown", type.arity, type));
    }

}
