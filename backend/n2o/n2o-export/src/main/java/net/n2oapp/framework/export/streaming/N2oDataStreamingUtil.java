package net.n2oapp.framework.export.streaming;

import net.n2oapp.framework.api.exception.N2oException;
import net.n2oapp.framework.api.metadata.aware.IdAware;
import net.n2oapp.framework.api.metadata.global.dao.N2oQuery;
import net.n2oapp.framework.api.metadata.global.view.widget.table.column.cell.N2oCell;
import net.n2oapp.framework.api.metadata.meta.widget.table.ColumnHeader;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static net.n2oapp.framework.api.metadata.global.dao.N2oQuery.Field.PK;
import static net.n2oapp.properties.StaticProperties.get;

public class N2oDataStreamingUtil {


    public static String toString(Object value) {

        if (value == null)
            return null;

        if (value instanceof Date)
            return new SimpleDateFormat(get("n2o.ui.export.format.date"))
                    .format((Date) value);

        return value.toString();
    }

    public static class Field {
        private String id;
        private String label;
        private String domain;

        public Field(String id, String label, String domain) {
            this.id = id;
            this.label = label;
            this.domain = domain;
        }

        public Field(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public String getLabel() {
            return label;
        }

        public String getDomain() {
            return domain;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Field)) return false;

            Field field = (Field) o;

            return id != null ? id.equals(field.id) : field.id == null;
        }

        @Override
        public int hashCode() {
            return id != null ? id.hashCode() : 0;
        }
    }

    /**
     * Отбираем поля выборки с "простым" доменом
     */
    public static List<Field> exportFields(List<String> columnIds,
                                           List<N2oQuery.Field> queryFields,
                                           List<N2oCell> columns,
                                           List<ColumnHeader> headers) {
        LinkedList<Field> res = new LinkedList<>();

        Map<String, N2oCell> columnsMap = columns.stream().collect(Collectors.toMap(IdAware::getId, c -> c));

        headers.stream().filter(h -> columnIds == null || columnIds.isEmpty() || columnIds.contains(h.getId()))
                .forEach(header -> {
                    N2oCell n2oCell = columnsMap.get(header.getId());
                    String id = n2oCell.getId();//todo n2oCell.getTextFieldId() нужен метод, возвращающий поле с текстом в любой ячейке
                    String name = header.getLabel();
                    String domain = findDomain(id, queryFields);
                    res.add(new Field(id, name, domain));
                });

        //если id нету то мы все равно его добавляем
        if (!res.contains(new Field(PK))) {
            for (N2oQuery.Field field : queryFields) {
                if (field.getId().equals(PK)) {
                    res.addFirst(new Field(PK, field.getName(), getDomain(field)));
                    break;
                }
            }
        }
        return res;
    }

    public static List<Field> exportFieldsFromQuery(List<String> columns, List<N2oQuery.Field> fields) {
        List<Field> res = new ArrayList<>();
        if (columns == null || columns.isEmpty()) {
            fields.stream().forEach(f -> res.add(new Field(f.getId(), f.getName(), f.getDomain())));
        } else {
            fields.stream().filter(f -> columns.contains(f.getId()))
                    .forEach(f -> res.add(new Field(f.getId(), f.getName(), f.getDomain())));
        }
        return res;
    }


    private static String findDomain(String id, List<N2oQuery.Field> fields) {
        for (N2oQuery.Field field : fields) {
            if (field.getId().equals(id))
                return getDomain(field);
        }
        throw new N2oException("not found field [" + id + "] in query");
    }


    /**
     * Вычисляем домен поля
     */
    private static String getDomain(N2oQuery.Field field) {
        return field.getDomain() != null ? field.getDomain().toLowerCase().trim() : "string";
    }


}
