package net.n2oapp.framework.export.format;

import com.linuxense.javadbf.DBFField;
import net.n2oapp.criteria.dataset.DataSet;
import net.n2oapp.framework.export.streaming.N2oDataStreamingUtil;
import net.n2oapp.data.streaming.converter.impl.dbf.DbfConverterBase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class DbfConverter extends DbfConverterBase<DataSet> {

    public static final Map<String, Byte> DOMAINS_MAPPING = new HashMap<>();

    static {
        DOMAINS_MAPPING.put("string", DBFField.FIELD_TYPE_C);
        DOMAINS_MAPPING.put("boolean", DBFField.FIELD_TYPE_L);
        DOMAINS_MAPPING.put("integer", DBFField.FIELD_TYPE_N);
        DOMAINS_MAPPING.put("long", DBFField.FIELD_TYPE_N);
        DOMAINS_MAPPING.put("short", DBFField.FIELD_TYPE_N);
        DOMAINS_MAPPING.put("byte", DBFField.FIELD_TYPE_N);
        DOMAINS_MAPPING.put("date", DBFField.FIELD_TYPE_D);
    }

    private List<N2oDataStreamingUtil.Field> n2oFields;

    public DbfConverter(int numberOfRecords, List<N2oDataStreamingUtil.Field> fields) {
        super(numberOfRecords, toFields(fields));
        this.n2oFields = fields;
    }


    /**
     * Извлекаем значения пригодны для dbf из dataSet.
     */
    @Override
    protected List retrieveValues(DataSet dataSet) {
        return n2oFields.stream()
                .map(field -> processValue(dataSet.get(field.getId())))
                .collect(toList());
    }

    private static Object processValue(Object o) {
        if (o instanceof Integer)
            return (double) ((int) o);
        if (o instanceof Long)
            return (double) ((long) o);
        if (o instanceof Short)
            return (double) ((short) o);
        if (o instanceof Byte)
            return (double) ((byte) o);
        return o;
    }


    /**
     * Преобразуем поля выборки в dbf поля.
     */
    private static List<DBFField> toFields(List<N2oDataStreamingUtil.Field> fields) {
        return fields.stream()
                .map(field -> {
                    DBFField dbfField = new DBFField();
                    dbfField.setName(trim(field.getId()));
                    String domain = field.getDomain();
                    Byte dbfType = DOMAINS_MAPPING.get(domain);
                    if (dbfType == null)
                        dbfType = DBFField.FIELD_TYPE_C;
                    dbfField.setDataType(dbfType);
                    if (domain.equals("boolean"))
                        dbfField.setFieldLength(1);
                    else if (!domain.equals("date"))
                        dbfField.setFieldLength(60);
                    dbfField.setDecimalCount(0);
                    return dbfField;
                })
                .collect(toList());
    }

    private static String trim(String s) {
        if (s.length() > 10)
            return s.substring(0, 10);
        return s;
    }


}
