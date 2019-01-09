package net.n2oapp.framework.api.data;


import com.fasterxml.jackson.databind.ObjectMapper;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.criteria.dataset.Interval;
import net.n2oapp.criteria.filters.FilterType;
import net.n2oapp.framework.api.StringUtils;
import net.n2oapp.framework.api.metadata.domain.Domain;
import net.n2oapp.framework.api.metadata.global.dao.object.InvocationParameter;
import net.n2oapp.framework.api.metadata.local.CompiledObject;


import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * User: iryabov
 * Date: 14.06.13
 * Time: 18:19
 */
public class DomainProcessor {
    private static DomainProcessor ourInstance = new DomainProcessor(new ObjectMapper(), "dd.MM.yyyy HH:mm:ss");
    public static final Map<Class, String> simpleDomainsMap = new HashMap<Class, String>();

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
        simpleDomainsMap.put(DataSet.class, "object");
        simpleDomainsMap.put(BigDecimal.class, "numeric");
        simpleDomainsMap.put(Long.class, "long");
        simpleDomainsMap.put(Byte.class, "byte");
        simpleDomainsMap.put(Short.class, "short");
    }

    private final ObjectMapper objectMapper;
    private final String dateFormat;

    public DomainProcessor(ObjectMapper objectMapper, String dateFormat) {
        this.objectMapper = objectMapper;
        this.dateFormat = dateFormat;
    }

    public DataSet domainConversionByAction(DataSet inDataSet, CompiledObject.Operation operation) {
        return doDomainConversation(inDataSet, operation.getInParametersMap().values());
    }

    public Object deserialize(Object value, Class<?> clazz) {
        String domain = simpleDomainsMap.get(clazz);
        Object object = doDomainConversion(domain, value);
        if (object != null && !StringUtils.isDynamicValue(object)) {
            if (!clazz.isAssignableFrom(object.getClass()))
                throw new ClassCastException(String.format("Value [%s] is not a %s", value, clazz));
        }
        return object;
    }

    public DataSet doDomainConversation(DataSet inDataSet, Collection<? extends InvocationParameter> values) {
        for (InvocationParameter param : values) {
            String paramName = param.getId();
            Object value = inDataSet.get(paramName);
            if (value == null)
                continue;
            String domain = param.getDomain();
            inDataSet.put(paramName, doDomainConversion(domain, value));
        }
        return inDataSet;
    }

    @SuppressWarnings("unchecked")
    public Object doDomainConversion(String domain, Object value) {
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
            //если домен списковый, например "integer[]", приводим к домену его элементы
            String domainElement = domain.replaceAll("\\[\\]", "");
            try {
                if (value instanceof String) {
                    String array = (String) value;
                    //json array?
                    if (!(array.startsWith("[") && array.endsWith("]"))) {
                        array = "[" + array + "]";
                    }
                    value = objectMapper.readValue(array, List.class);
                }
                Collection res = createCollection(value);
                ((Collection) value).forEach(val -> res.add(doDomainConversion(domainElement, val)));
                return res;
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        } else if (isInterval(domain)) {
            //если домен интервальный, например "interval{date}", приводим к домену начало и конец интервала
            Map map = (Map) value;//todo а если значение в json?
            Interval res = new Interval();
            domain = domain.replaceAll("interval\\{", "").replaceAll("\\}", "");
            res.put("begin", doDomainConversion(domain, map.get("begin")));
            res.put("end", doDomainConversion(domain, map.get("end")));
            return res;
        } else {
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
    }

    private Collection createCollection(Object source) {
        if (!(source instanceof Collection))
            throw new IllegalStateException("domain is array, but value [" + source + "] not instanceof collection");
        if (((Collection) source).isEmpty()) {
            return (Collection) source;
        }

        if (source instanceof Set) {
            return new HashSet();
        }
        if (source instanceof List) {
            return new ArrayList();
        }
        return new ArrayList();
    }

    public List<String> serializeListValue(List values) {
        if (values == null) return null;
        List<String> res = new ArrayList<>();
        for (Object value : values) {
            res.add(serializeSimpleValue(value));
        }
        return res;
    }

    public String[] serializeArrayValue(String[] values) {
        if (values == null) return null;
        String[] res = new String[values.length];
        int i = 0;
        for (String value : values) {
            res[i] = serializeSimpleValue(value);
            i++;
        }
        return res;
    }

    public String serializeSimpleValue(Object value) {
        if (value == null) return null;
        if (value instanceof String || value instanceof Integer || value instanceof Boolean || value instanceof BigDecimal
                || value instanceof Long || value instanceof Short)
            return value.toString();
        else if (value instanceof Date)
            return new SimpleDateFormat(getDateFormat()).format(value);
        throw new IllegalArgumentException(String.format("%s is not simple value", value.getClass()));
    }

    private String getDateFormat() {
        return dateFormat;
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
            String val = value.toString().toLowerCase();
            if (val.equals("true") || val.equals("false")) return Domain.bool.getName();
            if (val.matches("([\\d]+)")) {
                try {
                    Integer.parseInt(val);
                } catch (NumberFormatException e) {
                    return Domain.long_.getName();
                }
                return Domain.integer.getName();
            }
            if (val.matches("[\\d]+(\\.|\\,)[\\d]+")) return Domain.numeric.getName();
            if (val.matches("[0-9]{2}.[0-9]{2}.[0-9]{4}")) return Domain.date.getName();
            if (val.matches("[0-9]{2}.[0-9]{2}.[0-9]{4} [0-9]{2}:[0-9]{2}")) return Domain.date.getName();
            if (val.matches("[0-9]{2}.[0-9]{2}.[0-9]{4} [0-9]{2}:[0-9]{2}:[0-9]{2}")) return Domain.date.getName();
            return Domain.string.getName();
        }
        Domain domain = Domain.getByClass(value.getClass());//подбираем домен по классу значения
        return domain != null ? domain.getName() : null;
    }

    private Object toObject(String domain, String value) throws ParseException, IOException, NumberFormatException {
        if ((value == null) || (value.isEmpty())) return null;
        if (Domain.bool.getName().equals(domain)) return Boolean.parseBoolean(value);
        if (Domain.date.getName().equals(domain)) return new SimpleDateFormat(getDateFormat()).parse(value);
        if (Domain.localdate.getName().equals(domain)) return LocalDate.parse(value, DateTimeFormatter.ofPattern(getDateFormat()));
        if (Domain.localdatetime.getName().equals(domain)) return LocalDateTime.parse(value, DateTimeFormatter.ofPattern(getDateFormat()));
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
